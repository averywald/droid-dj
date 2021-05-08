package com.example.musicplayer;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.service.media.MediaBrowserService;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PlayerService extends Service {

    private IBinder serviceBinder = new ServiceBinder();
    private MediaPlayer player;
    private ArrayList<Song> queue = new ArrayList<>();

    public class ServiceBinder extends Binder {
        PlayerService getPlayerService() {
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        super.onCreate();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//
//        // TODO: something ...
//
//        // If we get killed, after returning from here, restart
//        return START_STICKY;
//    }

    @Override
    public IBinder onBind(Intent intent) {
        Song s = (Song) intent.getSerializableExtra("SONG_DATA_OBJECT");
        this.queueSong(s);
        this.initMediaPlayer();
        this.play();

        return serviceBinder;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public void prev() {}

    public void next() {}

    public void restart() {}

    public void play() {
        if (!this.player.isPlaying()) this.player.start();
    }

    public void pause() {
        if (this.player.isPlaying()) this.player.pause();
    }

    public void queueSong(Song s) {
        this.queue.add(s);
    }

    public void discardSong(Song s) {
        this.queue.remove(s);
    }

    public int getCurrentPosition() { return this.player.getCurrentPosition(); }

    public int getDuration() {
        return this.player.getDuration();
    }

    public boolean isPlaying() {
        return this.player.isPlaying();
    }

    public void seekTo(int duration) {
        this.player.seekTo(duration);
    }

    private Uri getResourceUri(int index) {
        int resourceId = this.queue.get(index).getResourceId();
        Resources resources = this.getResources();
        // set up media player
        Uri uri = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(resourceId))
                .appendPath(resources.getResourceTypeName(resourceId))
                .appendPath(resources.getResourceEntryName(resourceId))
                .build();
        return uri;
    }

    private void initMediaPlayer() {
        player = MediaPlayer.create(this, this.getResourceUri(0));

        player.setOnCompletionListener(l -> {
            // should go to next song
            this.next();
        });

        player.setVolume(1.0f, 1.0f);
    }

}
