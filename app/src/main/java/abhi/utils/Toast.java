package abhi.utils;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public final class Toast implements Loggable{
    private static Toast toast;
    private Toast(){

    }
    public static Toast getInstance(){
        if(toast==null) toast=new Toast();
        return toast;
    }

    private final Supplier<Context> getContext =()->{
        Context context = null;
        try{
            Field c = Preferences.class.getDeclaredField("getContext");
            c.setAccessible(true);
            context = (Context) c.get(Preferences.getInstance());
        }catch (IllegalAccessException | NoSuchFieldException e){
            getLog(e.getCause());
        }
        assert context!=null;
        return context;
    };

    public <T> void show(T info){
        android.widget.Toast.makeText(getContext.get(),String.valueOf(info), android.widget.Toast.LENGTH_SHORT).show();
    }
}
