package view;

import controller.LoginController;
import controller.RegisterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.User;
import util.GameFont;
import util.NeonButton;
import util.PixelTextField;
import util.PixelPasswordField;
import util.SoundManager;

public class StartScreen {
    private static final String bgUrl = "/tainguyen/pic/background.png";

    public static Scene create(double width, double height) {
        StackPane root = new StackPane();

        ImageView bg = null;
        if (bgUrl != null && !bgUrl.isBlank()) {
            bg = new ImageView(new Image(bgUrl, true));
            bg.setPreserveRatio(false);
            root.getChildren().add(bg);
        }

        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));
        card.setMinSize(320, 0);
        card.setPrefSize(380, 240);
        card.setMaxSize(420, 240);
        card.setStyle(
            "-fx-background-color: linear-gradient(#2b3137,#1e2328);"
          + "-fx-background-radius: 16;"
          + "-fx-border-color: #a7ff00;"
          + "-fx-border-width: 2;"
          + "-fx-border-radius: 16;"
        );

        VBox content = new VBox(12);
        content.setAlignment(Pos.CENTER);
        card.getChildren().setAll(content);

        root.getChildren().add(card);
        StackPane.setAlignment(card, Pos.CENTER);

        Scene scene = new Scene(root, width, height);
        if (bg != null) {
            bg.fitWidthProperty().bind(scene.widthProperty());
            bg.fitHeightProperty().bind(scene.heightProperty());
        }

        showHome(content);
        return scene;
    }

    private static void showHome(VBox content) {
        Label title = GameFont.makeTitle("TOWER DEFENCE", 28);
        NeonButton play = new NeonButton("PLAY");
        play.setPrefWidth(220);
        play.setOnAction(e -> showAuthMenu(content));
        content.getChildren().setAll(title, play);
    }

    private static void showAuthMenu(VBox content) {
        Label title = GameFont.makeTitle("PLEASE CHOSE", 26);
        NeonButton login = new NeonButton("LOG IN");
        login.setPrefWidth(220);
        NeonButton register = new NeonButton("REGISTER");
        register.setPrefWidth(180);

        login.setOnAction(e -> showLoginForm(content));
        register.setOnAction(e -> showRegisterForm(content));

        content.getChildren().setAll(title, login, register);  
    }

    private static void showLoginForm(VBox content) {
        Label title = GameFont.makeTitle("LOG IN YOUR ACC", 26);
        PixelTextField tfUser = new PixelTextField("Tên đăng nhập");
        PixelPasswordField pfPass = new PixelPasswordField("Mật khẩu");
        Label error = new Label();
        error.setTextFill(Color.web("#ff5252"));
        error.setVisible(false);

        NeonButton btn = new NeonButton("LOGIN");
        btn.setPrefWidth(220);

        btn.setOnAction(e -> {
            String u = tfUser.getText() == null ? "" : tfUser.getText().trim();
            String p = pfPass.getText() == null ? "" : pfPass.getText();
            if (u.isEmpty() || p.isEmpty()) {
                error.setText("Sai tên hoặc mật khẩu");
                error.setVisible(true);
                return;
            }
            User ok = new LoginController().HandleLogin(u, p);
            if (ok != null) {
                // TODO: chuyển sang GameView
                error.setVisible(false);
                content.getChildren().setAll(GameFont.makeTitle("LOADING...", 22));
                Stage stage = (Stage) content.getScene().getWindow();
                stage.setScene(GameLobbyView.create(1200, 675, ok));
                SoundManager.playLoopBgm("/tainguyen/music/musicloop.mp3");
            } else {
                error.setText("Sai tên hoặc mật khẩu");
                error.setVisible(true);
            }
        });

        content.getChildren().setAll(title, tfUser, pfPass, error, btn);
    }

    private static void showRegisterForm(VBox content) {
        Label title = GameFont.makeTitle("CREATE YOUR ACCOUNT", 26);
        PixelTextField tfUser = new PixelTextField("Tên đăng nhập");
        PixelPasswordField pfPass = new PixelPasswordField("Mật khẩu");
        Label note = new Label();
        note.setTextFill(Color.web("#7cff00"));
        note.setVisible(false);

        NeonButton btn = new NeonButton("REGISTER");
        btn.setPrefWidth(220);

        btn.setOnAction(e -> {
            String u = tfUser.getText() == null ? "" : tfUser.getText().trim();
            String p = pfPass.getText() == null ? "" : pfPass.getText();
            if (u.isEmpty() || p.isEmpty()) {
                note.setText("Vui lòng nhập đủ thông tin");
                note.setTextFill(Color.web("#ffcc00"));
                note.setVisible(true);
                return;
            }
            User ok = new RegisterController().HandleRegiser(u, p);
            if (ok != null) {
                note.setText("Tạo tài khoản thành công!");
                note.setTextFill(Color.web("#7cff00"));
                note.setVisible(true);
                // quay về menu đăng nhập/đăng ký sau 0.8s
                new Thread(() -> {
                    try { Thread.sleep(800); } catch (InterruptedException ignored) {}
                    javafx.application.Platform.runLater(() -> showAuthMenu(content));
                }).start();
            } else {
                note.setText("Tên đã tồn tại");
                note.setTextFill(Color.web("#ff5252"));
                note.setVisible(true);
            }
        });

        content.getChildren().setAll(title, tfUser, pfPass, note, btn);
    }
}

