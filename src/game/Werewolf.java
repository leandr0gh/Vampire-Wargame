
package game;

public class Werewolf extends Piece{

    public Werewolf(PieceColor color) {
        super(5, 5, 2, color);
    }

    @Override
    public int getMovementRange() {
        return 2;
    }


    @Override
    public String getSymbol() {
        return "WW";
    }

    @Override
    public int getShield() {
        return this.shield;
    }

    @Override
    public int getHealth() {
       return this.health;
    }
    
}
