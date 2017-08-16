package com.ongoza.colortest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;
import org.gearvrf.GVRTexture;
import org.gearvrf.animation.GVRAnimation;
import org.gearvrf.animation.GVROnFinish;
import org.gearvrf.animation.GVROpacityAnimation;
import org.gearvrf.animation.GVRPositionAnimation;
import org.gearvrf.animation.GVRRepeatMode;
import org.gearvrf.utility.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.gearvrf.GVRRenderData.GVRRenderingOrder.OVERLAY;

class ColorTestScene extends GVRScene {
    private PickHandler mPickHandler;
    private GVRContext gContext;
    private GVRPicker mPicker;
    private Main main;
    private static Map<String,Long> timerList= new HashMap<>();
    private static ArrayList<String[]> timerListRes = new ArrayList<>();
    private static final String TAG = Main.getTAG();
    private float[][] arrLoc = new float[8][2];
    private int[][] arrColor = new int[8][3];
    private int selectCounter=0;
    private GVRSceneObject rootResult;
    private GVRSceneObject rootScene;
    private GVRSceneObject rootAbout;
    private GVRSceneObject cameraCover;
    private GVRSceneObject headArrow;
    private Card[] listCards = new Card[8];
    private GVRSceneObject curSelection = null;
    private GVRSceneObject curTextMsg  = null;
    private int[] resultsLevels = new int[2];
//    private String[] arrNames = {"gray","blue","green","red","yellow","purple","brown","black" };
    private int[] selNames = new int[8]; private int selNamesCounter;

    ColorTestScene(GVRContext gContext,Main main) {
        super(gContext);
        this.gContext =gContext;
        this.main=main;
        GVRMaterial material = new GVRMaterial(gContext);
        material.setMainTexture(gContext.loadFutureTexture(new GVRAndroidResource(gContext, R.drawable.headtrackingpointer)));
        GVRSceneObject headTracker = new GVRSceneObject(gContext, gContext.createQuad(0.1f, 0.1f));
        headTracker.getRenderData().setMaterial(material);
        headTracker.getTransform().setPositionZ(-2.0f);
        headTracker.setName("Head");
        headTracker.getRenderData().setDepthTest(false);
        headTracker.getRenderData().setRenderingOrder(100000);
        getMainCameraRig().addChildObject(headTracker);

        GVRMaterial materialArrow = new GVRMaterial(gContext);
        materialArrow.setMainTexture(gContext.loadFutureTexture(new GVRAndroidResource(gContext, R.drawable.arrow)));
        headArrow = new GVRSceneObject(gContext, gContext.createQuad(1f, 1f));
        headArrow.getRenderData().setMaterial(materialArrow);
        headArrow.getTransform().setPositionZ(-5.0f);
        headArrow.setEnable(false);
        getMainCameraRig().addChildObject(headArrow);

        mPickHandler = new PickHandler();
//        Log.d(TAG, " start scene 000");
        rootScene = new GVRSceneObject(gContext);
        addSceneObject(rootScene);
        showCover();
//        createCameraRig();
        createArrays();
//        Log.d(TAG, "start color scene");
    }

    private void startApp(){

        getEventReceiver().addListener(mPickHandler);
        mPicker = new GVRPicker(gContext, this);
        show();
        main.trStep=true;
    }

    void showArrow(boolean plus){
//        Log.d(TAG, "show arrow "+plus);
        if(plus){
//            Log.d(TAG, "show arrow 00");
            headArrow.getTransform().setRotationByAxis(0,0,0,1);
        }else{
            headArrow.getTransform().setRotationByAxis(180,0,0,1);
        }
        headArrow.setEnable(true);
    }

    void hideArrow(){
        headArrow.setEnable(false);
    }

