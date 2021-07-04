package abhi.io;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PictureWriter {
    private static File file;

    public static void saveToFile(){
        File saveFolder = new File(Environment.getExternalStorageDirectory()+File.separator+
                Environment.DIRECTORY_DCIM);
        file = new File(saveFolder+File.separator+"Enprint"+File.separator+"IMG_ENPRINT_"
                +System.currentTimeMillis()+"_"+ Build.DEVICE +".jpg");
        if(!saveFolder.exists()){
            saveFolder.mkdirs();
        }
    }
    
    public static File getFile(){
        return file;
    }

    public void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
        }finally {
            if(outputStream!=null) outputStream.close();
        }
    }
}
