package com.stest.neteasycloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.stest.OtherhelperClass.ConstantVarible;

public class recommendFmActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtomenu;
    private Toolbar toolbar;
    private WebView fm_wv;
    private String THRURL="http://m.qingting.fm/categories/0";
    String lasturl;
    String returnMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_fm);
        backtomenu=(ImageView)findViewById(R.id.backtomenu);
        toolbar=(Toolbar)findViewById(R.id.tool_bar);
        fm_wv =(WebView)findViewById(R.id.daily_recommend_wv);
        backtomenu=(ImageView) findViewById(R.id.backtomenu);
        backtomenu.setOnClickListener(this);
        returnMessage=fm_wv.getUrl();
        toolbar.setBackgroundColor(ConstantVarible.CURRENTTHEMECOLOR);
        fm_wv.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                lasturl=view.getOriginalUrl();
                if(!url.startsWith("http"))
                    url="http://"+url;
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings setting= fm_wv.getSettings();
        setting.setJavaScriptEnabled(true);
        fm_wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        fm_wv.loadUrl(THRURL);

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
        if(lasturl==null||lasturl.equals(returnMessage))
            super.onBackPressed();
        else
        {
            fm_wv.loadUrl(lasturl);
        }

    }
}
