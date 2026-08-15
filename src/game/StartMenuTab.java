package game;

import javax.swing.*;
import java.awt.*;

public class StartMenuTab extends JPanel {
    
    private Image background;

    public StartMenuTab(MainWindow window) {
        
        background = new ImageIcon(getClass().getResource("/resources/vampirewargamemainbg.png")).getImage();
  
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel title = new JLabel("Vampire Wargame");
        title.setFont(FontLoader.getTitleFont(64f));
        title.setForeground(new Color(200, 170, 110));

        //botonessssssssdcndijzkm
        GothicButton loginButton = new GothicButton("Iniciar Sesión");
        GothicButton createUserButton = new GothicButton("Crear Usuario");
        GothicButton exitButton = new GothicButton("Salir");
        
        gbc.gridx=0;
        gbc.gridy=0;
        add(title, gbc);
        
        gbc.gridy=1;
        add(loginButton, gbc);
        gbc.gridy=2;
        add(createUserButton, gbc);
        gbc.gridy=3;
        add(exitButton, gbc);
        
        loginButton.addActionListener(e -> window.showWindow(MainWindow.login));
        createUserButton.addActionListener(e -> window.showWindow(MainWindow.crearJugador));
        exitButton.addActionListener(e -> System.exit(0));    
    }
    
   
    protected void paintComponent(Graphics g){
    super.paintComponent(g);
    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

}
