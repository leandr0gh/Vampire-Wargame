package game;

/*
hi
 */
import java.util.*;

public class Roulette {

    public enum RouletteResult {
        WEREWOLF, VAMPIRE, NECROMANCER
    }
    private static final RouletteResult[] options = {RouletteResult.WEREWOLF, RouletteResult.WEREWOLF,
        RouletteResult.VAMPIRE, RouletteResult.VAMPIRE,
        RouletteResult.NECROMANCER, RouletteResult.NECROMANCER};
    
    private Random random = new Random();
    
    public RouletteResult spin(){
    int index = random.nextInt(options.length);
    return options[index];
    }
    
    
}

