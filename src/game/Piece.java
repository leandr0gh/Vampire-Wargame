package game;

public abstract class Piece {
    
    public enum PieceColor {
    WHITE, BLACK
    }
    
    protected int attack;
    protected int shield;
    protected int health;
    private PieceColor color;

    public Piece(int attack, int health, int shield, PieceColor color) {
        this.attack = attack;
        this.shield = shield;
        this.health = health;
        this.color = color;
    }
    
    public final void receiveDamage(int amount, boolean ignoreShield){
        
        if (!ignoreShield && shield>0) {
            int absorbed = Math.min(shield, amount);
            shield-=absorbed;
            amount-=absorbed;
        }
        health-=amount;
        if (health < 0) {
            health=0;
        }
    }
    
    public final void attackPiece(Piece target){
    target.receiveDamage(this.attack, false);
    }
    
    public final boolean isAlive(){
    return health>0;
    }
    
    public abstract int getMovementRange();
    public abstract String getSymbol();
    public abstract int getShield();
    public abstract int getHealth();
    
    public int getAttack(){
    return attack;
    }
    
    public final PieceColor getColor() {
    return color;
}
    
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
    int rowDiff = Math.abs(toRow - fromRow);
    int colDiff = Math.abs(toCol - fromCol);
    int maxDiff = Math.max(rowDiff, colDiff);
    
    return maxDiff > 0 && maxDiff <= getMovementRange();
}
   


    

}

