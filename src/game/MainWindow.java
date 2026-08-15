package game;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private CardLayout cl;
    private Container cont;

    public static final String startMenu = "MENU INICIO";
    public static final String login = "LOGIN";
    public static final String crearJugador = "CREAR JUGADOR";
    public static final String mainMenu = "MENU PRINCIPAL";
    public static final String game = "Juego";
    public static final String myAccount = "Mi Cuenta";
    public static final String reports = "Reportes";

    private Jugador current;
    private PlayerManager playerManager;

    public MainWindow() {
        setTitle("Vampire Wargame");
        setIconImage(new ImageIcon(getClass().getResource("/resources/wargameloco.png")).getImage());
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        playerManager = new PlayerManager();
        cl = new CardLayout();
        cont = getContentPane();
        cont.setLayout(cl);
        StartMenuTab startMenuTab = new StartMenuTab(this);
        CreateUserTab createUserPanel = new CreateUserTab(this, playerManager);
        LoginTab loginpanel = new LoginTab(this, playerManager);
        MainMenuTab mainMenuPanel=new MainMenuTab(this, playerManager);
        
        
        //agregar paneles al contenedor
        cont.add(startMenuTab, startMenu);
        cont.add(loginpanel, login);
        cont.add(createUserPanel,crearJugador);
        cont.add(mainMenuPanel, mainMenu);
        

        //mostrar los cardlayouts de contenedor
        cl.show(cont, startMenu);
    }
    
    public void showMyAccount() {
    MyAccountTab myAccountPanel = new MyAccountTab(this, playerManager);
    cont.add(myAccountPanel, myAccount);
    cl.show(cont, myAccount);
}

    public void showWindow(String name) {
        cl.show(cont, name);
    }

    public Jugador getCurrent() {
        return current;
    }

    public void setCurrent(Jugador current) {
        this.current = current;
    }
    
    private Partida partidaActual;
    public void setPartidaActual(Partida partida){
    this.partidaActual = partida;
    }
    
    public Partida getPartidaActual(){
    return partidaActual;
    }
    
    //had to do this cause it gave me npexc
    public void mostrarNuevaPartida(){
    cont.add(new GameTab(this), game);
    cl.show(cont, game);
    }
    
    public void mostrarReportes(){
    cont.add(new ReportesTab(this, playerManager), reports);
    cl.show(cont, reports);
    }

}
