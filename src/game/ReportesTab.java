
package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class ReportesTab extends JPanel {
    private Image background;

    public ReportesTab(MainWindow window, PlayerManager playerManager) {
        background = new ImageIcon(getClass().getResource("/resources/vampirewargamemainbg.png")).getImage();

        setLayout(new BorderLayout());
        setBackground(new Color(30,30,30));
        JLabel title = new JLabel("Reportes", SwingConstants.CENTER);
        GothicUI.styleTitle(title, 32f);
        add(title, BorderLayout.NORTH);
        
        GothicButton returnButton = new GothicButton("Volver");
        returnButton.addActionListener(e -> window.showWindow(MainWindow.mainMenu));
        
        //new stuff
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(FontLoader.getButtonFont(16f));
        tabs.addTab("Ranking", buildRankingPanel(playerManager));
        tabs.addTab("Mi historial", buildHistorialPanel(window, playerManager));
        tabs.setBackground(Color.GRAY);
        add(tabs, BorderLayout.CENTER);
        add(returnButton, BorderLayout.SOUTH);
        
    }
    
    private JPanel buildRankingPanel(PlayerManager playerManager){
    ArrayList<Jugador> ranking = playerManager.getRankingOrdenado();
    
    String[] columnas = {"Posicion", "Usuario", "Puntos"};
    Object[][] datos = new Object[ranking.size()][3];
        for (int i = 0; i < ranking.size(); i++) {
            datos[i][0] = i+1;
            datos[i][1] = ranking.get(i).getUsername();
            datos[i][2] = ranking.get(i).getPuntos();
        }
        JTable tabla = new JTable(datos, columnas);
        tabla.setEnabled(false);
        tabla.setFont(FontLoader.getButtonFont(15f));
        tabla.setRowHeight(30);
        tabla.setBackground(new Color(20,20,20));
        tabla.setForeground(Color.white);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel buildHistorialPanel(MainWindow window, PlayerManager playerManager) {
        Jugador actual = window.getCurrent();
        ArrayList<MatchRecord> historial = playerManager.getHistorialDeJugador(actual.getUsername());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(FontLoader.getButtonFont(15f));
        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);

        if (historial.isEmpty()) {
            area.setText("Aún no has jugado ninguna partida.");
        } else {
            for (MatchRecord m : historial) {
                area.append(m.getSummaryFor(actual.getUsername()) + "\n");
            }
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
    
}
