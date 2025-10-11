package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

import model.Song;

public class SearchController {
    private static final String cliient_id = "3ce5b246";

    public List<Song> searchSong(String keyword){
        List<Song> songs = new ArrayList<>();

        try{
            String apiUrl = "https://api.jamendo.com/v3.0/tracks/?client_id="+ cliient_id + "&format=json&limit=10&search=" + keyword;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder rs = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                rs.append(line);
            }
            rd.close();

            JSONObject oj = new JSONObject(rs.toString());
            JSONArray jsonArray = oj.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject songJson = jsonArray.getJSONObject(i);

                String songid = songJson.getString("id");
                String title = songJson.getString("name");
                String artist = songJson.getString("artist_name");
                String songURL = songJson.getString("audiodownload");
                String imgUrl = songJson.getString("album_image");

                Song song = new Song(songid, title, artist, songURL, imgUrl);
                songs.add(song);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return songs;
    }
}
