package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainMenuTab extends JPanel {

    private Image background;

    public MainMenuTab(MainWindow window, PlayerManager jugador) {
        background = new ImageIcon(getClass().getResource("/resources/vampirewargamemainbg.png")).getImage();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel title = new JLabel("Menú Principal");
        GothicUI.styleTitle(title, 40f);

        GothicButton playButton = new GothicButton("JUGAR");
        GothicButton myAccountButton = new GothicButton("Mi Cuenta");
        GothicButton reportsButton = new GothicButton("Reportes");
        GothicButton logOffButton = new GothicButton("Cerrar Sesión");

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);
        gbc.gridy = 1;
        add(playButton, gbc);

        gbc.gridy = 2;
        add(myAccountButton, gbc);
        gbc.gridy = 3;
        add(reportsButton, gbc);
        gbc.gridy = 4;
        add(logOffButton, gbc);

        playButton.addActionListener(e -> {
            ArrayList<Jugador> otros = jugador.getOtherPlayers(window.getCurrent().getUsername());
            if (otros.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay otros jugadores registrados");
                return;
            }
            String[] nombres = new String[otros.size()];
            for (int i = 0; i < otros.size(); i++) {
                nombres[i] = otros.get(i).getUsername();
            }
            String seleccionado = (String) JOptionPane.showInputDialog(this, "Selecciona tu oponente: ",
                    "Nueva Partida", JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);

            if (seleccionado == null) {
                return;
            }

            Jugador oponente = null;
            for (int i = 0; i < otros.size(); i++) {
                if (otros.get(i).getUsername().equals(seleccionado)) {
                    oponente = otros.get(i);
                    break;
                }
            }

            Partida partida = new Partida(window.getCurrent(), oponente, jugador);
            window.setPartidaActual(partida);
            window.mostrarNuevaPartida();
        });
        myAccountButton.addActionListener(e -> window.showMyAccount());
        reportsButton.addActionListener(e -> window.mostrarReportes());
        logOffButton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(this, "Seguro que quieres cerrar sesion?", "Confirmacion", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                window.showWindow(MainWindow.login);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
