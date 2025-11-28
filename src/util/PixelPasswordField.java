package util;

import javafx.scene.control.PasswordField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class PixelPasswordField extends PasswordField {
    public PixelPasswordField() { this(""); }
    public PixelPasswordField(String prompt) {
        super();
        setPromptText(prompt);
        initLook();
    }
    private void initLook() {
        setPrefWidth(260);
        setMinHeight(34);
        setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: #222;" +
            "-fx-background-radius: 0;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 4;" +
            "-fx-border-radius: 0;" +
            "-fx-padding: 6 10 6 10;" +
            "-fx-font-size: 16px;"
        );
        DropShadow pixelShadow = new DropShadow();
        pixelShadow.setOffsetX(4);
        pixelShadow.setOffsetY(4);
        pixelShadow.setColor(Color.BLACK);
        pixelShadow.setRadius(0);
        setEffect(pixelShadow);

        focusedProperty().addListener((o, a, b) -> {
            if (b) setStyle(getStyle().replace("black;", "#111;"));
            else   setStyle(getStyle().replace("#111;", "black;"));
        });
    }

    public void setError(boolean on) {
        if (on) setStyle(getStyle().replace("black;", "#d00000;"));
        else    setStyle(getStyle().replace("#d00000;", "black;"));
    }
}