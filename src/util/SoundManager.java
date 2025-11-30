
// src/util/SoundManager.java
package util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public final class SoundManager {
    private static MediaPlayer bgm;
    private static boolean muted = false;

    private SoundManager() {}

    public static void playLoopBgm(String resourcePath) {
        stopBgm();
        Media media = new Media(SoundManager.class.getResource(resourcePath).toExternalForm());
        bgm = new MediaPlayer(media);
        bgm.setCycleCount(MediaPlayer.INDEFINITE);
        bgm.setStartTime(Duration.ZERO);
        bgm.setMute(muted);
        bgm.play();
    }
    public static void stopBgm() {
        if (bgm != null) { bgm.stop(); bgm.dispose(); bgm = null; }
    }

    public static void toggleMute() {
        muted = !muted;
        if (bgm != null) bgm.setMute(muted);
    }

    public static boolean isMuted() { return muted; }
}
