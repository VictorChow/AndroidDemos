package pers.victor.androiddemos.activity;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pers.victor.androiddemos.R;

public class KotlinLibActivity extends ToolbarActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_smart_go;
    }

    @Override
    public void initView() {
        final WebView webView = $(R.id.web_sg);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        webView.loadUrl("https://github.com/VictorChow/KotlinAndroidLib");
    }
}
