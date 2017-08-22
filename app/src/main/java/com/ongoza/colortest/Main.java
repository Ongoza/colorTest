
//TODO сделать сайт для статистики
//----------------------------
//TODO потом добавить поле ввода почты
//TODO сделать сайт с результатами

// Connection to MongoDB by Sleepy.Mongoose
// https://disqus.com/home/discussion/snailinaturtleneck/sleepymongoose_a_mongodb_rest_interface/newest/
// easy_install pymongo==2.7.2  - on error Connection after install

package com.ongoza.colortest;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.MotionEvent;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;


class Main extends GVRMain {

    private static final String TAG = "VRTest";
    private Context mContext;
    private int delayCounter;
    private int delayCounterDefault=3;
    private int delayCounterMulty;
    private int delayCounterMultyDefault=15;
//    private GVRContext gContext;
    private MainActivity mainActivity;
    private boolean trArrow=true;
    private static String guid;
    boolean trStep;
    private static ColorTestScene  colorTestScene;

    Main(MainActivity activity) {
//        Log.d(TAG, " start main 000");
        mainActivity = activity;   mContext = activity;  }

    @Override public void onInit(GVRContext gContext) throws Throwable {
//        Log.d(TAG, " start main 001"); this.gContext = gContext;
        colorTestScene = new ColorTestScene(gContext, this);
//        Log.d(TAG, " start main 002");
        trStep=false;
        gContext.setMainScene(colorTestScene);
//        Log.d(TAG, " start main 003");
        loadGUID();
        delayCounter = delayCounterDefault; trArrow=true; delayCounterMulty=delayCounterMultyDefault;
//        saveData("name", "\"times\":\"now\"");
    }

    @Override public void onStep() {
        if(trStep){
            if(delayCounterMulty<0){ delayCounterMulty = delayCounterMultyDefault;
//            float y = colorTestScene.getMainCameraRig().getHeadTransform().getRotationW();
//            Log.d(TAG,"mainRig rotate ="+colorTestScene.getMainCameraRig().getHeadTransform());
            if(Math.abs(colorTestScene.getMainCameraRig().getHeadTransform().getRotationW())<0.8f){
                if(delayCounter>0){delayCounter--;
                }else{
                    if(trArrow){
                        float[] y=colorTestScene.getMainCameraRig().getLookAt();
//                        Log.d(TAG,"mainRig rotate ="+y[0]);
                        if(y[0]<0){
                            colorTestScene.showArrow(false);
                        }else{colorTestScene.showArrow(true);}
                    trArrow=false;
                }}
            }else{delayCounter = delayCounterDefault; trArrow=true; colorTestScene.hideArrow();}
        }else{delayCounterMulty--;}}
    }

    MainActivity getMainActivity(){return mainActivity;}

    void saveData(String data, String times, String pickes, String total){
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService (Context.CONNECTIVITY_SERVICE);
        String timeZone = TimeZone.getDefault().getID();
        String date = Long.toString(System.currentTimeMillis());
//        Log.d(TAG, " start save guid="+guid+" data "+data+" time="+times+" date="+ date);
        String ips="["; boolean tr1 = false;
        try{
            Enumeration <NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface net = networks.nextElement();
                Enumeration<InetAddress> inetAddresses = net.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        int ipAddress = inetAddress.hashCode();
                        String ip = String.format(Locale.ENGLISH,"%d.%d.%d.%d", (ipAddress >> 24 & 0xff), (ipAddress >> 16 & 0xff),(ipAddress >> 8 & 0xff), (ipAddress & 0xff));
                        if(tr1){ips+=", \""+ip+"\"";}else{tr1=true; ips+="\""+ip+"\"";}
                    }}}
        }catch (Exception e){ Log.d(TAG,"Error get ip adress");}
        ips+="]";
        if(conMgr.getActiveNetworkInfo() != null){
            if(conMgr.getActiveNetworkInfo().isConnected()) {
                String sendingString = "docs=[{\"guid\":\""+guid
                        +"\",\"timeZone\":\""+timeZone
                        +"\",\"date\":\""+date
                        +"\",\"ips\":"+ips
                        +",\"colors\":\""+data
                        +"\",\"times\":{"+times+"}"
                        +",\"pickes\":{"+pickes+"}"
                        +",\"total\":"+total
                        +"}]";
    //            Log.d(TAG," sendingString="+sendingString);
                ServerConnection longOperation = new ServerConnection();
                try {
                    longOperation.execute(sendingString);
                } catch (Exception e) {
                    Log.d(TAG, "Not connected to DB "); }
            }
        }else{
            Log.d(TAG, "No Internet connection.");
        }
    }

    static String getTAG(){ return TAG; }

    private void loadGUID(){
//        Log.d(TAG, " start guid 1");
        SharedPreferences local_data = mContext.getSharedPreferences("com.ongoza.colortest.guid",Context.MODE_PRIVATE);
        try{ guid = local_data.getString("guid","");
//            Log.d(TAG,"loadData="+guid);
            if(guid.equals("")){
                guid = UUID.randomUUID().toString();
//                Log.d(TAG, " start guid 1 = "+guid);
                SharedPreferences.Editor editor = local_data.edit();
                editor.putString("guid",guid);
                editor.apply();
            }
        }catch (Exception e){
            Log.d(TAG,"exception: no local data");}
    }

    @Override public boolean onBackPress() {
//        mainActivity.finish();
        colorTestScene.showExitPromt();
//       Log.d(TAG, "dd="+mainActivity.getConfigurationManager().isHomeKeyPresent());
        return true;
    }


    void onTouchEvent(MotionEvent event) {
//        Log.d(TAG, "main motion event");
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                colorTestScene.onTouchEvent();
                break;
            default:
                break;
        }
    }


}
