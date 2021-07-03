package abhi.activity.camera;

import androidx.annotation.NonNull;

public class CameraDevice {
    static android.hardware.camera2.CameraDevice cameraDevice;

    android.hardware.camera2.CameraDevice.StateCallback stateCallback = new android.hardware.camera2.CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull android.hardware.camera2.CameraDevice camera) {
            cameraDevice=camera;
            new CameraPreview().createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull android.hardware.camera2.CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull android.hardware.camera2.CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice=null;
        }
    };
}
