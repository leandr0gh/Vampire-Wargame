package game;

public class GameBoard {

    private Piece[][] board;

    public GameBoard() {
        board = new Piece[6][6];
        placeStartPieces();
    }

    private void placeStartPieces() {
        //place black ones ontop
        board[0][0] = new Werewolf(Piece.PieceColor.BLACK);
        board[0][1] = new Vampire(Piece.PieceColor.BLACK);
        board[0][2] = new Necromancer(Piece.PieceColor.BLACK);
        board[0][3] = new Necromancer(Piece.PieceColor.BLACK);
        board[0][4] = new Vampire(Piece.PieceColor.BLACK);
        board[0][5] = new Werewolf(Piece.PieceColor.BLACK);

        //place white bottom
        board[5][0] = new Werewolf(Piece.PieceColor.WHITE);
        board[5][1] = new Vampire(Piece.PieceColor.WHITE);
        board[5][2] = new Necromancer(Piece.PieceColor.WHITE);
        board[5][3] = new Necromancer(Piece.PieceColor.WHITE);
        board[5][4] = new Vampire(Piece.PieceColor.WHITE);
        board[5][5] = new Werewolf(Piece.PieceColor.WHITE);
    }

    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < 6 && col >= 0 && col < 6;
    }

    private boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol) {

        //basically the direction where its stepping or going or whatevr
        int rowStep = Integer.compare(toRow, fromRow);
        int colStep = Integer.compare(toCol, fromCol);
        return isPathClearRec(fromRow + rowStep, fromCol + colStep, toRow, toCol, rowStep, colStep);
    }
    
    private boolean isPathClearRec(int currentRow, int currentCol, int toRow, int toCol, int rowStep, int colStep){
        if (currentRow == toRow && currentCol == toCol) {
            return true;
        }
        if (board[currentRow][currentCol] != null) {
            return false;
        }
        return isPathClearRec(currentRow+rowStep, currentCol+colStep, toRow, toCol, rowStep, colStep);
    }


    

    //see if i can move the piece with alot of conditions before giving it the go
    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {

        Piece piece = board[fromRow][fromCol];
        if (!isWithinBounds(fromRow, fromCol) || !isWithinBounds(toRow, toCol)) {
            return false;
        }

        if (piece == null) {
            return false;
        }

        if (board[toRow][toCol] != null) {
            return false;
        }

        if (!piece.isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        if (!isPathClear(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = null;

        return true;
    }

    public String attack(int fromRow, int fromCol, int toRow, int toCol) {
        Piece attacker = board[fromRow][fromCol];
        Piece defender = board[toRow][toCol];

        if (attacker == null || defender == null) {
            return null;
        }

        if (attacker.getColor() == defender.getColor()) {
            return null;
        }

        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        int distance = Math.max(rowDiff, colDiff);

        if (distance != 1) {
            return null;
        }
        attacker.attackPiece(defender);

        if (!defender.isAlive()) {
            board[toRow][toCol] = null;
            return "DESTROYED: " + defender.getSymbol()+":"+ defender.getColor();
        }

        return "DAMAGED:" + defender.getSymbol() + ":" + attacker.getAttack() + ":" + defender.getShield() + ":" + defender.getHealth() + ":" + defender.getColor();

    }

    //ataques especiales
    public String specialAttackNecro(int fromRow, int fromCol, int toRow, int toCol) {

        Piece attacker = board[fromRow][fromCol];
        Piece defender = board[toRow][toCol];
        if (!(attacker instanceof Necromancer)) {
            return null;
        }

        if (defender == null) {
            return null;
        }

        if (attacker.getColor() == defender.getColor()) {
            return null;
        }

        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        int distance = Math.max(rowDiff, colDiff);

        if (distance != 2) {
            return null;
        }
        defender.receiveDamage(2, true);

        if (!defender.isAlive()) {
            board[toRow][toCol] = null;
            return "DESTROYED:" + defender.getSymbol() + ":" + defender.getColor();
        }
        return "DAMAGED:" + defender.getSymbol() + ":2:" + defender.getShield() + ":" + defender.getHealth() + ":" + defender.getColor();
    }

    public String invokeZombie(int fromRow, int fromCol, int toRow, int toCol) {
        Piece attacker = board[fromRow][fromCol];
        Piece target = board[toRow][toCol];

        if (!(attacker instanceof Necromancer)) {
            return null;
        }

        if (target != null) {
            return null;
        }

        Piece.PieceColor color = attacker.getColor();
        board[toRow][toCol] = new Zombie(color);
        return "Zombie Invocado";
    }

    public String attackThroughZombie(int necroRow, int necroCol, int zombieRow, int zombieCol, int targetRow, int targetCol) {
        Piece necromancer = board[necroRow][necroCol];
        Piece zombie = board[zombieRow][zombieCol];
        Piece target = board[targetRow][targetCol];

        if (!(necromancer instanceof Necromancer)) {
            return null;
        }
        if (!(zombie instanceof Zombie)) {
            return null;
        }
        if (target == null) {
            return null;
        }
        if (necromancer.getColor() != zombie.getColor()) {
            return null;
        }

        if (necromancer.getColor() == target.getColor()) {
            return null;
        }

        int necroDistance = Math.max(Math.abs(targetRow - necroRow), Math.abs(targetCol - necroCol));
        int zombieDistance = Math.max(Math.abs(targetRow - zombieRow), Math.abs(targetCol - zombieCol));
        if (necroDistance <= 2) {
            return null;
        }
        if (zombieDistance != 1) {
            return null;
        }
        target.receiveDamage(1, false);

        if (!target.isAlive()) {
            board[targetRow][targetCol] = null;
             return "DESTROYED:" + target.getSymbol()+":" + target.getColor();
        }

        return "DAMAGED:" + target.getSymbol() + ":1:" + target.getShield() + ":" + target.getHealth() + ":" + target.getColor();

    }

    public String absorbBlood(int fromRow, int fromCol, int toRow, int toCol) {
        Piece attacker = board[fromRow][fromCol];
        Piece target = board[toRow][toCol];
        if (!(attacker instanceof Vampire)) {
            return null;
        }
        if (target == null) {
            return null;
        }

        if (attacker.getColor() == target.getColor()) {
            return null;
        }

        int distance = Math.max(Math.abs(toRow - fromRow), Math.abs(toCol - fromCol));
        if (distance != 1) {
            return null;
        }

        ((Vampire) attacker).bloodAbsorption(target);

        if (!target.isAlive()) {
            board[toRow][toCol] = null;
            return "DESTROYED:" + target.getSymbol()+ ":" + target.getColor();
        }
        return "DAMAGED:" + target.getSymbol() + ":1:" + target.getShield() + ":" + target.getHealth() + ":" + target.getColor();
    }

    //end special attacks
    //roulette stuff
    public boolean hasPieceOfType(Piece.PieceColor color, Roulette.RouletteResult type) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Piece piece = board[row][col];
                if (piece == null || piece.getColor() != color) {
                    continue;
                }
                if (type == Roulette.RouletteResult.WEREWOLF && piece instanceof Werewolf) {
                    return true;
                }
                if (type == Roulette.RouletteResult.VAMPIRE && piece instanceof Vampire) {
                    return true;
                }
                if (type == Roulette.RouletteResult.NECROMANCER && piece instanceof Necromancer) {
                    return true;
                }
            }
        }
        return false;
    }
    //end roulette stuff
    //start game end validation

    public boolean hasNoPiecesLeft(Piece.PieceColor color) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == color) {
                    return false;
                }

            }

        }
        return true;
    }

    public boolean pieceMatchesType(Piece piece, Roulette.RouletteResult type) {
        if (type == Roulette.RouletteResult.WEREWOLF) {
            return piece instanceof Werewolf;
        }
        if (type == Roulette.RouletteResult.VAMPIRE) {
            return piece instanceof Vampire;
        }
        if (type == Roulette.RouletteResult.NECROMANCER) {
            return piece instanceof Necromancer;
        }
        return false;
    }

    public boolean isMoveLegal(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isWithinBounds(fromRow, fromCol) || !isWithinBounds(toRow, toCol)) {
            return false;
        }

        Piece piece = board[fromRow][fromCol];
        if (piece == null) {
            return false;
        }
        if (board[toRow][toCol] != null) {
            return false;
        }
        if (!piece.isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }
        if (!isPathClear(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        return true;

    }
}
