package com.yeestor.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.yeestor.plugins.store.SPFile;
import com.yeestor.plugins.store.SPProperty;
import com.yeestor.plugins.store.SharedPrefrenceInjector;

@SPFile(stores = {})
public class MainActivity extends AppCompatActivity {


    private static final String TAG = "PluginDemo" ;

    @SPProperty(value = "123")
    public String name ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPrefrenceInjector.injectSP(this);

        Log.d(TAG, "onCreate:  ---- " + name);



    }



}
