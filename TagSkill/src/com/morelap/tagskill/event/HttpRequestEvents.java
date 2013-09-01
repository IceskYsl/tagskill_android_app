package com.morelap.tagskill.event;

import com.loopj.android.http.RequestParams;
import com.morelap.tagskill.net.HttpExecutor;

public class HttpRequestEvents {

    private HttpRequestEvents() {
    }

    public static abstract class HttpRequestEvent {
        public HttpExecutor.RequestMethod mRequestMethod;
        public HttpExecutor.HandleType mHandleType;
        public String mUrl;
        public RequestParams mRequestParams;
        public boolean mForceRefresh;
    }

    public static class SlidesRequestEvent extends HttpRequestEvent {
        public SlidesRequestEvent(String url) {
            mUrl = url;
        }
    }

    public static class ArticleListRequestEvent extends HttpRequestEvent {
        public ArticleListRequestEvent(String url, RequestParams requestParams) {
            mUrl = url;
            mRequestParams = requestParams;
        }
    }

    public static class UserInfoRequestEvent extends HttpRequestEvent {
        public String mResponse;

        public UserInfoRequestEvent(String url, RequestParams requestParams,
                String response) {
            mUrl = url;
            mRequestParams = requestParams;
            mResponse = response;
        }
    }

}
