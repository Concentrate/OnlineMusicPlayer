package com.stest.fragraments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stest.neteasycloud.NewSongRanking;
import com.stest.neteasycloud.R;
import com.stest.neteasycloud.onlineRecommend;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link online_recommend.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link online_recommend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class online_recommend extends Fragment implements OnClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public online_recommend() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment online_recommend.
     */
    // TODO: Rename and change types and number of parameters
    public static online_recommend newInstance(String param1, String param2) {
        online_recommend fragment = new online_recommend();
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

    private Date date;
    private SimpleDateFormat dateFm;
    private boolean isClick = false;

    private String getDate() {
        date = new Date();
        dateFm = new SimpleDateFormat("dd");
        return dateFm.format(date);
    }
    private View myview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview=inflater.inflate(R.layout.recommend, container, false);
        daily_text = (TextView)myview.findViewById(R.id.daily_text);
        daily_btn = (ImageButton) myview.findViewById(R.id.daily_btn);
        newsong_rank=(ImageButton)myview.findViewById(R.id.recommend_rank_newsong);
        personnal_fm=(ImageButton) myview.findViewById(R.id.personal_fm);
        daily_btn.setOnClickListener(this);
        daily_text.setText(getDate());
        newsong_rank.setOnClickListener(this);
        personnal_fm.setOnClickListener(this);
        return myview;
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

    private ImageButton daily_btn;
    private TextView daily_text;

    private ImageButton personnal_fm;
    private ImageButton newsong_rank;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.daily_btn:
                Intent in=new Intent(getActivity(),onlineRecommend.class);
                startActivity(in);
               break;
            case R.id.recommend_rank_newsong:
                Intent in2=new Intent(getActivity(), NewSongRanking.class);
                startActivity(in2);
                break;




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
}
