package abhi.activity.camera;

import android.graphics.SurfaceTexture;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.abhi.enprint.MainActivity;
import com.abhi.enprint.R;

public class Main {

    MainActivity activity;

    private Button shutterButton;
    private TextureView textureView;

    public Main(MainActivity activity){//main main activity
        this.activity=activity;
    }

    public void onCreate(){
        textureView = activity.findViewById(R.id.textureView);
        assert textureView != null; //throws assertion error if null

        //
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                openCamera();
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
        });

        shutterButton = activity.findViewById(R.id.shutterButton);
        shutterButton.setOnClickListener(view->{
            new Capture().takePicture();
        });
    }

    private void openCamera(){}
}
