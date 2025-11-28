package util;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class NeonButton extends Button {
    public NeonButton() { this("RESTART"); }
    public NeonButton(String text) {
        super(text);
        initSkin();
    }

    private void initSkin() {
        setMinSize(140, 50);
        setPrefSize(180, 56);
        setMaxHeight(60);

        SVGPath cut = new SVGPath();
        // bo góc kiểu "sci-fi panel"
        cut.setContent("M12,0 H168 L188,20 V36 L168,56 H12 L-8,36 V20 Z");
        setShape(cut);
        setPickOnBounds(false);

        setTextFill(Color.web("#E6FF00"));
        setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Segoe UI', 'Arial';" +
            // nền tối nhiều lớp
            "-fx-background-color:" +
            " linear-gradient(#2b3137,#1e2328)," +           /* lớp trong cùng */
            " linear-gradient(#1a1f24,#111418)," +           /* lớp giữa */
            " linear-gradient(#3a424a,#2a3036);" +           /* lớp ngoài cùng */
            ";" +
            "-fx-background-insets: 3 3 3 3, 2, 0;" +
            "-fx-background-radius: 12;" +
            // viền neon vàng
            "-fx-border-color: #a7ff00;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;" +
            // chữ cách mép
            "-fx-padding: 10 26 10 26;" +
            // khoảng chữ dạng “tech”
            "-fx-letter-spacing: 1.2px;"
        );

        DropShadow glow = new DropShadow(18, Color.web("#7cff00"));
        glow.setSpread(0.25);
        InnerShadow inner = new InnerShadow(12, Color.web("#0b0d10"));
        inner.setChoke(0.5);
        inner.setInput(glow);
        setEffect(inner);

        setOnMouseEntered(e -> {
            setTextFill(Color.web("#FFFF66"));
            setStyle(getStyle().replace("#a7ff00", "#e4ff38"));
        });
        setOnMouseExited(e -> {
            setTextFill(Color.web("#E6FF00"));
            setStyle(getStyle().replace("#e4ff38", "#a7ff00"));
        });
        setOnMousePressed(e -> setOpacity(0.88));
        setOnMouseReleased(e -> setOpacity(1.0));
    }
}


// code của em huynh và chattgpt em xin lỗi cô vì em đéo biết code front end ttthees nên là phải gì phải chịu