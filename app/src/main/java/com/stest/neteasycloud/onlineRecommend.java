package com.stest.neteasycloud;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.OtherhelperClass.ConstantVarible;
import com.stest.OtherhelperClass.UtilTool;

public class onlineRecommend extends AppCompatActivity implements View.OnClickListener {

    private WebView daily_recommend_wv;
    private String THRURL="http://m.kugou.com/index/index";
    private Toolbar toolbar;
    private ImageView backtomenu;
    private String lasturl;
    private String returnmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_recommend);
        daily_recommend_wv=(WebView)findViewById(R.id.daily_recommend_wv);
        toolbar=(Toolbar) findViewById(R.id.tool_bar);
        backtomenu=(ImageView) findViewById(R.id.backtomenu);
        backtomenu.setOnClickListener(this);
        toolbar.setBackgroundColor(ConstantVarible.CURRENTTHEMECOLOR);
        returnmessage=daily_recommend_wv.getUrl();

        daily_recommend_wv.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                lasturl=daily_recommend_wv.getOriginalUrl();
                if(!url.startsWith("http"))
                    url="http://"+url;
                view.loadUrl(url);
                return true;
            }


        });
        WebSettings setting=daily_recommend_wv.getSettings();
        setting.setJavaScriptEnabled(true);
        daily_recommend_wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        daily_recommend_wv.loadUrl(THRURL);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backtomenu:
                Intent i=new Intent(this,selected_MainActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(lasturl==null||lasturl.equals(returnmessage))
        super.onBackPressed();
        else
        {
            daily_recommend_wv.loadUrl(lasturl);
        }
    }
}
