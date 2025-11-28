package util;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.InputStream;

public final class GameFont {
    private static final String FONT_PATH = "/tainguyen/font/JungleAdventurer.ttf";
    private static Font base; // cached

    private GameFont() {}

    /** Load once; size here is only for initial load (JavaFX caches by family). */
    private static void ensureLoaded() {
        if (base != null) return;
        try (InputStream is = GameFont.class.getResourceAsStream(FONT_PATH)) {
            if (is == null) throw new IllegalStateException("Font not found: " + FONT_PATH);
            base = Font.loadFont(is, 12); // any size; we’ll derive later
        } catch (Exception e) {
            throw new RuntimeException("Failed to load font: " + FONT_PATH, e);
        }
    }

    /** Get the game font at a given size. */
    public static Font get(double size) {
        ensureLoaded();
        return Font.font(base.getFamily(), size);
    }

    /** Quick title label with “pixel gold” look + shadow like the sample. */
    public static Label makeTitle(String text, double size) {
        ensureLoaded();
        Label lb = new Label(text);
        lb.setFont(get(size));
        lb.setTextFill(Color.web("#FFD94A"));        // gold
        DropShadow dark = new DropShadow(6, Color.web("#2b214a")); // deep shadow
        DropShadow rim  = new DropShadow(2, Color.web("#FFE98A")); // light rim
        dark.setInput(rim);
        lb.setEffect(dark);
        return lb;
    }
}

