package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    public interface OnClick {
        void OnClick(View v, int i);
    }

    class SongViewHolder extends RecyclerView.ViewHolder {

        // UI Component elements
        ImageView albumArtImageView;
        TextView songNameTextView, artistTextView;

        public SongViewHolder(@NonNull final View itemView) {
            super(itemView);

            albumArtImageView = itemView.findViewById(R.id.albumArtImageView);
            songNameTextView = itemView.findViewById(R.id.songTextViewRecycler);
            artistTextView = itemView.findViewById(R.id.artistTextViewRecycler);

            itemView.setOnClickListener(v -> {
                if (songItemClickListener != null) {
                    songItemClickListener.OnClick(itemView, getAdapterPosition());
                }
            });
        }

    }

    private ArrayList<Song> songs;
    private OnClick songItemClickListener;

    public RecyclerViewAdapter(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public OnClick getSongItemClickListener() { return this.songItemClickListener; }

    public void SetOnSongItemClickListener(OnClick songItemClickListener) {
        this.songItemClickListener = songItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.song_item, parent, false);
        SongViewHolder myViewHolder = new SongViewHolder(itemView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final SongViewHolder songViewHolder = (SongViewHolder) holder; // cast ViewHolder to custom type
        Song s = songs.get(position);
        // add song info to the UI
        songViewHolder.artistTextView.setText(s.artist);
        songViewHolder.albumArtImageView.setImageResource(s.image);
        songViewHolder.songNameTextView.setText(s.title);
    }

    @Override
    public int getItemCount() {
        return this.songs.size();
    }

}
