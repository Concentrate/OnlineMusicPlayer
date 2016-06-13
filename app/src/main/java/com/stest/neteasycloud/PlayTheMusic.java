package com.stest.neteasycloud;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.stest.fragraments.playmusic_view_bottom;

/**
 * Created by ldy on 6/13/16.
 */
public class PlayTheMusic extends FragmentActivity implements playmusic_view_bottom.OnFragmentInteractionListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
