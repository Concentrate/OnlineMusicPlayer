package com.stest.neteasycloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.List;

/**
 * Created by ldy on 6/13/16.
 */
public class PlayTheMusic extends FragmentActivity implements View.OnClickListener {

    public static final String CTL_ACTION = "www.action.CTRONL_ACTION";
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
    private int duration;       //歌曲长度
    private int flag;           //播放标识
    private int repeatState;
    private final int isCurrentRepeat = 1; // 单曲循环
    private final int isAllRepeat = 2;      // 全部循环
    private boolean isPause = true;                // 暂停
    private boolean isNoneShuffle;           // 顺序播放
    private boolean isShuffle;          // 随机播放
    private BroadcastReceiver PlayMusicReceiver;

    private List<Mp3Info> mp3Infos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);
        ViewUtils.inject(this);
        initEvent();
        initData();
        initReceiver();
    }

    private void initData() {
        mp3Infos= UtilTool.getMp3Infos(this);
    }

    private void initReceiver() {
        PlayMusicReceiver = new UdpateActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantVarible.UPDATE_ACTION);
        filter.addAction(ConstantVarible.MUSIC_CURRENT);
        filter.addAction(ConstantVarible.MUSIC_DURATION);
        registerReceiver(PlayMusicReceiver, filter);


    }

    private void initEvent() {
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

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

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
                break;
            case R.id.collect_iv:
                ToShowSomeMessge("已经加入我喜欢的音乐");
                break;
            case R.id.download_iv:
                ToShowSomeMessge("正在下载");
                break;
            case R.id.comment_iv:
                ToShowSomeMessge("暂时没有评论");
                break;

        }


    }

    private void Play_NextSong() {
        Intent i = new Intent(this, CompletePlayService.class);
        i.putExtra("msg", ConstantVarible.PLAY_NEXT);
        startService(i);
    }

    private void Play_lastone() {

        Intent intent = new Intent(this, CompletePlayService.class);
        intent.putExtra("msg", ConstantVarible.PLAY_PREVIOUS);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(PlayMusicReceiver);
    }

    private void Play_music() {
        if (!isPause) {
            isPause = true;
            play_music.setImageResource(R.drawable.pause);
            Intent i = new Intent(this, CompletePlayService.class);
            i.putExtra("msg", ConstantVarible.PAUSE_MSG);
            startService(i);
        } else {
            play_music.setImageResource(R.drawable.play);
            isPause = false;
            if (!CompletePlayService.isFirstPlay()) {
                Intent i = new Intent(this, CompletePlayService.class);
                i.putExtra("msg", ConstantVarible.PLAY_CONTINUE);
                startService(i);
            } else {
                currentPlayItem = selected_MainActivity.getCurrentPlayItem();
                Intent m = new Intent(this, CompletePlayService.class);
                m.putExtra("msg", ConstantVarible.PLAY_MSG);
                m.putExtra("url", mp3Infos.get(currentPlayItem).getUrl());
                m.putExtra("current", currentPlayItem);
                startService(m);

            }
        }
    }

    private void ToShowSomeMessge(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();

    }

    private int currentPlayItem;

    public class UdpateActionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ConstantVarible.UPDATE_ACTION: {
                    currentPlayItem = intent.getIntExtra("currentPlayItemPlayItem", 0);
                    Mp3Info info = mp3Infos.get(currentPlayItem);
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

            }

        }
    }


}
