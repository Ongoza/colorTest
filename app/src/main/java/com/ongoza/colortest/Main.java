
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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;


class Main extends GVRMain {

    private static final String TAG = "VRTest";
    private Context mContext;
    private MainActivity mainActivity;
    private static String guid;
    private static ColorTestScene  colorTestScene;

    Main(MainActivity activity) {  Log.d(TAG, " start main 000");
        mainActivity = activity;   mContext = activity;  }

    @Override public void onInit(GVRContext gContext) throws Throwable {
        Log.d(TAG, " start main 001");
        colorTestScene = new ColorTestScene(gContext, this);
        Log.d(TAG, " start main 002");
        gContext.setMainScene(colorTestScene);
        Log.d(TAG, " start main 003");
        loadGUID();
//        saveData("name", "\"times\":\"now\"");
    }

    @Override public void onStep() {

//        Log.d(TAG,"mainRig="+gContext.getMainScne().getMainCameraRig().getTransform());
//        Log.d(TAG,"mainHead="+gContext.getMainScene().getMainCameraRig().getHeadTransform());
//        Log.d(TAG,"mainLookAt="+ Arrays.toString(gContext.getMainScene().getMainCameraRig().getLookAt()));
//        float a = colorTestScene.getMainCameraRig().getHeadTransform().getRotationW();
//        float b = colorTestScene.getMainCameraRig().getHeadTransform().getRotationX();
//        float c = colorTestScene.getMainCameraRig().getHeadTransform().getRotationY();
//        float d = colorTestScene.getMainCameraRig().getHeadTransform().getRotationZ();
//        colorTestScene.rootScene.getTransform().rotateWithPivot(a,b,c,d,0,0,0);

    }

    MainActivity getMainActivity(){return mainActivity;}

    void saveData(String data, String times){
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService (Context.CONNECTIVITY_SERVICE);
        String timeZone = TimeZone.getDefault().getID();
        String date = Long.toString(System.currentTimeMillis());
        Log.d(TAG, " start save guid="+guid+" data "+data+" time="+times+" date="+ date);
        String ips="["; boolean tr1 = false;
        try{Enumeration <NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
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
//         Log.d(TAG," ip="+ips);
        if(conMgr.getActiveNetworkInfo().isConnected()) {
            try {
                String sendingString = "docs=[{\"guid\":\"" + guid+ "\",\"timeZone\":\"" + timeZone + "\",\"date\":\"" + date+ "\",\"ips\":" + ips + ",\"colors\":\""+data+"\",\"times\":{"+times+"}}]";
                ServerConnection longOperation = new ServerConnection();
                longOperation.execute(sendingString);
            } catch (Exception e) {
                Log.d(TAG, "Not connected to DB "+Arrays.toString(e.getStackTrace())); }
        }else{Log.d(TAG,"No Internet connection");}
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

    void onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                colorTestScene.onTouchEvent();
                break;
            default:
                break;
        }
    }


}
