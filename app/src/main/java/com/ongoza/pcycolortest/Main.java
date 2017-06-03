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
/**
 * Created by os on 6/1/17.
 */

public class Main extends GVRMain {
    private static final String TAG = "VRPTest";
    private GVRContext gContext;
    public static Context mContext;
    private Resources resources;
    private static MainScene  mainScene;

    @Override public void onInit(GVRContext gvrContext) throws Throwable {
        this.gContext = gvrContext;
        mainScene = new MainScene(gContext, mContext);
        gContext.setMainScene(mainScene);
    }

    @Override public void onStep() {}

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mainScene.onTouchEvent();
                break;
            default:
                break;
        }
    }
}
