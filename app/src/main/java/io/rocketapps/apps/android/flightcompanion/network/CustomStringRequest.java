package io.rocketapps.apps.android.flightcompanion.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.rocketapps.apps.android.flightcompanion.model.Utils;

public class CustomStringRequest extends StringRequest {


    private Map<String, String> mParams;
    private String mToken;
    private Context mContext;

    public CustomStringRequest(int method, String url, JSONObject jsonRequest,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);


    }

    public CustomStringRequest(int method, String url, JSONObject jsonRequest,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener, Map<String, String> params) {
        super(method, url, listener, errorListener);

        this.mParams = params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("X-CSRF-Token", mToken);
        Utils.Log("GETHEADER", "CALLED");
        return headers;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Utils.Log("GETPARAMS", "CALLED");
        return this.mParams;
    }
}
