package com.ongoza.pcycolortest;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRPhongShader;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;
import org.gearvrf.animation.GVRAnimation;
import org.gearvrf.animation.GVROnFinish;
import org.gearvrf.animation.GVROpacityAnimation;
import org.gearvrf.animation.GVRRelativeMotionAnimation;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;
import org.gearvrf.utility.Log;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by os on 6/1/17.
 */

public class MainScene extends GVRScene {
    private PickHandler mPickHandler;
    private GVRContext gContext;
    private GVRPicker mPicker;
    private static final String TAG = "VRPTest";
    private float[][] arrLoc = new float[8][2];
    private float[][] arrColor = new float[8][3];
    private GVRSceneObject curSelection;
    private int selCount=0;
    private Map<String,String> colorObj;
    private String[] arrNames = {"gray","blue","brown","green","red","black","yellow","purple" };


    public MainScene(GVRContext gContext, Context mContext) {
        super(gContext);
        createArrays();
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

        String str = Main.mContext.getResources().getString(R.string.select0);
        showMsg(str);

    }

    private void createArrays(){
        arrColor[0] = new float[]{171, 171, 171};
        arrColor[1] = new float[]{51, 0, 168};
        arrColor[2] = new float[]{144, 88, 9};
        arrColor[3] = new float[]{3, 114, 21};
        arrColor[4] = new float[]{246, 6, 22};
        arrColor[5] = new float[]{0, 0, 0};
        arrColor[6] = new float[]{251, 251, 2};
        arrColor[7] = new float[]{168, 0, 59};
        float[] xLoc = {-2.5f,-0.75f,0.75f,2.5f};
        float[] yLoc = {0.75f,-0.75f};
        for (int i = 0; i < 2; i++){ int h=4;
            for (int j = 0; j < h; j++){ int k = i*h+j;
                arrLoc[k]=new float[]{xLoc[j],yLoc[i]};
            }}
    }

    public void onTouchEvent() {
        if (mPickHandler.PickedObject!=null){
            Log.d(TAG, "Pick name="+mPickHandler.PickedObject.getName());
            String type = mPickHandler.PickedObject.getTag().toString();
            String name = mPickHandler.PickedObject.getName();
            switch (type) {
                case "cMenu":
                    selectColor(name);
                    break;
                default:
                    break;
            }
        }
    }

    private void selectColor(String name){
        curSelection = gContext.getMainScene().getSceneObjectByName(name);
        GVRAnimation anim = new GVRRelativeMotionAnimation(curSelection, 1.0f, 0, 0, -50f);
        anim.start(gContext.getAnimationEngine());
        anim.setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation) {reoderColors(); }});
    }

    private void reoderColors(){
        Log.d(TAG,"Anim end");
        gContext.getMainScene().removeSceneObject(curSelection);
        curSelection = null;
        String str = Main.mContext.getResources().getString(R.string.select0);
        showMsg(str);
    }

    private void showMsg(String str){
            int textClr = Color.rgb(51, 55, 64);
            int bckClr = Color.rgb(240, 240, 240);

            GVRTextViewSceneObject item = new GVRTextViewSceneObject(gContext, 6, 1, str);
            item.setName("iMsg");
            item.setTextColor(textClr);
            item.setGravity(Gravity.CENTER);
            item.setTextSize(7);
            item.setBackgroundColor(bckClr);

//            GVRTextViewSceneObject itemDesc = new GVRTextViewSceneObject(gContext, 4, 1, data[2]);
//            itemDesc.setTextSize(4);
//            itemDesc.setName("tDesc" + data[0]);
//            itemDesc.getTransform().setPosition(0, -1.01f, 0);
//            itemDesc.setTextColor(textClr);
//            itemDesc.setBackgroundColor(bckClr);
//            itemName.addChildObject(itemDesc);
//
//            GVRTextViewSceneObject itemVideo = new GVRTextViewSceneObject(gContext,0.7f,0.3f, " Video");
//            itemVideo.setTextSize(6);
//            String[] arr= {tutorialTag,"video",data[0]};
//            itemVideo.setTag(arr);
//            itemVideo.setName("tVdo:" + data[0]);
//            itemVideo.getTransform().setPosition(-1.3f, -2.02f, 0);
//            itemVideo.setTextColor(textClr);
//            itemVideo.attachCollider(new GVRSphereCollider(gContext));
//            itemName.addChildObject(itemVideo);
//
//            GVRTextViewSceneObject itemTest = new GVRTextViewSceneObject(gContext,0.7f,0.3f, " Test");
//            itemTest.setTextSize(6);
//            itemTest.setName("tTst:" + data[0]);
//            itemTest.getTransform().setPosition(1.3f, -2.02f, 0);
//            itemTest.setTextColor(textClr);
//            String[] arrTest= {tutorialTag,"test",data[0]};
//            itemVideo.setTag(arrTest);
//            itemTest.attachCollider(new GVRSphereCollider(gContext));
//            itemName.addChildObject(itemTest);
//
//            GVRTextViewSceneObject itemEdit = new GVRTextViewSceneObject(gContext,0.7f,0.3f, " Edit");
//            itemEdit.setTextSize(6);
//            itemEdit.setName("tEdt:" + data[0]);
//            itemEdit.getTransform().setPosition(0, -2.02f, 0);
//            itemEdit.setTextColor(textClr);
//            String[] arrEdit= {tutorialTag,"edit",data[0]};
//            itemVideo.setTag(arrEdit);
//            itemEdit.attachCollider(new GVRSphereCollider(gContext));
//            itemName.addChildObject(itemEdit);
            curSelection = item;
            item.getTransform().setPosition(0, 0, -5);
//            itemName.getTransform().rotateByAxis(-90f, 0f, 1f, 0f);
            addSceneObject(item);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override  public void run() {
                animateDelete();
                }}, 5000);

    }

    private void startSelect(){
        gContext.getMainScene().removeSceneObject(curSelection);
        curSelection = null;
        for (int i=0;i<8;i++){ createBase(arrNames[i],i);}
    }

    private void animateDelete(){
        Log.d(TAG,"delete");
        GVRAnimation anim = new GVROpacityAnimation(curSelection,2,0);
        anim.start(gContext.getAnimationEngine());
        anim.setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation) { startSelect(); }});
    }
    private GVRSceneObject createBase(String name, int i){
        GVRSceneObject obj = new GVRSceneObject(gContext,1,1);
        GVRRenderData rdata1 = obj.getRenderData();
        GVRMaterial material = new GVRMaterial(gContext);
        material.setDiffuseColor(arrColor[i][0]*0.00392f,arrColor[i][1]*0.00392f,arrColor[i][2]*0.00392f,1);
        rdata1.setMaterial(material);
        rdata1.setShaderTemplate(GVRPhongShader.class);
        obj.attachCollider(new GVRSphereCollider(gContext));
        obj.setName(name);
//        String[] arr = {"cMenu", name};
        obj.setTag("cMenu");
        addSceneObject(obj);
        obj.getTransform().setPosition(arrLoc[i][0],arrLoc[i][1],-5);
        return obj;
    }

}
