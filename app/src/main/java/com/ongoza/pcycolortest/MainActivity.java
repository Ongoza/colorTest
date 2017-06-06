package com.ongoza.pcycolortest;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import org.gearvrf.GVRActivity;

public class MainActivity extends GVRActivity {
    private static final int BUTTON_INTERVAL = 500;
    private static final int TAP_INTERVAL = 300;
    private long mLatestButton = 0;
    private long mLatestTap = 0;
    private static final String TAG = "VRPTest";
    Main main = null;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = new Main(this);
        setMain(main, "gvr.xml");
//        main.mContext = this;
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, " motion event");
        main.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


//    @Override public void onBackPressed() {
//        if (System.currentTimeMillis() > mLatestButton + BUTTON_INTERVAL) {
//            mLatestButton = System.currentTimeMillis();
//        }
//    }
//    @Override public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            mLatestButton = System.currentTimeMillis();
//        }
//        return super.onKeyLongPress(keyCode, event);
//    }
//
//    //@Override
//    public boolean onSingleTap(MotionEvent e) {
////        Log.d(TAG, "onSingleTap");
//        if (System.currentTimeMillis() > mLatestTap + TAP_INTERVAL) {
//            mLatestTap = System.currentTimeMillis();
////            main.onSingleTap(e);
//        }
//        return false;
//    }

}