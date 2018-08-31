package tl.com.ease_camera_library.camera1;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import tl.com.ease_camera_library.CameraInterface;
import tl.com.ease_camera_library.PermisionUtils;
import tl.com.ease_camera_library.R;

/**
 * Created by tl on 2018-8-30
 * camera1 API
 */
public class CameraFragment extends Fragment implements CameraInterface {

  private SurfaceView surfaceView;
  private CameraManager cameraManager;
  private boolean hasSurface = false;

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
    surfaceView = view.findViewById(R.id.camera_sv);
    cameraManager = new CameraManager(getContext());
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

  }

  //摄像模式
  @Override
  public void photographyMode() {

  }

  //拍照模式
  @Override
  public void faceMode() {

  }

  //打开相册
  @Override
  public void openAlbum() {

  }

  //相机模式操作按钮(拍照/摄像)
  @Override
  public void doCamera() {

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


  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
      int[] grantResults) {
    switch (requestCode) {
      case PermisionUtils.REQUEST_CAMERA:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          cameraManager = new CameraManager(getContext());
          cameraManager.openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
          cameraManager.startPreview(surfaceView.getHolder());
        } else {
          Activity activity = getActivity();
          if (activity != null) {
            activity.finish();
          }
        }
        break;
    }
  }

}
