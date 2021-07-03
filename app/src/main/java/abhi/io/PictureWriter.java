package abhi.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PictureWriter {
    private static File file;
    public static void setFile(File f){
        file = f;
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
