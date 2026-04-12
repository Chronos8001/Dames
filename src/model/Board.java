package model;

public class Board {
    private final int size;
    private final Piece[][] board;
    
    /**
     * Constructs a new board of the specified size and initializes it with pieces.
     * @param size The board size (10 for International, 8 for English Draughts)
     */
    public Board(int size) {
        this.size = size;
        this.board = new Piece[size][size];
        // Update Piece class with board size for move validation
        Piece.BOARD_SIZE = size;
        initializeBoard();
    }
    
    private void initializeBoard() {
        // Initialize empty board (null values)
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = null;
            }
        }
        
        // Calculate how many rows for each player
        int piecesPerPlayer = size == 8 ? 3 : 4;
        
        // Place white pieces (first piecesPerPlayer rows)
        for (int row = 0; row < piecesPerPlayer; row++) {
            for (int col = 0; col < size; col++) {
                if ((row + col) % 2 == 1) { // Dark squares only
                    board[row][col] = new Pion(Piece.Color.WHITE);
                }
            }
        }
        
        // Place black pieces (last piecesPerPlayer rows)
        int blackStartRow = size - piecesPerPlayer;
        for (int row = blackStartRow; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if ((row + col) % 2 == 1) { // Dark squares only
                    board[row][col] = new Pion(Piece.Color.BLACK);
                }
            }
        }
    }
    
    /**
     * Gets the piece at the specified position.
     * 
     * @param row The row index
     * @param col The column index
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
        return row >= 0 && row < size && col >= 0 && col < size;
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
     * @return The board size (10 for International, 8 for English Draughts)
     */
    public int getSize() {
        return size;
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
        Board newBoard = new Board(size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
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
