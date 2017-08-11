
//-----------------------------------
//TODO потом добавить поле ввода почты
//TODO сделать сайт с результатами

// Connection to MongoDB by Sleepy.Mongoose
// https://disqus.com/home/discussion/snailinaturtleneck/sleepymongoose_a_mongodb_rest_interface/newest/

package com.ongoza.colortest;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.MotionEvent;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.net.HttpURLConnection;
import java.net.URL;

class Main extends GVRMain {
    private static final String TAG = "VRTest";
    private Context mContext;
    private MainActivity mainActivity;
    private static String guid;
    private static final String USER_AGENT = "Mozilla/5.0";
    private static ColorTestScene  colorTestScene;

    Main(MainActivity activity) {  Log.d(TAG, " start main 000");
        mainActivity = activity;   mContext = activity;  }

    @Override public void onInit(GVRContext gContext) throws Throwable {
        Log.d(TAG, " start main 001");
        colorTestScene = new ColorTestScene(gContext, this);
        Log.d(TAG, " start main 002");
        gContext.setMainScene(colorTestScene);
        Log.d(TAG, " start main 003");
//        saveData("name", "times");
        loadGUID();
    }

    @Override public void onStep() {}

    MainActivity getMainActivity(){return mainActivity;}

    void saveData(String data, String times){
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService (Context.CONNECTIVITY_SERVICE);
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
                        int ipAddress = inetAddress.hashCode();
                        String ip = String.format(Locale.ENGLISH,"%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
                        if(tr1){ips+=", "+ip;}else{tr1=true; ips+=ip;}
                    }}}
        }catch (Exception e){ Log.e(TAG,"Error get ip adress");}
        ips+="]";
//         Log.d(TAG," ip="+ips);
        HttpURLConnection con;
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
                Log.e(TAG, " start save response="+output);
                wd.close(); con.disconnect();
            } catch (Exception e) { Log.e(TAG, "Not connected to DB"); e.printStackTrace(); }
        }else{Log.e(TAG,"No Internet connection");}
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
            Log.e(TAG,"exception: no local data");}
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
