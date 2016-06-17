package com.stest.fragraments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.stest.adapter.AnchorAdapter;
import com.stest.neteasycloud.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link onlinebrocast_station.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link onlinebrocast_station#newInstance} factory method to
 * create an instance of this fragment.
 */
public class onlinebrocast_station extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //精彩节目推荐
    private ListView anchorListView;
    private OnFragmentInteractionListener mListener;

    public onlinebrocast_station() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment onlinebrocast_station.
     */
    // TODO: Rename and change types and number of parameters
    public static onlinebrocast_station newInstance(String param1, String param2) {
        onlinebrocast_station fragment = new onlinebrocast_station();
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
    private List<Map<String, Object>> anchorInfos = new ArrayList<>();
    private String[] teams = new String[]{"五月天", "苏打绿", "信乐团", "飞儿乐队", "凤凰传奇", "纵贯线",};
    private String[] places = new String[]{"上海演唱会", "香港红馆演唱会", "台湾火力全开演唱会", "北京鸟巢演唱会"};
    private  View myview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.anchor, container, false);
        anchorListView = (ListView) myview.findViewById(R.id.anchor_list_view);
        //加载主播电台布局
        for (int i = 1; i < 15; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("imageView", R.mipmap.list_fourth);
            item.put("txt_team", teams[new Random().nextInt(5)]);
            item.put("txt_place", places[new Random().nextInt(4)]);
            anchorInfos.add(item);
        }
        anchorListView.setAdapter(new AnchorAdapter(getActivity(), anchorInfos));
        return myview;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
