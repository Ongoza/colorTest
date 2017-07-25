package com.ongoza.pcycolortest;

import android.content.Context;
import android.content.res.Resources;
import android.view.MotionEvent;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by os on 6/1/17.
 */

public class Main extends GVRMain {
    private static final String TAG = "VRTest";
    private GVRContext gContext;
    public static Context mContext;
    private Resources resources;
    private static ColorTestScene  colorTestScene;

    public Main(MainActivity activity) {  mContext = activity;  }

    @Override public void onInit(GVRContext gvrContext) throws Throwable {
        this.gContext = gvrContext;
        colorTestScene = new ColorTestScene(gContext, mContext, this );
        gContext.setMainScene(colorTestScene);
    }

    @Override public void onStep() {}



//    public String getMain(){ return this; }

    public static String getTAG(){ return TAG; }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                colorTestScene.onTouchEvent();
                break;
            default:
                break;
        }
    }
}
