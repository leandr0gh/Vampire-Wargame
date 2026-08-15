package game;

import java.util.ArrayList;

public class PlayerManager {

    private ArrayList<Jugador> jugadores;
    private ArrayList<MatchRecord> historial = new ArrayList<>();

    public PlayerManager() {
        jugadores = new ArrayList<>();
    }

    public Jugador login(String username, String password) {
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).credentialsMatch(username, password)) {
                return jugadores.get(i);
            }
        }
        return null;
    }

    public boolean userExists(String username) {
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public String createUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "Debe escribir un nombre de usuario, el espacio no debe estar vacio";
        }
        if (userExists(username)) {
            return "Ya existe un usuario con ese nombre!";
        }
        if (password.length() != 5) {
            return "Su contrasena debe tener exactamente 5 caracteres";
        }
        jugadores.add(new Jugador(username, password));
        return null;
    }
    
    public boolean deleteUser(String username, String password) {
    return deleteUserRec(username, password, 0);
}

private boolean deleteUserRec(String username, String password, int i) {
    if (i >= jugadores.size()) {
        return false;
    }
    if (jugadores.get(i).credentialsMatch(username, password)) {
        jugadores.remove(i);
        return true;
    }
    return deleteUserRec(username, password, i + 1); 
}

public ArrayList<Jugador> getOtherPlayers(String currentUsername){
    ArrayList<Jugador> otros = new ArrayList<>();
    for (int i = 0; i < jugadores.size(); i++) {
        if (!jugadores.get(i).getUsername().equals(currentUsername)) {
            otros.add(jugadores.get(i));
        }
    }
    return otros;
}

public void addMatchRecord(MatchRecord record){
historial.add(record);
}

public ArrayList<MatchRecord> getHistorialDeJugador(String username){
ArrayList<MatchRecord> resultado = new ArrayList<>();
    for (int i = historial.size()-1; i >= 0; i--) {
        if (historial.get(i).involves(username)) {
            resultado.add(historial.get(i));
        }
    }
    return resultado;
}

public ArrayList<Jugador> getRankingOrdenado() {
    ArrayList<Jugador> copia = new ArrayList<>(jugadores);
    for (int i = 0; i < copia.size() - 1; i++) {
        for (int j = 0; j < copia.size() - 1 - i; j++) {
            if (copia.get(j).getPuntos() < copia.get(j + 1).getPuntos()) {
                Jugador temp = copia.get(j);
                copia.set(j, copia.get(j + 1));
                copia.set(j + 1, temp);
            }
        }
    }
    return copia;
}
    
    
}
