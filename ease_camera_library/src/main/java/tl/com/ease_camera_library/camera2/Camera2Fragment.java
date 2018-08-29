package tl.com.ease_camera_library.camera2;

import android.app.Activity;
import android.support.annotation.NonNull;

import tl.com.ease_camera_library.BaseFragment;


/**
 * Created by tl on 2018-8-28
 */
public class Camera2Fragment extends BaseFragment{
  public static final String TAG = "Camera2Fragment";




  @Override
  protected void qrMode() {

  }

  @Override
  protected void photographyMode() {

  }

  @Override
  protected void captureMode() {

  }

  @Override
  protected void faceMode() {

  }

  @Override
  protected void openAlbum() {

  }

  @Override
  protected void doCamera() {

  }

  @Override
  protected void changeCamera() {

  }

  @Override
  protected void startPreview(int cameraID) {
    cameraManagerUtil.openCamera(cameraID);
  }

}
