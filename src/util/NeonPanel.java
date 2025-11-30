package util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class NeonPanel extends HBox {
    private final Label title;
    private final NeonButton button;

    public NeonPanel() { this("TITLE", "RESTART"); }

    public NeonPanel(String text, String buttonText) {
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(16);
        setPadding(new Insets(12, 18, 12, 18));
        setPrefHeight(72);

        setStyle(
            "-fx-background-color: linear-gradient(#2b3137,#1e2328);" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: #a7ff00;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 14;"
        );

        DropShadow glow = new DropShadow(20, Color.web("#7cff00"));
        glow.setSpread(0.22);
        InnerShadow inner = new InnerShadow(10, Color.web("#0b0d10"));
        inner.setInput(glow);
        setEffect(inner);

        title = new Label(text);
        title.setFont(GameFont.get(24));
        title.setTextFill(Color.web("#FFD94A"));

        button = new NeonButton(buttonText);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(title, spacer, button);
    }

    public Label getTitleNode() { return title; }
    public NeonButton getButton() { return button; }
    public void setTitle(String text) { title.setText(text); }
    public void setButtonText(String text) { button.setText(text); }
}

