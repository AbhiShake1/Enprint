package abhi.activity.camera;

import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.TextureView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.abhi.enprint.MainActivity;
import com.abhi.enprint.R;

import abhi.utils.Loggable;

public class Main implements Loggable {

    private final MainActivity activity;

    public static TextureView textureView;

    public static final int REQUEST_CAMERA_PERMISSION = 200;

    static Handler mBackgroundHandler;
    static HandlerThread mBackgroundThread;

    public Main(MainActivity activity){//main main activity
        this.activity=activity;
    }

    public void onCreate(){
        textureView = activity.findViewById(R.id.textureView);
        assert textureView != null; //throws assertion error if null

        //
        textureView.setSurfaceTextureListener(textureListener);
        //checkPermission();

        Button shutterButton = activity.findViewById(R.id.shutterButton);
        shutterButton.setOnClickListener(view->new CaptureSession().takePicture(activity));
    }
    /*
    private void browse() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/xml");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                0);
    }*/

    public TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            new CameraManager().openCamera(activity);
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void reOpen(){
        if(textureView.isAvailable()){
            new CameraManager().openCamera(activity);
        }else{
            textureView.setSurfaceTextureListener(textureListener);
        }
    }
    public void startBackgroundThread(){
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start(); //run in separate parallel thread
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    public void stopBackgroundThread(){
        mBackgroundThread.quitSafely(); //wait for background process to complete then quit
        //reset values
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        }catch (InterruptedException ie){
            getLog(ie.getCause());
        }
    }
}
