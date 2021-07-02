package abhi.utils;


public class Preferences {
    //singleton class
    private static Preferences preferences;

    //creating a node tree to store values and make them persistent
    private java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(Preferences.class);

    private Preferences(){ //blocking constructor from being accessed directly

    }

    public static Preferences getInstance(){ //singleton access
        if (preferences == null) preferences=new Preferences();
        return preferences;
    }

    public int getInt(String key){
        return prefs.getInt(key, 0); //2nd arg->default value if key is unset
    }

    public float getFloat(String key){
        return prefs.getFloat(key, 0.0f);
    }

    public double getDouble(String key){
        return prefs.getDouble(key, 0.0);
    }

    public long getLong(String key){
        return prefs.getLong(key, 0L);
    }

    public boolean getBoolean(String key){
        return prefs.getBoolean(key, false);
    }

    public String getString(String key){
        return prefs.get(key, "");
    }

    public void setInt(String key, int value){
        prefs.putInt(key, value); //value will be forced when function runs
    }

    public void setFloat(String key, float value){
        prefs.putFloat(key, value);
    }

    public void setDouble(String key, double value){
        prefs.putDouble(key, value);
    }

    public void setLong(String key, long value){
        prefs.putLong(key, value);
    }

    public void setBoolean(String key, boolean value){
        prefs.putBoolean(key, value);
    }

    public void setString(String key, String value){
        prefs.put(key, value);
    }
}
