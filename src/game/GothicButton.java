package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class GothicButton extends JButton {

    private boolean hovering = false;

    public GothicButton(String text) {
        super(text);
        setFont(FontLoader.getButtonFont(20f));
        setForeground(new Color(200, 170, 110)); // dorado apagado
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(220, 48));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color top = hovering ? new Color(70, 20, 20) : new Color(40, 15, 15);
        Color bottom = hovering ? new Color(35, 10, 10) : new Color(20, 8, 8);
        GradientPaint gradient = new GradientPaint(0, 0, top, 0, h, bottom);
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, w - 1, h - 1, 14, 14);

        g2.setColor(new Color(150, 120, 70));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(1, 1, w - 3, h - 3, 14, 14);

        g2.dispose();
        super.paintComponent(g);
    }
}