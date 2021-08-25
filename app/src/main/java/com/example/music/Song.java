package com.example.music;

import java.io.Serializable;

public class Song implements Serializable {
    String Name, Singer;
    int data,imgSong;

    public int getImgSong() {
        return imgSong;
    }

    public Song(String name, String singer, int data, int imgSong) {
        Name = name;
        Singer = singer;
        this.data = data;
        this.imgSong = imgSong;
    }

    public void setImgSong(int imgSong) {
        this.imgSong = imgSong;
    }

    public Song(String name, String singer, int data) {
        Name = name;
        Singer = singer;
        this.data = data;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
