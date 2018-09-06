// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license
// information.

package tl.com.ease_camera_library.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.tzutalin.dlib.VisionDetRet;

import java.util.List;

/**
 * Created by xiekang on 1/23/2018.
 */

/**
 * 描绘脸部位置
 */
public class FaceOverlayView extends View {

  private Paint mPaint;
  private Paint mTextPaint;
  private int mDisplayOrientation;
  private int previewWidth;
  private int previewHeight;
  private int opendCameraID;//默认打开的是后置摄像头
  private List<VisionDetRet> face;


  public FaceOverlayView(Context context, Camera.Size previewSize) {
    super(context);
    initialize(previewSize);
  }

  private void initialize(Camera.Size previewSize) {
    // We want a green box around the face:
    DisplayMetrics metrics = getResources().getDisplayMetrics();

    int stroke = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setDither(true);
    mPaint.setColor(Color.GREEN);
    mPaint.setStrokeWidth(stroke);
    mPaint.setStyle(Paint.Style.STROKE);

    mTextPaint = new Paint();
    mTextPaint.setAntiAlias(true);
    mTextPaint.setDither(true);
    int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, metrics);
    mTextPaint.setTextSize(size);
    mTextPaint.setColor(Color.GREEN);
    mTextPaint.setStyle(Paint.Style.FILL);
    previewWidth= previewSize.width;
    previewHeight=previewSize.height;
  }

  public void setFaces(List<VisionDetRet> faces) {
    face = faces;
    invalidate();
  }

  public void setOpendCameraID(int opendCameraID) {
    this.opendCameraID = opendCameraID;
  }

  public void setDisplayOrientation(int displayOrientation) {
    mDisplayOrientation = displayOrientation;
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (face != null && face.size() > 0) {
      float scaleX = getScaleX();
      float scaleY = getScaleY();
      for (VisionDetRet visionDetRet : face) {
        if (opendCameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
          canvas.drawRect(visionDetRet.getLeft() *scaleX, visionDetRet.getTop() * scaleY,
              visionDetRet.getRight() * scaleX, visionDetRet.getBottom() * scaleY, mPaint);
        } else if (opendCameraID == Camera.CameraInfo.CAMERA_FACING_FRONT) {
          canvas.drawRect(getWidth() - visionDetRet.getRight() * scaleX, visionDetRet.getTop() *
              scaleY, getWidth() - visionDetRet.getLeft() * scaleX, visionDetRet.getBottom() *
              scaleY, mPaint);
        }

        canvas.save();
        canvas.restore();
      }
    }
  }

  public float getScaleX() {
    float scaleX = (float) getWidth() / (float) previewWidth;
    switch (mDisplayOrientation) {
      case 90:
      case 270:
        scaleX = (float) getWidth() / (float) previewHeight;
        break;
    }
    return scaleX;
  }

  public float getScaleY() {
    float scaleY = (float) getHeight() / (float) previewHeight;
    switch (mDisplayOrientation) {
      case 90:
      case 270:
        scaleY = (float) getHeight() / (float) previewWidth;
        break;
    }
    return scaleY;
  }
}