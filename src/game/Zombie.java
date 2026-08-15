
package game;

public final class Zombie extends Piece{

    public Zombie(PieceColor color) {
        super(1, 1, 0, color);
    }

    @Override
    public int getMovementRange() {
        return 0;
    }

    @Override
    public String getSymbol() {
        return "ZO";
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
