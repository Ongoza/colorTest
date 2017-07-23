package com.ongoza.pcycolortest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.Gravity;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRBitmapTexture;
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
import org.gearvrf.animation.GVRRepeatMode;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;
import org.gearvrf.utility.Log;

import java.util.Arrays;
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
    private static final String TAG = Main.getTAG();
    private float[][] arrLoc = new float[8][2];
    private String[] arrColorStr = new String[4];
    private int arrColorStrCounter =0;
    private int[][] arrColor = new int[8][3];
    private int[] selNum= new int[]{4,2,1,0};
    private int selNumCounter=0;
    private boolean trSelect = false;
    About abt;
//    private Map<String,String> colorObj;
    private GVRSceneObject curTaskText = null;
    private GVRSceneObject curSelection = null;
    private GVRSceneObject curTextMsg  = null;
    private GVRSceneObject curColorObj  = null;
    private String[] arrNames = {"gray","blue","brown","green","red","black","yellow","purple" };
    private int[] selNames = new int[8]; private int selNamesCounter;

    public MainScene(GVRContext gContext, Context mContext) {
        super(gContext);
        createArrays();
        trSelect = false;
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
        getMainCameraRig().addChildObject(headTracker);;
        arrColorStr[0]=Main.mContext.getResources().getString(R.string.select1);
        arrColorStr[1]=Main.mContext.getResources().getString(R.string.select2);
        arrColorStr[2]=Main.mContext.getResources().getString(R.string.select3);
        arrColorStr[3]=Main.mContext.getResources().getString(R.string.select4);
        mPickHandler = new PickHandler();
        getEventReceiver().addListener(mPickHandler);
        mPicker = new GVRPicker(gContext, this);
        abt = new About(gContext);
        addSceneObject(abt);
        show();
    }

    private void createArrays(){
        arrColor[0] = new int[]{171, 171, 171};
        arrColor[1] = new int[]{51, 0, 168};
        arrColor[2] = new int[]{144, 88, 9};
        arrColor[3] = new int[]{3, 114, 21};
        arrColor[4] = new int[]{246, 6, 22};
        arrColor[5] = new int[]{0, 0, 0};
        arrColor[6] = new int[]{251, 251, 2};
        arrColor[7] = new int[]{168, 0, 59};
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
            String[] tag =(String[]) mPickHandler.PickedObject.getTag();
            Log.d(TAG, "Pick name="+tag[0]);
            String name = mPickHandler.PickedObject.getName();
            switch (tag[0]) {
                case "cMenu":
                    if(trSelect){
                        trSelect = false;
                        selectColor(mPickHandler.PickedObject);}
                    break;
                case "mMenu":
                    Log.d(TAG, "Pick about 1 "+tag[1]);
                    switch(tag[1]){
                        case "aboutOk":
                            abt.hideAbout();
                            break;
                        case "about":
                            Log.d(TAG,"show about");
                            abt.showAbout();
                            break;
                       // case "restart": break;
                        //                  case "home": break;
                        default: break;
                    } break;
                default:
                    break;

        }
    }}

    private void selectColor(GVRSceneObject selObj){
//        Log.d(TAG,"start animation "+selObj.getName());
        if(curSelection==null && curSelection != selObj){
            curSelection = selObj;
            int i = Integer.parseInt(selObj.getName()); selNames[selNamesCounter]=i; selNamesCounter++;
//            Log.d(TAG,"start animation 2  "+selObj.getName());
            new GVROpacityAnimation(curSelection,1,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation) {startSelect(); }});
    }else{Log.e(TAG,"error. can not find object"+selObj.getName());}
    }

    private void showResult(String str){

        for (GVRSceneObject object : getWholeSceneObjects()) {
            if (object.getTag()!=null){
                String[] tag = (String[]) object.getTag();
                if(tag[0].equals("cMenu")){
                    Log.d(TAG,"Show result delete obj "+object.getName());
                    selNames[7]=Integer.parseInt(object.getName());
                    gContext.getMainScene().removeSceneObject(object);}}
        }
        Log.d(TAG,"Show result"+Arrays.toString(selNames));
        showTask(str);
    }

    private void showMsg(String str){
        Log.d(TAG,"Show msg: "+str);
        if(curTextMsg!=null){Log.d(TAG,"Show msg delete prev: "); gContext.getMainScene().removeSceneObject(curTextMsg); curTextMsg=null;}
        Log.d(TAG,"Show 2 msg: "+str);
            int textClr = Color.rgb(51, 55, 64);
            int bckClr = Color.rgb(240, 240, 240);
            GVRTextViewSceneObject item = new GVRTextViewSceneObject(gContext, 6, 2, str);
            item.setName("iMsg");
            item.setTextColor(textClr);
            item.setGravity(Gravity.CENTER);
            item.setTextSize(7);
            item.setBackgroundColor(bckClr);
            item.getRenderData().getMaterial().setOpacity(0);
            item.getTransform().setPosition(0, 0, -5);
        curTextMsg = item; addSceneObject(item);
        new GVROpacityAnimation(curTextMsg,1,1)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                    Log.d(TAG,"Show 3 msg: ");
                    animateDeleteMsg();}});


}

    private void show(){
        arrColorStrCounter =0; selNumCounter=0; trSelect = false;
        createArrays(); for(int i=0;i<8;i++) {selNames[i] = -1;} selNamesCounter=0;
        for (GVRSceneObject object : getWholeSceneObjects()) {
            if (object.getRenderData() != null && object.getRenderData().getMaterial() != null) {
                new GVROpacityAnimation(object, 1f, 1f).start(getGVRContext().getAnimationEngine());
            }
        }
        String str = Main.mContext.getResources().getString(R.string.selectStart);
        showMsg(str);
    }

    public void hide() {
//        if(curTimer!=null){curTimer.cancel();curTimer=null; }
//        if(mSeekbar!=null){
//            shortcutMenu.removeChildObject(mSeekbar);
//            mSeekbar = null; }
        for (GVRSceneObject object : getWholeSceneObjects()) {
            if (object.getRenderData() != null && object.getRenderData().getMaterial() != null) {
                object.getRenderData().getMaterial().setOpacity(0f);
            }
        }
    }

    private void startSelect(){
        if(curTextMsg!=null){gContext.getMainScene().removeSceneObject(curTextMsg); curTextMsg = null;}
//        if(curTaskText!=null){ gContext.getMainScene().removeSceneObject(curTaskText);curTaskText = null;}
        if(curSelection!=null){
            trSelect = true;
            Log.d(TAG,"startSelect "+curSelection.getName());
//            String[] tag =(String[]) curSelection.getTag();
            int i = Integer.parseInt(curSelection.getName());
//            Log.d(TAG,"find loc "+curSelection.getName()+" i="+i);
            if(i==1 || i==5){
//                Log.d(TAG,"i= 1,5");
                GVRSceneObject obj = gContext.getMainScene().getSceneObjectByName(String.valueOf(i-1));
                obj.getTransform().setPosition(arrLoc[i][0], arrLoc[i][1], -5);
            }
            if(i==2 || i==6){
//                Log.d(TAG,"i= 2,6");
                GVRSceneObject obj = gContext.getMainScene().getSceneObjectByName(String.valueOf(i+1));
                obj.getTransform().setPosition(arrLoc[i][0], arrLoc[i][1], -5);
            }
            gContext.getMainScene().removeSceneObject(curSelection);
            curSelection=null;
        }else{ createBase();}
//        Log.d(TAG,"selNumber="+selNumCounter);
        if(selNumCounter>0){ selNumCounter--;  //  Log.d(TAG,"One color selected");
        }else{
//            Log.d(TAG,"!!!!Start new task arrColorStrCounter="+arrColorStrCounter+" ="+selNum[arrColorStrCounter]);
            selNumCounter = selNum[arrColorStrCounter]-1;
//            Log.d(TAG,"!!!!!!2 selNumber="+selNumCounter);
            if(arrColorStrCounter<3){
//                Log.d(TAG," 2 Start new task arrColorStrCounter="+arrColorStr[arrColorStrCounter]);
                showTask(arrColorStr[arrColorStrCounter]);
                arrColorStrCounter++;
            }else{
//                Log.d(TAG," 3 end all tasks arrColorStrCounter="+arrColorStr[arrColorStrCounter]);
                showResult(arrColorStr[arrColorStrCounter]);
//                arrColorStrCounter++;
            }}
    }

    private void animateDeleteMsg(){
        Log.d(TAG,"delete Message");
        new GVROpacityAnimation(curTextMsg,1,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                     Log.d(TAG,"delete 2 Message");
                    gContext.getMainScene().removeSceneObject(curTextMsg); curTextMsg=null;
                    startSelect();
                }});

  }

    private void showTask(String str){
        Log.d(TAG,"Show task :"+str);
        if(curTaskText != null) {gContext.getMainScene().removeSceneObject(curTaskText);curTaskText = null;};
        int textClr = Color.rgb(51, 55, 64);
        int bckClr = Color.rgb(240, 240, 240);
        GVRTextViewSceneObject item = new GVRTextViewSceneObject(gContext, 6, 1, str);
        item.setName("iTask");
        item.setTextColor(textClr);
        item.setGravity(Gravity.CENTER);
        item.setTextSize(7);
        item.setBackgroundColor(bckClr);
        item.getTransform().setPosition(0, 2, -5);
//            itemName.getTransform().rotateByAxis(-90f, 0f, 1f, 0f);
        curTaskText = item;
        addSceneObject(item);

    }

    private void createBase(){
        for (int i=0;i<8;i++) {
            int[] colors = new int[300 * 300];
            Arrays.fill(colors, 0, 300 * 300, Color.argb(255, arrColor[i][0], arrColor[i][1], arrColor[i][2]));
            Bitmap bitmapAlpha = Bitmap.createBitmap(colors, 300, 300, Bitmap.Config.ARGB_8888);
            GVRBitmapTexture texture = new GVRBitmapTexture(gContext, bitmapAlpha);
            GVRSceneObject obj = new GVRSceneObject(gContext, 1, 1, texture);
//            Log.d(TAG, "obj Id =" + i +" name="+arrNames[i]);
            obj.attachCollider(new GVRSphereCollider(gContext));
            obj.setName(String.valueOf(i)); //   String[] arr = {"cMenu", name};
            String[] tag = {"cMenu", arrNames[i]};
            obj.setTag(tag);
            obj.getRenderData().getMaterial().setOpacity(0);
            obj.getTransform().setPosition(arrLoc[i][0], arrLoc[i][1], -5);
            addSceneObject(obj);
            if (obj.getRenderData() != null && obj.getRenderData().getMaterial() != null) {
//                Log.d(TAG, "obj start anim Id =" + i +" name="+arrNames[i]);
                GVRAnimation anim = new GVROpacityAnimation(obj, 2, 1)
                    .setRepeatMode(GVRRepeatMode.ONCE)
                    .setRepeatCount(1)
                    .start(gContext.getAnimationEngine())
                    .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
//                            Log.d(TAG,"create base end 3 msg: ");
                            trSelect = true;}});;

            }
        }
    }

}
