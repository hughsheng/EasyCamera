package tl.com.ease_camera_library;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import tl.com.ease_camera_library.R;

/**
 * Created by tl on 2018-8-30
 * 处理拍照的照片
 */
public class PhotoHandler extends Handler {

  @Override
  public void handleMessage(Message msg) {
    int type = msg.what;
    if (type == R.id.photo_save) {

    } else if (type == R.id.photo_quiet) {
      Looper looper = Looper.myLooper();//获取该handler持有的looper
      if (looper != null) {
        looper.quitSafely();
      }

    }
  }
}
