package com.stest.Service;

import java.io.FileDescriptor;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.stest.DataClass.Mp3Info;
import com.stest.OtherhelperClass.ConstantVarible;
import com.stest.OtherhelperClass.UtilTool;
import com.stest.neteasycloud.PlayTheMusic;


public class CompletePlayService extends Service {
    private MediaPlayer mediaPlayer;
    private String path;
    private int msg;
    private boolean isPause;
    private int current = 0;        // 记录当前正在播放的音乐
    private List<Mp3Info> mp3Infos;    //存放Mp3Info对象的集合
    private int status = 3;            //播放状态，默认为顺序播放
    private MyReceiver myReceiver;    //自定义广播接收器
    private int currentTime;        //当前播放进度
    private int duration;            //播放长度

    //服务要发送的一些Action
    public static final String UPDATE_ACTION = "com.action.UPDATE_ACTION";    //更新动作
    public static final String CTL_ACTION = "com.action.CTL_ACTION";        //控制动作
    public static final String MUSIC_CURRENT = "com.action.MUSIC_CURRENT";    //当前音乐播放时间更新动作
    public static final String MUSIC_DURATION = "com.action.MUSIC_DURATION";//新音乐长度更新动作

    /**
     * handler用来接收消息，来发送广播更新播放时间
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (mediaPlayer != null) {
                    currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                    Intent intent = new Intent();
                    intent.setAction(MUSIC_CURRENT);
                    intent.putExtra("currentTime", currentTime);
                    sendBroadcast(intent); // 给PlayerActivity发送广播
                    handler.sendEmptyMessageDelayed(1, 1000);
                }

            }
            if(msg.what==2)
            {

                Intent in=new Intent();
                in.setAction(ConstantVarible.UPDATE_ACTION);
                in.putExtra("current",current);
                sendBroadcast(in);
                handler.sendEmptyMessageDelayed(2,1000);

            }
            if(msg.what==3)
            {

                Intent in=new Intent();
                in.setAction(ConstantVarible.CTL_ACTION);
                in.putExtra("isPause",isPause);
                sendBroadcast(in);
                handler.sendEmptyMessageDelayed(3,1000);
            }
        }

        ;
    };

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service", "service created");
        mediaPlayer = new MediaPlayer();
        mp3Infos = UtilTool.getMp3Infos(CompletePlayService.this);


        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (status == 1) { // 单曲循环
                    mediaPlayer.start();
                } else if (status == 2) { // 全部循环
                    current++;
                    if (current > mp3Infos.size() - 1) {
                        current = 0;
                    }
                    Intent sendIntent = new Intent(UPDATE_ACTION);
                    sendIntent.putExtra("current", current);
                    // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                    sendBroadcast(sendIntent);
                    path = mp3Infos.get(current).getUrl();
                    play(0);
                } else if (status == 3) {    //随机播放
                    current = getRandomIndex(mp3Infos.size() - 1);
                    Intent sendIntent = new Intent(UPDATE_ACTION);
                    sendIntent.putExtra("current", current);
                    // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                    sendBroadcast(sendIntent);
                    path = mp3Infos.get(current).getUrl();
                    play(0);
                }
            }
        });

        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayTheMusic.CTL_ACTION);
        registerReceiver(myReceiver, filter);
    }


    protected int getRandomIndex(int end) {
        int index = (int) (Math.random() * end);
        return index;
    }
    public void UpdateState()
    {
        handler.sendEmptyMessage(2);
        handler.sendEmptyMessage(3);

    }


    private MyIBinder mybinder=new MyIBinder();
    @Override
    public IBinder onBind(Intent arg0) {
        return mybinder;
    }
    public class MyIBinder implements  IBinder
    {
        public CompletePlayService getCompletePlayService()
        {
            return CompletePlayService.this;
        }
        public void UpdateState()
        {
            UpdateState();
        }
        @Override
        public String getInterfaceDescriptor() throws RemoteException {
            return null;
        }

        @Override
        public boolean pingBinder() {
            return false;
        }

        @Override
        public boolean isBinderAlive() {
            return false;
        }

        @Override
        public IInterface queryLocalInterface(String s) {
            return null;
        }

        @Override
        public void dump(FileDescriptor fileDescriptor, String[] strings) throws RemoteException {

        }

        @Override
        public void dumpAsync(FileDescriptor fileDescriptor, String[] strings) throws RemoteException {

        }

        @Override
        public boolean transact(int i, Parcel parcel, Parcel parcel1, int i1) throws RemoteException {
            return false;
        }

        @Override
        public void linkToDeath(DeathRecipient deathRecipient, int i) throws RemoteException {

        }

        @Override
        public boolean unlinkToDeath(DeathRecipient deathRecipient, int i) {
            return false;
        }
    }

    private static boolean isFirstPlay=true;

    public static boolean isFirstPlay() {
        return isFirstPlay;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        if(intent!=null) {
            path = intent.getStringExtra("url");        //歌曲路径
            current = intent.getIntExtra("current", -1);    //当前播放歌曲的在mp3Infos的位置
            msg = intent.getIntExtra("msg", 0);            //播放信息
            if (msg == ConstantVarible.PLAY_MSG) {    //直接播放音乐
                play(0);
            } else if (msg == ConstantVarible.PAUSE_MSG) {    //暂停
                pause();
            } else if (msg == ConstantVarible.PLAY_STOP) {        //停止
                stop();
            } else if (msg == ConstantVarible.PLAY_CONTINUE) {    //继续播放
                resume();
            } else if (msg == ConstantVarible.PLAY_PREVIOUS) {    //上一首
                previous();
            } else if (msg == ConstantVarible.PLAY_NEXT) {        //下一首
                next();
            } else if (msg == ConstantVarible.SEEKTO) {

            }
            super.onStart(intent, startId);
        }

    }


    private void play(int currentTime) {
        try {
            isFirstPlay=false;
            isPause=false;
            mediaPlayer.reset();// 把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare(); // 进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
            handler.sendEmptyMessage(1);
            Intent sendIntent = new Intent();
            sendIntent.setAction(ConstantVarible.UPDATE_ACTION);
            sendIntent.putExtra("current", current);
            sendBroadcast(sendIntent);
            sendIntent.setAction(ConstantVarible.CTL_ACTION);
            sendIntent.putExtra("isPause",false);
            sendBroadcast(sendIntent);
            UpdateState();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
            Intent i = new Intent(CTL_ACTION);
            i.putExtra("isPause", true);
            sendBroadcast(i);

        }
    }

    private void resume() {
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * 上一首
     */
    private void previous() {
        current = current - 1;
        if (current < 0)
            current = mp3Infos.size() - 1;
        play(0);
    }

    /**
     * 下一首
     */
    private void next() {
        current = (current + 1) % mp3Infos.size();
        play(0);
    }

    /**
     * 停止音乐
     */
    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    /**
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     */
    private final class PreparedListener implements OnPreparedListener {
        private int currentTime;

        public PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start(); // 开始播放
            if (currentTime > 0) { // 如果音乐不是从头播放
                mediaPlayer.seekTo(currentTime);
            }
            Intent intent = new Intent();
            intent.setAction(MUSIC_DURATION);
            duration = mediaPlayer.getDuration();
            intent.putExtra("duration", duration);    //通过Intent来传递歌曲的总长度
            sendBroadcast(intent);
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("control", -1);
            switch (control) {
                case 1:
                    status = 1; // 将播放状态置为1表示：单曲循环
                    break;
                case 2:
                    status = 2;    //将播放状态置为2表示：全部循环
                    break;
                case 3:
                    status = 3;    //将播放状态置为4表示：随机播放
                    break;
            }
        }
    }

}
