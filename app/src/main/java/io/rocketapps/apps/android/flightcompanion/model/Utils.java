package io.rocketapps.apps.android.flightcompanion.model;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Utils {


    private static final String API_URL = "/api/android/v1";


    public static void Log(String getparams, String called) {
        Log.d(getparams, called);
    }

    public static String baseUrl() {
        return "http://192.168.20.238:8000"+API_URL;
    }

    public static String getDeviceId(Context applicationContext) {
//        return getUniquePsuedoID();
        return Settings.Secure.getString(applicationContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String getUniquePsuedoID() {

        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static  String getJsonFile(Context applicationContext, String ss) {

            String json = null;
            try {
                InputStream is = applicationContext.getAssets().open("json/"+ss);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
    }

    public class Response {
        public static final String RESPONSE_SERVER = "status";
        public static final int SUCCESS = 100;
    }


    public static Typeface getMediumTypeFace(Context c)
    {
        return Typeface.createFromAsset(c.getAssets(),"fonts/dosis/Dosis-Medium.otf");
    }

    public static Typeface getBoldTypeFace(Context c)
    {
        return Typeface.createFromAsset(c.getAssets(),"fonts/dosis/Dosis-SemiBold.otf");
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
//
//    public void makeRequest(final RequestVolley r) {
//        HashMap<String, String> mParams = (HashMap<String, String>) r.getParams(getApplicationContext());
////        mParams.put(Utils.USER_ID, String.valueOf(mManager.getUserId()));
////        mParams.put(Utils.TOKEN, String.valueOf(mManager.getToken()));
//        Utils.Log("PARAMS", r.getUrl());
//        CustomStringRequest jsonRequest = new CustomStringRequest(Request.Method.GET,
//                r.getUrl(), null,
//                new com.android.volley.Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String result) {
//                        Utils.Log(TAG, String.valueOf(result));
//                        try {
//
//                            r.onSuccess(result);
//
//                        } catch (Exception e) {
//                            r.onError();
//                        }
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Utils.Log(TAG, error.getMessage());
//                NetworkResponse networkResponse = error.networkResponse;
//
//            }
//        }, mParams);
//        jsonRequest.setTag(REQUEST_TAG);
//        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        mQueue.add(jsonRequest);
//    }

}
