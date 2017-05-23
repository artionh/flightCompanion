package io.rocketapps.apps.android.flightcompanion.model;

import android.content.Context;

import java.util.Map;

public interface RequestVolley {



    public void onSuccess(String s) throws Exception;
    public void onError();


    public String getUrl();

    public Map<String, String> getParams(Context applicationContext);
}
