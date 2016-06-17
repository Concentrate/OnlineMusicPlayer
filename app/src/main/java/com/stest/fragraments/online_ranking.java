package com.stest.fragraments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.stest.adapter.MyrankAdapter;
import com.stest.neteasycloud.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class online_ranking extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //排行榜布局
    private ListView listView;

    public online_ranking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment online_ranking.
     */
    // TODO: Rename and change types and number of parameters
    public static online_ranking newInstance(String param1, String param2) {
        online_ranking fragment = new online_ranking();
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
    private View myview;
    private int[] imageIds = new int[]{R.drawable.ranklist_first, R.drawable.ranklist_second, R.drawable.ranklist_third,
            R.drawable.ranklist_fourth, R.drawable.ranklist_five, R.drawable.ranklist_six};
    private List<Map<String, Object>> mInfos = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview=inflater.inflate(R.layout.ranking,container,false);
        listView = (ListView) myview.findViewById(R.id.listView);
        //加载排行榜ListView布局
        for (int i = 0; i < 12; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("imageView", imageIds[new Random().nextInt(6)]);
            item.put("rank_first_txt", "1.知足");
            item.put("rank_second_txt", "2.温柔");
            item.put("rank_third_txt", "3.干杯");
            mInfos.add(item);
        }
        listView.setAdapter(new MyrankAdapter(getActivity(), mInfos));
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
