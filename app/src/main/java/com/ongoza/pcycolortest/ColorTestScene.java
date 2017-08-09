package com.ongoza.pcycolortest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.vr.cardboard.DisplayUtils;

import org.gearvrf.GVRActivity;
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
import org.gearvrf.GVRTexture;
import org.gearvrf.animation.GVRAnimation;
import org.gearvrf.animation.GVRColorAnimation;
import org.gearvrf.animation.GVROnFinish;
import org.gearvrf.animation.GVROpacityAnimation;
import org.gearvrf.animation.GVRPositionAnimation;
import org.gearvrf.animation.GVRRelativeMotionAnimation;
import org.gearvrf.animation.GVRRepeatMode;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;
import org.gearvrf.scene_objects.GVRViewSceneObject;
import org.gearvrf.scene_objects.view.GVRFrameLayout;
import org.gearvrf.script.GVRScriptBehavior;
import org.gearvrf.script.GVRScriptException;
import org.gearvrf.script.GVRScriptFile;
import org.gearvrf.script.GVRScriptManager;
import org.gearvrf.utility.Log;
import org.json.JSONObject;
//import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;

import static org.gearvrf.GVRRenderData.GVRRenderingOrder.OVERLAY;

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
    private GVRSceneObject rootResult;
    private GVRSceneObject rootAbout;
    public GVRScriptManager scriptManager;
    private Card[] listCards = new Card[8];
    private GVRSceneObject curTaskText = null;
    private GVRSceneObject curSelection = null;
    private GVRSceneObject curTextMsg  = null;
    private int[] resultsLevels = new int[2];
    private GVRSceneObject lastColorObj  = null;
    private String[] arrNames = {"gray","blue","green","red","yellow","purple","brown","black" };
