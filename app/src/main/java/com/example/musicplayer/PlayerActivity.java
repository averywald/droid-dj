package com.example.musicplayer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    // TODO: add volume bar
    // TODO: add timestamp - update loop?

    private TextView songTitle, artistName, albumTitle, startTimestamp, endTimestamp;
    private SeekBar timeSlider;
    private ImageView albumArt;
    private ImageButton prev, play, next;

    private PlayerService playerService; // TODO: implement
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
        setContentView(R.layout.activity_player);

        // link UI elements
        startTimestamp = findViewById(R.id.startTimestamp);
        endTimestamp = findViewById(R.id.endTimeStamp);
        songTitle = findViewById(R.id.titleTextView);
        albumTitle = findViewById(R.id.albumTextView);
        artistName = findViewById(R.id.artistTextView);
        albumArt = findViewById(R.id.songImageView);

        timeSlider = findViewById(R.id.seekBar2);
        timeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int songLen = playerService.getDuration(); // get duration of song in ms
                float op = (float) (progress * .01); // multiply seekbar prog pctg to decimal
                float newProg = (float) (songLen * op); // get requested pctg of song in ms
                playerService.seekTo((int) newProg); // seek to percentage of the song len
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        // set up button logic
        // prev button
        prev = findViewById(R.id.prevButton);
        prev.setOnClickListener(l -> {
            // if the song is within the first second of playback
            if (playerService.getCurrentPosition() < 1000) {
                playerService.prev();
            } else {
                playerService.restart();
            }
            this.updateButtons();
        });
        prev.setOnLongClickListener(l -> {
            Toast toast = Toast.makeText(this, "double tap to go back",
                    Toast.LENGTH_LONG);
            toast.show();
            return false;
        });

        // play / pause button
        play = findViewById(R.id.playPauseButton);
        play.setImageResource(android.R.drawable.ic_media_pause);
        play.setOnClickListener(v -> {
            if ((this.playerService.isPlaying())) {
                playerService.pause();
            } else {
                playerService.play();
            }
            this.updateButtons();
        });

        // next button
        next = findViewById(R.id.nextButton);
        next.setOnClickListener(v -> {
            this.playerService.next();
            this.updateButtons();
        });

//        displayTrack(songs.get(this.index));
    }

    @Override
    protected void onPause() {
        super.onPause();
//        player.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        player.start();
    }

    @Override
    protected void onDestroy() {
//        if (playerService.isPlaying()) {
//            player.stop();
//        }
//        player.release();
        super.onDestroy();
    }

    private void displayTrack(Song s) {
        albumArt.setImageResource(s.image);
        songTitle.setText(s.title);
        artistName.setText(s.artist);
        albumTitle.setText(s.album);
    }

    private void updateButtons() {
        if (playerService.isPlaying()) {
            play.setImageResource(android.R.drawable.ic_media_pause); // set to pause icon
        } else {
            play.setImageResource(android.R.drawable.ic_media_play); // set to play icon
        }
    }
}