    private void showCover(){
//        Log.d(TAG, " start cover 000");
        GVRMaterial materialCover = new GVRMaterial(gContext);
        materialCover.setMainTexture(gContext.loadFutureTexture(new GVRAndroidResource(gContext, R.drawable.cover)));
        cameraCover = new GVRSceneObject(gContext, gContext.createQuad(15f, 15f));
        cameraCover.getRenderData().setMaterial(materialCover);
        cameraCover.getTransform().setPositionZ(-6);
        getMainCameraRig().addChildObject(cameraCover);

        GVRAnimation an= new GVROpacityAnimation(cameraCover,2,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine());
            an.setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                cameraCover.setEnable(false);
                getMainCameraRig().removeChildObject(cameraCover);
                startApp();
            }});
        getMainCameraRig().getLeftCamera().setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        getMainCameraRig().getRightCamera().setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
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

    void onTouchEvent() {
//        Log.d(TAG, "scene Touch event.");
        if (mPickHandler.PickedObject!=null){
//            Log.d(TAG, "Touch name="+mPickHandler.PickedObject.getName());
            String[] tag =(String[]) mPickHandler.PickedObject.getTag();
//            Log.d(TAG, "Touch type="+tag[0]);
            String name = mPickHandler.PickedObject.getName();
            switch (tag[0]) {
                case "cMenu": selectColor(getSceneObjectByName(name));
                    break;
                case "mMenu":
//                    Log.d(TAG, "Pick about 1 "+tag[1]);
                    switch(tag[1]){
                        case "Ok":
                            hideAbout();
                            showResult();
                            break;
                        case "About":
//                            Log.d(TAG,"show about");
                            hideResult();
                            showAbout();
                            break;
                        case "Restart":
                            hideResult();
                            hideAbout();
                            show();
                            break;
                        case "Exit":
                            main.getMainActivity().exitHome();
                            break;
                        default: break;
                    } break;
                default: break; }
    }}

    private void selectColor(GVRSceneObject selObj){
//        Log.d(TAG,"start selectColor for "+selObj.getName());
            if(curSelection != selObj){
            String curSelectionName = selObj.getName();
//            Log.d(TAG,"start animation color "+curSelectionName);
            curSelection = selObj;
            mPickHandler.onNoPick(mPicker);
            long curTime = System.currentTimeMillis();
            long addTime = curTime-timerList.get("startSelect");
            addTimerRes("select_"+curSelectionName,addTime);
            timerList.put("startSelect",curTime);
            int i = Integer.parseInt(curSelectionName); selNames[selNamesCounter]=i; selNamesCounter++;
//            Log.d(TAG,"colorSelect "+curSelectionName+" time ="+addTime);
            listCards[i].deleteCard();
            startSelect();
        }else{Log.d(TAG,"error. can not find object"+selObj.getName());}
    }

    private void hideResult() { if(rootResult!=null){ rootScene.removeChildObject(rootResult); rootResult=null;}}

    private void showAbout(){
        rootAbout = new GVRSceneObject(gContext);
//        Log.d(TAG,"show about info");
        String[] str = new String[8];
        str[0]="This application was developed by";
        str[1]="O.Skuibida & A.Sukhnin";
        str[2]="for the www.ongoza.com ";
        str[4]="More information about you";
        str[5]="on the www.my-medium.com/en";
        str[7]="Version 1.1";
        GVRTexture texture = createTextTexture(str,24,400,220,-1);
        GVRSceneObject item = new GVRSceneObject(gContext, 4, 3,texture);
        item.getTransform().setPosition(0, 0.5f, -4f);
        rootAbout.addChildObject(item);

        GVRSceneObject itemOk = createButton("Ok", 1.1f, 0.5f);
        itemOk.getTransform().setPosition(0, -1.5f, -4f);
        rootAbout.addChildObject(itemOk);
        for (GVRSceneObject child : rootAbout.children()) {
            child.getRenderData().getMaterial().setOpacity(0);
            new GVROpacityAnimation(child, 2, 1)
                    .setRepeatMode(GVRRepeatMode.ONCE)
                    .setRepeatCount(1)
                    .start(gContext.getAnimationEngine());
        }
        rootScene.addChildObject(rootAbout);
//        addSceneObject(rootAbout);

    }

    private void hideAbout(){  if(rootAbout!=null){rootScene.removeChildObject(rootAbout);rootAbout=null;}}

    private void showResult() {
//        Log.d(TAG,"start show results");
        rootResult = new GVRSceneObject(gContext);
        String[] str = new String[2];
        str[0]="Your results:";
        GVRTexture texture = createTextTexture(str,30,400,50,-1);
        GVRSceneObject item = new GVRSceneObject(gContext, 5, 0.7f,texture);
        item.getTransform().setPosition(0, 1.7f, -5f);
        rootResult.addChildObject(item);
        showPie(rootResult,0);

        GVRSceneObject itemAbout = createButton("About", 1.3f, 0.5f);
        itemAbout.getTransform().setPosition(-1.4f, -1.7f, -5f);
        rootResult.addChildObject(itemAbout);

//        GVRSceneObject itemExit = createButton("Exit", 1.3f, 0.5f);
//        itemExit.getTransform().setPosition(0f, -1.7f, -5f);
//        rootResult.addChildObject(itemExit);

        GVRSceneObject itemRestart = createButton("Restart", 1.3f, 0.5f);
        itemRestart.getTransform().setPosition(1.4f, -1.7f, -5f);
        rootResult.addChildObject(itemRestart);

        // add root obj

        for (GVRSceneObject child : rootResult.children()) {
            child.getRenderData().getMaterial().setOpacity(0);
            new GVROpacityAnimation(child, 2, 1)
                    .setRepeatMode(GVRRepeatMode.ONCE)
                    .setRepeatCount(1)
                    .start(gContext.getAnimationEngine());
        }
//        addSceneObject(rootResult);
        rootScene.addChildObject(rootResult);
//        Log.d(TAG,"start show results 7");
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
//                Log.d(TAG,"Show result delete obj "+i);
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
//                Log.d(TAG, "Calculate: i=" +i+" color="+selNames[i]+" s=" +s+" e="+e); //
            }
        String data = Arrays.toString(selNames);
        resultsLevels[0]= Math.round(s*100/42);
        resultsLevels[1]= Math.round((e-9)*100/12);
        main.saveData(data,strRes);
