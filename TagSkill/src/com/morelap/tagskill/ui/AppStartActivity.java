package com.morelap.tagskill.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.morelap.tagskill.R;
import com.morelap.tagskill.event.HttpRequestEvents;
import com.morelap.tagskill.eventbus.BusProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AppStartActivity extends Activity {

    public static final String ACCOUNT_INFO = "account_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        new Thread() {
            public void run() {
                String response = null;
                try {
                    FileInputStream fis = openFileInput(ACCOUNT_INFO);
                    byte[] buffer = new byte[fis.available()];
                    if (fis.read(buffer) == fis.available()) {
                        response = new String(buffer);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final String account_response = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BusProvider.getInstance().post(
                                new HttpRequestEvents.UserInfoRequestEvent(
                                        null, null, account_response));
                    }
                });
            };
        }.start();
        BusProvider.getInstance().post(
                new HttpRequestEvents.SlidesRequestEvent(
                        "http://www.tagskill.com/api/v1/articles/slides"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(AppStartActivity.this,
                        ArticleListActivity.class));
                finish();
            }
        }, 3000);
    }

}
