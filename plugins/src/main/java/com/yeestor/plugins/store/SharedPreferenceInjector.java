package com.yeestor.plugins.store;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SharedPreferenceInjector implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "SharedPrefrenceInjector" ;

    private static SharedPreferences sharedPreferences;

    private final HashMap<String,Field> fields = new HashMap<>() ;

    public static List<Context> bindingContext = new ArrayList<>();



    public static void injectSP(Context context) {
        SPFile file = context.getClass().getAnnotation(SPFile.class) ;

        if(file != null) {
            sharedPreferences = context.getSharedPreferences(file.name(), Context.MODE_PRIVATE);

            setValue(context,sharedPreferences);
            sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                }
            });

        }
    }


    public static void setValue(Context context ,SharedPreferences sharedPreferences){
        SPFile file = context.getClass().getAnnotation(SPFile.class) ;

        if(file != null) {
            Class clazz = context.getClass() ;
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(SPProperty.class)) {
                    SPProperty set = field.getAnnotation(SPProperty.class);
                    field.setAccessible(true);

                    Log.d(TAG, "inject: -- " + field.getName());
                    try {

                        String key = set.name().isEmpty() ? field.getName() : set.name() ;
                        String value = sharedPreferences.getString(key,set.value());
                        field.set(context,  value);


                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



    public static void inject(Application instance) {
        SPFile file = instance.getClass().getAnnotation(SPFile.class) ;

        if(file != null) {
            sharedPreferences = instance.getSharedPreferences(file.name(), Context.MODE_PRIVATE);

            Class[] clazzes = file.stores();

            for (Class clazz : clazzes) {

                Field[] fields = clazz.getDeclaredFields();


                for (Field field : fields) {
                    if (field.isAnnotationPresent(SPProperty.class)) {
                        SPProperty set = field.getAnnotation(SPProperty.class);
                        field.setAccessible(true);

                        Log.d(TAG, "inject: -- " + field.getName() );
                    }
                }
            }


        }


    }


    public void init(){

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
