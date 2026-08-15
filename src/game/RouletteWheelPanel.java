package game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class RouletteWheelPanel extends JPanel {

    private static final Roulette.RouletteResult[] SECTOR_ORDER = {
        Roulette.RouletteResult.WEREWOLF, // arriba
        Roulette.RouletteResult.VAMPIRE, // arriba-derecha
        Roulette.RouletteResult.NECROMANCER, // abajo-derecha
        Roulette.RouletteResult.WEREWOLF, // abajo
        Roulette.RouletteResult.VAMPIRE, // abajo-izquierda
        Roulette.RouletteResult.NECROMANCER // arriba-izquierda
    };

    private static final double FRAME_ORIG_W = 451;
    private static final double FRAME_ORIG_H = 627;
    private static final double HOLE_CX = 225;
    private static final double HOLE_CY = 310;
    private static final double HOLE_DIAM = 430;

    private int frameDisplayHeight = 300;
    private BufferedImage wheelImage;
    private BufferedImage frameImage;
    private BufferedImage hubImage;
    private double currentAngle = 0;
    private Timer animTimer;
    boolean decelerating = false;
    private double continuousSpeed = 14;
    private long decelStartTime;
    private double decelStartAngle;
    private double decelDistance;
    private double decelV0;
    private long decelDurationMs;
    private Runnable onSpinFinished;

    public RouletteWheelPanel() {
        try {
            hubImage = ImageIO.read(getClass().getResource("/resources/wheel_hub.png"));
            wheelImage = ImageIO.read(getClass().getResource("/resources/wheel_ring.png"));
            frameImage = ImageIO.read(getClass().getResource("/resources/roulette_frame.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int panelW = (int) (FRAME_ORIG_W / FRAME_ORIG_H * frameDisplayHeight) + 20;
        int panelH = frameDisplayHeight + 20;
        setPreferredSize(new Dimension(220, 260));
        setOpaque(false);
    }

    public void startContinuousSpin() {
        if (animTimer != null && animTimer.isRunning() && !decelerating) return;
        decelerating = false;
        if (animTimer != null) animTimer.stop();
        
        animTimer = new Timer(16, e -> {
        if (!decelerating) {
            currentAngle = (currentAngle + continuousSpeed) % 360;
            repaint();
        }
    });
        animTimer.start();
    }
    
    public void stopSpin(Roulette.RouletteResult result, Runnable onFinished) {
    if (animTimer == null || !animTimer.isRunning() || decelerating) return;

    int sectorIndex = 0;
    for (int i = 0; i < SECTOR_ORDER.length; i++) {
        if (SECTOR_ORDER[i] == result) { sectorIndex = i; break; }
    }
    double sectorAngle = sectorIndex * 60.0;
    double requiredDisplayAngle = (360 - sectorAngle) % 360;

    int extraFullSpins = 1;
    double currentMod = currentAngle % 360;
    double deltaToTarget = requiredDisplayAngle - currentMod;
    while (deltaToTarget < 0) deltaToTarget += 360;
    double distance = extraFullSpins * 360 + deltaToTarget;
    
    double v0 = continuousSpeed / 16.0;
    double duration = 2 * distance / v0;
            
    decelStartAngle = currentAngle;
    decelDistance = distance;
    decelV0 = v0;
    decelDurationMs = (long) duration;
    decelStartTime = System.currentTimeMillis();
    onSpinFinished = onFinished;
    decelerating = true;

    animTimer.stop();
    animTimer = new Timer(16, e -> animateDecelStep());
    animTimer.start();
}

    private void animateDecelStep() {
        long elapsed = System.currentTimeMillis() - decelStartTime;
        double t = Math.min(elapsed, decelDurationMs);
        double traveled = decelV0 * t * (1 - t / (2.0 * decelDurationMs));
        

        currentAngle = decelStartAngle +  traveled;
        repaint();

        if (elapsed >= decelDurationMs) {
            animTimer.stop();
            currentAngle = (decelStartAngle + decelDistance) % 360;
            decelerating = false;
            repaint();
            if (onSpinFinished != null) {
                onSpinFinished.run();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (frameImage == null || wheelImage == null) return; {
            
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        double frameScale = frameDisplayHeight / FRAME_ORIG_H;
        double frameW = FRAME_ORIG_W * frameScale;
        double frameH = FRAME_ORIG_H * frameScale;
        double frameX = (getWidth() - frameW) / 2.0;
        double frameY = (getHeight() - frameH) / 2.0;
        
        double holeCenterX = frameX + HOLE_CX * frameScale;
        double holeCenterY = frameY + HOLE_CY * frameScale;
        double holeDiam = HOLE_DIAM * frameScale;
        
        double wheelOrigDiam = (wheelImage.getWidth() + wheelImage.getHeight()) / 2.0;
        double wheelDiam = holeDiam * 0.92;
        double wheelScale = wheelDiam / wheelOrigDiam;
        
        AffineTransform old = g2.getTransform();
        g2.translate(holeCenterX, holeCenterY);
        g2.rotate(Math.toRadians(currentAngle));
        g2.scale(wheelScale, wheelScale);
        g2.drawImage(wheelImage, -wheelImage.getWidth() / 2, -wheelImage.getHeight() / 2, null);
        g2.setTransform(old);
        
        if (hubImage != null) {
            int hubSize = (int) (hubImage.getWidth()*wheelScale);
            g2.drawImage(hubImage, (int) holeCenterX - hubSize / 2, (int) holeCenterY - hubSize / 2, hubSize, hubSize, null);
        }
        
        g2.translate(frameX, frameY);
        g2.scale(frameScale, frameScale);
        g2.drawImage(frameImage, 0, 0, null);
        g2.setTransform(old);
        
        g2.dispose();

    }
}
