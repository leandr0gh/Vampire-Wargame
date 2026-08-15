
package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class ReportesTab extends JPanel {
    private Image background;

    public ReportesTab(MainWindow window, PlayerManager playerManager) {
        background = new ImageIcon(getClass().getResource("/resources/vampirewargamemainbg.png")).getImage();

        setLayout(new BorderLayout());
        GothicButton returnButton = new GothicButton("Volver");
        returnButton.addActionListener(e -> window.showWindow(MainWindow.mainMenu));
        
        //new stuff
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Ranking", buildRankingPanel(playerManager));
        tabs.addTab("Mi historial", buildHistorialPanel(window, playerManager));
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
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel buildHistorialPanel(MainWindow window, PlayerManager playerManager) {
        Jugador actual = window.getCurrent();
        ArrayList<MatchRecord> historial = playerManager.getHistorialDeJugador(actual.getUsername());

        JTextArea area = new JTextArea();
        area.setEditable(false);

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
