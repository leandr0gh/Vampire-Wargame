package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameTab extends JPanel {

    private MainWindow window;
    private GameBoard gameBoard;
    private Partida partida;
    private JLabel[][] cells = new JLabel[6][6];
    private JTextArea logArea;
    private JButton retireButton;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private int selectedZombieRow = -1;
    private int selectedZombieCol = -1;

    //stuff for roulette functionality
    private JLabel statusLabel;
    private JLabel rouletteLabel;
    private RouletteWheelPanel wheelPanel;
    private JButton rouletteButton;

    private Roulette.RouletteResult currentSpinResult;

    private JPanel setupRoulette() {
        wheelPanel = new RouletteWheelPanel();
        rouletteButton = new JButton("Detener");
        rouletteButton.setFocusable(false);

        JPanel roulettePanel = new JPanel(new BorderLayout());
        roulettePanel.add(wheelPanel, BorderLayout.CENTER);
        roulettePanel.add(rouletteButton, BorderLayout.SOUTH);

        //add(roulettePanel, BorderLayout.WEST);
        rouletteButton.addActionListener(e -> {
            rouletteButton.setEnabled(false);
            currentSpinResult = partida.spinRoulette();

            wheelPanel.stopSpin(currentSpinResult, () -> {

                if (!partida.resultIsUsable(currentSpinResult)) {
                    if (partida.canSpinAgain()) {
                        setStatus("No tienes piezas de ese tipo. Puedes girar de nuevo.");
                        currentSpinResult = null;
                        rouletteButton.setEnabled(true);
                        wheelPanel.startContinuousSpin();
                    } else {
                        partida.endTurn();
                        currentSpinResult = null;
                        setStatus("Pierdes el turno. Ahora le toca a: " + partida.getCurrentPlayer().getUsername());
                        rouletteButton.setEnabled(true);
                        wheelPanel.startContinuousSpin();

                    }
                } else {
                    setStatus("Selecciona una pieza de tipo " + currentSpinResult);
                }

            });
        });
        wheelPanel.startContinuousSpin();
        setStatus(partida.getCurrentPlayer().getUsername() + ", presiona Detener para detener");

        return roulettePanel;

    }

    private void setupSidePanel() {
        retireButton = new JButton("Retirarse de la partida");
        retireButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que deseas retirarte? El otro jugador ganará automáticamente.",
                    "Retirarse de la partida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                String mensaje = partida.forfeit(partida.getCurrentTurnColor());
                JOptionPane.showMessageDialog(this, mensaje);
                window.showWindow(MainWindow.mainMenu);
            }
        });
        retireButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logArea = new JTextArea(10, 18);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.WHITE);
        logArea.setCaretColor(Color.WHITE);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setMaximumSize(new Dimension(220, 180));
        logScroll.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel roulettePanel = setupRoulette();
        roulettePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.add(retireButton);
        sidePanel.add(logScroll);
        sidePanel.add(roulettePanel);

        add(sidePanel, BorderLayout.WEST);
    }

    public GameTab(MainWindow window) {
        this.window = window;
        gameBoard = window.getPartidaActual().getGameBoard();
        partida = window.getPartidaActual();

        setLayout(new BorderLayout());

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(statusLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(6, 6));

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Piece piece = gameBoard.getPieceAt(row, col);
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setOpaque(true);

                cells[row][col] = cell;
                final int r = row;
                final int c = col;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleCellClick(r, c);
                    }
                });

                boardPanel.add(cell);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        setupSidePanel();
        refreshBoard();
    }

    private void handleCellClick(int row, int col) {
        if (selectedRow == -1) {
            selectOrigin(row, col);
        } else if (selectedZombieRow != -1) {

            handleZombieTargetClick(row, col);
        } else {
            Piece attacker = gameBoard.getPieceAt(selectedRow, selectedCol);
            Piece clicked = gameBoard.getPieceAt(row, col);

            boolean esOtraPiezaPropia = clicked != null && clicked.getColor() == attacker.getColor()
                    && !(row == selectedRow && col == selectedCol);
            if (esOtraPiezaPropia) {
                if (attacker instanceof Necromancer && clicked instanceof Zombie) {
                    selectedZombieRow = row;
                    selectedZombieCol = col;
                    refreshBoard();
                    cells[selectedRow][selectedCol].setBorder(BorderFactory.createLineBorder(Color.RED));
                    highlightZombieTargets(row, col);
                    setStatus("Selecciona al enemigo para atacar por medio del zombie");
                    return;
                }
                selectedRow = -1;
                selectedCol = -1;
                refreshBoard();
                selectOrigin(row, col);
                return;
            }
            handleDestinationClick(row, col);
        }
    }

    private void selectOrigin(int row, int col) {
        Piece piece = gameBoard.getPieceAt(row, col);

        if (piece == null) {
            setStatus("Esta casilla esta vacia.");
            return;
        }
        if (piece.getColor() != partida.getCurrentTurnColor()) {
            setStatus("Esta pieza no es tuya.");
            return;
        }
        if (currentSpinResult == null) {
            setStatus("Debes girar la ruleta.");
            return;
        }
        if (!gameBoard.pieceMatchesType(piece, currentSpinResult)) {
            setStatus("Esa pieza no coincide con el resultado de la ruleta.");
            return;
        }

        selectedRow = row;
        selectedCol = col;
        cells[row][col].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        highlightValidDestinations(row, col);
    }

    private void highlightValidDestinations(int fromRow, int fromCol) {
        Piece piece = gameBoard.getPieceAt(fromRow, fromCol);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (gameBoard.isMoveLegal(fromRow, fromCol, i, j)) {
                    cells[i][j].setBorder(BorderFactory.createLineBorder(Color.red, 3));
                } else if (piece instanceof Necromancer && gameBoard.getPieceAt(i, j) == null) {
                    cells[i][j].setBorder(BorderFactory.createLineBorder(Color.CYAN));
                }
            }
        }
    }

    private void handleDestinationClick(int row, int col) {

        Piece attacker = gameBoard.getPieceAt(selectedRow, selectedCol);
        Piece target = gameBoard.getPieceAt(row, col);

        if (target == null) {
            if (attacker instanceof Necromancer) {
                boolean moveLegal = gameBoard.isMoveLegal(selectedRow, selectedCol, row, col);
                if (moveLegal) {
                    String[] opciones = {"MOVER", "INVOCAR ZOMBIE"};
                    int eleccion = JOptionPane.showOptionDialog(this, "Que deseas hacer?", "Necromante", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                    if (eleccion == 0) {
                        gameBoard.movePiece(selectedRow, selectedCol, row, col);
                        appendLog("El jugador " + partida.getCurrentPlayer().getUsername() + " movió su pieza " + attacker.getSymbol() + " a la casilla (" + (row+1) + "," + (col+1) + ").");
                        finishAction();
                    } else if (eleccion == 1) {
                        gameBoard.invokeZombie(selectedRow, selectedCol, row, col);
                        appendLog("El jugador " + partida.getCurrentPlayer().getUsername() + " invocó un Zombie en la casilla (" + (row+1) + "," + col+1 + ").");
                        finishAction();
                    }

                } else {
                    int confirmar = JOptionPane.showConfirmDialog(this, "Invocar Zombie en esta casilla?", "Invocar Zombie", JOptionPane.YES_NO_OPTION);
                    if (confirmar == JOptionPane.YES_OPTION) {
                        gameBoard.invokeZombie(selectedRow, selectedCol, row, col);
                        appendLog("Se invocó un Zombie en la casilla (" + (row+1) + "," + (col+1) + ").");
                        finishAction();
                    }

                }
            } else {
                boolean moved = gameBoard.movePiece(selectedRow, selectedCol, row, col);
                if (moved) {
                    appendLog("Se movió la pieza " + attacker.getSymbol() + " a la casilla (" + (row+1) + "," + (col+1) + ").");
                    finishAction();
                } else {

                    setStatus("Movimiento invalido. Selecciona otra casilla.");
                }
            }
        } else if (target.getColor() == attacker.getColor()) {
            setStatus("Esa casilla tiene una pieza tuya. Selecciona otra");
        } else {
            int distance = Math.max(Math.abs(row - selectedRow), Math.abs(col - selectedCol));
            String resultado = null;

            if (attacker instanceof Vampire && distance == 1) {
                String[] opciones = {"Ataque normal", "Absorción de sangre"};
                int eleccion = JOptionPane.showOptionDialog(this, "¿Qué tipo de ataque?", "Vampiro",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                if (eleccion == 0) {
                    resultado = gameBoard.attack(selectedRow, selectedCol, row, col);
                } else if (eleccion == 1) {
                    resultado = gameBoard.absorbBlood(selectedRow, selectedCol, row, col);
                } else {
                    return;
                }

            } else if (attacker instanceof Necromancer && distance == 2) {
                resultado = gameBoard.specialAttackNecro(selectedRow, selectedCol, row, col);
            } else if (distance == 1) {
                resultado = gameBoard.attack(selectedRow, selectedCol, row, col);
            } else {
                setStatus("Esa pieza está fuera de rango de ataque.");
                return;
            }

            if (resultado == null) {
                setStatus("Ataque inválido. Selecciona otra casilla.");
                return;
            }
            if (resultado.startsWith("DESTROYED")) {
                partida.recordPieceLost(target.getColor());
            }
            logAttackResult(resultado);
            finishAction();

        }
    }

    private void mostrarResultadoAtaque(String resultado) {
        String[] partes = resultado.split(":");
        if (partes[0].equals("DESTROYED")) {
            JOptionPane.showMessageDialog(this, "Se destruyó la pieza " + partes[1]);
        } else if (partes[0].equals("DAMAGED")) {
            JOptionPane.showMessageDialog(this, "Se atacó la pieza " + partes[1]
                    + "; le quedan " + partes[2] + " puntos de escudo y " + partes[3] + " de vida");
        }
    }
    

    private void finishAction() {
        refreshBoard();
        selectedRow = -1;
        selectedCol = -1;
        selectedZombieRow = -1;
        selectedZombieCol = -1;

        String winner = partida.checkWinner();
        if (winner != null) {
            JOptionPane.showMessageDialog(this, winner + ". ¡Felicidades, has ganado 3 puntos!");
            window.showWindow(MainWindow.mainMenu);
            return;
        }

        partida.endTurn();
        appendLog("--- Turno de " + partida.getCurrentPlayer().getUsername() + " ---");
        currentSpinResult = null;
        setStatus(partida.getCurrentPlayer().getUsername() + ", es tu turno.");
        rouletteButton.setEnabled(true);
        wheelPanel.startContinuousSpin();
    }

    private void refreshBoard() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Piece piece = gameBoard.getPieceAt(row, col);
                JLabel cell = cells[row][col];
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                if ((row+col) %2 == 0) {
                    cell.setBackground(new Color(225, 210, 180));
                } else {
                cell.setBackground(new Color(70,70,70));
                }

                if (piece != null) {
                    ImageIcon icon = new ImageIcon(getClass().getResource(getImagePath(piece)));
                    int size = Math.min(cell.getWidth(), cell.getHeight());
                    if (size <= 0) {
                        size = 60;
                    }
                    Image scaled = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                    cell.setIcon(new ImageIcon(scaled));
                    cell.setText("");
                } else {
                    cell.setIcon(null);
                    cell.setText("");
                }
            }
        }
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
    }

    private void logAttackResult(String resultado) {
        String[] partes = resultado.split(":");
        String attacker = partida.getCurrentPlayer().getUsername();
        
        if (partes[0].equals("DESTROYED")) {
            Piece.PieceColor color = Piece.PieceColor.valueOf(partes[2]);
            String defender = partida.getPlayerByColor(color).getUsername();
            appendLog("El jugador " + attacker + "destruyó la pieza " + partes[1] + " del jugador " + defender + ".");
        } else if (partes[0].equals("DAMAGED")) {
            Piece.PieceColor color = Piece.PieceColor.valueOf(partes[5]);
            String defender = partida.getPlayerByColor(color).getUsername();
            appendLog("El jugador " + attacker + "ataco la pieza" + partes[1] + " del jugador " + defender + "y le quito" + partes[2]
                    + " puntos; le quedan " + partes[3] + " puntos de escudo y " + partes[4] + " de vida.");
        }
    }

    private void appendLog(String text) {
        logArea.append(text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    

    //zombie handlers
    private void highlightZombieTargets(int zombieRow, int zombieCol) {
        Piece necro = gameBoard.getPieceAt(selectedRow, selectedCol);
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int r = zombieRow + dr;
                int c = zombieCol + dc;
                if (r < 0 || r >= 6 || c < 0 || c >= 6) {
                    continue;
                }

                Piece p = gameBoard.getPieceAt(r, c);
                int necroDistance = Math.max(Math.abs(r - selectedRow), Math.abs(c - selectedCol));
                if (p != null && p.getColor() != necro.getColor() && necroDistance > 2) {
                    cells[r][c].setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 3));
                }
            }
        }
    }

    private void handleZombieTargetClick(int row, int col) {
        Piece target = gameBoard.getPieceAt(row, col);
        String resultado = gameBoard.attackThroughZombie(selectedRow, selectedCol, selectedZombieRow, selectedZombieCol, row, col);

        selectedZombieRow = -1;
        selectedZombieCol = -1;

        if (resultado == null) {
            setStatus("Ataque a través del Zombie inválido.");
            refreshBoard();
            selectedRow = -1;
            selectedCol = -1;
            return;
        }

        if (resultado.startsWith("DESTROYED") && target != null) {
            partida.recordPieceLost(target.getColor());
        }

        logAttackResult(resultado);
        finishAction();
    }

    //for piece iamges
    private String getImagePath(Piece piece) {
        String color = (piece.getColor() == Piece.PieceColor.WHITE) ? "white" : "black";
        String tipo;
        if (piece instanceof Werewolf) {
            tipo = "werewolf";
        } else if (piece instanceof Vampire) {
            tipo = "vampire";
        } else if (piece instanceof Necromancer) {
            tipo = "necromancer";
        } else {
            tipo = "zombie";
        }

        return "/resources/" + tipo + "_" + color + ".png";
    }
}
