package com.ongoza.pcycolortest;

import android.graphics.Color;

import org.gearvrf.GVRPicker;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.IPickEvents;
import org.gearvrf.utility.Log;

/**
 * Created by os on 23.05.2017.
 */

public class PickHandler implements IPickEvents {
    private static final String TAG = Main.getTAG();
    private String tutorialTag = "tMenu";
    private long timer=0;
//    private static final int IN_FOCUS_COLOR = 8570046;
    private static final int IN_FOCUS_COLOR = Color.rgb(10,220,220);
    private static final int LOST_FOCUS_COLOR = 6186095;
    private static final int CLICKED_COLOR = 12631476;
    public GVRSceneObject PickedObject = null;

    public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) {
        String name = sceneObj.getName();
        Log.d(TAG,"focus="+name);
        if(sceneObj!=PickedObject){
            timer = System.currentTimeMillis();
    //        if(sceneObj.getTag().equals("")){
    //        sceneObj.getTransform().translate(0,0.1f,0.2f);
            sceneObj.getTransform().setScale(1.2f,1.2f,1.2f);
    //        }else{
    //            sceneObj.getRenderData().getMaterial().setColor(IN_FOCUS_COLOR);
    //        }
            PickedObject = sceneObj;
    }else {
            Log.d(TAG,"Already picked "+name);
        }
    }

    public void onExit(GVRSceneObject sceneObj) {
        if(timer>0) {
            ColorTestScene.addTimerRes(sceneObj.getName(), System.currentTimeMillis() - timer);
            Log.d(TAG,"time select for "+sceneObj.getName()+" ="+String.valueOf(System.currentTimeMillis() - timer));
            timer=0;
        }else{
            ColorTestScene.addTimerRes(sceneObj.getName(), System.currentTimeMillis() - ColorTestScene.getTimer("startSelect"));
            Log.d(TAG,"time select from prev select "+sceneObj.getName());}
        sceneObj.getTransform().setScale(1f,1f,1f);

//        if(sceneObj.getTag().equals(tutorialTag)){
//            sceneObj.getTransform().translate(0,-0.1f,-0.2f);
//        }else{
//            sceneObj.getRenderData().getMaterial().setColor(LOST_FOCUS_COLOR);
//        }
        PickedObject = null;
    }

    public void onNoPick(GVRPicker picker) {

        Log.d(TAG,"NOPick!!!!!!");
    }
//    public long getTimer(){return timer;}
//    public void setTimerNull(){timer=0;}

    public void onPick(GVRPicker picker) { // GVRPicker.GVRPickedObject picked = picker.getPicked()[0];
        Log.d(TAG,"!!!!!!Pick!!!!!!");
//        timer = System.currentTimeMillis();
    }


    public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) { }
//    private void NonSelect(GVRSceneObject Picked) { }
}
