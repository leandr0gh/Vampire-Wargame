package game;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.*;

public class LoginTab extends JPanel {

    private JTextField usernameField;
    private JPasswordField userPasswordField;
    private Image background;

    private void cleanFields() {
        usernameField.setText("");
        userPasswordField.setText("");
    }

    public LoginTab(MainWindow window, PlayerManager playerManager) {
        background = new ImageIcon(getClass().getResource("/resources/vampirewargamemainbg.png")).getImage();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel title = new JLabel("Log In");
        GothicUI.styleTitle(title, 36f);

        //usernaem
        JLabel usernameLabel = new JLabel("Nombre de Usuario: ");
        GothicUI.styleLabel(usernameLabel);
        usernameField = new JTextField(10);
        GothicUI.styleTextField(usernameField);

        //password
        JLabel passwordLabel = new JLabel("Contrasena: ");
        GothicUI.styleLabel(passwordLabel);
        userPasswordField = new JPasswordField(10);
        GothicUI.styleTextField(userPasswordField);
        char hiddenChar = userPasswordField.getEchoChar();
        boolean[] isVisible = {false};

        //morebuttons
        GothicButton showhideButton = new GothicButton("Mostrar");
        GothicButton enterButton = new GothicButton("Ingresar");
        GothicButton returnButton = new GothicButton("Volver");

        //gbc stuff
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(usernameLabel, gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(userPasswordField, gbc);
        gbc.gridx = 3;
        add(showhideButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(enterButton, gbc);
        gbc.gridx = 1;
        add(returnButton, gbc);

        //actionlisteners
        enterButton.addActionListener(e -> {

            String username = usernameField.getText();
            String password = new String(userPasswordField.getPassword());
            Jugador jugador = playerManager.login(username, password);
            if (jugador != null) {
                window.setCurrent(jugador);
                cleanFields();
                window.showWindow(MainWindow.mainMenu);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contrasena incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                cleanFields();
            }
        });

        userPasswordField.addActionListener(e -> enterButton.doClick());

        returnButton.addActionListener(e -> {
            cleanFields();
            window.showWindow(MainWindow.startMenu);
        });

        showhideButton.addActionListener(e -> {

            if (isVisible[0]) {
                userPasswordField.setEchoChar(hiddenChar);
                showhideButton.setText("Mostrar");
                isVisible[0] = !isVisible[0];
            } else {

                userPasswordField.setEchoChar((char) 0);
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
