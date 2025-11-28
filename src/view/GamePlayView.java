package view;

import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.User;
import util.GameFont;
import util.NeonButton;
import util.NeonPanel;

public class GamePlayView extends StackPane {
    public static final double WIDTH = 1280;
    public static final double HEIGHT = 720;

    private final User user;
    private final Runnable onReturn;

    public GamePlayView(User user) {
        this(user, null);
    }

    public GamePlayView(User user, Runnable onReturn) {
        this.user = user;
        this.onReturn = onReturn;

        setPrefSize(WIDTH, HEIGHT);
        setMinSize(WIDTH, HEIGHT);
        setMaxSize(WIDTH, HEIGHT);

        final String BG_PATH = "/tainguyen/pic/map/map1.png";

        java.net.URL url = GamePlayView.class.getResource(BG_PATH);
        if (url == null) {
            System.err.println("BG NOT FOUND on classpath: " + BG_PATH);
        } else {
            javafx.scene.image.ImageView bg =
                new javafx.scene.image.ImageView(new javafx.scene.image.Image(url.toExternalForm()));
            bg.setFitWidth(WIDTH);
            bg.setFitHeight(HEIGHT);
            bg.setPreserveRatio(false);
            getChildren().add(0, bg); // nền ở dưới cùng
        }

        StackPane card = new StackPane();
        card.setPadding(new Insets(32, 48, 40, 48));
        card.setMaxWidth(560);
        card.setMinWidth(560);

        VBox box = new VBox(22);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(18));
        box.setMaxSize(900, 400);
        box.setStyle(
            "-fx-background-color: linear-gradient(#2b3137,#1e2328);" +
            "-fx-background-radius: 16; -fx-border-radius: 16;" +
            "-fx-border-color: #a7ff00; -fx-border-width: 2;"
        );

        Label title = GameFont.makeTitle("LEVEL " + Math.max(1, user.getBaseLevel()), 26);

        NeonButton btnPlay = new NeonButton("Play");
        btnPlay.setOnAction(e -> {
            System.out.println("Start gameplay at level " + user.getBaseLevel()
                    + " for user " + user.getUsername());
            // TODO: khởi động gameplay thật
        });

        NeonButton btnReturn = new NeonButton("Return");
        btnReturn.setOnAction(e -> {
            if (onReturn != null) onReturn.run();
        });

        box.getChildren().addAll(title, btnPlay, btnReturn);
        card.getChildren().add(box);

        StackPane.setAlignment(card, Pos.CENTER);
        getChildren().add(card);
    }

    public Scene createScene() {
        return new Scene(this, WIDTH, HEIGHT);
    }
}
