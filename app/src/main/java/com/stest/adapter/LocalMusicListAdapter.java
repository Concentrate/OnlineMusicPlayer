package com.stest.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stest.DataClass.Mp3Info;
import com.stest.neteasycloud.R;

import java.util.List;

/**
 * Created by ldy on 6/12/16.
 */
public class LocalMusicListAdapter extends ArrayAdapter<Mp3Info> {

    private List<Mp3Info>thelist;
    private View myview;
    private int resourceId;
    private Activity mycontext;

    public LocalMusicListAdapter(Context context, int resource, int textViewResourceId, Mp3Info[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public LocalMusicListAdapter(Context context, int resource, List<Mp3Info> objects) {
        super(context, resource, objects);
        thelist=objects;
        resourceId=resource;
        mycontext=(Activity)context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
         TextView artist = null;
        TextView songname=null;
        if(convertView==null)
        {
           view=mycontext.getLayoutInflater().inflate(resourceId,null);
            artist=(TextView)view.findViewById(R.id.song_author);
            songname=(TextView)view.findViewById(R.id.song_name);
            ViewHodler holder=new ViewHodler();
            holder.setArtist(artist);
            holder.setSongname(songname);
            convertView=view;
            convertView.setTag(holder);

        }else
        {
            view=convertView;
            ViewHodler t1=(ViewHodler)convertView.getTag();
            artist=t1.getArtist();
            songname=t1.getSongname();

        }
        Mp3Info thesong=getItem(position);
        artist.setText(thesong.getArtist());
        songname.setText(thesong.getTitle());
        return view;
    }
    class ViewHodler
    {
        TextView artist;
        TextView songname;

        public ViewHodler() {
        }

        public TextView getSongname() {
            return songname;
        }

        public void setSongname(TextView songname) {
            this.songname = songname;
        }

        public TextView getArtist() {
            return artist;
        }

        public void setArtist(TextView artist) {
            this.artist = artist;
        }
    }

}
