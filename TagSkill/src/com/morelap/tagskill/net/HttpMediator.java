package com.morelap.tagskill.net;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.morelap.tagskill.event.HttpRequestEvents;
import com.morelap.tagskill.event.HttpResponseEvents;
import com.morelap.tagskill.eventbus.BusProvider;
import com.morelap.tagskill.model.ArticleList;
import com.morelap.tagskill.model.Slide;
import com.morelap.tagskill.model.User;
import com.morelap.tagskill.utils.LogUtil;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

public class HttpMediator {

    private static final String TAG = HttpMediator.class.getSimpleName();
    private Context mContext;

    public HttpMediator(Context context) {
        mContext = context;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private HttpResponseEvents.SlideResponseEvent mLastSlideResponseEvent;

    @Subscribe
    public void subscribeSlidesRequestEvent(
            HttpRequestEvents.SlidesRequestEvent event) {
        if (!isNetworkAvailable()) {
            // post network unavailable event event
            // post request failed event
        }

        // we will see whether mForceRefresh is true or false

        // add cache here, if we have cache and !mForceRefresh, use cache to
        // return ResponseEvent directly.

        HttpExecutor.get(event.mUrl, event.mRequestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        setLastSlideResponseEvent(response);
                        BusProvider.getInstance().post(
                                produceSlideResponseEvent());
                    }

                    @Override
                    public void onFailure(Throwable throwable, String msg) {
                        // post request failed event
                    }
                });
    }

    @Produce
    public HttpResponseEvents.SlideResponseEvent produceSlideResponseEvent() {
        return getLastSlideResponseEvent();
    }

    private HttpResponseEvents.SlideResponseEvent getLastSlideResponseEvent() {
        return mLastSlideResponseEvent;
    }

    private void setLastSlideResponseEvent(String response) {
        LogUtil.i(TAG, response);
        ObjectMapper mapper = new ObjectMapper();
        Slide slide = null;
        try {
            slide = mapper.readValue(response, Slide.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mLastSlideResponseEvent = new HttpResponseEvents.SlideResponseEvent(
                slide);
    }

    private HttpResponseEvents.ArticleListResponseEvent mLastArticleListResponseEvent;

    @Subscribe
    public void subscribeArticleListRequestEvent(
            HttpRequestEvents.ArticleListRequestEvent event) {
        if (!isNetworkAvailable()) {
            // post network unavailable event event
            // post request failed event
        }

        // we will see whether mForceRefresh is true or false

        // add cache here, if we have cache and !mForceRefresh, use cache to
        // return ResponseEvent directly.

        HttpExecutor.get(event.mUrl, event.mRequestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        setLastArticleListResponseEvent(response);
                        BusProvider.getInstance().post(
                                produceArticleListResponseEvent());
                    }

                    @Override
                    public void onFailure(Throwable throwable, String msg) {
                        // post request failed event
                    }
                });
    }

    @Produce
    public HttpResponseEvents.ArticleListResponseEvent produceArticleListResponseEvent() {
        return getLastArticleListResponseEvent();
    }

    private HttpResponseEvents.ArticleListResponseEvent getLastArticleListResponseEvent() {
        // TODO:need add null check
        return mLastArticleListResponseEvent;
    }

    private void setLastArticleListResponseEvent(String response) {
        LogUtil.i(TAG, response);
        ObjectMapper mapper = new ObjectMapper();
        ArticleList articleList = null;
        try {
            articleList = mapper.readValue(response, ArticleList.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mLastArticleListResponseEvent = new HttpResponseEvents.ArticleListResponseEvent(
                articleList);
    }

    private HttpResponseEvents.UserInfoResponseEvent mLastUserInfoResponseEvent;

    @Subscribe
    public void subscribeUserInfoRequestEvent(
            HttpRequestEvents.UserInfoRequestEvent event) {

        if (TextUtils.isEmpty(event.mUrl)) {
            setLastUserInfoResponseEvent(event.mResponse);
            BusProvider.getInstance().post(produceUserInfoResponseEvent());
            return;
        }
        if (!isNetworkAvailable()) {
            // post network unavailable event event
            // post request failed event
        }

        HttpExecutor.get(event.mUrl, event.mRequestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        setLastUserInfoResponseEvent(response);
                        BusProvider.getInstance().post(
                                produceUserInfoResponseEvent());
                    }

                    @Override
                    public void onFailure(Throwable throwable, String msg) {
                        // post request failed event
                    }
                });
    }

    public void setLastUserInfoResponseEvent(String response) {
        User user = null;
        LogUtil.i(TAG, "" + response);
        if (!TextUtils.isEmpty(response)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                user = mapper.readValue(response, User.class);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mLastUserInfoResponseEvent = new HttpResponseEvents.UserInfoResponseEvent(
                user);
    }

    @Produce
    public HttpResponseEvents.UserInfoResponseEvent produceUserInfoResponseEvent() {
        return mLastUserInfoResponseEvent == null ? new HttpResponseEvents.UserInfoResponseEvent(
                null) : mLastUserInfoResponseEvent;
    }

}
