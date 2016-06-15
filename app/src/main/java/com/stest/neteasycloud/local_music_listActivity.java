package com.stest.neteasycloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class local_music_listActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CHANGE_SONG_NUMBER = 10;
    private View popView;
    //弹出视图ListView
    private ListView popListView;
    private PopupWindow popupWindow;
    private List<HashMap<String, String>> popInfos;
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
    private boolean isPause = true;
    private List<Mp3Info> songlist = new ArrayList<>();
    private BroadcastReceiver Local_MusiclistReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music_list);
        ViewUtils.inject(this);
        initEvent();
        initReceiver();
        initViews();
        setAdapter();


    }

    private void initViews() {
        songNumber=(TextView)findViewById(R.id.songnumber_tv);
        loclmusic_listview=(ListView)findViewById(R.id.local_music_listview) ;
        songNumber.setText("("+songlist.size()+")");
    }

    private void initReceiver() {
        Local_MusiclistReceiver = new LocalListReceiver();
        IntentFilter fi = new IntentFilter();
        fi.addAction(ConstantVarible.CTL_ACTION);
        fi.addAction(ConstantVarible.UPDATE_ACTION);
        registerReceiver(Local_MusiclistReceiver, fi);


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
        popView = getLayoutInflater().inflate(R.layout.main_pop, null);
        popListView = (ListView) popView.findViewById(R.id.main_pop_listview);
        songlist = UtilTool.getMp3Infos(this);
        popInfos = UtilTool.getMusicMaps(songlist);


    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(Local_MusiclistReceiver);
    }

    public void setAdapter() {
        loclmusic_listview.setAdapter(new LocalMusicListAdapter(this, R.layout.localmusic_item, songlist));
        loclmusic_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (songlist != null) {
                    Mp3Info info = songlist.get(i);
                    Intent intent = new Intent();
                    currentplayItem = i;
                    intent.putExtra("url", info.getUrl());
                    intent.putExtra("message", ConstantVarible.PLAY_MSG);
                    intent.putExtra("current", i);//current is the local play music in the list position
                    intent.setClass(local_music_listActivity.this, CompletePlayService.class);
                    startService(intent);
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
                    Intent i = new Intent(this, CompletePlayService.class);
                    i.putExtra("msg", ConstantVarible.PAUSE_MSG);
                    startService(i);


                } else {
                    isPause = false;
                    play_all_items.setBackground(getResources().getDrawable(R.drawable.play_btn));
                    play_btn.setBackground(null);
                    play_btn.setImageResource(R.drawable.play_btn);
                    if (!CompletePlayService.isFirstPlay()) {
                        Intent i = new Intent(this, CompletePlayService.class);
                        i.putExtra("msg", ConstantVarible.PLAY_CONTINUE);
                        startService(i);
                    } else {
                        currentplayItem=selected_MainActivity.getCurrentPlayItem();
                        Intent i2 = new Intent(this, CompletePlayService.class);
                        i2.putExtra("msg", ConstantVarible.PLAY_MSG);
                        i2.putExtra("url", songlist.get(currentplayItem).getUrl());
                        i2.putExtra("current", currentplayItem);
                        startService(i2);
                    }
                }
                break;
            case R.id.music_to_showVIew_ll:
                Intent t1 = new Intent(this, PlayTheMusic.class);
                startActivity(t1);
                break;
            case R.id.bottom_music_more:
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
        Intent t2 = new Intent(this, CompletePlayService.class);
        t2.putExtra("msg", ConstantVarible.PLAY_NEXT);
        startService(t2);

    }

    private void startPlayMusic(int i) {

        Mp3Info info = songlist.get(i);
        Intent intent = new Intent();
        intent.putExtra("url", info.getUrl());
        intent.putExtra("message", ConstantVarible.PLAY_MSG);
        intent.putExtra("current", i);
        intent.setClass(local_music_listActivity.this, CompletePlayService.class);
        startService(intent);
    }

    private int currentplayItem=0;

    public class LocalListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String m = intent.getAction();
            switch (m) {
                case ConstantVarible.CTL_ACTION:
                    isPause= intent.getBooleanExtra("isPause", true);
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
