package com.morelap.tagskill.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpExecutor {

    public static enum RequestMethod {
        GET, POST
    }

    public static enum HandleType {
        JSON, CUSTOM
    }

    private static final String TAG = "HttpExecutor";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params,
            AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params,
            AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

}
