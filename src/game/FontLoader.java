package game;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

public final class FontLoader {

    private static Font titleFont;
    private static Font buttonFont;

    private FontLoader() {
    }

    public static Font getTitleFont(float size) {
        if (titleFont == null) {
            titleFont = loadFont("/resources/UnifrakturMaguntia-Book.ttf");
        }
        return titleFont.deriveFont(size);
    }

    public static Font getButtonFont(float size) {
        if (buttonFont == null) {
            buttonFont = loadFont("/resources/MedievalSharp.ttf");
        }
        return buttonFont.deriveFont(size);
    }

    private static Font loadFont(String path) {
        try (InputStream is = FontLoader.class.getResourceAsStream(path)) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Serif", Font.BOLD, 24);
        }
    }
}