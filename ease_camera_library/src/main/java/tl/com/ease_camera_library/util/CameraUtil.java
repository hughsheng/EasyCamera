package tl.com.ease_camera_library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.tzutalin.dlib.VisionDetRet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import tl.com.ease_camera_library.bean.FaceRectBean;

public class CameraUtil {

  /**
   * 获取图像中最大的人脸
   *
   * @param faceDetRets
   * @return
   */
  public static VisionDetRet getMaxFace(List<VisionDetRet> faceDetRets) {
    if (faceDetRets.size() == 1) {
      return faceDetRets.get(0);
    }
    List<FaceRectBean> rectBeanList = new ArrayList<>();
    int position = 0;
    for (int i = 0; i < faceDetRets.size(); i++) {
      VisionDetRet faceDetRet = faceDetRets.get(i);
      FaceRectBean rectBean = new FaceRectBean();
      rectBean.setOrder(i);
      rectBean.setAcreage(Math.abs(faceDetRet.getBottom() - faceDetRet.getTop()) *
          Math.abs(faceDetRet.getLeft() - faceDetRet.getRight()));
      rectBeanList.add(rectBean);
    }
    float max = rectBeanList.get(0).getAcreage();
    for (int j = 0; j < rectBeanList.size(); j++) {
      if (rectBeanList.get(j).getAcreage() > max) {
        max = rectBeanList.get(j).getAcreage();
        position = j;
      }
    }
    return faceDetRets.get(position);
  }

  /**
   * 判断获取的人脸是否符合要求
   * （人脸是否在指定的区域）
   *
   * @param faceBean
   * @return
   */
  public static boolean isGetfitFace(VisionDetRet faceBean, float scaleX, float scaleY, float
      screenWidth, float screenHight) {

    int faceTop = faceBean.getTop();
    int faceBottom = faceBean.getBottom();
    int faceRight = faceBean.getRight();
    int faceLeft = faceBean.getLeft();

    int acreage = Math.abs(faceTop - faceBottom) * Math.abs(faceRight - faceLeft);

    float left = screenWidth - faceRight * scaleX;//左边距
    float right = faceLeft * scaleX;//右边距
    float top = faceTop * scaleY;//上边距
    float bottom = screenHight - faceBottom * scaleY;//下边距

    return (left > ConstanceValues.POSITION_LIMIT && right > ConstanceValues
        .POSITION_LIMIT && top > ConstanceValues.POSITION_LIMIT &&
        bottom > ConstanceValues.POSITION_LIMIT && acreage > ConstanceValues.FACE_MIN_AREA);
  }

  /**
   * 判断获取的图片是否清晰
   * （人脸的晃动距离是否过大----40）
   *
   * @param faceBeanList
   * @return
   */
  public static boolean isFaceClear(List<VisionDetRet> faceBeanList) {
    int size = faceBeanList.size();
    if (size == 1) {//当只识别一张则直接返回
      return true;
    }
    int firstTop = faceBeanList.get(0).getTop();
    int firstBottom = faceBeanList.get(0).getBottom();
    int firstRight = faceBeanList.get(0).getRight();
    int firstLeft = faceBeanList.get(0).getLeft();

    int lastTop = faceBeanList.get(size - 1).getTop();
    int lastBottom = faceBeanList.get(size - 1).getBottom();
    int lastRight = faceBeanList.get(size - 1).getRight();
    int lastLeft = faceBeanList.get(size - 1).getLeft();

    int moveTop = Math.abs(firstTop - lastTop);
    int moveBottom = Math.abs(firstBottom - lastBottom);
    int moveRight = Math.abs(firstRight - lastRight);
    int moveLeft = Math.abs(firstLeft - lastLeft);

    return (moveTop < ConstanceValues.MOVELIMIT && moveBottom < ConstanceValues.MOVELIMIT &&
        moveLeft
            < ConstanceValues.MOVELIMIT && moveRight < ConstanceValues.MOVELIMIT);
  }


  /**
   * 获取摄像头与手持设备拍摄的角度
   *
   * @param context
   * @return
   */
  public static int getOrientation(Context context) {
    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
        .getDefaultDisplay();
    int rotation = display.getRotation();
    int orientation;
    boolean expectPortrait;
    switch (rotation) {
      case Surface.ROTATION_0:
      default:
        orientation = 90;
        expectPortrait = true;
        break;
      case Surface.ROTATION_90:
        orientation = 0;
        expectPortrait = false;
        break;
      case Surface.ROTATION_180:
        orientation = 270;
        expectPortrait = true;
        break;
      case Surface.ROTATION_270:
        orientation = 180;
        expectPortrait = false;
        break;
    }
    boolean isPortrait = display.getHeight() > display.getWidth();
    if (isPortrait != expectPortrait) {
      orientation = (orientation + 270) % 360;
    }
    return orientation;
  }

  public static Bitmap getProperBitmap(byte[] data, Context context, Camera.Size previewSize) {
    Bitmap bmp = decodeToBitMap(data, previewSize);
    // 定义矩阵对象
    Matrix matrix = new Matrix();
    // 缩放原图
    matrix.postScale(1f, 1f);
    matrix.postRotate(-getOrientation(context));
    if (bmp != null) {
      return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
          matrix, true);
    } else {
      return null;
    }
  }


  /**
   * 图像数组转化为bitmap
   *
   * @param data
   * @return
   */
  public static Bitmap decodeToBitMap(byte[] data, Camera.Size previewSize) {
    try {
      YuvImage image = new YuvImage(data, ImageFormat.NV21, previewSize.width,
          previewSize.height, null);
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      image.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height),
          100, stream);
      Bitmap bmp = BitmapFactory.decodeByteArray(
          stream.toByteArray(), 0, stream.size());
      stream.close();
      return bmp;
    } catch (Exception ex) {

    }
    return null;
  }

}
