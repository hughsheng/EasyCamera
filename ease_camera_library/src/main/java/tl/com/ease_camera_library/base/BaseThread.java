package tl.com.ease_camera_library.base;

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
        if (isPause) {
          synchronized (this) {
            wait();
          }
        }

        Thread.sleep(DETECH_TIME);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public boolean isPause() {
    return isPause;
  }

  public void pause() {
      isPause = true;
  }

  public void reStart() {
    if (isPause) {
      isPause = false;
      synchronized (this) {
        notify();
      }
    }
  }

  public void close() {
    interrupt();
  }

}


