package tl.com.ease_camera_library.base;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import tl.com.ease_camera_library.util.PermisionUtils;
import tl.com.ease_camera_library.R;
import tl.com.ease_camera_library.camera2.Camera2Fragment;
import tl.com.ease_camera_library.camera2.CameraManagerUtil;

/**
 * Created by tl on 2018-8-28
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {


  protected SurfaceView camera_sv;
  protected TextView qr_code_tv;
  protected TextView capture_tv;
  protected TextView photography_tv;
  protected TextView face_tv;
  protected ImageView album_iv;
  protected ImageView camera_iv;
  protected ImageView change_camera_iv;
  protected HandlerThread mBackgroundThread;
  protected Handler mBackgroundHandler;
  protected CameraManagerUtil cameraManagerUtil;
  private boolean hasSurface = false;
  private final int CAMERA_BACK_ID = 0;
  private final int CAMERA_FRONT_ID = 1;

  public static Camera2Fragment newInstance() {
    return new Camera2Fragment();
  }


  private void startBackgroundThread() {
    mBackgroundThread = new HandlerThread("CameraBackground");
    mBackgroundThread.start();
    mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

  }


  private void stopBackgroundThread() {
    mBackgroundThread.quitSafely();
    try {
      mBackgroundThread.join();
      mBackgroundThread = null;
      mBackgroundHandler = null;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }







  @Override
  public void onResume() {
    super.onResume();
    startBackgroundThread();
    if (hasSurface) {
      // The activity was paused but not stopped, so the surface still
      // exists. Therefore
      // surfaceCreated() won't be called, so init the camera here.
      cameraManagerUtil.openCamera(CAMERA_BACK_ID);
    } else {
      // Install the callback and wait for surfaceCreated() to init the
      // camera.
      camera_sv.getHolder().addCallback(callback);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    cameraManagerUtil.closeCamera();
    stopBackgroundThread();
    if (!hasSurface) {
      camera_sv.getHolder().removeCallback(callback);
    }
  }


  private void setListener() {
    qr_code_tv.setOnClickListener(this);
    capture_tv.setOnClickListener(this);
    photography_tv.setOnClickListener(this);
    face_tv.setOnClickListener(this);
    album_iv.setOnClickListener(this);
    camera_iv.setOnClickListener(this);
    change_camera_iv.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
//    int id = view.getId();
//    if (id == R.id.qr_code_tv) {
//      qrMode();
//    } else if (id == R.id.capture_tv) {
//      photographyMode();
//    } else if (id == R.id.photography_tv) {
//      captureMode();
//    } else if (id == R.id.face_tv) {
//      faceMode();
//    } else if (id == R.id.album_iv) {
//      openAlbum();
//    } else if (id == R.id.camera_iv) {
//      doCamera();
//    } else if (id == R.id.change_camera_iv) {
//      changeCamera();
//    }
  }


  SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {//需要在surface准备完成后再打开摄像头
      if (PermisionUtils.hasCameraPermission(getActivity())) {
        hasSurface = true;
        cameraManagerUtil.openCamera(CAMERA_BACK_ID);
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
          startBackgroundThread();
          cameraManagerUtil = new CameraManagerUtil(mBackgroundHandler, null, null, null, null,
              getActivity(), camera_sv.getHolder());
          startPreview(CAMERA_BACK_ID);
        } else {
          getActivity().finish();
        }
        break;
    }
  }


  public void closeCamera() {
    cameraManagerUtil.closeCamera();
  }


  public void openCamera(int  cameraID) {
    startPreview(cameraID);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    cameraManagerUtil.closeCamera();
  }

  protected abstract void qrMode();

  protected abstract void photographyMode();

  protected abstract void captureMode();

  protected abstract void faceMode();

  protected abstract void openAlbum();

  protected abstract void doCamera();

  protected abstract void changeCamera();

  protected abstract void startPreview(int cameraID);
}
