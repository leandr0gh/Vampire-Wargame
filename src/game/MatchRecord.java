
package game;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class MatchRecord {
    private String winner;
    private String loser;
    private boolean retiro;
    private String fecha;

    public MatchRecord(String winner, String loser, boolean retiro) {
        this.winner = winner;
        this.loser = loser;
        this.retiro = retiro;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        
    }
    
    public boolean involves(String username){
    return winner.equals(username) || loser.equals(username);
    }
    
    public String getSummaryFor(String username){
        if (winner.equals(username)) {
            return fecha + " - Ganaste contra "+ loser + (retiro ? "(el rival se retiro)" : "");
        } else {
            return fecha + " - Perdiste contra "+winner + (retiro? "(te retiraste)" : "");
        }
    }


    
}
