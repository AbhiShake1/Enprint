package abhi.activity.camera.ui;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;

import com.abhi.enprint.MainActivity;

import java.lang.reflect.Field;

import abhi.activity.camera.CameraManager;
import abhi.utils.Preferences;

public class CameraSwitchButton extends androidx.appcompat.widget.AppCompatButton {
    @RequiresApi(api = Build.VERSION_CODES.R)
    public CameraSwitchButton(Context context) {
        super(context);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public CameraSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public CameraSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private static int state = Preferences.getInstance().getInt("pref_lens_key");

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void init(){
        //has bug and needs reboot. #todo: fix
        setOnClickListener((c)->{
            state=(state+1)%2;
            Preferences.getInstance().setValue("pref_lens_key",String.valueOf(state));
            new CameraManager().openCamera(getActivity());
        });
    }

    private MainActivity getActivity(){
        MainActivity activity = null;
        try{
            Field a = MainActivity.class.getDeclaredField("activity");
            a.setAccessible(true);
            activity = (MainActivity) a.get(null);
        }catch (Exception e){}
        return activity;
    }
}
