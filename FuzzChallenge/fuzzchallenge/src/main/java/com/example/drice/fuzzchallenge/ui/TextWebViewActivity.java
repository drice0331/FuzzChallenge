package com.example.drice.fuzzchallenge.ui;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.drice.fuzzchallenge.R;

/**
 * Created by DrIce on 4/29/15.
 */
public class TextWebViewActivity extends Activity {

    private WebView webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textwebview);

        webview = (WebView)findViewById(R.id.webview);

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new CustomWebViewClient());
        webview.loadUrl("https://fuzzproductions.com/");
    }

    private class CustomWebViewClient  extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
