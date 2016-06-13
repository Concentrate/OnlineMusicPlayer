package com.stest.neteasycloud;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.OtherhelperClass.NoSclideViewpager;
import com.stest.adapter.MainPopAdapter;
import com.stest.adapter.main_selectedAdapter;
import com.stest.fragraments.friends_fragment;
import com.stest.fragraments.local_fragment;
import com.stest.fragraments.online_fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class selected_MainActivity extends FragmentActivity implements View.OnClickListener,friends_fragment.OnFragmentInteractionListener,online_fragment.OnFragmentInteractionListener,local_fragment.OnFragmentInteractionListener {

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
    private LinearLayout bottom_music_more;
    private View popView;
    //弹出视图ListView
    private ListView popListView;
    private LinearLayout bottom_music_ll;
    private PopupWindow popupWindow;
    private Fragment onlinefragment;
    private Fragment localfragment;
    private Fragment friendsfragment;
    private List<Fragment> listfragments=new ArrayList<>();
    private boolean isopen = false;
    private boolean isChanged = true;
    private List<Map<String, Object>> popInfos = new ArrayList<>();
    private FragmentManager myfragmentmanager;
    private String[] Songs=new String[]{"知足-","倔强-","我不愿让你一个人-","干杯-","宠上天-","突然好想你-","星空-","如烟-","第二人生-"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_main_activity);
        ViewUtils.inject(this);
        initWidgets();
        initEvent();

    }
    public static void setTheView(int i)
    {
        
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initWidgets() {
        play_btn=(ImageView)findViewById(R.id.play_btn) ;
        bottom_music_more=(LinearLayout) findViewById(R.id.bottom_music_more);
        onlinefragment = new online_fragment();
        friendsfragment = new friends_fragment();
        localfragment = new local_fragment();
        listfragments.add(onlinefragment);
        listfragments.add(localfragment);
        listfragments.add(friendsfragment);
        view_pager=(NoSclideViewpager)findViewById(R.id.view_pager);
        myfragmentmanager=getSupportFragmentManager();
        bottom_music_ll=(LinearLayout)findViewById(R.id.music_control);
        popView = getLayoutInflater().inflate(R.layout.main_pop, null);

    }

    private void initEvent() {
        bar_disco.setOnClickListener(this);
        next_song.setOnClickListener(this);
        popListView = (ListView) popView.findViewById(R.id.main_pop_listview);
        //加载PopWindow布局
        for (int i = 0; i < 15; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("imageView", R.mipmap.list_icn_delete);
            item.put("txt_author", "五月天");
            item.put("txt_name", Songs[new Random().nextInt(9)]);
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
        main_selectedAdapter myadapter=new main_selectedAdapter(myfragmentmanager,listfragments);
        view_pager.setAdapter(myadapter);


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
                if (isChanged) {
                    play_btn.setBackground(null);
                    play_btn.setImageDrawable(getResources().getDrawable(R.drawable.pause_btn));
                } else {
                    play_btn.setImageDrawable(getResources().getDrawable(R.drawable.play_btn));
                    play_btn.setBackground(getResources().getDrawable(R.drawable.list_bg));
                }
                isChanged = !isChanged;
                break;
            case R.id.bottom_music_more:
                if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(toolbar, 0, 450);
                }
                break;
            case R.id.music_control:
                Intent intent=new Intent(this,PlayTheMusic.class);
                startActivity(intent);
                break;
            case R.id.next_song:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
