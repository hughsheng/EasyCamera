package tl.com.ease_camera_library;

import android.os.Looper;

/**
 * Created by tl on 2018-8-30
 * 处理照片的线程
 */
public class PhotoThread extends BaseThread {
  private PhotoHandler photoHandler;

  public PhotoHandler getPhotoHandler() {
    return photoHandler;
  }

  @Override
  public void run() {
    super.run();
    Looper.prepare();
    photoHandler = new PhotoHandler();
    Looper.loop();
  }


}