//    private String[] arrNames = {"gray","blue","brown","green","red","black","yellow","purple" };
    private int[] selNames = new int[8]; private int selNamesCounter;

    public ColorTestScene(GVRContext gContext, Context mContext, Main main) {
        super(gContext);
        this.main = main;
        this.gContext =gContext;
        this.mContext =mContext;
        trSelect = false;
//        new GVROpacityAnimation(getMainCameraRig(),2,1);
        getMainCameraRig().getLeftCamera().setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        getMainCameraRig().getRightCamera().setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        GVRSceneObject headTracker = new GVRSceneObject(gContext, gContext.createQuad(0.1f, 0.1f),
                    gContext.loadTexture(new GVRAndroidResource(gContext, R.drawable.headtrackingpointer)));
        headTracker.getTransform().setPosition(0.0f, 0.0f, -1.0f);
        headTracker.setName("Head");
        headTracker.getRenderData().setDepthTest(false);
        headTracker.getRenderData().setRenderingOrder(100000);
//        getMainCameraRig().getOwnerObject().attachComponent(new GVRPicker(gContext, this));
        getMainCameraRig().addChildObject(headTracker);
        mPickHandler = new PickHandler();
        getEventReceiver().addListener(mPickHandler);
        mPicker = new GVRPicker(gContext, this);
//        show();
        resultsLevels[0]= 70;
        resultsLevels[1]= 30;
        showResult();
    }

    private void createArrays(){
        // {"gray","blue","green","red","yellow","purple","brown","black" };
        arrColor[0] = new int[]{171, 171, 171};
        arrColor[1] = new int[]{0, 0, 128};
        arrColor[2] = new int[]{3, 114, 21};
        arrColor[3] = new int[]{246, 6, 22};
        arrColor[4] = new int[]{251, 251, 2};
        arrColor[5] = new int[]{139, 0, 139};
        arrColor[6] = new int[]{139, 69, 19};
        arrColor[7] = new int[]{0, 0, 0};

        float[] xLoc = {-2.5f,-0.75f,0.75f,2.5f};
        float[] yLoc = {0.75f,-0.75f};
        for (int i = 0; i < 2; i++){ int h=4;
            for (int j = 0; j < h; j++){ int k = i*h+j;
                arrLoc[k]=new float[]{xLoc[j],yLoc[i]};
            }}
    }

    public void onTouchEvent() {
        Log.e(TAG, "Touch event.");
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
                        case "Ok":
                            hideAbout();
                            showResult();
                            break;
                        case "About":
                            Log.d(TAG,"show about");
                            hideResult();
                            showAbout();
                            break;
                       // case "restart": break;
                        //                  case "home": break;
                        default: break;
                    } break;
                default: break; }
    }}

    private void selectColor(GVRSceneObject selObj){
        Log.e(TAG,"start selectColor for "+selObj.getName());
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
            listCards[i].deleteCard();
            startSelect();
        }else{Log.e(TAG,"error. can not find object"+selObj.getName());}
    }

    private void hideResult() {
        removeSceneObject(rootResult);
    }

    private void showAbout(){
        rootAbout = new GVRSceneObject(gContext);
        Log.d(TAG,"show about info");
        String[] str = new String[6];
        str[0]="This application was developed by";
        str[1]="O.Skuibida & A.Sukhnin";
        str[2]="for the site www.ongoza.com";
        str[3]="";
        str[4]="More information about you";
        str[5]="on the www.mydedium.com";
        GVRTexture texture = createTextTexture(str,24,400,180,-1);
        GVRSceneObject item = new GVRSceneObject(gContext, 4, 2f,texture);
        item.getTransform().setPosition(0, 1.5f, -4f);
        rootAbout.addChildObject(item);

        GVRSceneObject itemOk = createButton("Ok", 1.1f, 0.5f);
        itemOk.getTransform().setPosition(0, 0, -4f);
        rootAbout.addChildObject(itemOk);

        addSceneObject(rootAbout);

    }

    private void hideAbout(){
        removeSceneObject(rootAbout);
    }

    private void showResult() {
        Log.e(TAG,"start show results");
        rootResult = new GVRSceneObject(gContext);
        String[] str = new String[1];
        str[0]="Your results:";
        GVRTexture texture = createTextTexture(str,30,200,50,-1);
        GVRSceneObject item = new GVRSceneObject(gContext, 3, 0.7f,texture);
        item.getTransform().setPosition(0, 1.5f, -4f);
        rootResult.addChildObject(item);
        showPie(rootResult,0);

        GVRSceneObject itemAbout = createButton("About", 1.1f, 0.5f);
        itemAbout.getTransform().setPosition(-1.5f, -1.6f, -4f);
        rootResult.addChildObject(itemAbout);

        GVRSceneObject itemExit = createButton("Exit", 1.1f, 0.5f);
        itemExit.getTransform().setPosition(0f, -1.6f, -4f);
        rootResult.addChildObject(itemExit);

        GVRSceneObject itemRestart = createButton("Restart", 1.1f, 0.5f);
        itemRestart.getTransform().setPosition(1.5f, -1.6f, -4f);
        rootResult.addChildObject(itemRestart);

        // add root obj
        addSceneObject(rootResult);
        Log.e(TAG,"start show results 7");
    }

    private GVRSceneObject createButton(String name, float width, float height){
        GVRTexture texture = createButtonTexure(name,width*100,height*100);
        GVRSceneObject item = new GVRSceneObject(gContext, width, height,texture);
        item.setName(name);
        String[] tag = {"mMenu", name}; item.setTag(tag);
        item.attachComponent(new GVRSphereCollider(gContext));
        return item;
    }

    private void showResultStart(){
        for (int i=0;i<8;i++) {
            if(!listCards[i].selected){
                Log.d(TAG,"Show result delete obj "+i);
                selNames[7]=i;
                curSelection=null;
                listCards[i].deleteCard();
                mPickHandler.onNoPick(mPicker);
                }
        }
        long total = System.currentTimeMillis() - timerList.get("start");
        String strRes = "\"Total time\": \"" + total + "ms\"";
        for (int j = 0; j < timerListRes.size(); j++) {
//            Log.d(TAG,"result j="+j);
            try {
                String[] strIter = timerListRes.get(j);
//                Log.d(TAG,"name="+strIter[0]+"res="+strIter[1]);
                long resTime = Long.parseLong(strIter[1]);
                String res = String.valueOf(Math.round(resTime * 100 / total));
                strRes += ",\""+strIter[0] +"\""+ ": \"" + res + "\"";
            } catch (Exception e) {  Log.d(TAG, "Error parse result arraylist");  }
        }
        float s = 0; float e = 0;
        float[] kArray= new float[] {8.1f,6.8f,6,5.3f,4.7f,4,3.2f,1.8f};
            for(int i=0;i<selNames.length;i++){
                if(i<3){if(selNames[i]==0 || selNames[i]==6 || selNames[i]==7){s+=kArray[i];}}
                if(i>4){if(selNames[i]==1 || selNames[i]==2 || selNames[i]==3 || selNames[i]==4){s+=kArray[7-i];}}
                if(selNames[i]==2 || selNames[i]==3 || selNames[i]==4){e+=kArray[i];}
                Log.e(TAG, "Calculate: i=" +i+" color="+selNames[i]+" s=" +s+" e="+e); //
            }
        String data = Arrays.toString(selNames);
        resultsLevels[0]= Math.round(s*100/42);
        resultsLevels[1]= Math.round((e-9)*100/12);
        Main.saveData(data,strRes);
        Log.d(TAG, "Show result" + data + " timer=" + strRes);
        Log.d(TAG, "Your Stress Level: s="+s+" %=" + resultsLevels[0]);
        Log.d(TAG, "Your Efficiency Level: e=" + e+" %="+resultsLevels[1]);
//        showMsg(arrColorStr[3]+"\nStress Level: "+Math.round(s*100/42)+"\nEfficiency Level: "+Math.round((e-9)*100/12),false);
        new GVROpacityAnimation(curTextMsg,1,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                    Log.d(TAG,"delete task Message");
                    if(curTextMsg != null) {gContext.getMainScene().removeSceneObject(curTextMsg);curTextMsg = null;};
                    Log.e(TAG,"start show results 0");
                    showResult();
                }});
    }

    private void showMsg2(String[] str, boolean delete, float width, float height){
        GVRTexture texture = createTextTexture(str,24,Math.round(width*100),Math.round(height*100),-1);
        GVRSceneObject item = new GVRSceneObject(gContext, width, height,texture);
        item.setName("iMsg");
        item.getRenderData().setDepthTest(false);
        item.getTransform().setPosition(0, 0, -4f);
        item.getRenderData().setRenderingOrder(OVERLAY);
        item.getRenderData().getMaterial().setOpacity(0);
        curTextMsg = item;
        addSceneObject(item);
        GVRAnimation an= new GVROpacityAnimation(curTextMsg,1,1)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine());
        if(delete){
            an.setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
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
        Log.e(TAG,"atrta show");
        String[] str = new String[2];
        str[0]="Please prepare to the test.";
        str[1]="You should select the more pleasure color one by one.";
//        String str = "You should select the more pleasure color one by one.";
//        showMsg(str,true);
        showMsg2(str,true,6.5f,0.8f);
//        showPie("Stress");
    }

    private GVRTexture createTextTexture(String[] str, int fontSize, int width, int height, int fillColor){
        Rect r = new Rect();
        Bitmap bitmapAlpha = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(8F);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        c.drawRoundRect(0,0,width,height,20,20,paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1F);
        paint.setTextSize(fontSize);
        for(int i=0;i<str.length;i++){
            paint.getTextBounds(str[i], 0, str[i].length(), r);
            float x = width/2f - r.width() / 2f - r.left;;
            c.drawText(str[i],x,35+25*i,paint);
        }
        if (fillColor>0){ c.drawColor(fillColor);}
        GVRBitmapTexture texture = new GVRBitmapTexture(gContext,bitmapAlpha);
        return texture;
    }

    private GVRTexture createButtonTexure(String str, float width, float height){
        Log.e(TAG,"Pie 5");
        Rect r = new Rect();
        int w = (int) width, h = (int) height;
        Bitmap bitmapAlpha = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(8F);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        Log.e(TAG,"Pie 6");
        c.drawRoundRect(0,0,width,height,20,20,paint);
//        c.drawLine(0F, 0F, 300F, 300F, paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2F);
        paint.setTextSize(30F);
        Log.e(TAG,"Pie 7");
            paint.getTextBounds(str, 0, str.length(), r);
            float x = width/2f - r.width() / 2f - r.left;;
            c.drawText(str,x,35,paint);
        c.drawColor(Color.argb(100,150,150,150));
        GVRBitmapTexture texture = new GVRBitmapTexture(gContext,bitmapAlpha);
        return texture;
    }

    private GVRTexture createResultTexure(String name, int data, int startColor, int endColor){
        int width = 500; int height =500;
        float w2 = width/2, h2 = height/2, stroke = 50, rotate=90f, angle = data*3.6f, s2= stroke/2, radius = (width-2*stroke)/2;;
        Rect r = new Rect();
        Bitmap bitmapAlpha = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // start location
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(startColor);
        // draw pie start position
        c.drawOval(w2-s2,height-stroke-s2,w2+s2,height-stroke+s2,paint);
        // Text part
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2F);
        // draw bound
        paint.setColor(Color.GRAY);
        float big = 20;
        float small = 1.5f*stroke;
        c.drawArc(big,big,width-big,height-big,0,360,false,paint);
        c.drawArc(small,small,width-small,height-small,0,360,false,paint);
        // draw text
        paint.setColor(Color.BLUE);
        String dataStr=String.valueOf(data)+"%";
        paint.setTextSize(100F);
        paint.getTextBounds(dataStr, 0, dataStr.length(), r);
        float x1 = w2 - r.width() / 2f - r.left;
        c.drawText(dataStr,x1,h2-20,paint);
        paint.setTextSize(60F);
        paint.getTextBounds(name, 0, name.length(), r);
        float x2 = w2 - r.width() / 2f - r.left;
        c.drawText(name,x2,h2+80,paint);

        // draw chart
        Shader gradient = new SweepGradient(w2,h2, startColor, endColor);
        Matrix gradientMatrix = new Matrix();
        gradientMatrix.preRotate(rotate, w2, h2);
        gradient.setLocalMatrix(gradientMatrix);
        paint.setShader(gradient);
        paint.setStrokeWidth(stroke);
        c.drawArc(stroke,stroke,width-stroke,height-stroke,rotate,angle,false,paint);
        paint.setStyle(Paint.Style.FILL);
        //draw end point of chart
        double c1 = w2+Math.cos(Math.toRadians(angle+90))*radius;
        double c2 = h2+Math.sin(Math.toRadians(angle+90))*radius;
        float cf1 = (float) c1; float cf2 = (float) c2;
        c.drawOval(cf1-s2,cf2-s2,cf1+s2,cf2+s2,paint);
        // fill draw
