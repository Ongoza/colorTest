package com.ongoza.colortest;

import android.util.Log;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;
import org.gearvrf.GVRTexture;
import org.gearvrf.animation.GVRAnimation;
import org.gearvrf.animation.GVROnFinish;
import org.gearvrf.animation.GVROpacityAnimation;
import org.gearvrf.animation.GVRRepeatMode;

class Card extends GVRSceneObject{
    private String TAG = Main.getTAG();
//    private String name;
//    private int number;
    boolean selected;
    private GVRContext gContext;
    private GVRSceneObject obj;
    private  GVRSceneObject root;

    Card (GVRContext gContext, GVRSceneObject r, int i, float[] arrLoc, GVRTexture texture) {
        super(gContext); root =r;
        String[] arrNames = {"gray","blue","green","red","yellow","purple","brown","black" };
        this.gContext = gContext;
        String name = String.valueOf(i);
//        this.number = i;
//        Log.d(TAG,"2 start create card "+name);
        obj = new GVRSceneObject(gContext, 1, 1, texture);
        obj.setName(name); String[] tag = {"cMenu", arrNames[i],"true"}; obj.setTag(tag);
        obj.getRenderData().getMaterial().setOpacity(0);
        obj.getTransform().setPosition(arrLoc[0], arrLoc[1], -5);
        root.addChildObject(obj);
//        gContext.getMainScene().addSceneObject(obj);
    }

    void showCard(){
//        Log.d(TAG,"start show card "+obj.getName());
        selected = false;
        obj.attachCollider(new GVRSphereCollider(gContext));
        if (obj.getRenderData() != null && obj.getRenderData().getMaterial() != null) {
//                Log.d(TAG, "obj start anim Id =" + name );
            new GVROpacityAnimation(obj, 2, 1)
                    .setRepeatMode(GVRRepeatMode.ONCE)
                    .setRepeatCount(1)
                    .start(gContext.getAnimationEngine());
        }
    }

    void deleteCard(){
        obj.detachCollider();
        selected = true;
//        Log.d(TAG, "obj start anim Id =" );
        new GVROpacityAnimation(obj,1,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation) {
                    root.removeChildObject(obj);
//                    gContext.getMainScene().removeSceneObject(obj);
                    obj=null;
                }});
    }


}
