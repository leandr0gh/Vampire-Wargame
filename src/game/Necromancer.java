package game;

public class Necromancer extends Piece {

    public Necromancer(PieceColor color) {
        super(4, 3, 1, color);
    }

    @Override
    public int getMovementRange() {
       return 1;
    }

    @Override
    public String getSymbol() {
        return "NM";
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
