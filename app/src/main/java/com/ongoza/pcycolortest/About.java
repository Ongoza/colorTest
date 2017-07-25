package com.ongoza.pcycolortest;

import android.graphics.Color;
import android.view.Gravity;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMeshCollider;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;
import org.gearvrf.utility.Log;

/**
 * Created by os on 7/22/17.
 */

public class About extends GVRSceneObject{
    private GVRContext gContext;
    public static int  msgTxtClr = Color.rgb(51, 55, 64);
    public static int msgBckClr = Color.rgb(240, 240, 240);
    public static int menuTxtClr = Color.rgb(50, 50, 100);
    public static int menuBckClr = Color.rgb(200, 200, 200);
    private GVRSceneObject itemAbout;
    private GVRSceneObject icon;

    private static final String TAG = Main.getTAG();

    public About (GVRContext gvrContext)  {
        super(gvrContext);
        getTransform().rotateByAxis(20, 0, 1, 0);
        getTransform().setPosition(0,-2f,0);
        this.gContext = gvrContext;
        addAbout();
    }

    private void addAbout(){
        Log.d(TAG,"start create about button");
        String name="about";
        icon = new GVRSceneObject(gContext, gContext.createQuad(0.9f, 0.3f),
                gContext.loadTexture(new GVRAndroidResource(gContext, R.drawable.ico_about)));
        icon.getTransform().setPosition(0.0f, 0.04f, 0.0f);
        icon.getTransform().rotateByAxis(-90, 1, 0, 0);
        icon.getRenderData().setRenderingOrder(GVRRenderData.GVRRenderingOrder.OVERLAY);
        icon.setName("mMenu_"+name);
        GVRMeshCollider collider2 = new GVRMeshCollider(gContext,true);
        icon.attachCollider(collider2);
        String[] arr = {"mMenu",name};
        icon.setTag(arr);
        this.addChildObject(icon);
    }

    public void showAbout(){
        String name="AboutText";
        Log.d(TAG,"show about info");
        String txt = "This application was developed by O.Skuibida & A.Sukhnin for the www.ongoza.com";
        GVRTextViewSceneObject item = new GVRTextViewSceneObject(gContext, 3.1f, 1f, txt);
        item.getTransform().setPosition(0.0f, - 2f, 0f);
        item.getTransform().rotateByAxis(-90, 1, 0, 0);
        item.getRenderData().setRenderingOrder(GVRRenderData.GVRRenderingOrder.OVERLAY);
        item.setName(name);
        item.setGravity(Gravity.CENTER);
        item.setTextSize(5);
        item.setTextColor(msgTxtClr);
        item.setBackgroundColor(msgBckClr);
        GVRTextViewSceneObject itemOkAction = new GVRTextViewSceneObject(gContext, 1f, 0.6f, "OK");
        itemOkAction.setName("showMenu_" + name + "_OK");
        itemOkAction.setTextSize(7);
        itemOkAction.getTransform().setPosition(0f, -1.6f, -1.8f);
        itemOkAction.setTextColor(menuTxtClr);
        itemOkAction.setGravity(Gravity.CENTER);
        itemOkAction.setBackgroundColor(menuBckClr);
        String[] arrItemDel = {"mMenu", "aboutOk"};
        itemOkAction.setTag(arrItemDel);
        GVRMeshCollider collider = new GVRMeshCollider(gContext,true);
        itemOkAction.attachCollider(collider);
        item.addChildObject(itemOkAction);
        itemAbout = item;
        removeChildObject(icon);
        gContext.getMainScene().addSceneObject(item);

    }


    public void hideAbout(){gContext.getMainScene().removeSceneObject(itemAbout);addAbout(); }
}
