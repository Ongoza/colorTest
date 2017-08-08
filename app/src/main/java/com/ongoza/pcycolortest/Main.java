//TODO добавить меню выход
//TODO добавить меню рестарт

//TODO сделать красивые сообщения и кнопки
//TODO сделать карточки как объекты - паралелльное исчезновение

//TODO потом добавить поле ввода почты
//TODO сделать сайт с результатами

package com.ongoza.pcycolortest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;
import org.gearvrf.scene_objects.GVRVideoSceneObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
//import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bson.Document;

/**
 * Created by os on 6/1/17.
 */

public class Main extends GVRMain {
    private static final String TAG = "VRTest";
    private GVRContext gContext;
    public static Context mContext;
    private static String guid;
    private Resources resources;
    private static final String USER_AGENT = "Mozilla/5.0";
    private static ColorTestScene  colorTestScene;

    public Main(MainActivity activity) {
//        Log.d(TAG, " start main 000");
        mContext = activity;  }

    @Override public void onInit(GVRContext gvrContext) throws Throwable {
        this.gContext = gvrContext;
//        Log.d(TAG, " start main 1");
        colorTestScene = new ColorTestScene(gContext, mContext, this );
        loadGUID();
        saveData("[5, 1, 2, 6, 3, 7, 4, 0]", "\"time\": \"15430ms\"");
//        Log.d(TAG, " start activity 2");
        gContext.setMainScene(colorTestScene);
    }

    @Override public void onStep() {}

    public static void saveData(String data, String times){
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService (Context.CONNECTIVITY_SERVICE);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String date = sdf.format();
        String timeZone = TimeZone.getDefault().getID();
        String date = Long.toString(System.currentTimeMillis());
        Log.e(TAG, " start save guid="+guid+" data "+data+" time="+times+" date="+ date);
        String ips="["; boolean tr1 = false;
        try{Enumeration <NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface net = networks.nextElement();
                Enumeration<InetAddress> inetAddresses = net.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        if(tr1){ips+=", "+ip;}else{tr1=true; ips+=ip;}
                    }}}
        }catch (Exception e){ Log.e(TAG,"Error get ip adress");}
        ips+="]";// Log.e(TAG," ip="+ips);
        HttpURLConnection con = null;
        if(conMgr.getActiveNetworkInfo().isConnected()) {
            try {
                String url = "http://192.168.1.30:27080/local/dbColors/_insert";
                URL obj = new URL(url);
                Log.e(TAG, " start save 11 url=" + url);
                con = (HttpURLConnection) obj.openConnection();
                con.setConnectTimeout(1000);
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setDoOutput(true);
//                con.setRequestMethod("GET");
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setRequestProperty("Accept-Language", "UTF-8");
                con.setRequestProperty("Accept-Charset", "UTF-8");
                con.connect();
                OutputStream wd = con.getOutputStream();
                String x = "docs=[{\"guid\":\"" + guid+ "\",\"timeZone\":\"" + timeZone + "\",\"date\":\"" + date+ "\",\"ips\":\"" + ips + "\",\"colors\":\""+data+"\",\"times\":{"+times+"}}]";
                Log.e(TAG, " start save x=" + x);
                wd.write(x.getBytes());
                wd.flush();
                String line, output = "";
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = rd.readLine()) != null)
                    output += line;
                rd.close();
//                int responseCode = con.getResponseCode();
//                Log.e(TAG, "Response Code : " + responseCode);
//                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String inputLine; StringBuffer response = new StringBuffer();
//                while ((inputLine = in.readLine()) != null) {response.append(inputLine);}
                Log.e(TAG, " start save response="+output);
                wd.close(); con.disconnect(); con = null;
            } catch (Exception e) { con=null; Log.e(TAG, "Not connected to DB"); e.printStackTrace(); }
        }else{Log.e(TAG,"No Internet connection");}
    }


//    public String getMain(){ return this; }

    public static String getTAG(){ return TAG; }

    private void loadGUID(){
//        Log.d(TAG, " start guid 1");
        SharedPreferences local_data = mContext.getSharedPreferences(mContext.getResources().getString(R.string.app_guid),Context.MODE_PRIVATE);
        try{ guid = local_data.getString("guid","");
            org.gearvrf.utility.Log.d(TAG,"loadData="+guid);
//            Log.d(TAG, " start guid 2 =");
            if(guid.equals("")){
                guid = UUID.randomUUID().toString();
//                Log.d(TAG, " start guid 1 = "+guid);
                SharedPreferences.Editor editor = local_data.edit();
                editor.putString("guid",guid);
                editor.commit();
            }
        }catch (Exception e){
            Log.d(TAG,"exception: no local data");}
    }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                colorTestScene.onTouchEvent();
                break;
            default:
                break;
        }
    }

    private void connectToDB(){}

}
