package tl.com.easycamera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tl.com.ease_camera_library.camera2.Camera2Fragment;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Camera2Fragment  camera2Fragment = Camera2Fragment.newInstance();
    if (null == savedInstanceState) {
      getFragmentManager().beginTransaction()
          .replace(R.id.container, camera2Fragment)
          .commit();
    }
  }

}
