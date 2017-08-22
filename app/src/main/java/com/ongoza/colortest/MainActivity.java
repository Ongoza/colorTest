package com.ongoza.colortest;

import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import org.gearvrf.GVRActivity;

public class MainActivity extends GVRActivity {
    private static final int BUTTON_INTERVAL = 500;
////    private static final int TAP_INTERVAL = 300;
    private long mLatestButton = 0;
//    private long mLatestTap = 0;
    private static final String TAG = "VRTest";
    Main main;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, " start activity 00");
        main = new Main(this);
//        Log.d(TAG, " start activity 01");
        setMain(main);
//        Log.d(TAG, " start activity 03");
    }


    @Override public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, "acrivity motion event");
        main.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


//    @Override public void onBackPressed() {
//        super.onPause();
//        super.onBackPressed();
//        main.
//        this.finishAffinity();
//    }

//    @Override public void onBackPressed() {
//        if (System.currentTimeMillis() > mLatestButton + BUTTON_INTERVAL) {
//            mLatestButton = System.currentTimeMillis();
//        }
//    }
//
//    @Override public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            mLatestButton = System.currentTimeMillis();
//        }
//        return super.onKeyLongPress(keyCode, event);
//    }

    //@Override
//    public boolean onSingleTap(MotionEvent e) {
//        Log.d(TAG, "onSingleTap");
//        if (System.currentTimeMillis() > mLatestTap + TAP_INTERVAL) {
//            mLatestTap = System.currentTimeMillis();
////            main.onSingleTap(e);
//        }
//        return false;
//    }

    public void exitHome(){
        this.finish();
//        super.onBackPressed();
//        System.exit(0);

//        nativeShowConfirmQuit(mPtr);
//        super.getConfigurationManager();
//        onBackPressed();
//        super.closeContextMenu();
//        super.onBackPressed();
//        Log.d(TAG,"exit");
//        super.closeOptionsMenu();
    }


}