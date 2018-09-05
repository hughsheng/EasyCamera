// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license
// information.

package tl.com.ease_camera_library.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.tzutalin.dlib.VisionDetRet;

import java.util.List;

import tl.com.ease_camera_library.util.ConstanceValues;

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
  private int mOrientation;
  private int previewWidth;
  private int previewHeight;
  //    private FaceResult[] mFaces;
//    private List<FacePositionBean.FaceBean> face;
  private List<VisionDetRet> face;
  private double fps;
  private boolean isFront = false;

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

  public void setFPS(double fps) {
    this.fps = fps;
  }

  //    public void setFaces(FaceResult[] faces) {
//        mFaces = faces;
//        invalidate();
//    }
//    public void setFaces(List<FacePositionBean.FaceBean> faces) {
//        face = faces;
//        invalidate();
//    }

  public void setFaces(List<VisionDetRet> faces) {
    face = faces;
    invalidate();
  }

  public void setOrientation(int orientation) {
    mOrientation = orientation;
  }

  public void setDisplayOrientation(int displayOrientation) {
    mDisplayOrientation = displayOrientation;
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (face != null && face.size() > 0) {
      float scaleX = (float) getWidth() / (float) previewWidth;
      float scaleY = (float) getHeight() / (float) previewHeight;
      switch (mDisplayOrientation) {
        case 90:
        case 270:
          scaleX = (float) getWidth() / (float) previewHeight;
          scaleY = (float) getHeight() / (float) previewWidth;
          break;
      }
      canvas.save();
      canvas.rotate(-mOrientation);
      RectF rectF = new RectF();
      for (VisionDetRet visionDetRet : face) {
        rectF.set(new RectF(visionDetRet.getLeft() * scaleX, visionDetRet.getTop() * scaleY,
            visionDetRet.getRight() * scaleX, visionDetRet.getBottom() * scaleY));
        if (isFront) {
          float left = rectF.left;
          float right = rectF.right;
          rectF.left = getWidth() - right;
          rectF.right = getWidth() - left;
        }
        canvas.drawRect(rectF, mPaint);
        canvas.save();
        canvas.restore();
      }
    }
  }

  public void setPreviewWidth(int previewWidth) {
    this.previewWidth = previewWidth;
  }

  public void setPreviewHeight(int previewHeight) {
    this.previewHeight = previewHeight;
  }

  public void setFront(boolean front) {
    isFront = front;
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