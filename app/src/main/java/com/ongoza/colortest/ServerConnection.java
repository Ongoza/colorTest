package com.ongoza.colortest;

import android.os.AsyncTask;
import android.util.Log;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.BufferedReader;
import java.util.Arrays;
import java.io.InputStreamReader;



class ServerConnection extends AsyncTask<String, Void, Void> {
    private static final String TAG = Main.getTAG();
    private static final String USER_AGENT = "Mozilla/5.0";
//    private static final String ip = "192.168.1.30";
    private static final String ip = "91.212.177.22";

    @Override
    protected Void doInBackground(String... data) {
//        Log.d(TAG,"conData="+ Arrays.toString(data));
        try {
//            pingServer();
              String sendingString = data[0];
//              Log.d(TAG, " start save sendingString=" + sendingString);
                    // db.createCollection(dbColors)
                    URL url = new URL("http://"+ip+":27080/local/dbColors/_insert");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setReadTimeout(600); // millis
                    con.setConnectTimeout(1000); // millis
                    con.setRequestProperty("Accept-Language", "UTF-8");
                    con.setRequestProperty("Accept-Charset", "UTF-8");
                    con.setRequestProperty("User-Agent", USER_AGENT);
                    con.connect();
                    OutputStream wd = con.getOutputStream();
                    wd.write(sendingString.getBytes());
                    wd.flush();
                    String line; String output = "";
                    try { BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        while ((line = in.readLine()) != null){ output += line;}
                    } finally {  con.disconnect(); }
//                    Log.d(TAG, " start save 2 response="+output);
                }catch(Exception e){
//            Log.d(TAG, "Not connected to DB "+ Arrays.toString(e.getStackTrace()));
            Log.d(TAG, "Not connected to DB ");}
        return null;
    }

//    protected void onProgressUpdate(Integer... progress) {
//        Log.d(TAG,"update progress");
//    }

//    protected void onPostExecute(Long result) {
//        Log.d(TAG,"end progress");
//        // here you call the function, the task is ended.
//
//    }

//    private boolean pingServer(){
//        Runtime runtime = Runtime.getRuntime();
//        try{
//            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 "+ip);
//            int mExitValue = mIpAddrProcess.waitFor();
//            if(mExitValue==0){
////                Log.d(TAG," ping Ok");
//                return true;
//            }else{
////                Log.d(TAG," ping not Ok");
//                return false; }
//        }catch (InterruptedException ignore){ Log.d(TAG,"Ignore Exception:");
//        }catch (IOException e){ Log.d(TAG," Exception:"+e);}
//        return false;
//    }
}
