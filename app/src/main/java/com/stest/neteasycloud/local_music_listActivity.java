package com.stest.neteasycloud;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.DataClass.Mp3Info;
import com.stest.OtherhelperClass.ConstantVarible;
import com.stest.OtherhelperClass.UtilTool;
import com.stest.Service.CompletePlayService;
import com.stest.adapter.LocalMusicListAdapter;
import com.stest.adapter.MainPopAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class local_music_listActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CHANGE_SONG_NUMBER = 10;
    private View popView;
    //弹出视图ListView
    private ListView popListView;
    private PopupWindow popupWindow;
    private List<Map<String, Object>> popInfos;
    @ViewInject(R.id.back)
    ImageView backtoMenu;
    @ViewInject(R.id.bar_search)
    ImageView search;
    @ViewInject(R.id.play_all_items)
    ImageView play_all_items;
    @ViewInject(R.id.local_music_listview)
    ListView loclmusic_listview;
    @ViewInject(R.id.songnumber_tv)
    TextView songNumber;
    @ViewInject(R.id.play_btn)
    ImageView play_btn;
    @ViewInject(R.id.bottom_music_more)
    ImageView play_more;
    @ViewInject(R.id.next_song)
    ImageView next_song;
    @ViewInject(R.id.music_to_showVIew_ll)
    LinearLayout music_ll;
    @ViewInject(R.id.song_picture)
    ImageView song_picture_iv;
    @ViewInject(R.id.music_name)
    TextView song_name_tv;
    @ViewInject(R.id.music_artist)
    TextView artist_tv;
    @ViewInject(R.id.tool_bar)
    Toolbar toolbar;
    private boolean isPause = true;
    private List<Mp3Info> songlist;
    private BroadcastReceiver Local_MusiclistReceiver;
    private Context mycontext;
    private Handler serviceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music_list);
        mycontext = this;
        ViewUtils.inject(this);
        initEvent();
        initViews();
        setAdapter();


    }


    private void initViews() {
        songNumber = (TextView) findViewById(R.id.songnumber_tv);
        loclmusic_listview = (ListView) findViewById(R.id.local_music_listview);
        songNumber.setText("(" + songlist.size() + ")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReceiver();


    }

    private CompletePlayService myservice;
    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            CompletePlayService.MyIBinder binder=((CompletePlayService.MyIBinder)iBinder);
            myservice = ((CompletePlayService.MyIBinder) iBinder).getCompletePlayService();
            if(binder!=null)
                binder.UpdateState();
            if (myservice != null)
                myservice.UpdateState();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    private Intent serviceIntent;

    private void initReceiver() {
        Local_MusiclistReceiver = new LocalListReceiver();
        IntentFilter fi = new IntentFilter();
        fi.addAction(ConstantVarible.CTL_ACTION);
        fi.addAction(ConstantVarible.UPDATE_ACTION);
        registerReceiver(Local_MusiclistReceiver, fi);
        serviceIntent= new Intent(local_music_listActivity.this, CompletePlayService.class);//In order to At first refresh the ui
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);



    }

    private void initEvent() {
        play_btn.setImageResource(R.drawable.pause_btn);
        backtoMenu.setOnClickListener(this);
        search.setOnClickListener(this);
        play_all_items.setOnClickListener(this);
        play_btn.setOnClickListener(this);
        music_ll.setOnClickListener(this);
        play_more.setOnClickListener(this);
        next_song.setOnClickListener(this);
        songlist = UtilTool.getMp3Infos(this);
        toolbar.setBackgroundColor(ConstantVarible.CURRENTTHEMECOLOR);
        popInfos = new ArrayList<>();
        for (int i = 0; i < songlist.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            Mp3Info m = songlist.get(i);
            item.put("imageView", R.mipmap.list_icn_delete);
            item.put("txt_author", m.getArtist());
            item.put("txt_name", m.getTitle());
            popInfos.add(item);
        }
        popView = getLayoutInflater().inflate(R.layout.main_pop, null);
        TextView playallItem=(TextView)popView.findViewById(R.id.play_all_items) ;
        playallItem.setText("播放全部("+songlist.size()+")");
        popListView = (ListView) popView.findViewById(R.id.main_pop_listview);
        popListView.setAdapter(new MainPopAdapter(this, popInfos));
        popupWindow = new PopupWindow(popView, ViewPager.LayoutParams.MATCH_PARENT,
                ViewPager.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        popupWindow.setOutsideTouchable(true);
        //刷新状态
        popupWindow.update();
        popupWindow.setTouchable(true);
        //这样点击返回键也能消失
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mp3Info m1 = songlist.get(i);
                currentplayItem = i;
                Intent t1 = new Intent(mycontext, CompletePlayService.class);
                t1.putExtra("message", ConstantVarible.PLAY_MSG);
                t1.putExtra("url", m1.getUrl());
                t1.putExtra("current", currentplayItem);
                startService(t1);
            }
        });


    }


    @Override
    protected void onDestroy() {
        if (Local_MusiclistReceiver != null) {
            if (serviceIntent != null) {
                stopService(serviceIntent);
            }
            unregisterReceiver(Local_MusiclistReceiver);
            unbindService(conn);

        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    public void setAdapter() {
        loclmusic_listview.setAdapter(new LocalMusicListAdapter(this, R.layout.localmusic_item, songlist));
        loclmusic_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (songlist != null) {
                    Mp3Info info = songlist.get(i);
                    serviceIntent= new Intent();
                    currentplayItem = i;
                    serviceIntent.putExtra("url", info.getUrl());
                    serviceIntent.putExtra("message", ConstantVarible.PLAY_MSG);
                    serviceIntent.putExtra("current", i);//current is the local play music in the list position
                    serviceIntent.setClass(local_music_listActivity.this, CompletePlayService.class);
                    startService(serviceIntent
                    );
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent(this, selected_MainActivity.class);
                startActivity(intent);
                break;
            case R.id.bar_search:
                break;
            case R.id.play_btn:
                if (!isPause) {
                    isPause = true;
                    play_all_items.setBackground(getResources().getDrawable(R.drawable.pause_btn));
                    play_btn.setBackground(null);
                    play_btn.setImageResource(R.drawable.pause_btn);
                    serviceIntent = new Intent(this, CompletePlayService.class);
                    serviceIntent.putExtra("msg", ConstantVarible.PAUSE_MSG);
                    startService(serviceIntent);


                } else {
                    isPause = false;
                    play_all_items.setBackground(getResources().getDrawable(R.drawable.play_btn));
                    play_btn.setBackground(null);
                    play_btn.setImageResource(R.drawable.play_btn);
                    if (!CompletePlayService.isFirstPlay()) {
                        serviceIntent= new Intent(this, CompletePlayService.class);
                        serviceIntent.putExtra("msg", ConstantVarible.PLAY_CONTINUE);
                        startService(serviceIntent);
                    } else {
                        currentplayItem = selected_MainActivity.getCurrentPlayItem();
                        serviceIntent= new Intent(this, CompletePlayService.class);
                        serviceIntent.putExtra("msg", ConstantVarible.PLAY_MSG);
                        serviceIntent.putExtra("url", songlist.get(currentplayItem).getUrl());
                        serviceIntent.putExtra("current", currentplayItem);
                        startService(serviceIntent
                        );
                    }
                }
                break;
            case R.id.music_to_showVIew_ll:
                Intent t1 = new Intent(this, PlayTheMusic.class);
                startActivity(t1);
                break;
            case R.id.bottom_music_more:
                if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(backtoMenu, 0, 450);
                }
                break;
            case R.id.next_song:
                playNextSong();
                break;
            case R.id.play_all_items:
                if (isPause) {
                    isPause = false;
                    play_all_items.setBackground(getResources().getDrawable(R.drawable.play_btn));
                    play_btn.setBackground(null);
                    play_btn.setImageResource(R.drawable.play_btn);
                    startPlayMusic(0);
                }
                break;


        }
    }

    private void playNextSong() {
        serviceIntent= new Intent(this, CompletePlayService.class);
        serviceIntent.putExtra("msg", ConstantVarible.PLAY_NEXT);
        startService(serviceIntent);

    }

    private void startPlayMusic(int i) {

        Mp3Info info = songlist.get(i);
        serviceIntent = new Intent();
        serviceIntent.putExtra("url", info.getUrl());
        serviceIntent.putExtra("msg", ConstantVarible.PLAY_MSG);
        serviceIntent.putExtra("current", i);
        serviceIntent.setClass(local_music_listActivity.this, CompletePlayService.class);
        startService(serviceIntent);
    }

    private int currentplayItem = 0;

    public class LocalListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String m = intent.getAction();
            switch (m) {
                case ConstantVarible.CTL_ACTION:
                    isPause = intent.getBooleanExtra("isPause", true);
                    if (isPause) {
                        play_all_items.setBackground(getResources().getDrawable(R.drawable.pause_btn));
                        play_btn.setBackground(null);
                        play_btn.setImageResource(R.drawable.pause_btn);
                    } else {
                        play_all_items.setBackground(getResources().getDrawable(R.drawable.play_btn));
                        play_btn.setBackground(null);
                        play_btn.setImageResource(R.drawable.play_btn);
                    }
                    break;
                case ConstantVarible.UPDATE_ACTION:
                    currentplayItem = intent.getIntExtra("current", 0);
                    artist_tv.setText(songlist.get(currentplayItem).getArtist());
                    song_name_tv.setText(songlist.get(currentplayItem).getTitle());
                    break;
            }

        }
    }
}
