package com.stest.neteasycloud;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.OtherhelperClass.NoSclideViewpager;
import com.stest.adapter.main_selectedAdapter;
import com.stest.fragraments.friends_fragment;
import com.stest.fragraments.local_fragment;
import com.stest.fragraments.online_fragment;

import java.util.ArrayList;
import java.util.List;

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
    private Fragment onlinefragment;
    private Fragment localfragment;
    private Fragment friendsfragment;
    private List<Fragment> listfragments=new ArrayList<>();
    private boolean isopen = false;
    private FragmentManager myfragmentmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_main_activity);
        ViewUtils.inject(this);
        initWidgets();
        initEvent();

    }

    private void initWidgets() {
        onlinefragment = new online_fragment();
        friendsfragment = new friends_fragment();
        localfragment = new local_fragment();
        listfragments.add(onlinefragment);
        listfragments.add(localfragment);
        listfragments.add(friendsfragment);
        view_pager=(NoSclideViewpager)findViewById(R.id.view_pager);
        myfragmentmanager=getSupportFragmentManager();

    }

    private void initEvent() {
        bar_disco.setOnClickListener(this);
        bar_music.setOnClickListener(this);
        bar_friends.setOnClickListener(this);
        bar_search.setOnClickListener(this);
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
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
