package com.morelap.tagskill.ui;

import com.actionbarsherlock.app.SherlockActivity;
import com.morelap.tagskill.R;
import com.morelap.tagskill.R.id;
import com.morelap.tagskill.R.layout;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ArticleDetailActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebView webview = (WebView) findViewById(R.id.article_detail_webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDefaultFontSize(15);
        webview.loadUrl(url);
        getSupportActionBar().setTitle("Article Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
