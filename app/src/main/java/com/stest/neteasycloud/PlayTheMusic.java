package com.stest.neteasycloud;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.DataClass.Mp3Info;
import com.stest.OtherhelperClass.ConstantVarible;
import com.stest.OtherhelperClass.UtilTool;
import com.stest.Service.CompletePlayService;
import com.stest.adapter.MainPopAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by ldy on 6/13/16.
 */
public class PlayTheMusic extends FragmentActivity implements View.OnClickListener {

    
    public static final String CTL_ACTION = "www.action.CTRONL_ACTION";
    private View popView;
    //弹出视图ListView
    private ListView popListView;
    private PopupWindow popupWindow;
    private List<Map<String, Object>> popInfos;
    @ViewInject(R.id.backtomenu)
    Button backtomenu;
    @ViewInject(R.id.musicTitle)
    TextView musicTitle;
    @ViewInject(R.id.musicArtist)
    TextView musicArtist;
    @ViewInject(R.id.lrcScrollView)
    ScrollView lrcScrollview;
    @ViewInject(R.id.shuff_iv)
    ImageView shuff;
    @ViewInject(R.id.last_one)
    ImageView last_song;
    @ViewInject(R.id.next_song)
    ImageView next_song;
    @ViewInject(R.id.more)
    ImageView song_more;
    @ViewInject(R.id.collect_iv)
    ImageView collect_to_like;
    @ViewInject(R.id.download_iv)
    ImageView download_song;
    @ViewInject(R.id.comment_iv)
    ImageView comment;
    @ViewInject(R.id.lyric_tv)
    TextView lyric;
    @ViewInject(R.id.audioTrack)
    SeekBar audioTrack;
    @ViewInject(R.id.play_music)
    ImageView play_music;
    @ViewInject(R.id.current_progress)
    TextView current_progress;
    @ViewInject(R.id.final_progress)
    TextView final_progress;
    private String title;       //歌曲标题
    private String artist;      //歌曲艺术家
    private String url;         //歌曲路径
    private int currentTime;    //当前歌曲播放时间
    private int duration=0;       //歌曲长度
    private int flag;           //播放标识
    private int repeatState;
    private final int isCurrentRepeat = 1; // 单曲循环
    private final int isAllRepeat = 2;      // 全部循环
    private boolean isPause = true;                // 暂停
    private boolean isNoneShuffle;           // 顺序播放
    private boolean isShuffle;          // 随机播放
    private BroadcastReceiver PlayMusicReceiver;

