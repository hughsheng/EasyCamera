package tl.com.easycamera;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tl.com.ease_camera_library.camera1.CameraFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
    CameraFragment.CameraFragmentListener {
  private TextView qr_code_tv;
  private TextView capture_tv;
  private TextView photography_tv;
  private TextView face_tv;
  private ImageView album_iv;
  private ImageView camera_iv;
  private ImageView change_camera_iv;
  private CameraFragment cameraFragment;
  private ImageView capture_iv;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_camera);
    cameraFragment = CameraFragment.newInstance();
    if (null == savedInstanceState) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.container, cameraFragment)
          .commit();
    }
    initView();
    setListener();
  }


  private void initView() {
    qr_code_tv = findViewById(R.id.qr_code_tv);
    capture_tv = findViewById(R.id.capture_tv);
    photography_tv = findViewById(R.id.photography_tv);
    face_tv = findViewById(R.id.face_tv);
    album_iv = findViewById(R.id.album_iv);
    camera_iv = findViewById(R.id.camera_iv);
    change_camera_iv = findViewById(R.id.change_camera_iv);
    capture_iv = findViewById(R.id.capture_iv);
  }


  private void setListener() {
    qr_code_tv.setOnClickListener(this);
    capture_tv.setOnClickListener(this);
    photography_tv.setOnClickListener(this);
    face_tv.setOnClickListener(this);
    album_iv.setOnClickListener(this);
    camera_iv.setOnClickListener(this);
    change_camera_iv.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()) {
      case R.id.qr_code_tv:
        cameraFragment.qrMode();
        Toast.makeText(this, "开始识别二维码", Toast.LENGTH_SHORT).show();
        break;

      case R.id.capture_tv:
        cameraFragment.photoMode();
        break;

      case R.id.photography_tv:

        break;

      case R.id.face_tv:
        Toast.makeText(this, "开始检测人脸", Toast.LENGTH_SHORT).show();
        cameraFragment.faceMode();
        break;

      case R.id.album_iv:

        break;

      case R.id.camera_iv:
        cameraFragment.takePhoto();
        break;

      case R.id.change_camera_iv:
        cameraFragment.changeCamera();
        break;
    }

  }


  /**
   * 如果需要拍照完马上显示照片请实现该接口
   *
   * @param photoBitmap bitmap
   */
  @Override
  public void showPhoto(final Bitmap photoBitmap) {
    capture_iv.setImageBitmap(photoBitmap);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        clearPic();
      }
    }, 3000);
  }

  /**
   * 清空照片
   */
  private void clearPic() {
    BitmapDrawable bitmapDrawable = (BitmapDrawable) capture_iv.getBackground();
    if (bitmapDrawable != null) {
      Bitmap hisBitmap = bitmapDrawable.getBitmap();
      if (!hisBitmap.isRecycled()) {
        hisBitmap.recycle();
      }
    }
    capture_iv.setImageDrawable(null);
  }
}
