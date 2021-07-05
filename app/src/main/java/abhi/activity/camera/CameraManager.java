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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import abhi.utils.Loggable;
import abhi.utils.Preferences;

public final class CameraManager implements Loggable {

    static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        //param 1st->key, 2nd->value
        //fix inverted orientation
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    public static String[] cameraIds;

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void openCamera(MainActivity activity){
        android.hardware.camera2.CameraManager cameraManager =
                (android.hardware.camera2.CameraManager)activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            //accessing logical id x to use x lens
            cameraIds = new CameraManager2(cameraManager).getCameraIDList();

            String cameraId = cameraIds[
                    Preferences.getInstance().getInt("pref_lens_key")];
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

    //handle logical ids operations here. do not expose to non-CameraManager classes
    private class CameraManager2 implements Loggable{

        Set<String> mCameraIDs = new HashSet<>();

        Preferences preferences = Preferences.getInstance();

        @RequiresApi(api = Build.VERSION_CODES.R)
        CameraManager2(android.hardware.camera2.CameraManager cameraManager){
            BiPredicate<Integer, Object> removeLens = (i,o)->i==4 && "2.1762.421".equals(o);
            BiPredicate<String, List<String>> checkCaps = (s,l)->{
                if(l.size()==0) return false;
                AtomicBoolean caps = new AtomicBoolean(false);
                l.forEach(e->{
                    if(e.equals(s)){
                        caps.set(true);
                        return;
                    }
                });
                return caps.get();
            };
            final List<String> cams = new ArrayList<>();
            Consumer<android.hardware.camera2.CameraManager> getCameraID = c-> IntStream.range(0,121)
                    .forEach(cam->{
                try {
                    CameraCharacteristics characteristics = cameraManager
                            .getCameraCharacteristics(String.valueOf(cam));
                    if(characteristics!=null){
                        StringBuilder sb = new StringBuilder();
                        float focalLength = ((float[]) characteristics
                                .get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS))[0];
                        sb.append(focalLength);
                        float aperture = ((float[]) characteristics
                                .get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES))[0];
                        sb.append(aperture);
                        int aeModes = ((int[]) characteristics
                                .get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES)).length;
                        sb.append(aeModes);
                        int facing = (Integer) characteristics
                                .get(CameraCharacteristics.LENS_FACING);
                        sb.append(facing);
                        String props = sb.toString();
                        if(removeLens.test(cam, props)) props = "6.952.4441";
                        if(!checkCaps.test(props, cams)){
                            cams.add(props);
                            mCameraIDs.add(String.valueOf(cam));
                        }
                    }
                }catch (Exception e){
                    getLog(e.getCause());
                }
            });
            if(preferences.getInt("pref_enable_camera_key") == 0){
                getCameraID.accept(cameraManager);
                save();
                return; //terminate the constructor
            }
            mCameraIDs = preferences.getListSet("pref_list_camera_key");
        }

        String[] getCameraIDList(){
            Set<String> set = mCameraIDs;
            String[] stArr = (String[]) set.toArray(new String[set.size()]);
            int[] iArr = new int[stArr.length];
            for(int i=0;i<stArr.length;i++){
                iArr[i] = Integer.parseInt(stArr[i]);
            }
            Arrays.sort(iArr);
            for(int i2=0;i2<stArr.length;i2++){
                stArr[i2]=String.valueOf(iArr[i2]);
            }
            return stArr;
        }

        void save(){
            preferences.setValue("pref_enable_camera_key","1");
            preferences.setList("pref_list_camera_key",mCameraIDs);
        }
    }
}
