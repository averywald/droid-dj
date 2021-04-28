package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<Song> songs = new ArrayList<Song>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        // add songs to app
        this.songs.add(new Song("Bag Talk", "Joey Purp", "QUARTERTHING",
                R.raw.joey_purp_bag_talk, R.drawable.quarterthing));
        this.songs.add(new Song("Les Jupes", "Charlotte Cardin",
                R.raw.charlotte_cardin_les_jupes, R.drawable.les_jupes));
        this.songs.add(new Song("Tangerine", "LaF", "Hôtel Délices",
                R.raw.laf_tangerine, R.drawable.tangerine));
        this.songs.add(new Song("Merry Go Round", "The Equatics", "Doin It!!!!",
                R.raw._the_equatics_merry_go_round, R.drawable.merry_go_round));

        this.adapter = new RecyclerViewAdapter(this.songs); // init RecyclerViewAdapter
        this.recyclerView.setAdapter(this.adapter); // attach it to the RecyclerView

        /*
        TODO: this.adapter needs an onclick listener to start the player activity?
            play the song without bringing up the player activity?
        */

//        this.testButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(com.example.musicplayer.MainActivity.this,
//                        PlayerActivity.class);
//                startActivity(i);
//            }
//        });
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
    protected void onDestroy() {
        super.onDestroy();
    }

}