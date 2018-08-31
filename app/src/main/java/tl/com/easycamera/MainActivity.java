package tl.com.easycamera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tl.com.ease_camera_library.camera1.CameraFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView qr_code_tv;
  private TextView capture_tv;
  private TextView photography_tv;
  private TextView face_tv;
  private ImageView album_iv;
  private ImageView camera_iv;
  private ImageView change_camera_iv;
  private CameraFragment cameraFragment;

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
    qr_code_tv = findViewById(tl.com.ease_camera_library.R.id.qr_code_tv);
    capture_tv = findViewById(tl.com.ease_camera_library.R.id.capture_tv);
    photography_tv = findViewById(tl.com.ease_camera_library.R.id.photography_tv);
    face_tv = findViewById(tl.com.ease_camera_library.R.id.face_tv);
    album_iv = findViewById(tl.com.ease_camera_library.R.id.album_iv);
    camera_iv = findViewById(tl.com.ease_camera_library.R.id.camera_iv);
    change_camera_iv = findViewById(tl.com.ease_camera_library.R.id.change_camera_iv);
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

        break;

      case R.id.capture_tv:

        break;

      case R.id.photography_tv:

        break;

      case R.id.face_tv:

        break;

      case R.id.album_iv:

        break;

      case R.id.camera_iv:

        break;

      case R.id.change_camera_iv:
        cameraFragment.changeCamera();
        break;
    }

  }
}
