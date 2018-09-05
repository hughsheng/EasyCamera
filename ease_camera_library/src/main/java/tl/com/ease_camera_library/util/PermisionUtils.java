package tl.com.ease_camera_library.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermisionUtils {

  // Storage Permissions
  public static final int REQUEST_EXTERNAL_STORAGE = 0x999;
  public static final int REQUEST_CAMERA = 0x998;
  private static final String[] PERMISSIONS_STORAGE = {
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE};
  private static final String CAMERA = Manifest.permission.CAMERA;

  /**
   * 申请读写权限
   *
   * @param activity activity
   */
  public static void verifyStoragePermissions(Activity activity) {
    // Check if we have write permission
    int permission = ActivityCompat.checkSelfPermission(activity,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);

    if (permission != PackageManager.PERMISSION_GRANTED) {
      // We don't have permission so prompt the user
      ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
          REQUEST_EXTERNAL_STORAGE);
    }
  }

  /**
   * 申请相机权限
   *
   * @param activity activity
   */
  public static void verifyCameraPermissions(Activity activity) {

    int permission = ActivityCompat.checkSelfPermission(activity, CAMERA);

    if (permission != PackageManager.PERMISSION_GRANTED) {
      // We don't have permission so prompt the user
      ActivityCompat.requestPermissions(activity, new String[]{CAMERA},
          REQUEST_CAMERA);
    }
  }


  /**
   * 是否有读权限
   *
   * @param context context
   * @return b
   */
  public static boolean hasReadPermission(Context context) {
    int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission
        .READ_EXTERNAL_STORAGE);
    return permission == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * 是否有写权限
   *
   * @param context
   * @return
   */
  public static boolean hasWritePermission(Context context) {
    int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission
        .WRITE_EXTERNAL_STORAGE);
    return permission == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * 是否有相机权限
   *
   * @param context
   * @return
   */
  public static boolean hasCameraPermission(Context context) {
    int permission = ActivityCompat.checkSelfPermission(context, CAMERA);
    return permission == PackageManager.PERMISSION_GRANTED;
  }
}
