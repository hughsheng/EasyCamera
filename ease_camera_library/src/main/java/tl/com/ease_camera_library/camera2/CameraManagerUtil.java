package tl.com.ease_camera_library.camera2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static tl.com.ease_camera_library.camera2.Camera2Fragment.TAG;

/**
 * Created by tl on 2018-8-28
 * 管理camera的一系列操作
 */
public class CameraManagerUtil {

  private Handler previewHandler;
  private Handler photoHandler;
  private Handler qrHandler;
  private Handler faceHandler;
  private Handler videoHander;
  private CameraDevice mCameraDevice;
  private CameraManager manager;
  private ImageReader mImageReader;
  private Context context;
  private SurfaceHolder holder;
  private CameraCaptureSession mCameraCaptureSession;

  private String faceFrontCameraId;
  private int faceFrontCameraOrientation;
  private CameraCharacteristics frontCameraCharacteristics;

  private String faceBackCameraId;
  private int faceBackCameraOrientation;
  private CameraCharacteristics backCameraCharacteristics;

  private int cameraNum;

  public CameraManagerUtil(Handler previewHandler,
                           Handler photoHandler,
                           Handler qrHandler,
                           Handler faceHandler,
                           Handler videoHander,
                           Context context, SurfaceHolder holder) {
    this.previewHandler = previewHandler;
    this.photoHandler = photoHandler;
    this.qrHandler = qrHandler;
    this.faceHandler = faceHandler;
    this.videoHander = videoHander;
    this.context = context;
    this.holder = holder;
    getCameraInfo();
  }

  /**
   * 打开摄像头
   *
   * @param cameraID id
   */
  @SuppressLint("MissingPermission")
  public void openCamera(int cameraID) {
    if (manager != null && checkCamera(cameraID)) {
      try {
        manager.openCamera("" + cameraID, mStateCallback, previewHandler);
      } catch (CameraAccessException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 检查该设备是否有对应id的摄像头
   *
   * @param cameraID id
   * @return boolean
   */
  private boolean checkCamera(int cameraID) {
    boolean useful = false;
    try {
      String[] list = manager.getCameraIdList();
      useful = Arrays.asList(manager.getCameraIdList()).contains(String.valueOf(cameraID));
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
    return useful;
  }


  /**
   * 当前摄像头状态回调
   */
  private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

    @Override
    public void onOpened(@NonNull CameraDevice cameraDevice) {
      // This method is called when the camera is opened.  We start camera preview here.
      mCameraDevice = cameraDevice;
      takePreview(holder);
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice cameraDevice) {
      cameraDevice.close();
      mCameraDevice = null;
    }

    @Override
    public void onError(@NonNull CameraDevice cameraDevice, int error) {
      cameraDevice.close();
      mCameraDevice = null;
    }

  };


  /**
   * 初始化ImageReader
   */
  private void setmImageReader() {
    mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1);
    mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
      //可以在这里处理拍照得到的临时照片 例如，写入本地
      @Override
      public void onImageAvailable(ImageReader reader) {
        //mCameraDevice.close();
        // 拿到拍照照片数据
        Image image = reader.acquireNextImage();
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);//由缓冲区存入字节数组
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null) {

        }
      }
    }, photoHandler);

  }


  /**
   * 开始预览
   */
  private void takePreview(SurfaceHolder mSurfaceHolder) {
    try {

      if (mImageReader == null) {
        setmImageReader();
      }

      // 创建预览需要的CaptureRequest.Builder
      final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest
          (CameraDevice.TEMPLATE_PREVIEW);
      // 将SurfaceView的surface作为CaptureRequest.Builder的目标
      previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
      CaptureRequest previewRequest = previewRequestBuilder.build();
      // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
      mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader
          .getSurface()), getPreViewSessionCallback(previewRequest), null);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }


  /**
   * 预览会话状态回调
   *
   * @param previewRequest android device发送给camera device的请求
   * @return
   */
  private CameraCaptureSession.StateCallback getPreViewSessionCallback(final CaptureRequest
                                                                           previewRequest) {

    return new CameraCaptureSession.StateCallback() {
      @Override
      public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
        if (null == mCameraDevice) return;
        // 当摄像头已经准备好时，开始显示预览
        mCameraCaptureSession = cameraCaptureSession;
        try {
          // 自动对焦
//          previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest
//              .CONTROL_AF_MODE_CONTINUOUS_PICTURE);
          // 打开闪光灯
//          previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest
//              .CONTROL_AE_MODE_ON_AUTO_FLASH);
          // 显示预览
          mCameraCaptureSession.setRepeatingRequest(previewRequest, null, previewHandler);
        } catch (CameraAccessException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

      }
    };
  }

  /**
   * 获取相机参数
   */
  private void getCameraInfo() {
    manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    try {
      if (manager != null) {
        final String[] ids = manager.getCameraIdList();
        cameraNum = ids.length;
        for (String id : ids) {
          final CameraCharacteristics characteristics = manager.getCameraCharacteristics(id);

          final int orientation = characteristics.get(CameraCharacteristics.LENS_FACING);
          if (orientation == CameraCharacteristics.LENS_FACING_FRONT) {
            faceFrontCameraId = id;
            faceFrontCameraOrientation = characteristics.get(CameraCharacteristics
                .SENSOR_ORIENTATION);
            frontCameraCharacteristics = characteristics;
          } else {
            faceBackCameraId = id;
            faceBackCameraOrientation = characteristics.get(CameraCharacteristics
                .SENSOR_ORIENTATION);
            backCameraCharacteristics = characteristics;
          }
        }
      }
    } catch (Exception e) {
      Log.e(TAG, "Error during camera initialize");
    }

  }


  /**
   * 判断是否使用camera2 API
   *
   * @param mContext context
   * @return boolean
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static boolean hasCamera2(Context mContext) {
    if (mContext == null) return false;
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return false;
    try {
      CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
      String[] idList = manager.getCameraIdList();
      boolean notFull = true;
      if (idList.length == 0) {
        notFull = false;
      } else {
        for (final String str : idList) {
          if (str == null || str.trim().isEmpty()) {
            notFull = false;
            break;
          }
          final CameraCharacteristics characteristics = manager.getCameraCharacteristics(str);

          final int supportLevel = characteristics.get(CameraCharacteristics
              .INFO_SUPPORTED_HARDWARE_LEVEL);
          if (supportLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            notFull = false;
            break;
          }
        }
      }
      return notFull;
    } catch (Throwable ignore) {
      return false;
    }
  }


  /**
   * 关闭摄像头
   */
  public void closeCamera() {
    if (null != mCameraCaptureSession) {
      mCameraCaptureSession.close();
      mCameraCaptureSession = null;
    }
    if (null != mCameraDevice) {
      mCameraDevice.close();
      mCameraDevice = null;
    }
    if (null != mImageReader) {
      mImageReader.close();
      mImageReader = null;
    }
  }

}
