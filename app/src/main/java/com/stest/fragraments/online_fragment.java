package com.stest.fragraments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

    private ImageButton daily_btn;
    private TextView daily_text;

    private ImageView anim_image;
    private boolean isClick = false;
    //存放每个Pager的View
    private List<View> mViewList;
    //存放Title
    private List<String> mTitleList;
    private View recommend;
    private View list;
    private View anchor;
    private View ranking;
    //底部弹出视图

    private Date date;
    private SimpleDateFormat dateFm;
    private AnimationDrawable animationDrawable;
    //排行榜布局
    private ListView listView;

    //精彩节目推荐
    private ListView anchorListView;
    private int[] imageIds = new int[]{R.drawable.ranklist_first, R.drawable.ranklist_second, R.drawable.ranklist_third,
            R.drawable.ranklist_fourth, R.drawable.ranklist_five, R.drawable.ranklist_six};
    private List<Map<String, Object>> mInfos = new ArrayList<>();

    private List<Map<String, Object>> anchorInfos = new ArrayList<>();

    private String[] teams = new String[]{"五月天", "苏打绿", "信乐团", "飞儿乐队", "凤凰传奇", "纵贯线",};
    private String[] places = new String[]{"上海演唱会", "香港红馆演唱会", "台湾火力全开演唱会", "北京鸟巢演唱会"};

    private Activity myAcitivty;
    private String mParam1;
    private String mParam2;
    private LayoutInflater myinflater;
    private View myview;

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
        ViewUtils.inject(getActivity());//视图注入
        myinflater = inflater;
        initWidget();
        addView();
        initEvent();
        return myview;
    }

    private void initEvent() {
        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        //给ViewPager设置适配器
        view_pager.setAdapter(mAdapter);
        //将ViewPager和TabLayout连接起来
        tabLayout.setupWithViewPager(view_pager);
        tabLayout.setTabsFromPagerAdapter(mAdapter);
        //设置点击事件
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                view_pager.setCurrentItem(tab.getPosition());
                if (view_pager.getCurrentItem() == 1) {
                    anim_image.setImageResource(R.drawable.loadanimation);
                    animationDrawable = (AnimationDrawable) anim_image.getDrawable();
                    animationDrawable.start();
                } else {
                    anim_image.setImageResource(R.drawable.loadanimation);
                    animationDrawable = (AnimationDrawable) anim_image.getDrawable();
                    animationDrawable.stop();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        int notselected_color=getResources().getColor(R.color.tab_not_selected);
        tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(ConstantVarible.CURRENTTHEMECOLOR);
        tabLayout.setTabTextColors(notselected_color,ConstantVarible.CURRENTTHEMECOLOR);
    }

    private void initWidget() {
        myAcitivty = getActivity();
        int notselected_color=getResources().getColor(R.color.tab_not_selected);
        tabLayout = (TabLayout) myview.findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(ConstantVarible.CURRENTTHEMECOLOR);
        tabLayout.setTabTextColors(notselected_color,ConstantVarible.CURRENTTHEMECOLOR);
        recommend = myinflater.inflate(R.layout.recommend, null);
        list = myinflater.inflate(R.layout.list, null);
        anchor = myinflater.inflate(R.layout.anchor, null);
        ranking = myinflater.inflate(R.layout.ranking, null);

        view_pager = (ViewPager) myview.findViewById(R.id.view_pager_in_online);
        view_pager.setOffscreenPageLimit(2);
        daily_text = (TextView) recommend.findViewById(R.id.daily_text);
        daily_btn = (ImageButton) recommend.findViewById(R.id.daily_btn);
        anim_image = (ImageView) list.findViewById(R.id.anim_image);
        listView = (ListView) ranking.findViewById(R.id.listView);

        anchorListView = (ListView) anchor.findViewById(R.id.anchor_list_view);
        daily_btn.setOnClickListener(this);


        //自适配长、框设置


        mViewList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        getDate();
        daily_text.setText(getDate());

        //加载排行榜ListView布局
        for (int i = 0; i < 12; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("imageView", imageIds[new Random().nextInt(6)]);
            item.put("rank_first_txt", "1.知足");
            item.put("rank_second_txt", "2.温柔");
            item.put("rank_third_txt", "3.干杯");
            mInfos.add(item);
        }
        listView.setAdapter(new MyrankAdapter(myAcitivty, mInfos));

        //加载主播电台布局
        for (int i = 1; i < 15; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("imageView", R.mipmap.list_fourth);
            item.put("txt_team", teams[new Random().nextInt(5)]);
            item.put("txt_place", places[new Random().nextInt(4)]);
            anchorInfos.add(item);
        }
        anchorListView.setAdapter(new AnchorAdapter(myAcitivty, anchorInfos));

    }

    private void addView() {
        mViewList.add(recommend);
        mViewList.add(list);
        mViewList.add(anchor);
        mViewList.add(ranking);

        mTitleList.add("个性推荐");
        mTitleList.add("歌单");
        mTitleList.add("主播电台");
        mTitleList.add("排行榜");
        //设置标签的模式,默认系统模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        //添加TabLayout上的文本元素
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(3)));
    }

    private String getDate() {
        date = new Date();
        dateFm = new SimpleDateFormat("dd");
        return dateFm.format(date);
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
        switch (view.getId()) {

            case R.id.daily_btn:
                if (!isClick) {
                    daily_btn.setBackground(getResources().getDrawable(R.drawable.dailly_prs));
                    daily_text.setTextColor(Color.parseColor("#ffffff"));
                    isClick = true;
                } else if (isClick) {
                    daily_btn.setBackground(getResources().getDrawable(R.drawable.dailly_normal));
                    daily_text.setTextColor(Color.parseColor("#ffce3d3a"));
                    isClick = false;
                }

            default:
                break;
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

    }

}

