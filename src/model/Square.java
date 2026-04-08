package model;

/**
 * Represents a single square on the checkers board.
 * Each square can either be empty or contain a piece.
 * Encapsulation: piece is private, accessed through getters/setters.
 */
public class Square {
    private Piece piece;
    private final int row;
    private final int col;
    
    /**
     * Constructs an empty square at the specified position.
     * 
     * @param row The row index (0-9 for international checkers)
     * @param col The column index (0-9 for international checkers)
     */
    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null;
    }
    
    /**
     * Checks if the square is empty.
     * 
     * @return true if the square contains no piece, false otherwise
     */
    public boolean isEmpty() {
        return piece == null;
    }
    
    /**
     * Gets the piece on this square.
     * 
     * @return The piece on this square, or null if empty
     */
    public Piece getPiece() {
        return piece;
    }
    
    /**
     * Sets the piece on this square.
     * 
     * @param piece The piece to place on this square (can be null)
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    
    /**
     * Removes the piece from this square.
     * 
     * @return The removed piece
     */
    public Piece removePiece() {
        Piece temp = piece;
        piece = null;
        return temp;
    }
    
    /**
     * Gets the row index of this square.
     * 
     * @return The row index
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Gets the column index of this square.
     * 
     * @return The column index
     */
    public int getCol() {
        return col;
    }
}
