package com.stest.OtherhelperClass;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.provider.MediaStore;

import com.stest.DataClass.Mp3Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldy on 6/14/16.
 */
public class GetLocalSong {
    private static  List<Mp3Info> songlist=new ArrayList<>();
    private static  Context context;

    public GetLocalSong(Context con) {
        context=con;
        getMp3Info();
    }

    public static Context getContext() {
        return context;
    }

    public static List<Mp3Info> getSonglist() {
        return songlist;
    }

    public  List<Mp3Info> getMp3Info()
    {

        Cursor cursor=context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        for(int i=0;i<cursor.getCount();i++)
        {
            Mp3Info m=new Mp3Info();
            cursor.moveToNext();
            long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artise=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long durition=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int isMusic=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if(isMusic!=0)
            {
                m.setArtist(artise);
                m.setDuration(durition);
                m.setId(id);
                m.setSize(size);
                m.setTitle(title);
                m.setUrl(url);
            }
            songlist.add(m);
        }
        return songlist;
    }

}
