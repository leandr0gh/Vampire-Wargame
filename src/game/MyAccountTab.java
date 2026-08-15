package game;

import javax.swing.*;
import java.awt.*;

public class MyAccountTab extends JPanel {

    private Image background;

    public MyAccountTab(MainWindow window, PlayerManager jugador) {
        background = new ImageIcon(getClass().getResource("/resources/vampirewargamemainbg.png")).getImage();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        Jugador current = window.getCurrent();

        JLabel title = new JLabel("Mi Cuenta");
        GothicUI.styleTitle(title, 36f);

        JLabel usernameLabel = new JLabel("Usuario: " + current.getUsername());
        GothicUI.styleLabel(usernameLabel);
        JLabel pointsLabel = new JLabel("Puntos: " + current.getPuntos());
        GothicUI.styleLabel(pointsLabel);
        JLabel registerDate = new JLabel("Fecha de Ingreso: " + current.getFechaIngreso());
        GothicUI.styleLabel(registerDate);

        GothicButton changePasswordButton = new GothicButton("Cambiar Contraseña");
        GothicButton deleteAccountButton = new GothicButton("Cerrar mi Cuenta");
        GothicButton returnButton = new GothicButton("Volver");

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);
        gbc.gridy = 1;
        add(usernameLabel, gbc);
        gbc.gridy = 2;
        add(pointsLabel, gbc);
        gbc.gridy = 3;
        add(registerDate, gbc);
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        add(changePasswordButton, gbc);
        gbc.gridy = 5;
        add(deleteAccountButton, gbc);
        gbc.gridy = 6;
        add(returnButton, gbc);

        changePasswordButton.addActionListener(e -> {
            String currentPassword = JOptionPane.showInputDialog(this, "Ingresa la contrasena actual", "Cambiar Contrasena", JOptionPane.PLAIN_MESSAGE);
            if (currentPassword == null) {
                return;
            }
            if (currentPassword.equals(current.getPassword())) {
                String newPassword = JOptionPane.showInputDialog(this, "Ingresa la nueva contasena", "Cambiar Contrasena", JOptionPane.PLAIN_MESSAGE);
                if (newPassword != null && !newPassword.trim().isEmpty()) {
                    current.setPassword(newPassword);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Contrasena actual no es correcta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteAccountButton.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this, "Estas seguro/a que quieres borrar tu cuenta? Esta accion es irreversible.", "Confirmacion",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                String password = JOptionPane.showInputDialog(this, "Ingrese su contrasena actual para confirmar", "Confirmacion", JOptionPane.PLAIN_MESSAGE);
                if (password == null) {
                    return;
                }
                boolean deleted = jugador.deleteUser(current.getUsername(), password);
                if (deleted) {
                    window.showWindow(MainWindow.startMenu);
                } else {
                    JOptionPane.showMessageDialog(this, "Contrasena incorrecta. No hubieron cambios");
                }
            } else {
                return;
            }
        });

        returnButton.addActionListener(e -> window.showWindow(MainWindow.mainMenu));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}