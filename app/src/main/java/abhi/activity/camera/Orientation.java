package abhi.activity.camera;

import android.util.SparseIntArray;
import android.view.Surface;

public class Orientation {
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        //1st param->key, 2nd->value
        //fix inverted orientation
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
}
