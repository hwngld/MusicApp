package com.example.music;

import static com.example.music.MyService.ACTION_EXIT;
import static com.example.music.MyService.ACTION_NEXT;
import static com.example.music.MyService.ACTION_PAUSE;
import static com.example.music.MyService.ACTION_PLAY;
import static com.example.music.MyService.ACTION_PREVIOUS;
import static com.example.music.MyService.isPlaying;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView imgNext, imgPrev, imgPauseOrPlay, imgExit;
    Button btnRandom;
    TextView tvName, tvSinger;
    List<Song> songList;
    RelativeLayout layout,itemSong;
    RecyclerView recyclerView;
    SongAdapter songAdapter;
    boolean isRunning;
    int pos;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra("action", 0);
            if(action!=0){
                handleAction(action);
            }
        }
    };

    private void handleAction(int action) {
        switch (action){
            case MyService.ACTION_EXIT:
                Toast.makeText(getApplicationContext(), "exit", Toast.LENGTH_SHORT).show();
                stopServiceMusic();
                break;
            case MyService.ACTION_NEXT:
                if(pos < songList.size() - 1){
                    pos++;
                }else{
                    pos = 0;
                }
                setInfo(songList.get(pos));
                startServiceMusic(songList.get(pos));
                break;
            case MyService.ACTION_PAUSE:
                imgPauseOrPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                break;
            case MyService.ACTION_PLAY:
                imgPauseOrPlay.setImageResource(R.drawable.ic_baseline_pause_24);
                break;
            case MyService.ACTION_PREVIOUS:
                if(pos > 0){
                    pos--;
                }else{
                    pos = songList.size() - 1;
                }
                setInfo(songList.get(pos));
                startServiceMusic(songList.get(pos));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();

        layout.setVisibility(View.GONE);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("action_to_activity"));

        songList = new ArrayList<>();
        songList = getData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songAdapter = new SongAdapter(new SongAdapter.OnItemClick() {
            @Override
            public void ItemClick(Song song,int position) {
                startServiceMusic(song);
                pos = position;
            }
        });
        recyclerView.setAdapter(songAdapter);
        songAdapter.setData(songList);

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = new Random().nextInt(songList.size());
                startServiceMusic(songList.get(pos));
            }
        });
        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopServiceMusic();
            }
        });
        imgPauseOrPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    handleClick(ACTION_PAUSE);
                }else{
                    handleClick(ACTION_PLAY);
                }
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick(ACTION_NEXT);
            }
        });
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClick(ACTION_PREVIOUS);
            }
        });
    }

    private void setInfo(Song song) {
        tvName.setText(song.getName());
        tvSinger.setText(song.getSinger());
    }

    private List<Song> getData() {
        List<Song> list = new ArrayList<>();
        list.add(new Song("Sai cách yêu","Hương Ly", R.raw.saicachyeu));
        list.add(new Song("Em say rồi","Thương Võ", R.raw.emsayroi));
        list.add(new Song("Níu Duyên","Lê Bảo Bình", R.raw.niuduyen));
        list.add(new Song("Sài gòn đau lòng quá","Ca sĩ", R.raw.saigondaulongqua));
        list.add(new Song("Sai cách yêu 1","Hương Ly", R.raw.saicachyeu));
        list.add(new Song("Sai cách yêu 2","Hương Ly", R.raw.saicachyeu));
        list.add(new Song("Em say rồi 1","Thương Võ", R.raw.emsayroi));
        list.add(new Song("Níu Duyên 2","Lê Bảo Bình", R.raw.niuduyen));
        list.add(new Song("Sài gòn đau lòng quá 2","Ca sĩ", R.raw.saigondaulongqua));
        list.add(new Song("Sai cách yêu 3","Hương Ly", R.raw.saicachyeu));
        return list;
    }

    private void handleClick(int action) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("action_music", action);
        startService(intent);
    }

    private void InitView() {
        imgNext = findViewById(R.id.imgNext);
        imgPrev = findViewById(R.id.imgPrevious);
        imgPauseOrPlay = findViewById(R.id.imgPlayOrPause);
        imgExit = findViewById(R.id.imgExit);
        layout = findViewById(R.id.layoutMusic);
        recyclerView = findViewById(R.id.listSong);
        tvName = findViewById(R.id.tv_SongName);
        tvSinger = findViewById(R.id.tv_Singer);
        btnRandom = findViewById(R.id.btnRandom);
    }

    private void stopServiceMusic() {
        isRunning = false;
        Intent intent = new Intent(this, MyService.class);
        layout.setVisibility(View.GONE);
        stopService(intent);
    }

    private void startServiceMusic(Song song) {
        setInfo(song);
        if(isRunning){
            stopServiceMusic();
        }
        isRunning = true;
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("song",song);
        layout.setVisibility(View.VISIBLE);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}