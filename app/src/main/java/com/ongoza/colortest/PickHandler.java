package com.ongoza.colortest;

import org.gearvrf.GVRPicker;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.IPickEvents;
import org.gearvrf.utility.Log;

class PickHandler implements IPickEvents {
    private static final String TAG = Main.getTAG();
    private long timer=0;
    GVRSceneObject PickedObject = null;

    public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) { }

    public void onExit(GVRSceneObject sceneObj) { }

    public void onNoPick(GVRPicker picker) {
        if(PickedObject!=null){
            long curTime = System.currentTimeMillis();
                if(timer>0) {
                    ColorTestScene.addTimerRes(PickedObject.getName(), curTime - timer);
                    timer=0;
                }else{
                    long selTime = ColorTestScene.getTimer("startSelect");
                    ColorTestScene.addTimerRes(PickedObject.getName(), curTime - selTime);}

                PickedObject.getTransform().setScale(1f,1f,1f);
                PickedObject = null;
        }else{Log.d(TAG, "no Pick no name timer="+timer);timer=0;}
    }

    public void onPick(GVRPicker picker) {
        if(picker.getPicked().length>0){
            GVRSceneObject sceneObj = picker.getPicked()[0].getHitObject();
            if(sceneObj!=PickedObject){
                if(PickedObject!=null){
                    String name = PickedObject.getName();
                    long curTime = System.currentTimeMillis();
//                    Log.d(TAG, "Pick no Pick name ="+name);
                    if(timer>0) {
                        ColorTestScene.addTimerRes(name, curTime - timer);
                    }else{
                        long selTime = ColorTestScene.getTimer("startSelect");
                        ColorTestScene.addTimerRes(name, curTime - selTime);}
                    PickedObject.getTransform().setScale(1f,1f,1f);
                    PickedObject = null;}
                timer = System.currentTimeMillis();
                sceneObj.getTransform().setScale(1.2f,1.2f,1.2f);
                PickedObject = sceneObj;
            }else { Log.d(TAG,"Already picked ");}
        }
    }

    public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) { }

}
