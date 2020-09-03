package com.yeestor.plugins.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {

    public static Map<String, String> cacheHeaders ;

    private static final String TAG = "GsonRequest" ;

    public boolean shouldOverwriteHeader = false ;
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Response.Listener<T> listener;

    public GsonRequest(String url, Class<T> clazz, Map<String, String> params,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(params == null ? Method.GET : Method.POST , url, errorListener);
        this.clazz = clazz;
        this.params = params ;
        this.headers = null;
        this.listener = listener;
    }
    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(params == null ? Method.GET : Method.POST, url, errorListener);
        this.shouldOverwriteHeader = shouldOverwriteHeader ;
        this.clazz = clazz;
        this.params = params ;
        this.headers = headers;
        this.listener = listener;
    }

    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, boolean shouldOverwriteHeader, Map<String, String> params,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(params == null ? Method.GET : Method.POST, url, errorListener);
        this.shouldOverwriteHeader = shouldOverwriteHeader ;
        this.clazz = clazz;
        this.params = params ;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers,"UTF-8"));
            Log.i(TAG,"resp:"+json);

            if(cacheHeaders == null || this.shouldOverwriteHeader ) {
                cacheHeaders = response.headers ;
                Log.i(TAG,"response.headers:"+response.headers);
            }


            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {

            Log.w(TAG,"UnsupportedEncodingException :"+e.getMessage());
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            Log.w(TAG,"JsonSyntaxException :"+e.getMessage());
            return Response.error(new ParseError(e));
        }
    }
}
