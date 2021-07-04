package abhi.activity.camera;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.util.Size;
import android.view.Surface;
import androidx.annotation.NonNull;
import java.util.Collections;
import abhi.utils.Loggable;

public class CameraPreview  implements Loggable {
    static Size imageDimension;

    void createCameraPreview() {
        try{
            SurfaceTexture texture = Main.textureView.getSurfaceTexture();
            assert texture!=null;
            texture.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
            Surface surface = new Surface(texture);
            CaptureSession captureSession = new CaptureSession();
            CameraDevice cameraDevice = new CameraDevice();
            captureSession.captureRequestBuilder = CameraDevice.cameraDevice
                    .createCaptureRequest(android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW);
            captureSession.captureRequestBuilder.addTarget(surface);
            CameraDevice.cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if(CameraDevice.cameraDevice ==null) return;
                    captureSession.captureSession=session;
                    updatePreview(cameraDevice, captureSession);
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    getLog("configuration failed for capture session state callback");
                }
            }, null); //handler:null
        }catch (CameraAccessException cae){
            getLog(cae.getCause());
        }
    }

    private void updatePreview(CameraDevice cameraDevice, CaptureSession captureSession){
        Thread previewThread = new Thread(()->{ //avoid a slight delay after capture completes
            if (cameraDevice == null) getLog("camera device null. preview can not be updated");
            captureSession.captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
            try{
                captureSession.captureSession.setRepeatingRequest(captureSession.captureRequestBuilder.build(),
                        null, Main.mBackgroundHandler);
            }catch (CameraAccessException cae){
                getLog(cae.getCause());
            }
        });
        previewThread.start();
    }
}
