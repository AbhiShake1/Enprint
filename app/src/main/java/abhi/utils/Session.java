package abhi.utils;


import com.abhi.enprint.MainActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Session implements Loggable{
    private static Session session;
    private Session(){

    }
    public static Session getInstance(){
        if (session==null) session = new Session();
        return session;
    }

    private MainActivity getMainActivity(){
        MainActivity activity = null;
        //accessing private variable
        try {
            Field mainActivity = MainActivity.class.getDeclaredField("activity");
            mainActivity.setAccessible(true);
            activity = (MainActivity) mainActivity.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLog(e.getCause());
        }
        assert activity != null; //check if variable was successfully accessed
        return activity;
    }

    public void restartMainActivity(){
        MainActivity activity = getMainActivity();
        activity.finish();
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0,0);
    }
}
