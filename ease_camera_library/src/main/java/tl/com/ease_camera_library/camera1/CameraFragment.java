package tl.com.ease_camera_library.camera1;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.tzutalin.dlib.VisionDetRet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tl.com.ease_camera_library.R;
import tl.com.ease_camera_library.interfaces.CameraInterface;
import tl.com.ease_camera_library.thread.FaceHandler;
import tl.com.ease_camera_library.thread.FaceThread;
import tl.com.ease_camera_library.thread.QRHandler;
import tl.com.ease_camera_library.thread.QRThread;
import tl.com.ease_camera_library.util.CameraUtil;
import tl.com.ease_camera_library.util.ConstanceValues;
import tl.com.ease_camera_library.util.PermisionUtils;
import tl.com.ease_camera_library.views.FaceOverlayView;

/**
 * Created by tl on 2018-8-30
 * camera1 API
 */
public class CameraFragment extends Fragment implements CameraInterface, CameraManager
    .CameraListener, Camera.PreviewCallback, Handler.Callback {

  private SurfaceView surfaceView;
  private CameraManager cameraManager;
  private boolean hasSurface = false;
  private CameraFragmentListener listener;
  private QRThread qrThread;
  private FaceThread faceThread;
  private Handler resultHandler;
  private FaceOverlayView mFaceView;//绘制人脸检测框
  private int screenWidth;
  private int screenHeight;

  public static CameraFragment newInstance() {

    Bundle args = new Bundle();

    CameraFragment fragment = new CameraFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_preview, container, false);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    cameraManager = new CameraManager(getContext(), this);
    resultHandler = new Handler(this);
    getPhoneWidthAndHeight();
    surfaceView = view.findViewById(R.id.camera_sv);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof CameraFragmentListener) {
      listener = (CameraFragmentListener) context;
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (hasSurface) {
      // The activity was paused but not stopped, so the surface still
      // exists. Therefore
      // surfaceCreated() won't be called, so init the camera here.
      //手机息屏时surface没有销毁,不会执行surfaceCreated()方法
      cameraManager.openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
      cameraManager.startPreview(surfaceView.getHolder());
    } else {
      // Install the callback and wait for surfaceCreated() to init the
      // camera.
      surfaceView.getHolder().addCallback(callback);
    }
  }


  @Override
  public void onPause() {
    super.onPause();
    cameraManager.closeCamera();
    cameraManager.stopPreview();
    if (!hasSurface) {
      surfaceView.getHolder().removeCallback(callback);
    }
  }

  //识别二维码模式
  @Override
  public void qrMode() {
    if (faceThread != null) {
      faceThread.pause();
    }

    if (qrThread == null) {
      qrThread = new QRThread(cameraManager, QRThread.ALL_MODE, resultHandler);
      qrThread.start();
    } else {
      qrThread.reStart();
    }
    cameraManager.setPreviewCallback(this);
  }

  //拍照模式
  @Override
  public void photoMode() {
    if (faceThread != null) {
      faceThread.pause();
    }

    if (qrThread != null) {
      qrThread.pause();
    }
  }

  //摄像模式
  @Override
  public void photographyMode() {
    if (faceThread != null) {
      faceThread.pause();
    }

    if (qrThread != null) {
      qrThread.pause();
    }
  }

  //人脸模式
  @Override
  public void faceMode() {
    if (qrThread != null) {
      qrThread.pause();
    }

    if (faceThread == null) {
      faceThread = new FaceThread(getContext(), cameraManager, resultHandler);
      faceThread.start();
    } else {
      faceThread.reStart();
    }
    cameraManager.setPreviewCallback(this);
  }

  //打开相册
  @Override
  public void openAlbum() {

  }

  //相机模式操作按钮(拍照/摄像)
  @Override
  public void takePhoto() {
    cameraManager.takePhoto();
  }

  //切换摄像头
  @Override
  public void changeCamera() {
    cameraManager.changeCamera(surfaceView.getHolder());
  }

  //是否打开闪光灯
  @Override
  public void openFlash(boolean isOpen) {
    if (isOpen) {
      cameraManager.openLight();
    } else {
      cameraManager.offLight();
    }
  }

  SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {//需要在surface准备完成后再打开摄像头
      if (PermisionUtils.hasCameraPermission(getActivity())) {
        hasSurface = true;
        cameraManager.openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        cameraManager.startPreview(surfaceHolder);
        if (mFaceView == null) {
          addFaceFrame();
        }
      } else {
        PermisionUtils.verifyCameraPermissions(getActivity());
      }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
      hasSurface = false;
    }
  };


  private void addFaceFrame() {
    mFaceView = new FaceOverlayView(getContext(), cameraManager.getPreviewSize());
    mFaceView.setFront(true);
    mFaceView.setDisplayOrientation(CameraUtil.getOrientation(getContext()));
    getActivity().addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams
        .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
  }


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
      int[] grantResults) {
    switch (requestCode) {
      case PermisionUtils.REQUEST_CAMERA:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          cameraManager = new CameraManager(getContext(), this);
          cameraManager.openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
          cameraManager.startPreview(surfaceView.getHolder());
          if (mFaceView == null) {
            addFaceFrame();
          }
        } else {
          Activity activity = getActivity();
          if (activity != null) {
            activity.finish();
          }
        }
        break;
    }
  }

  @Override
  public void showPhoto(Bitmap photo) {
    if (listener != null) {
      listener.showPhoto(photo);
    }
  }

  @Override
  public void onPreviewFrame(byte[] data, Camera camera) {
    //二维码识别
    if (qrThread != null && !qrThread.isPause()) {
      QRHandler qrHandler = qrThread.getQRHandler();
      if (qrHandler != null) {
        Message qrMsg = qrHandler.obtainMessage(ConstanceValues.QR_DETECH, data);
        qrHandler.sendMessage(qrMsg);

      }
    }

    //人脸识别
    if (faceThread != null && !faceThread.isPause()) {
      FaceHandler faceHandler = faceThread.getFaceHandler();
      if (faceHandler != null) {
        Message faceMsg = faceHandler.obtainMessage(ConstanceValues.FACE_DETECH, data);
        faceHandler.sendMessage(faceMsg);
      }
    }


  }

  @Override
  public boolean handleMessage(Message msg) {

    switch (msg.what) {
      case ConstanceValues.QR_SUCCESS:
        Result result = (Result) msg.obj;
        Toast.makeText(getContext(), result.getText(), Toast.LENGTH_SHORT).show();
        break;

      case ConstanceValues.QR_FAIL:
        // Toast.makeText(getContext(), "no qr", Toast.LENGTH_SHORT).show();
        cameraManager.setPreviewCallback(this);
        break;

      case ConstanceValues.FACE_SUCCESS:
        Toast.makeText(getContext(), "检测到人脸", Toast.LENGTH_SHORT).show();
        mFaceView.setFaces(null);
        cameraManager.setPreviewCallback(this);
        break;

      case ConstanceValues.FACE_FAIL:
        //  Toast.makeText(getContext(), "未检测到人脸", Toast.LENGTH_SHORT).show();
        cameraManager.setPreviewCallback(this);
        break;

      case ConstanceValues.FACE_UNCLEAR:
        Toast.makeText(getContext(), getString(R.string.unclear), Toast.LENGTH_SHORT).show();
        cameraManager.setPreviewCallback(this);
        break;

      case ConstanceValues.FACE_IMPERFECT:
        Toast.makeText(getContext(), getString(R.string.imperfect), Toast.LENGTH_SHORT).show();
        cameraManager.setPreviewCallback(this);
        break;

      case ConstanceValues.FACE_DRAW:
        List<VisionDetRet> detect = (List<VisionDetRet>) msg.obj;
        mFaceView.setFaces(detect);//发送人脸数据，绘制人脸框
        Map<String, Float> scaleMap = new HashMap<>();
        scaleMap.put("scaleX", mFaceView.getScaleX());
        scaleMap.put("scaleY", mFaceView.getScaleY());
        scaleMap.put("screenWidth", (float) screenWidth);
        scaleMap.put("screenHeight", (float) screenHeight);
        Message drawOverMsg = faceThread.getFaceHandler().obtainMessage(ConstanceValues
            .FACE_DRAW_OVER, scaleMap);
        faceThread.getFaceHandler().sendMessage(drawOverMsg);
        break;

      case ConstanceValues.FACE_NO_PHOTO:
        Toast.makeText(getContext(), "未获取到图像！", Toast.LENGTH_SHORT).show();
        cameraManager.setPreviewCallback(this);
        break;
    }

    return false;
  }


  private void getPhoneWidthAndHeight() {
    DisplayMetrics dm = new DisplayMetrics();
    dm = new DisplayMetrics();
    getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
    float densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
    screenWidth = dm.widthPixels; // 屏幕宽（dip，如：320dip）
    screenHeight = dm.heightPixels; // 屏幕高（dip，如：533dip）
    int screenWidthPX = (int) (screenWidth * density + 0.5f); // 屏幕宽（px，如：720px）
    int screenHeightPX = (int) (screenHeight * density + 0.5f); // 屏幕高（px，如：1280px）
  }

  public interface CameraFragmentListener {
    void showPhoto(Bitmap photo);
  }


}
