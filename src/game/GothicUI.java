package game;

import javax.swing.*;
import java.awt.*;

public final class GothicUI {

    public static final Color GOLD = new Color(200, 170, 110);
    public static final Color PARCHMENT = new Color(230, 210, 180);
    public static final Color DARK_INPUT_BG = new Color(30, 20, 15);
    public static final Color BORDER_COLOR = new Color(150, 120, 70);

    private GothicUI() {
    }

    public static void styleLabel(JLabel label) {
        label.setFont(FontLoader.getButtonFont(16f));
        label.setForeground(GOLD);
    }

    public static void styleTitle(JLabel label, float size) {
        label.setFont(FontLoader.getTitleFont(size));
        label.setForeground(GOLD);
    }

    public static void styleTextField(JTextField field) {
        field.setFont(FontLoader.getButtonFont(16f));
        field.setBackground(DARK_INPUT_BG);
        field.setForeground(PARCHMENT);
        field.setCaretColor(PARCHMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }
}