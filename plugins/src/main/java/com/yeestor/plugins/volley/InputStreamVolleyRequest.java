package com.yeestor.plugins.volley;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

public class InputStreamVolleyRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener ;
    private final Map<String,String> mParams ;


    public InputStreamVolleyRequest(int method, String url , Map<String,String> params, Response.Listener<byte[]>  respListener , @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = respListener ;
        mParams = params ;
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        return Response.success(response.data,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }
}
