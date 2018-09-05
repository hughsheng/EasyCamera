package tl.com.ease_camera_library.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import tl.com.ease_camera_library.base.BaseThread;
import tl.com.ease_camera_library.camera1.CameraManager;

/**
 * Created by tl on 2018-9-4
 */
public class FaceThread extends BaseThread {

  private FaceHandler faceHandler;

  public FaceThread(Context context,CameraManager cameraManager, Handler resultHandler) {
    faceHandler = new FaceHandler(context,cameraManager,resultHandler);
  }

  public FaceHandler getFaceHandler() {
    return faceHandler;
  }

  public void removeHandler(){
    faceHandler=null;
  }

  @Override
  public void run() {
    super.run();
    Looper.prepare();
    Looper.loop();
  }
}
