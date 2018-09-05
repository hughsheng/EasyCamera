package tl.com.ease_camera_library.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;

import com.tzutalin.dlib.VisionDetRet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by tl on 2018-9-4
 */
public class BitmapUtil {


  /**
   * 旋转图片
   *
   * @param bm     原图片
   * @param degree 角度
   * @return
   */
  public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
    Bitmap returnBm = null;

    // 根据旋转角度，生成旋转矩阵
    Matrix matrix = new Matrix();
    matrix.postRotate(degree);
    try {
      // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
      returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    } catch (OutOfMemoryError e) {
    }
    if (returnBm == null) {
      returnBm = bm;
    }
    if (bm != returnBm) {
      bm.recycle();
    }
    return returnBm;
  }


  /**
   * 镜像图片
   *
   * @param bitmap 原图片
   * @return
   */
  public static Bitmap mirrorImg(Bitmap bitmap) {
    Bitmap mirrorBitmap = null;
    mirrorBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
        bitmap.getConfig());
    if (mirrorBitmap != null) {
      Canvas canvas = new Canvas(mirrorBitmap);
      Matrix orig = new Matrix();
      orig.setScale(-1, 1);                     //翻转X
      orig.postTranslate(bitmap.getWidth(), 0);//平移
      canvas.drawBitmap(bitmap, orig, null);
    } else {
      mirrorBitmap = bitmap;
    }

    if (mirrorBitmap != bitmap) {
      bitmap.recycle();
    }

    return mirrorBitmap;
  }


  /**
   * 读取图片的旋转的角度
   *
   * @param path 图片绝对路径
   * @return 图片的旋转角度
   */
  public static int getBitmapDegree(String path) {
    int degree = 0;
    try {
      // 从指定路径下读取图片，并获取其EXIF信息
      ExifInterface exifInterface = new ExifInterface(path);
      // 获取图片的旋转信息
      int orientation = exifInterface.getAttributeInt(
          ExifInterface.TAG_ORIENTATION,
          ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          degree = 90;
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          degree = 180;
          break;
        case ExifInterface.ORIENTATION_ROTATE_270:
          degree = 270;
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return degree;
  }


  /**
   * 将图片的旋转角度置为0  ，此方法可以解决某些机型拍照后图像，出现了旋转情况
   *
   * @param path
   * @return void
   */
  public static void setPictureDegreeZero(String path) {
    try {
      ExifInterface exifInterface = new ExifInterface(path);
      // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
      // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
      exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
      exifInterface.saveAttributes();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }


  /**
   * bitmap转base64
   *
   * @param bitmap bitmap
   * @return str
   */
  public static String bitmapToBase64(Bitmap bitmap,int quality) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
    byte[] b = baos.toByteArray();
    return Base64.encodeToString(b, Base64.DEFAULT);
  }


  /**
   * base64转bitmap
   * @param base64 base64
   * @return bitmap
   */
  public static Bitmap base64ToBitmap(String base64) {
    byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
  }

  /**
   * 根据路径返回bitmap用于显示(限制最大1m)
   *
   * @param path path
   * @return bitmap
   */
  public static Bitmap getBitmapFromPath(String path) {

    if (!new File(path).exists()) {
      System.err.println("getBitmapFromPath: file not exists");
      return null;
    }

    byte[] buf = new byte[1024 * 1024];// 1M
    Bitmap bitmap = null;

    try {

      FileInputStream fis = new FileInputStream(path);
      int len = fis.read(buf, 0, buf.length);
      bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
      if (bitmap == null) {
        System.out.println("len= " + len);
        System.err.println("path: " + path + "  could not be decode!!!");
      }
    } catch (Exception e) {
      e.printStackTrace();

    }

    return bitmap;
  }


  /**
   * 根据路径获得图片并根据需要的宽高缩放返回bitmap用于显示
   *
   * @param
   * @return
   */
  public static Bitmap getSmallBitmap(String filePath, int width, int height) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(filePath, options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, width, height);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;

    return BitmapFactory.decodeFile(filePath, options);
  }


  /**
   * 根据路径添加图片文件到相册
   */
  public static void putPicToAlbum(Context context, String path) {
    Intent mediaScanIntent = new Intent(
        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(path);
    Uri contentUri = Uri.fromFile(f);
    mediaScanIntent.setData(contentUri);
    context.sendBroadcast(mediaScanIntent);
  }


  /**
   * 根据需要的宽高计算最小的缩放比例
   *
   * @param options   原图片options
   * @param reqWidth  需要的宽度
   * @param reqHeight 需要的高度
   * @return
   */
  public static int calculateInSampleSize(BitmapFactory.Options options,
                                          int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    //
    if (height > reqHeight || width > reqWidth) {

      // Calculate ratios of height and width to requested height and
      // width
      final int heightRatio = Math.round((float) height
          / (float) reqHeight);
      final int widthRatio = Math.round((float) width / (float) reqWidth);

      // Choose the smallest ratio as inSampleSize value, this will
      // guarantee
      // a final image with both dimensions larger than or equal to the
      // requested height and width.
      inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
  }


  public static byte[] resultBitmapData(VisionDetRet faceBean, Bitmap bitmap) {
    int left = faceBean.getLeft();
    int top = faceBean.getTop();
    int right = faceBean.getRight();
    int bottom = faceBean.getBottom();

    Bitmap curBitmap = cutBitmap(bitmap, left, right, top, bottom);
    Bitmap mirrorBitmap = mirrorImg(curBitmap);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    mirrorBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
    mirrorBitmap.recycle();
    curBitmap.recycle();
    return os.toByteArray();
  }


  public static Bitmap resultBitmap(VisionDetRet faceBean, Bitmap bitmap) {
    int left = faceBean.getLeft();
    int top = faceBean.getTop();
    int right = faceBean.getRight();
    int bottom = faceBean.getBottom();
    Bitmap curBitmap = cutBitmap(bitmap, left, right, top, bottom);
    return mirrorImg(curBitmap);
  }


  public static Bitmap cutBitmap(Bitmap bitmap, int left, int right, int top, int bottom) {
    if (bitmap == null) {

      return null;
    }
    int bitmapWidth = bitmap.getWidth();
    int bitmapHeight = bitmap.getHeight();
    // 定义矩阵对象
    int widthFace = Math.abs(right - left);
    int heightFace = Math.abs(bottom - top);
    Matrix matrix = new Matrix();
    // 缩放图像
    matrix.postScale(1.5f, 1.5f);

    int x;//截图X起点
    int xx;//截图X起点
    int y;//截图Y起点
    int yy;//截图Y起点
    if (left < widthFace * 0.2) {
      x = 0;
      xx = left * 2;
    } else {
      x = (int) (left - (widthFace * 0.2));
      xx = (int) (widthFace * 0.4);
    }

    if (top < heightFace * 0.33) {
      y = 0;
      yy = top * 2;
    } else {
      y = (int) (top - (heightFace * 0.33));
      yy = (int) (heightFace * 0.66);
    }

    int width;
    int height;

    width = Math.abs(right - left) + xx;
    height = Math.abs(bottom - top) + yy;

    if ((x + width) > bitmapWidth) {
      width = bitmapWidth - x;
    }

    if ((y + height) > bitmapHeight) {
      height = bitmapHeight - y;
    }

    return Bitmap.createBitmap(bitmap, x, y, width, height);
  }



}