//        c.drawColor(Color.argb(100,150,150,150));
        GVRBitmapTexture texture = new GVRBitmapTexture(gContext,bitmapAlpha);
        return texture;
    }

    private void showPie(GVRSceneObject root, float yPos){
        Log.e(TAG,"Pie 4");
        GVRTexture texture = createResultTexure("Stress",resultsLevels[0],Color.GREEN,Color.RED);
        GVRSceneObject item = new GVRSceneObject(gContext, 2, 2,texture);
        item.getTransform().setPosition(-1.3f, yPos, -4f);
        GVRTexture texture2 = createResultTexure("Efficiency",resultsLevels[1],Color.RED,Color.GREEN);
        root.addChildObject(item);
        GVRSceneObject item2 = new GVRSceneObject(gContext, 2, 2,texture2);
        item2.getTransform().setPosition(1.3f, yPos, -4f);
        root.addChildObject(item2);
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
        if(curSelection!=null){
            trSelect = true;
            Log.d(TAG,"startSelect "+curSelection.getName());
//            String[] tag =(String[]) curSelection.getTag();
            int i = Integer.parseInt(curSelection.getName());
            curSelection=null;
            selectCounter++;
            if(selectCounter==7){ showResultStart();}
        }else{//showTaskStart(arrColorStr[0]);
            Log.d(TAG,"start create cards");
            createBaseObjs();
        }
    }

    private void animateDeleteMsg(){
        Log.d(TAG,"delete Message");
        new GVRPositionAnimation(curTextMsg,1,0, 1.7f, -4f)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                     Log.d(TAG,"delete 2 Message");
//                    gContext.getMainScene().removeSceneObject(curTextMsg); curTextMsg=null;
                    startSelect();
                }});

  }

    private void createBaseObjs(){
        Log.d(TAG,"start create cards 2");
        for (int i=0;i<8;i++) {
            Log.d(TAG,"start create card "+i);
            int[] colors = new int[300 * 300];
            Arrays.fill(colors, 0, 300 * 300, Color.argb(255, arrColor[i][0], arrColor[i][1], arrColor[i][2]));
            Bitmap bitmapAlpha = Bitmap.createBitmap(colors, 300, 300, Bitmap.Config.ARGB_8888);
            GVRBitmapTexture texture = new GVRBitmapTexture(gContext, bitmapAlpha);
            listCards[i] = new Card(gContext,i,arrLoc[i],texture);
            listCards[i].showCard();
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
