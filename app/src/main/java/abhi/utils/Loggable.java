package abhi.utils;

import android.util.Log;

import java.lang.reflect.Array;

public interface Loggable {

    default String TAG(){
        return this.getClass().getName(); //getting class name with full path
    }

    default <T> void getLog(T... msg) {
        for(T val : msg){
            if(!val.getClass().isArray()){
                Log.i(TAG(), String.valueOf(val)); //if array
            }else{
                for(int i=0;i< Array.getLength(val);i++){
                    Log.i(TAG(), String.valueOf(Array.get(val,i)));
                }
            }
        }
    }
}
