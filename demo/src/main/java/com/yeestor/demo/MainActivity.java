package com.yeestor.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yeestor.plugins.smartinfo.SmartInfoReader;
import com.yeestor.plugins.store.SPFile;
import com.yeestor.plugins.store.SPProperty;
import com.yeestor.plugins.store.SharedPreferenceInjector;

@SPFile(stores = {})
public class MainActivity extends AppCompatActivity {


    private static final String TAG = "PluginDemo" ;

    @SPProperty(value = "123")
    public String name ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferenceInjector.injectSP(this);

        Log.d(TAG, "onCreate:  ---- " + name);



    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void onStartClicked(View v){

        SmartInfoReader infoReader = new SmartInfoReader();
        Log.d(TAG, "onStartClicked: "+getExternalCacheDir().getPath());
        byte[] datas = infoReader.readSmartInfo(getExternalCacheDir().getPath()+"/");
        Toast.makeText(this, "datas is null : "+ (datas == null), Toast.LENGTH_SHORT).show();

    }
}
