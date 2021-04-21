package com.example.musicplayer;

public class Song {

    public String title;
    public String artist;
    public String album;
    protected int image;
    private int resourceId;

    public Song(String name, String artist, int trackResource, int imageResource) {
        this.title = name;
        this.artist = artist;
        this.album = name + " - Single"; // if no album, format as single
        this.resourceId = trackResource;
        this.image = imageResource;
    }

    public Song(String name, String artist, String album, int rscId, int imgId) {
        this.title = name;
        this.artist = artist;
        this.album = album;
        this.resourceId = rscId;
        this.image = imgId;
    }

    protected int getResourceId() {
        return this.resourceId;
    }

}
