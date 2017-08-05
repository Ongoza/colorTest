package com.ongoza.pcycolortest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.Gravity;

import com.google.vr.cardboard.DisplayUtils;

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
import org.gearvrf.script.GVRScriptBehavior;
import org.gearvrf.script.GVRScriptException;
import org.gearvrf.script.GVRScriptFile;
import org.gearvrf.script.GVRScriptManager;
import org.gearvrf.utility.Log;
//import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by os on 6/1/17.
 */

public class ColorTestScene extends GVRScene {
    private PickHandler mPickHandler;
    private GVRContext gContext;
    private GVRPicker mPicker;
    private static Map<String,Long> timerList= new HashMap<String, Long>();
    private static ArrayList<String[]> timerListRes = new ArrayList<String[]>();
    private static final String TAG = Main.getTAG();
    private float[][] arrLoc = new float[8][2];
    private String[] arrColorStr = new String[5];
    private int arrColorStrCounter =0;
    private Context mContext;
    private int[][] arrColor = new int[8][3];
    private int[] selNum= new int[]{4,2,1,0};
    private int selNumCounter=0;
    private boolean trSelect = false;
    About abt;
//    private Map<String,String> colorObj;
    private int selectCounter=0;
    private Main main;
    public GVRScriptManager scriptManager;
    private GVRSceneObject curTaskText = null;
    private GVRSceneObject curSelection = null;
    private GVRSceneObject curTextMsg  = null;
    private GVRSceneObject lastColorObj  = null;
    private String[] arrNames = {"gray","blue","green","red","yellow","purple","brown","black" };
//    private String[] arrNames = {"gray","blue","brown","green","red","black","yellow","purple" };
    private int[] selNames = new int[8]; private int selNamesCounter;

    public ColorTestScene(GVRContext gContext, Context mContext, Main main) {
        super(gContext);
//        Log.d(TAG, " scene 0");
        this.main = main;
        this.gContext =gContext;
        this.mContext =mContext;
        trSelect = false;
        createArrays();
//        Log.d(TAG, " scene 1");
        scriptManager = gContext.getScriptManager();
//        Log.d(TAG, " scene 2");
        GVRScriptFile scriptFile;
        try { scriptFile = scriptManager.loadScript( new GVRAndroidResource(getGVRContext(),"script.js"),
                    GVRScriptManager.LANG_JAVASCRIPT);
            scriptManager.attachScriptFile(main, scriptFile);}
        catch (IOException | GVRScriptException e) { e.printStackTrace();}
        scriptManager.addVariable("test",new ScriptObj());
        scriptManager.addVariable("utils", new ScriptUtils());
//        Log.d(TAG, " scene 3");
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
//        Log.d(TAG, " scene 4");
        arrColorStr[0]="Please select the more pleasure color one by one.";
        arrColorStr[1]=Main.mContext.getResources().getString(R.string.select2);
        arrColorStr[2]=Main.mContext.getResources().getString(R.string.select3);
        arrColorStr[3]=Main.mContext.getResources().getString(R.string.select4);
//        Log.d(TAG, " scene 5");
        arrColorStr[4]=Main.mContext.getResources().getString(R.string.select5);
//        Log.d(TAG, " scene 6");
        mPickHandler = new PickHandler();
        getEventReceiver().addListener(mPickHandler);
        mPicker = new GVRPicker(gContext, this);
//        Log.d(TAG, " scene 7");
        abt = new About(gContext);
//        Log.d(TAG, " scene 8");
        addSceneObject(abt);
//        Log.d(TAG, " scene 9");
        show();
    }

    private void createArrays(){
        arrColor[0] = new int[]{171, 171, 171};
        arrColor[1] = new int[]{51, 0, 168};
        arrColor[2] = new int[]{3, 114, 21};
        arrColor[3] = new int[]{246, 6, 22};
        arrColor[4] = new int[]{251, 251, 2};
        arrColor[5] = new int[]{168, 0, 59};
        arrColor[6] = new int[]{144, 88, 9};
        arrColor[7] = new int[]{0, 0, 0};

        float[] xLoc = {-2.5f,-0.75f,0.75f,2.5f};
        float[] yLoc = {0.75f,-0.75f};
        for (int i = 0; i < 2; i++){ int h=4;
            for (int j = 0; j < h; j++){ int k = i*h+j;
                arrLoc[k]=new float[]{xLoc[j],yLoc[i]};
            }}
    }

