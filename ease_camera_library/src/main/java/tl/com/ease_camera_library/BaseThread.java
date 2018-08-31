package tl.com.ease_camera_library;

/**
 * Created by tl on 2018-8-30
 * 添加控制线程基础类
 */
public class BaseThread extends Thread {

  protected int DETECH_TIME = 1000;//更新当前线程状态间隔

  protected boolean isPause = false;

  @Override
  public void run() {

    try {
      while (true) {
        synchronized (this) {
          while (isPause) {
            wait();
          }
        }
        Thread.sleep(DETECH_TIME);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void pause() {
    isPause = true;
  }

  public void reStart() {
    isPause = false;
    notify();
  }

  public void close() {
    interrupt();
  }

}