//        Log.d(TAG, "Show result" + data + " timer=" + strRes);
//        Log.d(TAG, "Your Stress Level: s="+s+" %=" + resultsLevels[0]);
//        Log.d(TAG, "Your Efficiency Level: e=" + e+" %="+resultsLevels[1]);
//        showMsg(arrColorStr[3]+"\nStress Level: "+Math.round(s*100/42)+"\nEfficiency Level: "+Math.round((e-9)*100/12),false);
        new GVROpacityAnimation(curTextMsg,1,0)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
//                    Log.d(TAG,"delete task Message");
                    if(curTextMsg != null) {
                        rootScene.removeChildObject(curTextMsg);
//                        gContext.getMainScene().removeSceneObject(curTextMsg);
                        curTextMsg = null;}
//                    Log.d(TAG,"start show results 0");
                    showResult();
                }});
    }

    private void showMsg2(String[] str, boolean delete, float width, float height){
        GVRTexture texture = createTextTexture(str,24,Math.round(width*100),Math.round(height*100),-1);
        GVRSceneObject item = new GVRSceneObject(gContext, width, height,texture);
        item.setName("iMsg");
        item.getRenderData().setDepthTest(false);
        item.getTransform().setPosition(0, 0, -5f);
        item.getRenderData().setRenderingOrder(OVERLAY);
        item.getRenderData().getMaterial().setOpacity(0);
        curTextMsg = item;
        rootScene.addChildObject(item);
//        addSceneObject(item);
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
        selectCounter=0; rootResult=null; rootAbout=null;
        timerList.clear(); resultsLevels[0]=0; resultsLevels[1]=0;
        timerList.put("start",System.currentTimeMillis());
        timerList.put("startSelect",System.currentTimeMillis());
        for(int i=0;i<8;i++) {selNames[i] = -1;} selNamesCounter=0;
        String[] str = new String[3];
        str[0]="Please select";
        str[1]="the more pleasure color one by one.";
        showMsg2(str,true,6.5f,0.8f);
    }

    private GVRTexture createTextTexture(String[] str, int fontSize, int width, int height, int fillColor){
        Rect r = new Rect();
        Bitmap bitmapAlpha = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float stroke = 4f;
        paint.setStrokeWidth(stroke);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        c.drawRoundRect(stroke,stroke,width-stroke,height-stroke,20,20,paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1F);
        paint.setTextSize(fontSize);
        for(int i=0;i<str.length;i++){
            if(str[i]!=null){
            paint.getTextBounds(str[i], 0, str[i].length(), r);
            float x = width/2f - r.width() / 2f - r.left;
            if(i==str.length-1){paint.setTextSize(10); x=30;}
            c.drawText(str[i],x,35+25*i,paint);
        }}
        if (fillColor>0){ c.drawColor(fillColor);}
        return new GVRBitmapTexture(gContext,bitmapAlpha);
    }

    private GVRTexture createButtonTexure(String str, float width, float height){
//        Log.d(TAG,"Pie 5");
        Rect r = new Rect();
        int w = (int) width, h = (int) height;
        Bitmap bitmapAlpha = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float stroke = 4f;
        paint.setStrokeWidth(stroke);
        paint.setColor(Color.rgb(46,191,248));
        paint.setStyle(Paint.Style.STROKE);
        c.drawRoundRect(stroke,stroke,width-stroke,height-stroke,20,20,paint);
        paint.setColor(Color.argb(100,46,191,248));
        paint.setStyle(Paint.Style.FILL);
        c.drawRoundRect(stroke,stroke,width-stroke,height-stroke,20,20,paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2F);
        paint.setTextSize(30F);
            paint.getTextBounds(str, 0, str.length(), r);
            float x = width/2f - r.width() / 2f - r.left;
            c.drawText(str,x,35,paint);
//        c.drawColor(Color.argb(100,150,150,150));
//        c.drawColor(Color.argb(100,46,191,248));
        return new GVRBitmapTexture(gContext,bitmapAlpha);
    }

    private GVRTexture createPieTexure(String name, int data, int startColor, int endColor){
        int width = 500; int height =500;
        float w2 = width/2, h2 = height/2, stroke = 50, rotate=90f, angle = data*3.6f, s2= stroke/2, radius = (width-2*stroke)/2;
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
        float big = 0.5f*stroke;
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
        return new GVRBitmapTexture(gContext,bitmapAlpha);
    }

    private void showPie(GVRSceneObject root, float yPos){
        GVRTexture texture = createPieTexure("Stress",resultsLevels[0],Color.GREEN,Color.RED);
        GVRSceneObject item = new GVRSceneObject(gContext, 2, 2,texture);
        item.getTransform().setPosition(-1.3f, yPos, -5f);
        GVRTexture texture2 = createPieTexure("Efficiency",resultsLevels[1],Color.RED,Color.GREEN);
        root.addChildObject(item);
        GVRSceneObject item2 = new GVRSceneObject(gContext, 2, 2,texture2);
        item2.getTransform().setPosition(1.3f, yPos, -5f);
        root.addChildObject(item2);
    }

    private void startSelect(){
        if(curSelection!=null){
//            Log.d(TAG,"startSelect "+curSelection.getName());
            curSelection=null;
            selectCounter++;
            if(selectCounter==7){ showResultStart();}
        }else{//showTaskStart(arrColorStr[0]);
//            Log.d(TAG,"start create cards");
            createBaseObjs();
        }
    }

    private void animateDeleteMsg(){
//        Log.d(TAG,"delete Message");
        new GVRPositionAnimation(curTextMsg,1,0, 2f, -5f)
                .setRepeatMode(GVRRepeatMode.ONCE)
                .setRepeatCount(1)
                .start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
//                     Log.d(TAG,"delete 2 Message");
//                    gContext.getMainScene().removeSceneObject(curTextMsg); curTextMsg=null;
                    startSelect();
                }});

  }

    private void createBaseObjs(){
//        Log.d(TAG,"start create cards 2");
        for (int i=0;i<8;i++) {
//            Log.d(TAG,"start create card "+i);
            int[] colors = new int[300 * 300];
            Arrays.fill(colors, 0, 300 * 300, Color.argb(255, arrColor[i][0], arrColor[i][1], arrColor[i][2]));
            Bitmap bitmapAlpha = Bitmap.createBitmap(colors, 300, 300, Bitmap.Config.ARGB_8888);
            GVRBitmapTexture texture = new GVRBitmapTexture(gContext, bitmapAlpha);
            listCards[i] = new Card(gContext,rootScene,i,arrLoc[i],texture);
            listCards[i].showCard();
        }

    }

    static long getTimer(String name){ return timerList.get(name);}

    static void addTimerRes(String name, long time){
//        Log.d(TAG,"1 add data to res "+name+" "+time);
        String[] strArr ={name, String.valueOf(time)};
        timerListRes.add(strArr);
//        Log.d(TAG,"2 add data to res "+strArr[0]+" "+strArr[1]+" lenght="+timerListRes.size());
    }
}
