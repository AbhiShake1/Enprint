package abhi.utils;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Toast implements Loggable{
    private static Toast toast;
    private Toast(){

    }
    public static Toast getInstance(){
        if(toast==null) toast=new Toast();
        return toast;
    }

    private Context getContext(){
        Context context = null;
        try {
            Method c = Preferences.class.getMethod("getContext");
            c.setAccessible(true);
            context = (Context) c.invoke(Preferences.getInstance());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            getLog(e.getCause());
        }
        assert context!=null;
        return context;
    }

    public <T> void show(T info){
        android.widget.Toast.makeText(getContext(),String.valueOf(info), android.widget.Toast.LENGTH_SHORT).show();
    }
}
