package com.stest.neteasycloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
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
import com.stest.OtherhelperClass.GetLocalSong;
import com.stest.OtherhelperClass.NoSclideViewpager;
import com.stest.Service.CompletePlayService;
import com.stest.adapter.MainPopAdapter;
import com.stest.adapter.main_selectedAdapter;
import com.stest.fragraments.friends_fragment;
import com.stest.fragraments.local_fragment;
import com.stest.fragraments.online_fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class selected_MainActivity extends FragmentActivity implements View.OnClickListener, friends_fragment.OnFragmentInteractionListener, online_fragment.OnFragmentInteractionListener, local_fragment.OnFragmentInteractionListener {

    private static final int PLAY_STOP = 3;
    private static final int PLAY_PAUSE = 2;
    private static final int PLAY_NEW_SONG = 1;
    @ViewInject(R.id.bar_disco)
    private ImageView bar_disco;
    @ViewInject(R.id.bar_music)
    private ImageView bar_music;
    @ViewInject(R.id.bar_friends)
    private ImageView bar_friends;
    @ViewInject(R.id.bar_search)
    private ImageView bar_search;
    @ViewInject(R.id.navigation_view)
    private NavigationView mNavigationView;
    @ViewInject(R.id.tool_bar)//the bar upper the top
    private Toolbar toolbar;
    @ViewInject(R.id.drawerIcon)
    private ImageView drawerIcon;//drawer
    @ViewInject(R.id.drawer)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.view_pager)
    private NoSclideViewpager view_pager;
    @ViewInject(R.id.next_song)
    private ImageView next_song;
    private ImageView play_btn;
    private ImageView bottom_music_more;
    private View popView;
    //弹出视图ListView
    private ListView popListView;
    private LinearLayout bottom_music_ll;
    private PopupWindow popupWindow;
    private Fragment onlinefragment;
    private Fragment localfragment;
    private Fragment friendsfragment;
    private List<Fragment> listfragments = new ArrayList<>();
    private boolean isopen = false;
    private boolean isChanged = true;
    private List<Map<String, Object>> popInfos = new ArrayList<>();
    private FragmentManager myfragmentmanager;
    private GetLocalSong helperClass;
    private List<Mp3Info> localmusiclist;
    @ViewInject(R.id.music_name)
    private TextView song_name_tv;
    @ViewInject(R.id.music_artist)
    private TextView artist_tv;
    @ViewInject(R.id.song_picture)
    private ImageView song_picture_iv;
    private Context mycontext;
    private BroadcastReceiver selected_mainReceiver;
    private boolean isPause = true;
    private android.os.Handler selecte_MainHandler = new android.os.Handler() {
        @Override
        public String getMessageName(Message message) {
            return super.getMessageName(message);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

        @Override
        public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
            return super.sendMessageAtTime(msg, uptimeMillis);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_main_activity);
        ViewUtils.inject(this);
        initWidgets();
        initEvent();
        LocalInitData();
        LocalInitView();
        initReceiver();
        mycontext = this;


    }

    private void initReceiver() {
        selected_mainReceiver = new SelectedMainReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantVarible.UPDATE_ACTION);
        filter.addAction(ConstantVarible.CTL_ACTION);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void LocalInitView() {
        Mp3Info m2 = localmusiclist.get(currentPlayItem);
        song_name_tv.setText(m2.getTitle());
        artist_tv.setText(m2.getArtist());

    }

    private void LocalInitData() {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
        currentPlayItem = sh.getInt(CurrentItem, 0);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initWidgets() {
        play_btn = (ImageView) findViewById(R.id.play_btn);
        bottom_music_more = (ImageView) findViewById(R.id.bottom_music_more);
        onlinefragment = new online_fragment();
        friendsfragment = new friends_fragment();
        localfragment = new local_fragment();
        listfragments.add(onlinefragment);
        listfragments.add(localfragment);
        play_btn.setImageResource(R.drawable.pause_btn);
        listfragments.add(friendsfragment);
        view_pager = (NoSclideViewpager) findViewById(R.id.view_pager);
        myfragmentmanager = getSupportFragmentManager();
        bottom_music_ll = (LinearLayout) findViewById(R.id.music_to_showVIew_ll);
        popView = getLayoutInflater().inflate(R.layout.main_pop, null);
        helperClass = new GetLocalSong(this);
        localmusiclist = helperClass.getMp3Info();

    }

    private static int currentPlayItem = 0;

    public static int getCurrentPlayItem() {
        return currentPlayItem;
    }

    private void initEvent() {
        bar_disco.setOnClickListener(this);
        next_song.setOnClickListener(this);
        popListView = (ListView) popView.findViewById(R.id.main_pop_listview);
        TextView num_tv = (TextView) popView.findViewById(R.id.play_all_items);
        num_tv.setText("播放全部(" + localmusiclist.size() + ")");
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mp3Info m1 = localmusiclist.get(i);
                currentPlayItem = i;
                Intent t1 = new Intent(mycontext, CompletePlayService.class);
                t1.putExtra("message", ConstantVarible.PLAY_MSG);
                t1.putExtra("url", m1.getUrl());
                t1.putExtra("current", currentPlayItem);
                startService(t1);

            }
        });
        //加载PopWindow布局


        for (int i = 0; i < localmusiclist.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            Mp3Info m = localmusiclist.get(i);
            item.put("imageView", R.mipmap.list_icn_delete);
            item.put("txt_author", m.getArtist());
            item.put("txt_name", m.getTitle());
            popInfos.add(item);
        }
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

        bar_music.setOnClickListener(this);
        bar_friends.setOnClickListener(this);
        bar_search.setOnClickListener(this);
        play_btn.setOnClickListener(this);
        bottom_music_more.setOnClickListener(this);
        bottom_music_ll.setOnClickListener(this);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isopen = true;

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isopen = false;

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isopen) {
                    //LEFT和RIGHT指的是现存DrawerLayout的方向
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                    isopen = true;
                }

            }
        });
        main_selectedAdapter myadapter = new main_selectedAdapter(myfragmentmanager, listfragments);
        view_pager.setAdapter(myadapter);


    }

    private boolean isFirstPlay = true;

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(selected_mainReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bar_disco:
                view_pager.setCurrentItem(0);
                if (bar_music.isSelected()) {
                    bar_disco.setSelected(true);
                    bar_music.setSelected(false);
                } else if (bar_friends.isSelected()) {
                    bar_disco.setSelected(true);
                    bar_friends.setSelected(false);
                }
                break;
            case R.id.bar_music:
                view_pager.setCurrentItem(1);
                if (bar_disco.isSelected()) {
                    bar_friends.setSelected(true);
                    bar_disco.setSelected(false);
                } else if (bar_music.isSelected()) {
                    bar_friends.setSelected(true);
                    bar_music.setSelected(false);

                }
                break;
            case R.id.bar_friends:
                view_pager.setCurrentItem(2);
                if (bar_disco.isSelected()) {
                    bar_disco.setSelected(false);
                    bar_music.setSelected(true);
                } else if (bar_friends.isSelected()) {
                    bar_friends.setSelected(false);
                    bar_music.setSelected(true);
                }
                break;
            case R.id.play_btn:
                if (!isPause) {
                    isPause = true;
                    play_btn.setBackground(null);
                    play_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause_btn));
                    Intent t1 = new Intent(this, CompletePlayService.class);
                    t1.putExtra("message", ConstantVarible.PAUSE_MSG);
                    startService(t1);

                } else {
                    isPause = false;
                    play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play_btn));
                    play_btn.setBackground(getResources().getDrawable(R.drawable.list_bg));
                    if (!isFirstPlay) {
                        Intent t1 = new Intent(this, CompletePlayService.class);
                        t1.putExtra("message", ConstantVarible.PLAY_CONTINUE);
                        startService(t1);
                    } else {
                        isFirstPlay = false;
                        Intent t2 = new Intent(this, CompletePlayService.class);
                        t2.putExtra("message", ConstantVarible.PLAY_MSG);
                        t2.putExtra("current", currentPlayItem);
                        t2.putExtra("url", localmusiclist.get(currentPlayItem).getUrl());
                        startService(t2);
                    }
                }
                break;
            case R.id.bottom_music_more:
                if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(toolbar, 0, 450);
                }
                break;
            case R.id.music_to_showVIew_ll:
                Intent intent = new Intent(this, PlayTheMusic.class);
                startActivity(intent);
                break;
            case R.id.next_song:
                Intent t3 = new Intent(this, CompletePlayService.class);
                t3.putExtra("msg", ConstantVarible.PLAY_NEXT);
                startService(t3);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BeforeLeaveStore();
    }

    private final static String CurrentItem = "currentItem";

    private void BeforeLeaveStore() {
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor e = sh.edit();
        e.putInt(CurrentItem, currentPlayItem);
        e.commit();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SelectedMainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String m = intent.getAction();
            switch (m) {
                case ConstantVarible.CTL_ACTION:
                    isPause = intent.getBooleanExtra("isPause", true);
                    if (isPause) {
                        play_btn.setBackground(null);
                        play_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause_btn));

                    } else {
                        play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play_btn));
                        play_btn.setBackground(getResources().getDrawable(R.drawable.list_bg));
                    }
                    break;
                case ConstantVarible.UPDATE_ACTION:
                    currentPlayItem = intent.getIntExtra("current", -1);
                    if (currentPlayItem != -1) {
                        song_name_tv.setText(localmusiclist.get(currentPlayItem).getTitle());
                        artist_tv.setText(localmusiclist.get(currentPlayItem).getArtist());
                    }
                    break;

            }
        }
    }
}
