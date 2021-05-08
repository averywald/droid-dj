package com.example.musicplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<Song> songs = new ArrayList<Song>();
    private Button showPlayerActivityButton;

    private PlayerService playerService;
    private boolean isBound = false;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.ServiceBinder binder = (PlayerService.ServiceBinder) service;
            playerService = binder.getPlayerService(); // expose the service object to activity
            isBound = true; // set bound activity state
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false; // unset bound activity state
            // TODO: more?
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link UI components
        this.showPlayerActivityButton = findViewById(R.id.showPlayerButton);
        this.showPlayerActivityButton.setOnClickListener(l -> {
            // intent
            Intent i = new Intent(this, PlayerActivity.class);
            startActivity(i);
            // run intent
        });

        // add songs to app
        this.songs.add(new Song("Bag Talk", "Joey Purp", "QUARTERTHING",
                R.raw.joey_purp_bag_talk, R.drawable.quarterthing));
        this.songs.add(new Song("Les Jupes", "Charlotte Cardin",
                R.raw.charlotte_cardin_les_jupes, R.drawable.les_jupes));
        this.songs.add(new Song("Tangerine", "LaF", "Hôtel Délices",
                R.raw.laf_tangerine, R.drawable.tangerine));
        this.songs.add(new Song("Merry Go Round", "The Equatics", "Doin It!!!!",
                R.raw._the_equatics_merry_go_round, R.drawable.merry_go_round));

        this.recyclerView = findViewById(R.id.recyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        this.adapter = new RecyclerViewAdapter(this.songs); // init RecyclerViewAdapter
        this.recyclerView.setAdapter(this.adapter); // attach it to the RecyclerView

        this.adapter.SetOnSongItemClickListener((view, position) -> {
            Song selectedSongObject = this.songs.get(position); // get data for selected song
            Intent serviceHandoff = new Intent(this, PlayerService.class); // prepare to start player service
            serviceHandoff.putExtra("SONG_DATA_OBJECT", selectedSongObject); // pass song data to service

            if (this.isBound) {
                // TODO: change the song on the player service
                this.isBound = false;
            }
            else {
                bindService(serviceHandoff, this.connection, Context.BIND_AUTO_CREATE);
                this.isBound = true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.isBound) {
            unbindService(this.connection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}