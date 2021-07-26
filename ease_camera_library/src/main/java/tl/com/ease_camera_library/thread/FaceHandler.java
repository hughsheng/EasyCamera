package tl.com.ease_camera_library.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tl.com.ease_camera_library.camera1.CameraManager;
import tl.com.ease_camera_library.util.BitmapUtil;
import tl.com.ease_camera_library.util.CameraUtil;
import tl.com.ease_camera_library.util.ConstanceValues;

/**
 * Created by tl on 2018-9-4
 */
public class FaceHandler extends Handler {
  private CameraManager cameraManager;
  private Handler resultHandler;
  //private List<VisionDetRet> detect;
  //private FaceDet mFaceDet;
 // private List<VisionDetRet> faceBeanList;//一次连拍照片集合
  private Bitmap photoBitmap;
  private Context context;

  /**
   * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
   */
  private LruCache<String, Bitmap> mMemoryCache;


  public FaceHandler(Context context, CameraManager cameraManager, Handler resultHandler) {
    this.context = context;
    this.cameraManager = cameraManager;
    this.resultHandler = resultHandler;
    setLruCache();
  }

  @Override
  public void handleMessage(Message msg) {
//    switch (msg.what) {
//      case ConstanceValues.FACE_DETECH:
//        byte[] data = (byte[]) msg.obj;
//        photoBitmap = CameraUtil.getProperBitmap(data, context, cameraManager.getPreviewSize(),
//            cameraManager.getOpendCameraID());
//        if (photoBitmap == null) {
//          resultHandler.sendEmptyMessage(ConstanceValues.FACE_NO_PHOTO);//未检测到图像
//          break;
//        }
//        if (mFaceDet == null) {
//          mFaceDet = new FaceDet(Constants.getFaceShapeModelPath());
//        }
//        detect = mFaceDet.detect(photoBitmap);//检测人脸
//         //String base64=BitmapUtil.bitmapToBase64(photoBitmap,100 );
//        if (detect == null || detect.size() == 0) {
//          photoBitmap.recycle();
//          resultHandler.sendEmptyMessage(ConstanceValues.FACE_FAIL);//未检测到人脸
//          break;
//        }
//
//        Message drawMessage = resultHandler.obtainMessage(ConstanceValues.FACE_DRAW, detect);
//        resultHandler.sendMessage(drawMessage);//绘制人脸框
//
//        VisionDetRet faceBean = CameraUtil.getMaxFace(detect);//获取最大人脸
//        if (faceBeanList == null) {
//          faceBeanList = new ArrayList<VisionDetRet>();
//        }
//        faceBeanList.add(faceBean);//保存获取到的人脸数据
//        break;
//
//      case ConstanceValues.FACE_DRAW_OVER:
//
//        //              if (faceBeanList.size() < ConstantValue.FACENUM) {
////                continue;
////              }
//
//
////              if (!CameraUtil.isFaceClear(faceBeanList)) {//判断人脸是否清晰
////                faceBeanList.clear();
////                handler.sendEmptyMessage(ConstantValue.HANDLER_FACE_UNCLEAR);
////                continue;
////              }
//
//        VisionDetRet faceBeanEnd = faceBeanList.get(faceBeanList.size() - 1);
//        Map scaleMap = (Map) msg.obj;
//        //判断人脸是否完整
//        if (!CameraUtil.isGetfitFace(faceBeanEnd, (float) scaleMap.get("scaleX"), (float)
//            scaleMap.get("scaleY"), (float) scaleMap.get("screenWidth"), (float) scaleMap.get
//            ("screenHeight"))) {
//          faceBeanList.clear();
//          photoBitmap.recycle();
//          resultHandler.sendEmptyMessage(ConstanceValues.FACE_IMPERFECT);
//        } else {
//          byte[] padImgBytes = BitmapUtil.resultBitmapData(faceBeanEnd, photoBitmap);//获取人脸照片流
//          faceBeanList.clear();
////              ConstantValue.FACE_IMAGE = padImgBytes;
////              ConstantValue.FULL_IMAGE = CommonUtil.bitmapToByte(bitmap);
//
//          mMemoryCache.put(ConstanceValues.FACE_CACHE, BitmapUtil.resultBitmap(faceBeanEnd,
//              photoBitmap));
//          mMemoryCache.put(ConstanceValues.FULL_CACHE, BitmapUtil.mirrorImg(photoBitmap));
//          Message successMessage = resultHandler.obtainMessage(ConstanceValues.FACE_SUCCESS,
//              mMemoryCache);
//          resultHandler.sendMessage(successMessage);
//          photoBitmap.recycle();
//        }
//
//        break;
//    }
  }

  //原生的人脸检测api效果很差，建议不要使用
//  private boolean detectionFace(Bitmap b) {
//    // 检测前必须转化为RGB_565格式。文末有详述连接
//    Bitmap bitmap = b.copy(Bitmap.Config.RGB_565, true);
//    b.recycle();
//    // 设定最大可查的人脸数量
//    int MAX_FACES = 5;
//    FaceDetector faceDet = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACES);
//    // 将人脸数据存储到faceArray 中
//    FaceDetector.Face[] faceArray = new FaceDetector.Face[MAX_FACES];
//    // 返回找到图片中人脸的数量，同时把返回的脸部位置信息放到faceArray中，过程耗时
//    int findFaceCount = faceDet.findFaces(bitmap, faceArray);
//    if(findFaceCount>0){
//      // 获取传回的脸部数组中的第一张脸的信息
//      FaceDetector.Face face1 = faceArray[0];
//      // 获取双眼的中心点，用一个PointF来接收其x、y坐标
//      PointF point = new PointF();
//      face1.getMidPoint(point);
//      // 获取该部位为人脸的可信度，0~1
//      float confidence = face1.confidence();
//      // 获取双眼间距
//      float eyesDistance = face1.eyesDistance();
//      // 获取面部姿势
//      // 传入X则获取到x方向上的角度，传入Y则获取到y方向上的角度，传入Z则获取到z方向上的角度
//      float angle = face1.pose(FaceDetector.Face.EULER_X);

//    }
//
//    return findFaceCount > 0;
//  }

  private void setLruCache() {
    // 获取应用程序最大可用内存
    int maxMemory = (int) Runtime.getRuntime().maxMemory();
    int cacheSize = maxMemory / 8;
    // 设置图片缓存大小为程序最大可用内存的1/8
    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {};
  }
}
