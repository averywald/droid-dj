package com.example.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<Song> songs;

    public RecyclerViewAdapter(ArrayList<Song> songs) {
        this.songs = songs;
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

    class SongViewHolder extends RecyclerView.ViewHolder {

        // UI Component elements
        ImageView albumArtImageView;
        TextView songNameTextView, artistTextView;

        public SongViewHolder(@NonNull final View itemView) {
            super(itemView);

            albumArtImageView = itemView.findViewById(R.id.albumArtImageView);
            songNameTextView = itemView.findViewById(R.id.songTextViewRecycler);
            artistTextView = itemView.findViewById(R.id.artistTextViewRecycler);

            // TODO: add onclick listener to play that song
            // TODO: add queue long-press menu thing

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (onItemClickListener != null) {
//                        onItemClickListener.onItemClick(itemView, getAdapterPosition());
//                    }
//                }
//            });
        }
    }
}
