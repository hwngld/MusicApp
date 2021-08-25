package com.example.music;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyService extends Service {
    public static final int ACTION_EXIT = 1;
    public static final int ACTION_NEXT = 2;
    public static final int ACTION_PAUSE = 3;
    public static final int ACTION_PLAY = 4;
    public static final int ACTION_PREVIOUS = 5;
    public static boolean isPlaying;
    RemoteViews remoteViewsCollapse ;
    MediaPlayer mediaPlayer;
    Song song,mSong;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra("action_music",0);
        song = (Song) intent.getSerializableExtra("song");
        if(song != null){
            mSong = song;
            mediaPlayer = MediaPlayer.create(getApplicationContext(),mSong.data);
            mediaPlayer.start();
            isPlaying = true;
        }
        if(action!=0){
            handleActionMusic(action);
        }

        RemoteViews remoteViewsExtend = new RemoteViews(getPackageName(), R.layout.layout_notification_extend);

        remoteViewsCollapse.setOnClickPendingIntent(R.id.imgExit, getPendingIntent(ACTION_EXIT));
        remoteViewsCollapse.setOnClickPendingIntent(R.id.imgNext, getPendingIntent(ACTION_NEXT));
        remoteViewsCollapse.setOnClickPendingIntent(R.id.imgPrevious, getPendingIntent(ACTION_PREVIOUS));
        remoteViewsCollapse.setTextViewText(R.id.tv_SongName, mSong.getName());
        remoteViewsCollapse.setTextViewText(R.id.tv_Singer, mSong.getSinger());
        if(isPlaying){
            remoteViewsCollapse.setOnClickPendingIntent(R.id.imgPlayOrPause, getPendingIntent(ACTION_PAUSE));
        }else{
            remoteViewsCollapse.setOnClickPendingIntent(R.id.imgPlayOrPause, getPendingIntent(ACTION_PLAY));
        }

        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCustomContentView(remoteViewsCollapse)
                .setSound(null)
                //.setCustomBigContentView(remoteViewsExtend)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background))
                .build();
        startForeground(1,notification);
        return START_NOT_STICKY;
    }

    private void handleActionMusic(int action) {
        switch (action){
            case ACTION_EXIT:
                onDestroy();
                stopSelf();
                sendActionToActivity(ACTION_EXIT);
                break;
            case ACTION_NEXT:
                sendActionToActivity(ACTION_NEXT);
                break;
            case ACTION_PREVIOUS:
                sendActionToActivity(ACTION_PREVIOUS);
                break;
            case ACTION_PAUSE:
                sendActionToActivity(ACTION_PAUSE);
                isPlaying = false;
                remoteViewsCollapse.setImageViewResource(R.id.imgPlayOrPause, R.drawable.ic_baseline_play_arrow_24);
                mediaPlayer.pause();
                break;
            case ACTION_PLAY:
                sendActionToActivity(ACTION_PLAY);
                isPlaying = true;
                remoteViewsCollapse.setImageViewResource(R.id.imgPlayOrPause, R.drawable.ic_baseline_pause_24);
                mediaPlayer.start();
                break;
        }
    }

    private PendingIntent getPendingIntent(int action) {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("action_music", action);
        return PendingIntent.getBroadcast(this, action, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    @Override
    public void onDestroy() {
        isPlaying = false;
        mediaPlayer.release();
        super.onDestroy();
    }
    public void sendActionToActivity(int action){
        Intent intent = new Intent("action_to_activity");
        intent.putExtra("action",action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        remoteViewsCollapse = new RemoteViews(getPackageName(),R.layout.layout_notification_collapse);
    }
}
