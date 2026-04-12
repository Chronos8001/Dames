package model;

import java.util.ArrayList;
import java.util.List;
import view.GameVariant;

public class Game {
    private Board board;
    private int boardSize;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private List<Move> moveHistory;
    private boolean gameOver;
    private Player winner;
    private String errorMessage;
    
    /**
     * @param player1Name Name of the first player (WHITE)
     * @param player2Name Name of the second player (BLACK)
     * @param variant The game variant (International 10x10 or English 8x8)
     */
    public Game(String player1Name, String player2Name, GameVariant variant) {
        this.boardSize = variant.getBoardSize();
        this.board = new Board(boardSize);
        this.player1 = new Player(player1Name, Piece.Color.WHITE);
        this.player2 = new Player(player2Name, Piece.Color.BLACK);
        this.currentPlayer = player1; // White player starts
        this.moveHistory = new ArrayList<>();
        this.gameOver = false;
        this.winner = null;
        this.errorMessage = "";
    }
    
    /**
     * Legacy constructor for backwards compatibility (defaults to 10x10)
     * @param player1Name Name of the first player (WHITE)
     * @param player2Name Name of the second player (BLACK)
     */
    public Game(String player1Name, String player2Name) {
        this(player1Name, player2Name, GameVariant.INTERNATIONAL);
    }
    
    // Getters
    
    public Board getBoard() { 
        return board; 
    }
    
    public Player getCurrentPlayer() { 
        return currentPlayer; 
    }
    
    public Player getPlayer1() { 
        return player1; 
    }
    
    public Player getPlayer2() { 
        return player2; 
    }
    
    public List<Move> getMoveHistory() { 
        return moveHistory; 
    }
    
    public boolean isGameOver() { 
        return gameOver; 
    }
    
    public Player getWinner() { 
        return winner; 
    }
    
    public String getErrorMessage() { 
        return errorMessage; 
    }
    
    // Game Logic
    
    /**
     * @param fromRow Starting row
     * @param fromCol Starting column
     * @param toRow Destination row
     * @param toCol Destination column
     * @return true if the move was valid and executed, false otherwise
     */

    // executing a move
    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        errorMessage = "";
        
        // Validate input bounds
        if (!isValidMoveInput(fromRow, fromCol, toRow, toCol)) {
            return false;
        }
        
        Piece piece = board.getPiece(fromRow, fromCol);
        Piece destPiece = board.getPiece(toRow, toCol);
        
        // Check if source has a piece
        if (piece == null) {
            errorMessage = "No piece at source position";
            return false;
        }
        
        // Check piece ownership
        if (piece.getColor() != currentPlayer.getColor()) {
            errorMessage = "Cannot move opponent's piece";
            return false;
        }
        
        // Vérification diagonale
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        if (rowDiff != colDiff || rowDiff == 0) {
            errorMessage = "Invalid move distance or direction";
            return false;
        }

        // Vérifie si c'est un déplacement normal ou une capture
        // On délègue aux méthodes de la pièce (polymorphisme : Pion ou Dame)
        boolean isInNormalMoves  = isMoveInList(board.getValidMoves(fromRow, fromCol), toRow, toCol);
        boolean isInCaptureMoves = isMoveInList(board.getCaptures(fromRow, fromCol), toRow, toCol);

        // La destination doit être vide (vérifié seulement pour les mouvements normaux,
        // pour les captures la case est forcément vide car getCaptures() le garantit)
        if (destPiece != null && !isInCaptureMoves) {
            errorMessage = "Destination square is not empty";
            return false;
        }

        Piece capturedPiece = null;