    public void onTouchEvent() {
        Log.d(TAG, "Touch event.");
        if (mPickHandler.PickedObject!=null){
            Log.d(TAG, "Touch name="+mPickHandler.PickedObject.getName());
            String[] tag =(String[]) mPickHandler.PickedObject.getTag();
//            Log.d(TAG, "Touch type="+tag[0]);
            String name = mPickHandler.PickedObject.getName();
            switch (tag[0]) {
                case "cMenu": selectColor(getSceneObjectByName(mPickHandler.PickedObject.getName()));
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
                default: break; }
    }}

    private void selectColor(GVRSceneObject selObj){
        Log.d(TAG,"start selectColor for "+selObj.getName());
        if(curSelection==null && curSelection != selObj){
//            selObj.detachCollider();
//            selObj.setPickingEnabled(false);
            String curSelectionName = selObj.getName();
            Log.d(TAG,"start animation color "+curSelectionName);
            curSelection = selObj;
            mPickHandler.onNoPick(mPicker);
            long curTime = System.currentTimeMillis();
            long addTime = curTime-timerList.get("startSelect");
            addTimerRes("select_"+curSelectionName,addTime);
            timerList.put("startSelect",curTime);
            int i = Integer.parseInt(curSelectionName); selNames[selNamesCounter]=i; selNamesCounter++;
            Log.d(TAG,"colorSelect "+curSelectionName+" time ="+addTime);
            new GVROpacityAnimation(curSelection,1,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation) {startSelect(); }});
    }else{Log.e(TAG,"error. can not find object"+selObj.getName());}
    }

    private void showResult() {
        long total = System.currentTimeMillis() - timerList.get("start");
        String strRes = "Total time: " + total + "ms; ";
//        Log.d(TAG,"result lengh="+timerListRes.size());
//        Iterator<String[]> strIter = timerListRes.iterator();
        for (int j = 0; j < timerListRes.size(); j++) {
//            Log.d(TAG,"result j="+j);
            try {
                String[] strIter = timerListRes.get(j);
//                Log.d(TAG,"name="+strIter[0]+"res="+strIter[1]);
                long resTime = Long.parseLong(strIter[1]);
                String res = String.valueOf(Math.round(resTime * 100 / total));
                strRes += strIter[0] + ": " + res + "%; ";
            } catch (Exception e) {
                Log.d(TAG, "Error parse result arraylist");
            }
        }
        float s = 0; float e = 0;
        float[] kArray= new float[] {8.1f,6.8f,6,5.3f,4.7f,4,3.2f,1.8f};
        if (selNames.length > 7) {
            for(int i=0;i<selNames.length;i++){
                if(i<3){if(selNames[i]==0 || selNames[i]==6 || selNames[i]==7){s+=kArray[i];}}
                if(i>4){if(selNames[i]==1 || selNames[i]==2 || selNames[i]==3 || selNames[i]==4){s+=kArray[7-i];}}
                if(selNames[i]==2 || selNames[i]==3 || selNames[i]==4){e+=kArray[i];}
                Log.d(TAG, "Calculate: i=" +i+" color="+selNames[i]+" s=" +s+" e="+e); //
            }
            Log.d(TAG, "Show result" + Arrays.toString(selNames) + " timer=" + strRes);
            Log.d(TAG, "Your Stress Level: s="+s+" %=" + Math.round(s*100/42));
            Log.d(TAG, "Your Efficiency Level: e=" + e+" %="+Math.round((e-9)*100/12));
            showMsg(arrColorStr[3]+"\nStress Level: "+Math.round(s*100/42)+"\nEfficiency Level: "+Math.round((e-9)*100/12),false);
        }else{showTaskStart(arrColorStr[4]);}
    }

    private void showResultStart(){
        for (GVRSceneObject object : getWholeSceneObjects()) {
            if (object.getTag()!=null){
                String[] tag = (String[]) object.getTag();
                if(tag[0].equals("cMenu")){
                    Log.d(TAG,"Show result delete obj "+object.getName());
                    lastColorObj = object;
                    selNames[7]=Integer.parseInt(object.getName());
                    new GVROpacityAnimation(lastColorObj,1,0)
                            .setRepeatMode(GVRRepeatMode.ONCE)
                            .setRepeatCount(1)
                            .start(gContext.getAnimationEngine())
                            .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                                Log.d(TAG,"delete task Message");
//                                gContext.getMainScene().removeSceneObject(lastColorObj);
                                if(lastColorObj != null) {gContext.getMainScene().removeSceneObject(lastColorObj);lastColorObj = null;};
                                showResult();
                            }});
                }}
        }
        new GVROpacityAnimation(curTaskText,1,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                    Log.d(TAG,"delete task Message");
                    if(curTaskText != null) {gContext.getMainScene().removeSceneObject(curTaskText);curTaskText = null;};
                    showResult();
                }});
    }

    private void showMsg(String str, boolean delete){
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
        GVRAnimation an= new GVROpacityAnimation(curTextMsg,1,1)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine());
        if(delete){
                an.setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
//                    Log.d(TAG,"Show 3 msg: ");
                    animateDeleteMsg();}});
        }

}

    private void show(){
        selectCounter=0;
        timerList.put("start",System.currentTimeMillis());
        timerList.put("startSelect",System.currentTimeMillis());
        arrColorStrCounter =0; selNumCounter=0; trSelect = true;
        createArrays(); for(int i=0;i<8;i++) {selNames[i] = -1;} selNamesCounter=0;
        for (GVRSceneObject object : getWholeSceneObjects()) {
            if (object.getRenderData() != null && object.getRenderData().getMaterial() != null) {
                new GVROpacityAnimation(object, 1f, 1f).start(getGVRContext().getAnimationEngine());
            }
        }
        String str = "Please prepare to the test. You should select the more pleasure color one by one.";
        showMsg(str,true);
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
        timerList.clear();
    }

    private void startSelect(){
//        if(curTextMsg!=null){gContext.getMainScene().removeSceneObject(curTextMsg); curTextMsg = null;}
//        if(curTaskText!=null){ gContext.getMainScene().removeSceneObject(curTaskText);curTaskText = null;}
        if(curSelection!=null){
            trSelect = true;
            Log.d(TAG,"startSelect "+curSelection.getName());
//            String[] tag =(String[]) curSelection.getTag();
            int i = Integer.parseInt(curSelection.getName());
//            Log.d(TAG,"find loc "+curSelection.getName()+" i="+i);
//            if(i==1 || i==5){
////                Log.d(TAG,"i= 1,5");
//                GVRSceneObject obj = gContext.getMainScene().getSceneObjectByName(String.valueOf(i-1));
//                obj.getTransform().setPosition(arrLoc[i][0], arrLoc[i][1], -5);
//            }
//            if(i==2 || i==6){
////                Log.d(TAG,"i= 2,6");
//                GVRSceneObject obj = gContext.getMainScene().getSceneObjectByName(String.valueOf(i+1));
//                obj.getTransform().setPosition(arrLoc[i][0], arrLoc[i][1], -5);
//            }

            gContext.getMainScene().removeSceneObject(curSelection);
            curSelection=null;
            selectCounter++;
            if(selectCounter==7){ showResultStart();}
            //else{showTask(arrColorStr[0]);}
        }else{showTaskStart(arrColorStr[0]); createBase();}
//        Log.d(TAG,"selNumber="+selNumCounter);
//        if(selNumCounter>0){ selNumCounter--;  //  Log.d(TAG,"One color selected");
//        }else{
////            Log.d(TAG,"!!!!Start new task arrColorStrCounter="+arrColorStrCounter+" ="+selNum[arrColorStrCounter]);
//            selNumCounter = selNum[arrColorStrCounter]-1;
////            Log.d(TAG,"!!!!!!2 selNumber="+selNumCounter);
//            if(arrColorStrCounter<3){
////                Log.d(TAG," 2 Start new task arrColorStrCounter="+arrColorStr[arrColorStrCounter]);
//                showTask(arrColorStr[arrColorStrCounter]);
//                arrColorStrCounter++;
//            }else{
////                Log.d(TAG," 3 end all tasks arrColorStrCounter="+arrColorStr[arrColorStrCounter]);
//                showResult(arrColorStr[arrColorStrCounter]);
////                arrColorStrCounter++;
//            }}
    }

    private void animateDeleteMsg(){
//        Log.d(TAG,"delete Message");
        new GVROpacityAnimation(curTextMsg,1,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
//                     Log.d(TAG,"delete 2 Message");
                    gContext.getMainScene().removeSceneObject(curTextMsg); curTextMsg=null;
                    startSelect();
                }});

  }

    private void showTaskStart(String str){
        Log.d(TAG,"Show task :"+str);
        if(curTaskText != null) {gContext.getMainScene().removeSceneObject(curTaskText);curTaskText = null;};
        int textClr = Color.rgb(51, 55, 64);
        int bckClr = Color.rgb(240, 240, 240);
        GVRTextViewSceneObject item = new GVRTextViewSceneObject(gContext, 6, 1, str);
        item.setName("iTask");
        item.setTextColor(textClr);
        item.setGravity(Gravity.CENTER);
        item.setTextSize(7);
        item.getRenderData().getMaterial().setOpacity(0);
        item.setBackgroundColor(bckClr);
        item.getTransform().setPosition(0, 2.25f, -5);
//            itemName.getTransform().rotateByAxis(-90f, 0f, 1f, 0f);
        curTaskText = item;
        addSceneObject(item);
        new GVROpacityAnimation(curTaskText,1,1)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                    Log.d(TAG,"End show task.");
                    }});

    }

    private void createBase(){
        for (int i=0;i<8;i++) {
            int[] colors = new int[300 * 300];
            Arrays.fill(colors, 0, 300 * 300, Color.argb(255, arrColor[i][0], arrColor[i][1], arrColor[i][2]));
            Bitmap bitmapAlpha = Bitmap.createBitmap(colors, 300, 300, Bitmap.Config.ARGB_8888);
            GVRBitmapTexture texture = new GVRBitmapTexture(gContext, bitmapAlpha);
            GVRSceneObject obj = new GVRSceneObject(gContext, 1, 1, texture);
//            try {
//              ff.setScriptText("function onPick(){utils.log(\" !!!script log: enter\");}", GVRScriptManager.LANG_JAVASCRIPT);
//              ff.onPick(mPicker);
//            }catch (Exception e){Log.d(TAG,"Error add script");}
//            obj.attachComponent(ff);
            obj.attachComponent(new GVRSphereCollider(gContext));
            obj.setName(String.valueOf(i)); //   String[] arr = {"cMenu", name};
            String[] tag = {"cMenu", arrNames[i]};
            obj.setTag(tag);
            obj.getRenderData().getMaterial().setOpacity(0);
            obj.getTransform().setPosition(arrLoc[i][0], arrLoc[i][1], -5);
            addSceneObject(obj);
//            Log.d(TAG,"a1");
//            ff.attachScript(obj);
//            Log.d(TAG,"a2");
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

    public static long getTimer(String name){ return timerList.get(name);}

    public static void addTimerRes(String name, long time){
//        Log.d(TAG,"1 add data to res "+name+" "+time);
        String[] strArr ={name, String.valueOf(time)};
        timerListRes.add(strArr);
//        Log.d(TAG,"2 add data to res "+strArr[0]+" "+strArr[1]+" lenght="+timerListRes.size());
    }
}
