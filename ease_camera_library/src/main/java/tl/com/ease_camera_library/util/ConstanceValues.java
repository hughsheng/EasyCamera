package tl.com.ease_camera_library.util;

/**
 * Created by tl on 2018-9-4
 */
public class ConstanceValues {


  // Camera nv21格式预览帧的尺寸，默认设置640*480
  public static final int PREVIEW_WIDTH = 640;
  public static final int PREVIEW_HEIGHT = 480;
  //限制人脸在屏幕中的位置
  public static final int POSITION_LIMIT = 20;
  //允许人脸的最小面积
  public static final int FACE_MIN_AREA = 8100;
  //允许人脸偏移量
  public static final int MOVELIMIT = 50;
  //设置抓取照片数
  public static final int FACENUM = 2;

  public static String FACE_CACHE = "face_cache";

  public static String FULL_CACHE = "full_cache";

  public static final int QR_DETECH = 0x100;//二维码检测

  public static final int QR_SUCCESS = 0x101;//二维码检测成功

  public static final int QR_QUIET = 0x102;

  public static final int QR_FAIL = 0x103;//二维码检测失败

  public static final int FACE_DETECH = 0x200;//人脸检测

  public static final int FACE_SUCCESS = 0x201;//人脸检测成功

  public static final int FACE_FAIL = 0x203;//人脸识别失败

  public static final int FACE_NO_PHOTO = 0x204;//未获取到图片

  public static final int FACE_DRAW = 0x205;//画出人脸框

  public static final int FACE_DRAW_OVER = 0x206;//人脸框描绘完毕

  public static final int FACE_UNCLEAR = 0x207;//人脸不清晰

  public static final int FACE_IMPERFECT = 0x208;//人脸不完整

  public static final int FACE_QUIET = 0x202;
}
