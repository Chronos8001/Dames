package model;

public class Board {
    private static final int SIZE = 10;
    private final Piece[][] board;
    
    /**
     * Constructs a new 10x10 board and initializes it with pieces in starting positions.
     */
    public Board() {
        this.board = new Piece[SIZE][SIZE];
        initializeBoard();
    }
    
    private void initializeBoard() {
        // Initialize empty board (null values)
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = null;
            }
        }
        
        // Place white pieces (rows 0-3) - Pion objects
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) { // Dark squares only
                    board[row][col] = new Pion(Piece.Color.WHITE);
                }
            }
        }
        
        // Place black pieces (rows 6-9) - Pion objects
        for (int row = 6; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) { // Dark squares only
                    board[row][col] = new Pion(Piece.Color.BLACK);
                }
            }
        }
    }
    
    /**
     * Gets the piece at the specified position.
     * 
     * @param row The row index (0-9)
     * @param col The column index (0-9)
     * @return The piece at the position, or null if empty or out of bounds
     */
    public Piece getPiece(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }
    
    /**
     * Sets a piece at the specified position.
     * 
     * @param row The row index (0-9)
     * @param col The column index (0-9)
     * @param piece The piece to place (null to empty the square)
     */
    public void setPiece(int row, int col, Piece piece) {
        if (isValidPosition(row, col)) {
            board[row][col] = piece;
        }
    }
    
    /**
     * Removes and returns the piece at the specified position.
     * 
     * @param row The row index (0-9)
     * @param col The column index (0-9)
     * @return The removed piece, or null if the square was empty
     */
    public Piece removePiece(int row, int col) {
        if (isValidPosition(row, col)) {
            Piece temp = board[row][col];
            board[row][col] = null;
            return temp;
        }
        return null;
    }
    
    /**
     * Checks if a position is valid on the board.
     * 
     * @param row The row index
     * @param col The column index
     * @return true if the position is within board boundaries, false otherwise
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
    
    /**
     * Checks if a square is a dark square (playable square for pieces).
     * International checkers uses dark squares only.
     * 
     * @param row The row index
     * @param col The column index
     * @return true if the square is dark, false otherwise
     */
    public boolean isDarkSquare(int row, int col) {
        return (row + col) % 2 == 1;
    }
    
    /**
     * Gets the size of the board.
     * 
     * @return The board size (10 for international checkers)
     */
    public int getSize() {
        return SIZE;
    }
    
    /**
     * Gets the underlying 2D board array.
     * Used by partner's Pion and Dame classes for move calculation.
     * 
     * @return The 2D array of pieces
     */
    public Piece[][] getBoardArray() {
        return board;
    }
    
    /**
     * Gets all valid non-capture moves for a piece at the specified position.
     * Delegates to the piece's getValidMoves() method.
     * 
     * @param row The row index
     * @param col The column index
     * @return A 2D array of valid destination coordinates
     */
    public int[][] getValidMoves(int row, int col) {
        Piece piece = getPiece(row, col);
        
        if (piece == null) {
            return new int[0][]; // No moves for empty square
        }
        
        return piece.getValidMoves(board, row, col);
    }
    
    /**
     * Gets all valid capture moves for a piece at the specified position.
     * Delegates to the piece's getCaptures() method.
     * 
     * @param row The row index
     * @param col The column index
     * @return A 2D array of valid capture destination coordinates
     */
    public int[][] getCaptures(int row, int col) {
        Piece piece = getPiece(row, col);
        
        if (piece == null) {
            return new int[0][]; // No captures for empty square
        }
        
        return piece.getCaptures(board, row, col);
    }
    
    /**
     * Creates a deep copy of the board.
     * Useful for AI or move prediction without modifying the actual board.
     * 
     * @return A new board with the same state
     */
    public Board copy() {
        Board newBoard = new Board();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != null) {
                    // Note: This creates new Pion/Dame objects, not exact copies
                    Piece originalPiece = board[row][col];
                    Piece newPiece;
                    
                    if (originalPiece.isDame()) {
                        newPiece = new Dame(originalPiece.getColor());
                    } else {
                        newPiece = new Pion(originalPiece.getColor());
                    }
                    
                    newBoard.board[row][col] = newPiece;
                }
            }
        }
        return newBoard;
    }
}
