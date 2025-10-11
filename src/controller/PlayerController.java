package controller;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import model.Song;

public class PlayerController {
    private volatile Player player;
    private volatile Thread playThread;
    private volatile Song current;

    public synchronized void setPlaySong(Song song) {
        stop();             
        this.current = song;
        System.out.println("[Player] set track: " + (song == null ? "null" : song.getTitle()));
    }

    public synchronized void play() {
        if (current == null) {
            System.out.println("[Player] No song selected.");
            return;
        }
        stop();

        playThread = new Thread(() -> {
            HttpURLConnection conn = null;
            InputStream in = null;
            try {
                String rawUrl = current.getURL(); // audiodownload deo phai audio
                System.out.println("[Player] Opening: " + rawUrl);

                URL url = new URL(rawUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setInstanceFollowRedirects(true);
                conn.setRequestProperty("User-Agent", 
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124 Safari/537.36");
                conn.setRequestProperty("Accept", "audio/mpeg,*/*;q=0.9");

                int code = conn.getResponseCode();
                String ctype = conn.getContentType();
                System.out.println("[HTTP] code=" + code + ", content-type=" + ctype);
                if (code >= 300 && code < 400) {
                    System.out.println("[HTTP] Redirect to: " + conn.getHeaderField("Location"));
                }
                if (code >= 400) {
                    // đọc error stream
                    InputStream err = conn.getErrorStream();
                    System.out.println("[HTTP] Error response: " + (err != null ? "(non-empty)" : "(null)"));
                    throw new RuntimeException("HTTP error " + code);
                }

                in = new BufferedInputStream(conn.getInputStream());
                player = new Player(in);
                System.out.println("[Player] Start playing...");
                player.play();         
                System.out.println("[Player] Finished.");
            } catch (Exception ex) {
                System.out.println("[Player] ERROR: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                try { if (in != null) in.close(); } catch (Exception ignore) {}
                if (conn != null) conn.disconnect();
                synchronized (PlayerController.this) {
                    player = null;
                    playThread = null;
                }
            }
        }, "MP3-Player-Thread");

        playThread.setDaemon(true);
        playThread.start();
    }

    public synchronized void stop() {
        if (player != null) {
            try {
                System.out.println("[Player] Stop/Close current player.");
                player.close();
            } catch (Exception ignore) {}
            player = null;
        }
        if (playThread != null) {
            try { playThread.interrupt(); } catch (Exception ignore) {}
            playThread = null;
        }
    }

}

