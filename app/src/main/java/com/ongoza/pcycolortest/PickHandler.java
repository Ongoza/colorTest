package com.ongoza.pcycolortest;

import android.graphics.Color;

import org.gearvrf.GVRPicker;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.IPickEvents;

/**
 * Created by os on 23.05.2017.
 */

public class PickHandler implements IPickEvents {
    private static final String TAG = "VRTP";
    private String tutorialTag = "tMenu";
//    private static final int IN_FOCUS_COLOR = 8570046;
    private static final int IN_FOCUS_COLOR = Color.rgb(10,220,220);
    private static final int LOST_FOCUS_COLOR = 6186095;
    private static final int CLICKED_COLOR = 12631476;
    public GVRSceneObject PickedObject = null;

    public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) {
//        Log.d(TAG,"focus="+String.valueOf(sceneObj.getTag()));
//        if(sceneObj.getTag().equals(tutorialTag)){
//        sceneObj.getTransform().translate(0,0.1f,0.2f);
//        }else{
//            sceneObj.getRenderData().getMaterial().setColor(IN_FOCUS_COLOR);
//        }
        PickedObject = sceneObj;
    }

    public void onExit(GVRSceneObject sceneObj) {

//        if(sceneObj.getTag().equals(tutorialTag)){
//            sceneObj.getTransform().translate(0,-0.1f,-0.2f);
//        }else{
//            sceneObj.getRenderData().getMaterial().setColor(LOST_FOCUS_COLOR);
//        }
        PickedObject = null;
    }

    public void onNoPick(GVRPicker picker) {
//        Log.d(TAG,"NOPick!!!!!!");
    }

    public void onPick(GVRPicker picker) { // GVRPicker.GVRPickedObject picked = picker.getPicked()[0];
//        Log.d(TAG,"!!!!!!Pick!!!!!!");
    }

    public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) { }

//    private void NonSelect(GVRSceneObject Picked) { }
}
