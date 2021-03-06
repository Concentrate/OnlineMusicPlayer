package com.stest.fragraments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.DataClass.Mp3Info;
import com.stest.OtherhelperClass.ConstantVarible;
import com.stest.OtherhelperClass.UtilTool;
import com.stest.neteasycloud.R;
import com.stest.neteasycloud.local_music_listActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link local_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link local_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class local_fragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private LinearLayout local_music;
    private View myview;
    private LinearLayout recent_music_ll;
    private LinearLayout download_music_ll;
    private LinearLayout mySinger_ll;
    private TextView localMusicNum;
    private TextView recentPlaynum;
    private TextView downloadNum;
    private TextView mysingerNum;
    private List<Mp3Info>songlist;




    private OnFragmentInteractionListener mListener;

    public local_fragment() {
    }


    public static local_fragment newInstance(String param1, String param2) {
        local_fragment fragment = new local_fragment();
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
    public void onResume() {
        int color=ConstantVarible.CURRENTTHEMECOLOR;
        super.onResume();
        ImageView local_iv=(ImageView)myview.findViewById(R.id.local_music_iv);
        ImageView recently_iv=(ImageView)myview.findViewById(R.id.recent_play_iv);
        ImageView download_iv=(ImageView)myview.findViewById(R.id.download_iv);
        ImageView mysinger_iv=(ImageView)myview.findViewById(R.id.mysinger_iv);
        local_iv.setColorFilter(color);
        recently_iv.setColorFilter(color);
        download_iv.setColorFilter(color);
        mysinger_iv.setColorFilter(color);
    }

    private void initWidget() {
        local_music=(LinearLayout)myview.findViewById(R.id.local_music);
        recent_music_ll=(LinearLayout)myview.findViewById(R.id.recent_play);
        download_music_ll=(LinearLayout)myview.findViewById(R.id.download_song_ll);
        mySinger_ll=(LinearLayout)myview.findViewById(R.id.mysinger_ll);
        localMusicNum=(TextView)myview.findViewById(R.id.local_music_num);
        recentPlaynum=(TextView)myview.findViewById(R.id.recent_play_num);
        downloadNum=(TextView)myview.findViewById(R.id.download_song_num);
        mysingerNum=(TextView)myview.findViewById(R.id.mysingernum);
        songlist= UtilTool.getMp3Infos(getActivity());
        int color=ConstantVarible.CURRENTTHEMECOLOR;
        ImageView local_iv=(ImageView)myview.findViewById(R.id.local_music_iv);
        ImageView recently_iv=(ImageView)myview.findViewById(R.id.recent_play_iv);
        ImageView download_iv=(ImageView)myview.findViewById(R.id.download_iv);
        ImageView mysinger_iv=(ImageView)myview.findViewById(R.id.mysinger_iv);
        local_iv.setColorFilter(color);
        recently_iv.setColorFilter(color);
        download_iv.setColorFilter(color);
        mysinger_iv.setColorFilter(color);

        if(songlist!=null)
            localMusicNum.setText("("+songlist.size()+")");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.fragment_local_fragment, container, false);
        initWidget();
        initEvent();

        return myview;

    }

    private void initEvent() {
        local_music.setOnClickListener(this);
        download_music_ll.setOnClickListener(this);
        recent_music_ll.setOnClickListener(this);
        mySinger_ll.setOnClickListener(this);

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
        switch (view.getId())
        {
            case R.id.local_music:
                Intent intent=new Intent(getActivity(), local_music_listActivity.class);
                startActivity(intent);
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
