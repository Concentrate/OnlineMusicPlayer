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

public class NewSongRanking extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtomenu;
    private Toolbar toolbar;
    private WebView newsongrank_wv;
    private String THRURL="http://m.y.qq.com/#toplist";
    String lasturl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_song_ranking);
        backtomenu=(ImageView)findViewById(R.id.backtomenu);
        toolbar=(Toolbar)findViewById(R.id.tool_bar);
        newsongrank_wv =(WebView)findViewById(R.id.daily_recommend_wv);
        backtomenu=(ImageView) findViewById(R.id.backtomenu);
        backtomenu.setOnClickListener(this);
        toolbar.setBackgroundColor(ConstantVarible.CURRENTTHEMECOLOR);

        newsongrank_wv.setWebViewClient(new WebViewClient()
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
        WebSettings setting= newsongrank_wv.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setBlockNetworkImage(true);
        newsongrank_wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        newsongrank_wv.loadUrl(THRURL);

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
        if(lasturl==null)
        super.onBackPressed();
        else
        {
            newsongrank_wv.loadUrl(lasturl);
        }

    }
}
