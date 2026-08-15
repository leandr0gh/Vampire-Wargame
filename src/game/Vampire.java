package game;

public class Vampire extends Piece {

    public Vampire(PieceColor color) {
        super(3, 4, 5, color);
    }

    @Override
    public int getMovementRange() {
        return 1;
    }

    public void bloodAbsorption(Piece target) {

        target.receiveDamage(1, false);
            this.health++;
        

    }


    @Override
    public String getSymbol() {
        return "VA";
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
