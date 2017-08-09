package com.ongoza.pcycolortest;

import android.util.Log;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;
import org.gearvrf.GVRTexture;
import org.gearvrf.animation.GVRAnimation;
import org.gearvrf.animation.GVROnFinish;
import org.gearvrf.animation.GVROpacityAnimation;
import org.gearvrf.animation.GVRRepeatMode;

import java.lang.reflect.Array;

/**
 * Created by os on 8/8/17.
 */

public class Card extends GVRSceneObject{
    private String TAG = Main.getTAG();
    private String name;
    private int number;
    public boolean selected;
    private GVRContext gContext;
    GVRSceneObject obj;
    private String[] arrNames = {"gray","blue","green","red","yellow","purple","brown","black" };

    public Card (GVRContext gContext, int i, float[] arrLoc, GVRTexture texture) {
        super(gContext);
        this.gContext = gContext;
        this.name = String.valueOf(i);
        this.number = i;
        Log.d(TAG,"2 start create card "+name);
        obj = new GVRSceneObject(gContext, 1, 1, texture);
        obj.setName(name); String[] tag = {"cMenu", arrNames[i]}; obj.setTag(tag);
        obj.getRenderData().getMaterial().setOpacity(0);
        obj.getTransform().setPosition(arrLoc[0], arrLoc[1], -5);
        gContext.getMainScene().addSceneObject(obj);
    }

    public void showCard(){
        Log.d(TAG,"start show card "+name);
        selected = false;
        obj.attachComponent(new GVRSphereCollider(gContext));
        if (obj.getRenderData() != null && obj.getRenderData().getMaterial() != null) {
                Log.d(TAG, "obj start anim Id =" + name );
            GVRAnimation anim = new GVROpacityAnimation(obj, 2, 1)
                    .setRepeatMode(GVRRepeatMode.ONCE)
                    .setRepeatCount(1)
                    .start(gContext.getAnimationEngine());
        }
    }

    public void deleteCard(){
        selected = true;
        obj.detachCollider();
//        Log.d(TAG, "obj start anim Id =" );
        new GVROpacityAnimation(obj,1,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation) {
//                    gContext.getMainScene().removeSceneObject(obj);
                }});
    }


}
