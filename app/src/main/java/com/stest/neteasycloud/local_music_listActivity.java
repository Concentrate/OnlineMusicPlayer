package com.stest.neteasycloud;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import com.stest.Service.PlayService;
import com.stest.adapter.LocalMusicListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class local_music_listActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CHANGE_SONG_NUMBER =10 ;
    private View popView;
    //弹出视图ListView
    private ListView popListView;
    private PopupWindow popupWindow;
    private List<Map<String, Object>> popInfos = new ArrayList<>();
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
    private List<Mp3Info>songlist=new ArrayList<>();
    private Handler local_music_list_handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==CHANGE_SONG_NUMBER)
            {
                songNumber.setText("("+songlist.size()+")");

            }

        }

        @Override
        public String getMessageName(Message message) {
            return super.getMessageName(message);
        }
    };
    private boolean isChanged=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music_list);
        ViewUtils.inject(this);
        getMp3Info();
        setAdapter();
        initEvent();


    }

    public List<Mp3Info> getSonglist() {
        return songlist;
    }

    private void initEvent() {
        backtoMenu.setOnClickListener(this);
        search.setOnClickListener(this);
        play_all_items.setOnClickListener(this);
        play_btn.setOnClickListener(this);
        music_ll.setOnClickListener(this);
        play_more.setOnClickListener(this);
        next_song.setOnClickListener(this);
        popView = getLayoutInflater().inflate(R.layout.main_pop, null);
        popListView = (ListView) popView.findViewById(R.id.main_pop_listview);

    }

    public void getMp3Info()
    {
        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for(int i=0;i<cursor.getCount();i++)
        {
            Mp3Info m=new Mp3Info();
            cursor.moveToNext();
            long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artise=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long durition=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int isMusic=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if(isMusic!=0)
            {
                m.setArtist(artise);
                m.setDuration(durition);
                m.setId(id);
                m.setSize(size);
                m.setTitle(title);
                m.setUrl(url);
            }
            songlist.add(m);
        }
        Message msg=new Message();
        msg.arg1=CHANGE_SONG_NUMBER;
        local_music_list_handler.sendMessage(msg);


    }
    private static final int PLAY_NEW_SONG=1;
    private static final int PLAY_STOP = 3;
    private static final int PLAY_PAUSE = 2;
    public void setAdapter()
    {
        loclmusic_listview.setAdapter(new LocalMusicListAdapter(this,R.layout.localmusic_item,songlist));
        loclmusic_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(songlist!=null)
                {
                    Mp3Info info=songlist.get(i);
                    Intent intent=new Intent();
                    intent.putExtra("url",info.getUrl());
                    intent.putExtra("message",PLAY_NEW_SONG);
                    intent.setClass(local_music_listActivity.this, PlayService.class);
                    startService(intent);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.back:
                Intent intent=new Intent(this,selected_MainActivity.class);
                startActivity(intent);
                break;
            case R.id.bar_search:
                break;
            case R.id.play_btn:
                if (isChanged) {
                    play_all_items.setBackground(getResources().getDrawable(R.drawable.pause_btn));
                    play_btn.setBackground(null);
                    play_btn.setImageResource(R.drawable.pause_btn);
                } else {
                    play_all_items.setBackground(getResources().getDrawable(R.drawable.play_btn));
                    play_btn.setBackground(null);
                    play_btn.setImageResource(R.drawable.play_btn);
                }
                isChanged = !isChanged;
                break;
            case R.id.music_to_showVIew_ll:
                Intent t1=new Intent(this,PlayTheMusic.class);
                startActivity(t1);
                break;
            case R.id.bottom_music_more:
                break;
            case R.id.next_song:
                playNextSong();
                break;
            case R.id.play_all_items:
                if (isChanged) {
                    play_all_items.setBackground(getResources().getDrawable(R.drawable.pause_btn));
                    play_btn.setBackground(null);
                    play_btn.setImageResource(R.drawable.pause_btn);
                } else {
                    play_all_items.setBackground(getResources().getDrawable(R.drawable.play_btn));
                    play_btn.setBackground(null);
                    play_btn.setImageResource(R.drawable.play_btn);
                }
                isChanged=!isChanged;
                startPlayMusic(0);
                break;






        }
    }

    private void playNextSong() {

    }

    private void startPlayMusic(int i) {

        Mp3Info info=songlist.get(i);
        Intent intent=new Intent();
        intent.putExtra("url",info.getUrl());
        intent.putExtra("message",PLAY_NEW_SONG);
        intent.setClass(local_music_listActivity.this, PlayService.class);
        startService(intent);
    }
}
