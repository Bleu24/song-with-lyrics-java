package com.example;

public class Song {
    private String title;
    private String lyrics;

    public Song(String title, String lyrics) {
        this.title = title;
        this.lyrics = lyrics;
    }

    public String getLyrics() {
        return lyrics;  // Getter for lyrics
    }

    @Override
    public String toString() {
        return title;  // Returns the title for JComboBox display
    }
}
