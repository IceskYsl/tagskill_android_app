package com.morelap.tagskill.ui;

import com.actionbarsherlock.app.SherlockActivity;
import com.loopj.android.http.RequestParams;
import com.morelap.tagskill.R;
import com.morelap.tagskill.event.HttpRequestEvents;
import com.morelap.tagskill.event.HttpResponseEvents;
import com.morelap.tagskill.eventbus.BusProvider;
import com.squareup.otto.Subscribe;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends SherlockActivity {

    private TextView mLoginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mLoginStatus = (TextView) findViewById(R.id.login_info);
        try {
            String versionName = getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionName;
            TextView versionInfo = (TextView) findViewById(R.id.version_info);
            versionInfo.setText(versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void subscribeUserInfoResponseEvent(
            HttpResponseEvents.UserInfoResponseEvent event) {
        if (event.mUser != null) {
            mLoginStatus.setText("" + event.mUser.username);
            View loginPanel = findViewById(R.id.login_panel);
            if (loginPanel != null) {
                loginPanel.setVisibility(View.GONE);
            }
        } else {
            ViewStub stub = (ViewStub) findViewById(R.id.stub);
            stub.inflate();
            Button submit = (Button) findViewById(R.id.submit_login);
            final EditText edit = (EditText) findViewById(R.id.edit_login);
            submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(edit.getText())) {
                        String token = edit.getText().toString().trim();
                        RequestParams requestParams = new RequestParams("token", token);
                        BusProvider
                                .getInstance()
                                .post(new HttpRequestEvents.UserInfoRequestEvent(
                                        "http://www.tagskill.com/api/v1/articles/profile",
                                        requestParams, null));
                    }
                }
            });
        }
    }
}