        if (isInNormalMoves) {
            // Déplacement normal validé
        } else if (isInCaptureMoves) {
            // For Dame, the captured piece may not be exactly in the middle
            // Search for it on the diagonal between from and to position
            int rowDir = (toRow - fromRow) > 0 ? 1 : -1;
            int colDir = (toCol - fromCol) > 0 ? 1 : -1;
            int r = fromRow + rowDir;
            int c = fromCol + colDir;
            while (r != toRow || c != toCol) {
                if (board.getPiece(r, c) != null) {
                    capturedPiece = board.removePiece(r, c);
                    break;
                }
                r += rowDir;
                c += colDir;
            }

            if (capturedPiece == null) {
                errorMessage = "No piece to capture";
                return false;
            }
            if (capturedPiece.getColor() == currentPlayer.getColor()) {
                errorMessage = "Cannot capture own piece";
                return false;
            }

            Player opponent = currentPlayer == player1 ? player2 : player1;
            opponent.decrementPiecesCount();
        } else {
            errorMessage = "Invalid move";
            return false;
        }
        
        // Execute move
        piece = board.removePiece(fromRow, fromCol);
        board.setPiece(toRow, toCol, piece);
        
        // CORRECTION : WHITE avance vers row 9, BLACK avance vers row 0
        Piece promotedPiece = null;
        if (!piece.isDame() && ((currentPlayer.getColor() == Piece.Color.WHITE && toRow == 9) ||
                                (currentPlayer.getColor() == Piece.Color.BLACK && toRow == 0))) {
            // Promote to Dame
            piece.setDame(true);
            promotedPiece = piece;
            currentPlayer.decrementPiecesCount();
            currentPlayer.incrementDamesCount();
        }
        
        // Record move in history (ArrayList)
        Move move = new Move(fromRow, fromCol, toRow, toCol, capturedPiece, promotedPiece, 
                            currentPlayer.getName());
        moveHistory.add(move);
        
        // Check for game over
        checkGameOver();
        
        // Switch player if game is not over
        if (!gameOver) {
            switchPlayer();
        }
        
        return true;
    }
    
    /**
     * Checks if a move exists in the list of valid moves.
     * 
     * @param moves 2D array of moves
     * @param row Target row
     * @param col Target column
     * @return true if the move is in the list
     */
    private boolean isMoveInList(int[][] moves, int row, int col) {
        for (int[] move : moves) {
            if (move[0] == row && move[1] == col) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Validates the move input bounds.
     */
    private boolean isValidMoveInput(int fromRow, int fromCol, int toRow, int toCol) {
        if (!board.isValidPosition(fromRow, fromCol) || !board.isValidPosition(toRow, toCol)) {
            errorMessage = "Move out of board bounds";
            return false;
        }
        
        if (fromRow == toRow && fromCol == toCol) {
            errorMessage = "Source and destination are the same";
            return false;
        }
        
        return true;
    }
    

    private void switchPlayer() {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
    }
    
    // Checks if the game is over.
    // Game ends when a player has no pieces left.
    
    private void checkGameOver() {
        // Check if current player has no pieces
        if (currentPlayer.getTotalPieces() == 0) {
            gameOver = true;
            winner = currentPlayer == player1 ? player2 : player1;
            return;
        }
        
        // Check if opponent has no pieces
        Player opponent = currentPlayer == player1 ? player2 : player1;
        if (opponent.getTotalPieces() == 0) {
            gameOver = true;
            winner = currentPlayer;
            return;
        }
        
    }
    
    // Resets the game to the initial state
    public void resetGame() {
        this.board = new Board(boardSize);
        this.player1 = new Player(player1.getName(), Piece.Color.WHITE);
        this.player2 = new Player(player2.getName(), Piece.Color.BLACK);
        this.currentPlayer = player1;
        this.moveHistory.clear();
        this.gameOver = false;
        this.winner = null;
        this.errorMessage = "";
    }
    
    /**
     * Gets all valid non-capture moves for a piece at the specified position.
     * @param row The row index
     * @param col The column index
     * @return A 2D array of valid destination coordinates
     */
    public int[][] getValidMoves(int row, int col) {
        return board.getValidMoves(row, col);
    }
    
    /**
     * Gets all valid capture moves for a piece at the specified position.
     * 
     * @param row The row index
     * @param col The column index
     * @return A 2D array of valid capture destination coordinates
     */
    public int[][] getCaptures(int row, int col) {
        return board.getCaptures(row, col);
    }
}
