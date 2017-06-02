package com.ongoza.pcycolortest;

import android.content.Context;
import android.graphics.Color;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRPhongShader;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.utility.Log;

/**
 * Created by os on 6/1/17.
 */

public class MainScene extends GVRScene {
    private PickHandler mPickHandler;
    private GVRContext gContext;
    private GVRPicker mPicker;
    private static final String TAG = "VRPTest";

    public MainScene(GVRContext gContext, Context mContext) {
        super(gContext);
        this.gContext =gContext;
        getMainCameraRig().getOwnerObject().attachComponent(new GVRPicker(gContext, this));
        getMainCameraRig().getLeftCamera().setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        getMainCameraRig().getRightCamera().setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        getMainCameraRig().getTransform().setPosition(0.0f, 0.0f, 0.0f);
        GVRSceneObject headTracker = new GVRSceneObject(gContext, gContext.createQuad(0.1f, 0.1f),
                    gContext.loadTexture(new GVRAndroidResource(gContext, R.drawable.headtrackingpointer)));
        headTracker.getTransform().setPosition(0.0f, 0.0f, -1.0f);
        headTracker.setName("Head");
        headTracker.getRenderData().setDepthTest(false);
        headTracker.getRenderData().setRenderingOrder(100000);
        getMainCameraRig().addChildObject(headTracker);
        mPickHandler = new PickHandler();
        getEventReceiver().addListener(mPickHandler);
        mPicker = new GVRPicker(gContext, this);

        GVRSceneObject gray = createBase("gray",171, 171, 171);
        gray.getTransform().setPosition(-2.5f,0.75f,-5);
        GVRSceneObject blue = createBase("blue",51, 0, 168);
        blue.getTransform().setPosition(-0.75f,0.75f,-5);
        GVRSceneObject brown = createBase("brown",144, 88, 9);
        brown.getTransform().setPosition(0.75f,0.75f,-5);
        GVRSceneObject green = createBase("green",3, 114, 21);
        green.getTransform().setPosition(2.5f,0.75f,-5);

        GVRSceneObject red = createBase("red",246, 6, 22);
        red.getTransform().setPosition(-2.5f,-0.75f,-5);
        GVRSceneObject black = createBase("black",0, 0, 0);
        black.getTransform().setPosition(-0.75f,-0.75f,-5);
        GVRSceneObject yellow = createBase("yellow",251, 251, 2);
        yellow.getTransform().setPosition(0.75f,-0.75f,-5);
        GVRSceneObject purple = createBase("purple",168, 0, 59);
        purple.getTransform().setPosition(2.5f,-0.75f,-5);

    }

    public void onTouchEvent() {
        if (mPickHandler.PickedObject!=null){
            Log.d(TAG, "Pick name="+mPickHandler.PickedObject.getName());
//            String type = mPickHandler.PickedObject.getTag().toString();
            String name = mPickHandler.PickedObject.getName();
        }
    }

    private GVRSceneObject createBase(String name, float r, float g, float b){
        GVRSceneObject obj = new GVRSceneObject(gContext,1,1);
        GVRRenderData rdata1 = obj.getRenderData();
        GVRMaterial material = new GVRMaterial(gContext);
        material.setDiffuseColor(r*0.00392f,g*0.00392f,b*0.00392f,1);
        rdata1.setMaterial(material);
        rdata1.setShaderTemplate(GVRPhongShader.class);
        obj.attachCollider(new GVRSphereCollider(gContext));
        obj.setName(name);
        obj.setTag("colorBtn");
        addSceneObject(obj);
        return obj;
    }

}
