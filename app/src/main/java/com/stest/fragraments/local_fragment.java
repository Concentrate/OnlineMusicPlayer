package com.stest.fragraments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.stest.neteasycloud.R;
import com.stest.neteasycloud.local_music_listActivity;

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
    private void initWidget() {
        local_music=(LinearLayout)myview.findViewById(R.id.local_music);
        recent_music_ll=(LinearLayout)myview.findViewById(R.id.recent_play);
        download_music_ll=(LinearLayout)myview.findViewById(R.id.download_song_ll);
        mySinger_ll=(LinearLayout)myview.findViewById(R.id.mysinger_ll);
        localMusicNum=(TextView)myview.findViewById(R.id.local_music_num);
        recentPlaynum=(TextView)myview.findViewById(R.id.recent_play_num);
        downloadNum=(TextView)myview.findViewById(R.id.download_song_num);
        mysingerNum=(TextView)myview.findViewById(R.id.mysingernum);
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
