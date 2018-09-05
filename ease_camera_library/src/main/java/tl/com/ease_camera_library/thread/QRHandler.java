package tl.com.ease_camera_library.thread;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import tl.com.ease_camera_library.camera1.CameraManager;
import tl.com.ease_camera_library.util.ConstanceValues;

/**
 * Created by tl on 2018-8-30
 * 处理拍照的照片
 */
public class QRHandler extends Handler {
  private CameraManager cameraManager;
  private MultiFormatReader multiFormatReader;
  private Handler resultHandler;

  public QRHandler(CameraManager cameraManager, Map<DecodeHintType, Object> hints, Handler
      resultHandler) {
    multiFormatReader = new MultiFormatReader();
    multiFormatReader.setHints(hints);
    this.cameraManager = cameraManager;
    this.resultHandler = resultHandler;
  }



  @Override
  public void handleMessage(Message msg) {

    switch (msg.what) {
      case ConstanceValues.QR_DETECH:
        byte[] data = (byte[]) msg.obj;
        Camera.Size size = cameraManager.getPreviewSize();
        if (size != null) {
          decode(data, size.width, size.height);
        }
        break;

      case ConstanceValues.QR_QUIET:
        Looper looper = Looper.myLooper();//获取该handler持有的looper
        if (looper != null) {
          looper.quitSafely();
        }
        break;
    }


  }


  /**
   * Decode the data within the viewfinder rectangle, and time how long it
   * took. For efficiency, reuse the same reader objects from one decode to
   * the next.
   *
   * @param data   The YUV preview frame.
   * @param width  The width of the preview frame.
   * @param height The height of the preview frame.
   */
  void decode(byte[] data, int width, int height) {
    Camera.Size size = cameraManager.getPreviewSize();

    // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
    byte[] rotatedData = new byte[data.length];
    for (int y = 0; y < size.height; y++) {
      for (int x = 0; x < size.width; x++)
        rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
    }

    // 宽高也要调整
    int tmp = size.width;
    size.width = size.height;
    size.height = tmp;

    Result rawResult = null;
    PlanarYUVLuminanceSource source = buildLuminanceSource(rotatedData, size.width, size.height);
    if (source != null) {
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      try {
        rawResult = multiFormatReader.decodeWithState(bitmap);
      } catch (ReaderException re) {
        // continue
      } finally {
        multiFormatReader.reset();
      }
    }

    if (rawResult != null) {
      // Don't log the barcode contents for security.
      if (resultHandler != null) {
        Message message = Message.obtain(resultHandler, ConstanceValues.QR_SUCCESS, rawResult);
        Bundle bundle = new Bundle();
        bundleThumbnail(source, bundle);
        message.setData(bundle);
        message.sendToTarget();
      }
    } else {
      if (resultHandler != null) {
        Message message = Message.obtain(resultHandler, ConstanceValues.QR_FAIL);
        message.sendToTarget();
      }
    }

  }

  /**
   * 二维码截图
   *
   * @param source source
   * @param bundle bundle
   * @return
   */
  private byte[] bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
    int[] pixels = source.renderThumbnail();
    int width = source.getThumbnailWidth();
    int height = source.getThumbnailHeight();
    Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
    return out.toByteArray();
  }

  /**
   * A factory method to build the appropriate LuminanceSource object based on
   * the format of the preview buffers, as described by Camera.Parameters.
   *
   * @param data   A preview frame.
   * @param width  The width of the image.
   * @param height The height of the image.
   * @return A PlanarYUVLuminanceSource instance.
   */
  private PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
//    Rect rect = scanManager.getCropRect();
//    if (rect == null) {
//      return null;
//    }
    //全屏检测二维码(需要替换宽高，因为检测的数据宽高已经替换了需要保持一致)
    Rect rect = new Rect(0, 0, cameraManager.getPreviewSize().height, cameraManager.getPreviewSize
        ().width);

    // Go ahead and assume it's YUV rather than die.
    return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(),
        rect.height(), false);
  }

}
