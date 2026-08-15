package game;
import java.time.*;
import javax.swing.text.PasswordView;

public class Jugador {

    private String username;
    private String password;
    private int puntos;
    private LocalDateTime fechaIngreso;
    private boolean active;

    public Jugador(String username, String password) {
        this.username = username;
        this.password = password;
        this.puntos = 0;
        this.fechaIngreso = LocalDateTime.now();
        this.active = true;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }

    public int getPuntos() {
        return puntos;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public boolean isActive() {
        return active;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean credentialsMatch(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    public void addPoints(int amt){
    
    this.puntos += amt;}

}
