package abhi.utils;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Supplier;

public final class Preferences implements Loggable{

    private static Preferences preferences;

    private Preferences(){ //blocking constructor from being accessed directly

    }

    //singleton access for constructor
    public static Preferences getInstance(){
        if (preferences == null) preferences=new Preferences();
        return preferences;
    }

    public int getInt(String key){
        return Integer.parseInt(getString(key));
    }

    public float getFloat(String key){
        return Float.parseFloat(getString(key));
    }

    public double getDouble(String key){
        return Double.parseDouble(getString(key));
    }

    public long getLong(String key){
        return Long.parseLong(getString(key));
    }

    public boolean getBoolean(String key){
        return Boolean.parseBoolean(getString(key));
    }

    public String getString(String key){
        //2nd arg->default value if key value unset or illegal value set
        return sharedPreferences.getString(key, "0");
    }

    public void setValue(String key, String value){
        sharedPreferences.edit().putString(key, value).apply(); //works for all datatypes
    }

    public void setList(String key, Set set){
        sharedPreferences.edit().putStringSet(key, set).apply();
    }

    public Set getListSet(String key){
        return sharedPreferences.getStringSet(key, null); //no default values
    }

    //accessing private AppGlobals class using reflection
    private final Supplier<Context> getContext = ()->{
        //position of the supplier matters. cant be accessed above this
        Context context = null;
        try{
            Method method = Class.forName("android.app.AppGlobals").getMethod("getInitialApplication");
            Application application = (Application) method.invoke(null);
            assert application != null;
            String packageName = application.getPackageName();
            context = application.createPackageContext(packageName,Context.CONTEXT_INCLUDE_CODE)
                    .getApplicationContext();
        }catch (Exception e){
            getLog(e.getCause());
        }
        return context;
    };
    //position of this variable does not matter
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext.get());
}