    private List<Mp3Info> songlist;
    private Context mycontext;
    private CompletePlayService myservice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);
        RelativeLayout musicLayout=(RelativeLayout)findViewById(R.id.MusicLayout);
        ViewUtils.inject(this);
        initEvent();
        mycontext=this;
    }


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

    private void initReceiver() {
        PlayMusicReceiver = new UdpateActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantVarible.UPDATE_ACTION);
        filter.addAction(ConstantVarible.MUSIC_CURRENT);
        filter.addAction(ConstantVarible.MUSIC_DURATION);
        filter.addAction(ConstantVarible.CTL_ACTION);
        registerReceiver(PlayMusicReceiver,filter);
        serviceIntent =new Intent(PlayTheMusic.this,CompletePlayService.class);
        bindService(serviceIntent,conn ,Context.BIND_AUTO_CREATE);
    }

    private void initEvent() {
        songlist= UtilTool.getMp3Infos(this);
        play_music.setImageResource(R.drawable.pause);
        backtomenu.setOnClickListener(this);
        collect_to_like.setOnClickListener(this);
        download_song.setOnClickListener(this);
        comment.setOnClickListener(this);
        shuff.setOnClickListener(this);
        last_song.setOnClickListener(this);
        play_music.setOnClickListener(this);
        next_song.setOnClickListener(this);
        song_more.setOnClickListener(this);
        audioTrack.setProgress(0);
        audioTrack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
                currentPlayItem= i;
                serviceIntent = new Intent(mycontext, CompletePlayService.class);
                serviceIntent.putExtra("message", ConstantVarible.PLAY_MSG);
                serviceIntent.putExtra("url", m1.getUrl());
                serviceIntent.putExtra("current", currentPlayItem);
                startService(serviceIntent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private boolean isLike=false;
    private boolean isDownload=false;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backtomenu:
                Intent t1 = new Intent(this, local_music_listActivity.class);
                startActivity(t1);
                break;
            case R.id.shuff_iv:

                break;
            case R.id.play_music:
                Play_music();
                break;
            case R.id.last_one:
                Play_lastone();
                break;
            case R.id.next_song:
                Play_NextSong();

                break;
            case R.id.more:
                if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(backtomenu, 0, 450);
                }
                break;
            case R.id.collect_iv:
                if(!isLike) {
                    isLike=true;
                    collect_to_like.setImageResource(R.drawable.start);
                    ToShowSomeMessge("已经加入我喜欢的音乐");
                }else
                {
                    isLike=false;
                    collect_to_like.setImageResource(R.drawable.like_black);
                    ToShowSomeMessge("已经从我喜欢的音乐移除");
                }
                break;
            case R.id.download_iv:
                if(!isDownload) {
                    isDownload=true;
                    download_song.setImageResource(R.drawable.download_color);
                    ToShowSomeMessge("正在下载");
                }else
                {
                    isDownload=false;
                    download_song.setImageResource(R.drawable.downloadsong);
                    ToShowSomeMessge("缺消下载");

                }
                break;
            case R.id.comment_iv:
                ToShowSomeMessge("暂时没有评论");
                break;

        }


    }

    private void Play_NextSong() {
        serviceIntent = new Intent(this, CompletePlayService.class);
        serviceIntent.putExtra("msg", ConstantVarible.PLAY_NEXT);
        startService(serviceIntent);
    }

    private void Play_lastone() {

        serviceIntent = new Intent(this, CompletePlayService.class);
        serviceIntent.putExtra("msg", ConstantVarible.PLAY_PREVIOUS);
        startService(serviceIntent);
    }
    Intent serviceIntent;

    @Override
    protected void onDestroy() {
        if(PlayMusicReceiver!=null) {
            if (serviceIntent != null) {
                stopService(serviceIntent);
            }
            unregisterReceiver(PlayMusicReceiver);
            unbindService(conn);

        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    private void Play_music() {
        if (!isPause) {
            isPause = true;
            play_music.setImageResource(R.drawable.pause);
            serviceIntent = new Intent(this, CompletePlayService.class);
            serviceIntent.putExtra("msg", ConstantVarible.PAUSE_MSG);
            startService(serviceIntent);
        } else {
            play_music.setImageResource(R.drawable.play);
            isPause = false;
            if (!CompletePlayService.isFirstPlay()) {
                serviceIntent = new Intent(this, CompletePlayService.class);
                serviceIntent.putExtra("msg", ConstantVarible.PLAY_CONTINUE);
                startService(serviceIntent);
            } else {
                currentPlayItem = selected_MainActivity.getCurrentPlayItem();
                serviceIntent = new Intent(this, CompletePlayService.class);
                serviceIntent.putExtra("msg", ConstantVarible.PLAY_MSG);
                serviceIntent.putExtra("url", songlist.get(currentPlayItem).getUrl());
                serviceIntent.putExtra("current", currentPlayItem);
                startService(serviceIntent
                );

            }
        }
    }

    private void ToShowSomeMessge(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();

    }
    protected void onResume() {
        super.onResume();
        initReceiver();


    }

    private int currentPlayItem;

    public class UdpateActionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ConstantVarible.UPDATE_ACTION: {
                    currentPlayItem = intent.getIntExtra("current", 0);
                    Mp3Info info = songlist.get(currentPlayItem);
                    musicTitle.setText(info.getTitle());
                    musicArtist.setText(info.getArtist());
                    lyric.setText("暂时没有 " + info.getTitle() + "  的歌词");
                    break;
                }
                case ConstantVarible.CTL_ACTION: {
                    isPause = intent.getBooleanExtra("isPause", false);
                    if (isPause) {
                        play_music.setImageResource(R.drawable.pause);
                    } else {
                        play_music.setImageResource(R.drawable.play);
                    }

                }
                break;
                case ConstantVarible.MUSIC_DURATION:
                    duration=intent.getIntExtra("duration",0);
                    audioTrack.setMax(duration);
                    final_progress.setText(UtilTool.formatTime(duration));

                    break;
                case ConstantVarible.MUSIC_CURRENT:
                    currentTime=intent.getIntExtra("currentTime",0);
                    current_progress.setText(UtilTool.formatTime(currentTime));
                    if(duration!=0)
                    audioTrack.setProgress(currentTime);
                    break;

            }

        }
    }


}
