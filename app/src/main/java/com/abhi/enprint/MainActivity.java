package com.abhi.enprint;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import abhi.activity.camera.Main;
import abhi.utils.Preferences;

public class MainActivity extends AppCompatActivity {

    private static MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_main);
        new Main(this).onCreate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Main.REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission required to access camera", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Main cameraActivity = new Main(this);
        cameraActivity.startBackgroundThread();
        Main.textureView.setSurfaceTextureListener(new Main(this).textureListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Main(this).stopBackgroundThread();
    }
}
