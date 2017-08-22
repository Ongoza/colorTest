package com.ongoza.colortest;

import org.gearvrf.GVRPicker;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.IPickEvents;
import org.gearvrf.utility.Log;

class PickHandler implements IPickEvents {
    private static final String TAG = Main.getTAG();
    private long timer=0;
    boolean modal = false;
    GVRSceneObject PickedObject = null;

    public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) { }

    public void onExit(GVRSceneObject sceneObj) { }

    public  void setModal(boolean m){
        if(PickedObject!=null){ PickedObject.getTransform().setScale(1f, 1f, 1f);  PickedObject = null;}
        modal = m;
    }

    public void onNoPick(GVRPicker picker) {
        if(PickedObject!=null){
            if (!modal) {
//                Log.d(TAG, "no Pick no name card");
                long curTime = System.currentTimeMillis();
                if (timer > 0) {
                    ColorTestScene.addTimerPick(PickedObject.getName(), curTime - timer);
                    timer = 0;
                } else {
                    long selTime = ColorTestScene.getTimer("startSelect");
                    ColorTestScene.addTimerPick(PickedObject.getName(), curTime - selTime);
                }
            }else{timer=0;}
            PickedObject.getTransform().setScale(1f, 1f, 1f);
            PickedObject = null;
//            Log.d(TAG, "no Pick no name");
        }else{
//            Log.d(TAG, "no Pick no name timer="+timer);
            timer=0;}
    }

    public void onPick(GVRPicker picker) {
//        Log.d(TAG,"pick="+picker.getPicked()[0].getHitObject());
        if(picker.getPicked().length>0){
            GVRSceneObject sceneObj = picker.getPicked()[0].getHitObject();
            if(sceneObj!=PickedObject){
                if(PickedObject!=null){
                    if(!modal){
                        String name = PickedObject.getName();
                        long curTime = System.currentTimeMillis();
    //                    Log.d(TAG, "Pick no Pick name ="+name);
                        if(timer>0) {  ColorTestScene.addTimerPick(name, curTime - timer);
                        }else{long selTime = ColorTestScene.getTimer("startSelect");
                        ColorTestScene.addTimerPick(name, curTime - selTime);}}
                    PickedObject.getTransform().setScale(1f,1f,1f);
                    PickedObject = null;}
                timer = System.currentTimeMillis();
                sceneObj.getTransform().setScale(1.2f,1.2f,1.2f);
                PickedObject = sceneObj;
//            }else { Log.d(TAG,"Already picked ");
            }
        }
    }

    public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) { }

}
