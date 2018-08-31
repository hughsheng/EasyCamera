package tl.com.ease_camera_library.camera1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/**
 * Created by tl on 2018-8-31
 * 管理camera的一些操作
 */
public class CameraManager {
  private int cameraNum;
  private Camera.CameraInfo frontCameraInfo;
  private Camera.CameraInfo backCameraInfo;
  private Camera camera;
  private int currentCamera = -1;
  private CameraConfigurationManager configurationManager;
  private boolean isInitParms = false;

  public CameraManager(Context context) {
    configurationManager = new CameraConfigurationManager(context);
  }


  /**
   * 打开摄像头
   *
   * @param cameraID id
   */
  public void openCamera(int cameraID) {
    cameraNum = Camera.getNumberOfCameras();
    if (cameraNum == 0) return;
    getCameraInfo();
    if (cameraID == Camera.CameraInfo.CAMERA_FACING_FRONT && frontCameraInfo != null) {
      camera = Camera.open(cameraID);
      currentCamera = cameraID;
    } else if (cameraID == Camera.CameraInfo.CAMERA_FACING_BACK && backCameraInfo != null) {
      camera = Camera.open(cameraID);
      currentCamera = cameraID;
    } else {
      Log.e("Camera", "this device has no such camera");
    }

  }


  /**
   * 关闭摄像头
   */
  public void closeCamera() {
    if (camera != null) {
      camera.release();
      camera = null;
    }
  }


  /**
   * 开启预览
   */
  public void startPreview(SurfaceHolder holder) {
    if (camera != null) {
      try {

        if (!isInitParms) {
          configurationManager.initFromCameraParameters(camera);//配置预览尺寸
        }

        configurationManager.setDesiredCameraParameters(camera, false);
        camera.setPreviewDisplay(holder);
        camera.startPreview();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  /**
   * 关闭预览
   */
  public void stopPreview() {
    if (camera != null) {
      camera.stopPreview();
    }
  }


  /**
   * 获取当前开启的摄像头id
   *
   * @return id
   */
  public int getOpendCameraID() {
    return currentCamera;
  }


  /**
   * 打开闪光灯
   */
  public void openLight() {
    if (camera != null) {
      Camera.Parameters parameter = camera.getParameters();
      parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
      camera.setParameters(parameter);
    }
  }

  /**
   * 关闭闪光灯
   */
  public void offLight() {
    if (camera != null) {
      Camera.Parameters parameter = camera.getParameters();
      parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
      camera.setParameters(parameter);
    }
  }


  /**
   * 切换摄像头
   *
   * @throws IOException
   */
  public void changeCamera(SurfaceHolder holder) {
    closeCamera();
    stopPreview();
    if (currentCamera == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      currentCamera = Camera.CameraInfo.CAMERA_FACING_BACK;
      openCamera(currentCamera);
    } else if (currentCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
      currentCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
      openCamera(currentCamera);
    }
    startPreview(holder);
  }


  /**
   * 获取相机分辨率
   *
   * @return
   */
//  public Point getCameraResolution() {
//    return configManager.getCameraResolution();
//  }

  /**
   * 获取预览尺寸
   *
   * @return size
   */
  public Camera.Size getPreviewSize() {
    if (null != camera) {
      return camera.getParameters().getPreviewSize();
    }
    return null;
  }


  /**
   * 拍照
   */
  public void takePhoto() {
    if (camera != null) {
      Camera.Parameters parameters = camera.getParameters();
      List<Camera.Size> supportedSizes = parameters.getSupportedPictureSizes();
      Camera.Size sizePicture = (supportedSizes.get((supportedSizes.size() - 1) / 2));
      parameters.setPictureSize(sizePicture.width, sizePicture.height);
      camera.setParameters(parameters);

      camera.takePicture(null, null, new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
          Bitmap roateBitmap = null;
          try {
            Bitmap originBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (currentCamera == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//              Bitmap mirrorBitmap = FileUtils.mirrorImg(originBitmap);
//              roateBitmap = FileUtils.rotateBitmap(mirrorBitmap, 90);
            } else {
//              roateBitmap = FileUtils.rotateBitmapByDegree(originBitmap, 90);
            }
            camera.startPreview();

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }
  }


  /**
   * 获取设备摄像头信息
   */
  private void getCameraInfo() {
    for (int i = 0; i < cameraNum; i++) {
      Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
      Camera.getCameraInfo(i, cameraInfo);
      if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
        frontCameraInfo = cameraInfo;
      } else {
        backCameraInfo = cameraInfo;
      }
    }
  }

}
