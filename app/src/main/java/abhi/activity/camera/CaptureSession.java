package abhi.activity.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.abhi.enprint.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import abhi.io.PictureWriter;
import abhi.utils.Loggable;

public class CaptureSession implements Loggable {
    CameraCaptureSession captureSession;
    CaptureRequest.Builder captureRequestBuilder;
    void takePicture(MainActivity activity){
        if(new CameraDevice().cameraDevice == null) return;
        CameraManager cameraManager = (CameraManager)activity //camera2/CameraManager
                .getSystemService(Context.CAMERA_SERVICE);
        try{ //camera2/CameraCharacteristics
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(
                    new CameraDevice().cameraDevice.getId()
            );
            Size[] jpgSizes = null;
            if(cameraManager!=null) jpgSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    .getOutputSizes(ImageFormat.JPEG); //api level full

            //capture images with custom size
            int width = 640; //if jpg sizes don't return a value
            int height = 480;
            if(jpgSizes!=null && jpgSizes.length>0){
                width = jpgSizes[0].getWidth();
                height = jpgSizes[0].getHeight();
            }
            //width,height,format,max images
            ImageReader imageReader = ImageReader.newInstance(width,height,ImageFormat.JPEG,35);
            List<Surface> outputSurface = new ArrayList();
            outputSurface.add(imageReader.getSurface());
            outputSurface.add(new Surface(Main.textureView.getSurfaceTexture()));

            CaptureRequest.Builder captureBuilder = new CameraDevice().cameraDevice
                    .createCaptureRequest(android.hardware.camera2.CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());
            //auto control exposure, white balance, focus
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //check orientation based on device
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            //captureBuilder.set(CaptureResult.JPEG_ORIENTATION, new Orientation().ORIENTATIONS.get(rotation));

            new PictureWriter().setFile(new File(Environment.getExternalStorageDirectory()+File.separator+
                    UUID.randomUUID()+".jpg"));

            ImageReader.OnImageAvailableListener readerListener = (imageReader1)->{
                Image image = null;
                try{
                    image = imageReader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);
                    new PictureWriter().save(bytes);
                }catch (FileNotFoundException fnfe){
                    getLog(fnfe.getCause());
                }catch (IOException ioe){
                    getLog(ioe.getCause());
                }
                finally {//execute even if exception is thrown/caught
                    {
                        if(image!=null) image.close();
                    }
                }
            };
            imageReader.setOnImageAvailableListener(readerListener,Main.mBackgroundHandler);

            CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Toast.makeText(activity,"Capture completed",Toast.LENGTH_SHORT).show();
                    new CameraPreview().createCameraPreview();
                }
            };

            new CameraDevice().cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        session.capture(captureBuilder.build(), captureListener, Main.mBackgroundHandler);
                    }catch (CameraAccessException cae){
                        getLog(cae.getCause());
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            },Main.mBackgroundHandler);

        }catch (CameraAccessException cae){
            getLog(cae.getCause());
        }
    }
}
