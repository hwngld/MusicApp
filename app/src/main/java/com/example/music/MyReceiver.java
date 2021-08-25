package com.example.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, MyService.class);
        int action  = intent.getIntExtra("action_music", 0);
        intentService.putExtra("action_music", action);
        context.startService(intentService);
    }
}
