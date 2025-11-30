package util;

import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/** Retro/pixel input like the sample image. */
public class PixelTextField extends TextField {
    public PixelTextField() { this(""); }
    public PixelTextField(String prompt) {
        super();
        setPromptText(prompt);
        initLook();
    }
    protected void initLook() {
        setPrefWidth(260);
        setMinHeight(34);
        setStyle(
            "-fx-background-color: white;" +          // fill
            "-fx-text-fill: #222;" +
            "-fx-background-radius: 0;" +
            "-fx-border-color: black;" +              // thick black outline
            "-fx-border-width: 4;" +
            "-fx-border-radius: 0;" +
            "-fx-padding: 6 10 6 10;" +
            "-fx-font-size: 16px;"
        );
        DropShadow pixelShadow = new DropShadow();
        pixelShadow.setOffsetX(4);
        pixelShadow.setOffsetY(4);
        pixelShadow.setColor(Color.BLACK);
        pixelShadow.setRadius(0);  // crisp, no blur
        setEffect(pixelShadow);

        focusedProperty().addListener((obs, a, b) -> {
            if (b) setStyle(getStyle().replace("black;", "#111;"));
            else   setStyle(getStyle().replace("#111;", "black;"));
        });
    }

    /** Optional helper to highlight error state. */
    public void setError(boolean on) {
        if (on) setStyle(getStyle().replace("black;", "#d00000;"));
        else    setStyle(getStyle().replace("#d00000;", "black;"));
    }
}

/** Password variant with the same look. */

