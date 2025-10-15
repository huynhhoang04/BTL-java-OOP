package model;

public class Song {
    private String songid;
    private String title;
    private String artist;
    private String songURL;
    private String imgUrl;

    public Song(String songid, String title, String artist, String songURL, String imgUrl){
        this.songid = songid;
        this.title = title;
        this.artist = artist;
        this.songURL =songURL;
        this.imgUrl = imgUrl;
    }

    public String getSongID(){
        return songid;
    }

    public String getTitle(){
        return title;
    }

    public String getArtist(){
        return artist;
    }

    public String getURL(){
        return songURL;
    }

    public String getImgUrl(){
        return imgUrl;
    }

    @Override
    public String toString(){
        return title+ " " + artist;
    }
}
