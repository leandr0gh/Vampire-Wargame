package game;

import javax.swing.*;
import java.awt.*;

public class CreateUserTab extends JPanel {

    private JTextField userField;
    private JPasswordField passwordField;
    private Image background;

    private void cleanFields() {
        userField.setText("");
        passwordField.setText("");
    }

    boolean[] isVisible = {false};

    public CreateUserTab(MainWindow window, PlayerManager jugador) {
        background = new ImageIcon(getClass().getResource("/resources/vampirewargamemainbg.png")).getImage();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel title = new JLabel("Crear Usuario");
        GothicUI.styleTitle(title, 36f);

        JLabel userLabel = new JLabel("Usuario:");
        GothicUI.styleLabel(userLabel);
        userField = new JTextField(10);
        GothicUI.styleTextField(userField);

        JLabel passwordLabel = new JLabel("<html>Contraseña:<br>(debe ser de exactamente 5 caracteres)</html>");
        GothicUI.styleLabel(passwordLabel);
        passwordField = new JPasswordField(10);
        GothicUI.styleTextField(passwordField);

        GothicButton createButton = new GothicButton("Crear");
        GothicButton returnButton = new GothicButton("Volver");
        GothicButton showhideButton = new GothicButton("Mostrar");
        char hiddenChar = passwordField.getEchoChar();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(userLabel, gbc);
        gbc.gridx = 1;
        add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);
        gbc.gridx = 2;
        add(showhideButton, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(createButton, gbc);
        gbc.gridy = 4;
        add(returnButton, gbc);

        createButton.addActionListener(e -> {
            String nombre = userField.getText();
            String password = new String(passwordField.getPassword());
            String status = jugador.createUser(nombre, password);
            if (status == null) {
                Jugador newPlayer = jugador.login(nombre, password);
                window.setCurrent(newPlayer);
                cleanFields();
                jugador.login(newPlayer.getUsername(), password);
                window.showWindow(MainWindow.mainMenu);
            } else {
                JOptionPane.showMessageDialog(this, status, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        returnButton.addActionListener(e -> window.showWindow(MainWindow.startMenu));

        showhideButton.addActionListener(e -> {
            if (isVisible[0]) {
                passwordField.setEchoChar(hiddenChar);
                showhideButton.setText("Mostrar");
                isVisible[0] = !isVisible[0];
            } else {
                passwordField.setEchoChar((char) 0);
                showhideButton.setText("Ocultar");
                isVisible[0] = !isVisible[0];
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}