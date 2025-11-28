import javafx.application.Application;
import javafx.stage.Stage;
import view.StartScreen;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Tower Defence");
        stage.setScene(StartScreen.create(1200, 675));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


