package abhi.ui.button;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.abhi.enprint.MainActivity;

import java.lang.reflect.Field;

import abhi.utils.Loggable;
import abhi.utils.Preferences;

public class CameraSwitchButton extends Main implements Loggable {

    //super class already invokes init(non constructor)
    public CameraSwitchButton(@NonNull Context context) {
        super(context);
    }

    public CameraSwitchButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraSwitchButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onClick() {
        int state = Preferences.getInstance().getInt("pref_lens_key");
        state=(state+1)%2;
        Preferences.getInstance().setValue("pref_lens_key",String.valueOf(state));
        new abhi.activity.camera.Main(getActivity()).reOpen();
        //int[] backgrounds = {R.drawable.ic_launcher_background,R.drawable.ic_launcher_foreground};
        //setBackgroundResource(backgrounds[state]);
    }

    private MainActivity getActivity(){
        MainActivity activity = null;
        try{
            Field a = MainActivity.class.getDeclaredField("activity");
            a.setAccessible(true);
            activity = (MainActivity) a.get(null);
        }catch (Exception e){
            getLog(e.getCause());
        }
        return activity;
    }
}
