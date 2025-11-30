package util;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NeonButton extends Button {
    public NeonButton() { this("RESTART"); }
    public NeonButton(String text) {
        super(text);
        initSkin();
    }

    private void initSkin() {
        setFocusTraversable(false);
        setText(getText().toUpperCase());

        // size cơ bản
        setPrefWidth(140);
        setPrefHeight(40);

        setFont(Font.font("Consolas", FontWeight.NORMAL, 14));
        setTextFill(Color.WHITE);

        String baseStyle =
                "-fx-background-color: #1e1e1e;" +   // nền tối
                "-fx-border-color: steelblue;" +         // viền trắng
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" +
                "-fx-background-radius: 0;" +
                "-fx-padding: 4 12 4 12;";

        String hoverStyle =
                "-fx-background-color: #2a2a2a;" +
                "-fx-border-color: steelblue;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" +
                "-fx-background-radius: 0;" +
                "-fx-padding: 4 12 4 12;";

        String pressedStyle =
                "-fx-background-color: #d4f038ff;" +
                "-fx-border-color: steelblue;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" +
                "-fx-background-radius: 0;" +
                "-fx-padding: 4 12 4 12;";

        setStyle(baseStyle);

        setOnMouseEntered(e -> setStyle(hoverStyle));
        setOnMouseExited(e -> {
            setStyle(baseStyle);
            setTranslateY(0);
        });

        setOnMousePressed(e -> {
            setStyle(pressedStyle);
            setTranslateY(1);
        });

        setOnMouseReleased(e -> {
            setStyle(isHover() ? hoverStyle : baseStyle);
            setTranslateY(0);
        });
    }


}


// code của em huynh và chattgpt em xin lỗi cô vì em đéo biết code front end ttthees nên là phải gì phải chịu