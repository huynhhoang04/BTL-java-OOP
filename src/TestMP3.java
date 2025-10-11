import javazoom.jl.player.Player;
import java.io.BufferedInputStream;
import java.net.URL;

public class TestMP3 {
    public static void main(String[] args) {
        try {
            String url = "https://prod-1.storage.jamendo.com/?trackid=113592&format=mp31";
            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
            Player p = new Player(in);
            System.out.println("Playing...");
            p.play();
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

