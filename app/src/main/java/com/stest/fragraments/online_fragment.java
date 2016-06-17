package com.stest.fragraments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.stest.OtherhelperClass.ConstantVarible;
import com.stest.adapter.AnchorAdapter;
import com.stest.adapter.MyrankAdapter;
import com.stest.adapter.main_selectedAdapter;
import com.stest.neteasycloud.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link online_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link online_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class online_fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ViewPager view_pager;
    private TabLayout tabLayout;
    //存放Title
    private List<String> mTitleList;
    private Fragment recommend;
    private Fragment list;
    private Fragment anchor;
    private Fragment ranking;
    //底部弹出视图
    private AnimationDrawable animationDrawable;

    private Activity myAcitivty;
    private String mParam1;
    private String mParam2;
    private LayoutInflater myinflater;
    private View myview;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    public online_fragment() {
    }

    public static online_fragment newInstance(String param1, String param2) {
        online_fragment fragment = new online_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.fragment_online_fragment, container, false);
        myinflater = inflater;
        initWidget();
        addView();
        initEvent();
        return myview;
    }

    private void initEvent() {
        FragmentPagerAdapter onlineadapter = new main_selectedAdapter(getFragmentManager(), fragmentList);
        //给ViewPager设置适配器
        view_pager.setAdapter(onlineadapter);
        //将ViewPager和TabLayout连接起来
        tabLayout.setupWithViewPager(view_pager);
        tabLayout.setTabsFromPagerAdapter(onlineadapter);
        //设置点击事件

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                view_pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(0).setText(mTitleList.get(0));
        tabLayout.getTabAt(1).setText(mTitleList.get(1));
        tabLayout.getTabAt(2).setText(mTitleList.get(2));
        tabLayout.getTabAt(3).setText(mTitleList.get(3));
    }

    @Override
    public void onResume() {
        super.onResume();

        int notselected_color = getResources().getColor(R.color.tab_not_selected);
        tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(ConstantVarible.CURRENTTHEMECOLOR);
        tabLayout.setTabTextColors(notselected_color, ConstantVarible.CURRENTTHEMECOLOR);
    }

    private void initWidget() {
        myAcitivty = getActivity();
        int notselected_color = getResources().getColor(R.color.tab_not_selected);
        tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(ConstantVarible.CURRENTTHEMECOLOR);
        tabLayout.setTabTextColors(notselected_color, ConstantVarible.CURRENTTHEMECOLOR);
        recommend = new online_recommend();
        list = new online_songlist();
        anchor = new onlinebrocast_station();
        ranking = new online_ranking();

        view_pager = (ViewPager) myview.findViewById(R.id.view_pager_in_online);
        view_pager.setOffscreenPageLimit(4);

        mTitleList = new ArrayList<>();


    }

    private void addView() {
        mTitleList.clear();
        if(fragmentList.size()==0)
        {
            fragmentList.add(recommend);
            fragmentList.add(list);
            fragmentList.add(anchor);
            fragmentList.add(ranking);
        }

        mTitleList.add("个性推荐");
        mTitleList.add("歌单");
        mTitleList.add("主播电台");
        mTitleList.add("排行榜");

        //设置标签的模式,默认系统模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //添加TabLayout上的文本元素
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
