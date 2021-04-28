package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

    private ArrayList<Song> songs = new ArrayList<>();
    private int index = 0;
    private TextView songTitle, artistName, albumTitle, startTimestamp, endTimestamp;
    private SeekBar timeSlider;
    private MediaPlayer player;
    private ImageView albumArt;
    private ImageButton prev, play, next;

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
                int songLen = player.getDuration(); // get duration of song in ms
                float op = (float) (progress * .01); // multiply seekbar prog pctg to decimal
                float newProg = (float) (songLen * op); // get requested pctg of song in ms
                player.seekTo((int) newProg); // seek to percentage of the song len
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

        });

        // add songs to app
        songs.add(new Song("Bag Talk", "Joey Purp", "QUARTERTHING",
                R.raw.joey_purp_bag_talk, R.drawable.quarterthing));
        songs.add(new Song("Les Jupes", "Charlotte Cardin",
                R.raw.charlotte_cardin_les_jupes, R.drawable.les_jupes));
        songs.add(new Song("Tangerine", "LaF", "Hôtel Délices",
                R.raw.laf_tangerine, R.drawable.tangerine));
        songs.add(new Song("Merry Go Round", "The Equatics", "Doin It!!!!",
                R.raw._the_equatics_merry_go_round, R.drawable.merry_go_round));

        // set up button logic
        // prev button
        prev = findViewById(R.id.prevButton);
        prev.setOnClickListener(l -> {
            // if the song is within the first second of playback
            if (player.getCurrentPosition() < 1000) {
                player.pause();
                cycleTrack(-1);
                player.start();
            } else {
                player.seekTo(0); // go to beginning of song
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
            if (player.isPlaying()) {
                play.setImageResource(android.R.drawable.ic_media_play);
                player.pause();
            } else {
                play.setImageResource(android.R.drawable.ic_media_pause);
                player.start();
            }
        });

        // next button
        next = findViewById(R.id.nextButton);
        next.setOnClickListener(v -> {
            player.pause();
            cycleTrack(1);
            player.start();
            this.updateButtons();
        });

        // create MediaPlayer
        player = MediaPlayer.create(this, this.getResourceUri(this.index));
        player.setOnCompletionListener(l -> {
            // should go to next song
            player.pause();
            cycleTrack(1);
            player.start();
        });
        displayTrack(songs.get(this.index));
        player.setVolume(1.0f, 1.0f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.start();
    }

    @Override
    protected void onDestroy() {
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
        super.onDestroy();
    }

    private void cycleTrack(int direction) {
        this.index += direction; // add index modifier
        // if mod reaches end of playlist indices
        if (this.index > songs.size() - 1) {
            this.index = 0; // set to first song
        } if (this.index < 0) {
            this.index = songs.size() - 1;
        }
        player = MediaPlayer.create(this, this.getResourceUri(this.index));
        player.setOnCompletionListener(l -> {
            // should go to next song
            player.pause();
            cycleTrack(1);
            player.start();
        });
        displayTrack(songs.get(this.index));
        player.setVolume(1.0f, 1.0f);
    }

    private void displayTrack(Song s) {
        albumArt.setImageResource(s.image);
        songTitle.setText(s.title);
        artistName.setText(s.artist);
        albumTitle.setText(s.album);
    }

    private Uri getResourceUri(int index) {
        int resourceId = songs.get(index).getResourceId();
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

    private void updateButtons() {
        if (player.isPlaying()) {
            play.setImageResource(android.R.drawable.ic_media_pause); // set to pause icon
        } else {
            play.setImageResource(android.R.drawable.ic_media_play); // set to play icon
        }
    }
}