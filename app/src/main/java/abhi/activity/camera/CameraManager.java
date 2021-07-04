package abhi.activity.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.SparseIntArray;
import android.view.Surface;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.abhi.enprint.MainActivity;

import abhi.utils.Loggable;
import abhi.utils.Preferences;

public class CameraManager implements Loggable {
    static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        //param 1st->key, 2nd->value
        //fix inverted orientation
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void openCamera(MainActivity activity){
        android.hardware.camera2.CameraManager cameraManager =
                (android.hardware.camera2.CameraManager)activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[
                    Preferences.getInstance().getInt("pref_lens_key")]; //first logical id. usually main lens
            //get properties of id "x" such as focal length, aperture etc
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            CameraPreview.imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            try{
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            //fix accessing all files in android 11 and higher. problem with google play verification
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    }, Main.REQUEST_CAMERA_PERMISSION);
                }
                cameraManager.openCamera(cameraId, new abhi.activity.camera.CameraDevice().stateCallback, null);
            }catch (Exception e){
                openCamera(activity);
            }
        }catch (CameraAccessException cae){
            new CameraManager().getLog(cae.getCause());
        }
    }
}
