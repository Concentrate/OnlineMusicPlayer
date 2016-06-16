package com.stest.fragraments;

import android.content.Context;
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

import com.stest.neteasycloud.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class friends_fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ViewPager view_pager_friends;
    private TabLayout tabLayout_friends;
    private View myview;

    private OnFragmentInteractionListener mListener;
    private List<String> mTitleList = new ArrayList<>();
    private List<View> mViewList = new ArrayList<>();
    private LayoutInflater myinflater;

    public friends_fragment() {
    }


    public static friends_fragment newInstance(String param1, String param2) {
        friends_fragment fragment = new friends_fragment();
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
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_friends_fragment, container, false);
        myinflater = inflater;
        initWidgets();
        return myview;
    }

    private void initWidgets() {
        mViewList.clear();
        mTitleList.clear();
        view_pager_friends = (ViewPager) myview.findViewById(R.id.view_pager_in_friends);
        tabLayout_friends = (TabLayout) myview.findViewById(R.id.tabs_friends);
        mTitleList.add("我的好友");
        mTitleList.add("附近的人");
        mTitleList.add("我的关注");
        //设置标签的模式,默认系统模式
        tabLayout_friends.setTabMode(TabLayout.MODE_FIXED);
        mViewList.add(myinflater.inflate(R.layout.friendsmodel_friends,null));
        mViewList.add(myinflater.inflate(R.layout.friends_local,null));
        mViewList.add(myinflater.inflate(R.layout.friendsmodel_payattation,null));

        //添加TabLayout上的文本元素
        tabLayout_friends.addTab(tabLayout_friends.newTab().setText(mTitleList.get(0)));
        tabLayout_friends.addTab(tabLayout_friends.newTab().setText(mTitleList.get(1)));
        tabLayout_friends.addTab(tabLayout_friends.newTab().setText(mTitleList.get(2)));
        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        //给ViewPager设置适配器
        view_pager_friends.setAdapter(mAdapter);
        //将ViewPager和TabLayout连接起来
        tabLayout_friends.setupWithViewPager(view_pager_friends);
        tabLayout_friends.setTabsFromPagerAdapter(mAdapter);
        //设置点击事件
        tabLayout_friends.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                       @Override
                                                       public void onTabSelected(TabLayout.Tab tab) {

                                                           view_pager_friends.setCurrentItem(tab.getPosition());

                                                       }

                                                       @Override
                                                       public void onTabUnselected(TabLayout.Tab tab) {

                                                       }

                                                       @Override
                                                       public void onTabReselected(TabLayout.Tab tab) {

                                                       }
                                                   }
        );
//        tabLayout_friends.getTabAt(0).setText("我的好友");
//        tabLayout_friends.getTabAt(0).setText("附近的人");
//        tabLayout_friends.getTabAt(0).setText("我的关注");

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
