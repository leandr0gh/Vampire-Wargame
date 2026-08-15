
package game;

import java.math.MathContext;


public class Partida {
    private Jugador player1;
    private Jugador player2;
    private GameBoard gameBoard;
    private Piece.PieceColor currentTurnColor;
    private int piecesLostByWhite;
    private int piecesLostByBlack;
    private boolean GameOver;
    private PlayerManager playerManager;
    
    public Partida(Jugador player1, Jugador player2, PlayerManager playerManager){
    this.playerManager = playerManager;
    this.player1 = player1;
    this.player2 = player2;
    this.gameBoard = new GameBoard();
    this.currentTurnColor = Piece.PieceColor.WHITE;
    this.piecesLostByWhite = 0;
    this.piecesLostByBlack = 0;
    this.GameOver = false;
    }
    
    public Jugador getCurrentPlayer(){
    return currentTurnColor == Piece.PieceColor.WHITE ? player1 : player2;
    }

    public Piece.PieceColor getCurrentTurnColor() {
        return currentTurnColor;
    }
    
    public Jugador getPlayerByColor(Piece.PieceColor color){
    return (color == Piece.PieceColor.WHITE) ? player1 : player2;
    }
    
    
    
    public void switchTurn(){
    currentTurnColor = (currentTurnColor == Piece.PieceColor.WHITE) ? Piece.PieceColor.BLACK
            : Piece.PieceColor.WHITE;
    }
    
    public void recordPieceLost(Piece.PieceColor color){
        if (color == Piece.PieceColor.WHITE) {
            piecesLostByWhite++;
        } else {
        piecesLostByBlack++;
        }
    }
    
    public int getMaxSpins(Piece.PieceColor color){
    int lost = (color == Piece.PieceColor.WHITE) ? piecesLostByWhite : piecesLostByBlack;
    
    return 1+ (lost/2);
    }
    
    public GameBoard getGameBoard(){
    return gameBoard;}
    
    public boolean isGameOver(){
    return GameOver;
    }
    
    //more roullete stuff
    private Roulette roulette = new Roulette();
    private int spinsUsedThisTurn = 0;
    
    public Roulette.RouletteResult spinRoulette(){
    
    spinsUsedThisTurn++;
    return roulette.spin();
    }
    
    public boolean canSpinAgain(){
    
    return spinsUsedThisTurn < getMaxSpins(currentTurnColor);
    }
    
    public boolean resultIsUsable(Roulette.RouletteResult result){
    return gameBoard.hasPieceOfType(currentTurnColor, result);
    }
    public void endTurn(){
    spinsUsedThisTurn = 0;
    switchTurn();
    }
    //forfeit and check for winners after attack blah blah blah
    
    public String checkWinner(){
        if (gameBoard.hasNoPiecesLeft(Piece.PieceColor.WHITE)) {
            player2.addPoints(3);
            playerManager.addMatchRecord(new MatchRecord(player2.getUsername(), player1.getUsername(), false));
            return player2.getUsername() + " ha vencido a " + player1.getUsername();
        }
        if (gameBoard.hasNoPiecesLeft(Piece.PieceColor.BLACK)) {
            player1.addPoints(3);
            playerManager.addMatchRecord(new MatchRecord(player1.getUsername(), player2.getUsername(), false));
            return player1.getUsername() + " venció a " + player2.getUsername();
        }
        return null;
    }
    
    public String forfeit(Piece.PieceColor colorQueSeRinde){
    Jugador retirado = (colorQueSeRinde == Piece.PieceColor.WHITE ? player1 : player2);
    Jugador ganador = (colorQueSeRinde == Piece.PieceColor.WHITE ? player2: player1);
    ganador.addPoints(3);
    playerManager.addMatchRecord(new MatchRecord(ganador.getUsername(), retirado.getUsername(), true));
    GameOver = true;
    return retirado.getUsername() + " se ha rendido. Gana: "+ganador.getUsername()+". Felicidades! Has ganado 3 puntos.";
    }
    
}
