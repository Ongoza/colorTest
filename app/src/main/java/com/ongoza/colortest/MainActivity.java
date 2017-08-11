package com.ongoza.colortest;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import org.gearvrf.GVRActivity;

public class MainActivity extends GVRActivity {

    private static final String TAG = "VRTest";
    Main main = null;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, " start activity "+TAG);
        main = new Main(this);
//        Log.d(TAG, " start activity 01");
        setMain(main, "gvr.xml");
    }


    @Override public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, " motion event");
        main.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    public void exitHome(){
        this.finishAffinity();
//        System.exit(0);
    }


}