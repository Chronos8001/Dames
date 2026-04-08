package model;

/**
 * Represents a move made during the game.
 * Stores the source and destination positions, and captured pieces if any.
 * Used for move history tracking with ArrayList.
 */
public class Move {
    private final int fromRow;
    private final int fromCol;
    private final int toRow;
    private final int toCol;
    private final Piece capturedPiece;
    private final Piece promotedPiece;
    private final String playerName;
    private final long timestamp;
    
    /**
     * Constructs a move with all details.
     * 
     * @param fromRow Starting row
     * @param fromCol Starting column
     * @param toRow Destination row
     * @param toCol Destination column
     * @param capturedPiece The piece captured (null if none)
     * @param promotedPiece The piece after promotion (null if no promotion)
     * @param playerName The name of the player making the move
     */
    public Move(int fromRow, int fromCol, int toRow, int toCol, 
                Piece capturedPiece, Piece promotedPiece, String playerName) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.capturedPiece = capturedPiece;
        this.promotedPiece = promotedPiece;
        this.playerName = playerName;
        this.timestamp = System.currentTimeMillis();
    }
    
    public int getFromRow() { return fromRow; }
    public int getFromCol() { return fromCol; }
    public int getToRow() { return toRow; }
    public int getToCol() { return toCol; }
    public Piece getCapturedPiece() { return capturedPiece; }
    public Piece getPromotedPiece() { return promotedPiece; }
    public String getPlayerName() { return playerName; }
    public long getTimestamp() { return timestamp; }
    
    /**
     * Checks if this move involves a capture.
     * 
     * @return true if a piece was captured, false otherwise
     */
    public boolean isCapture() {
        return capturedPiece != null;
    }
    
    /**
     * Checks if this move involves a promotion.
     * 
     * @return true if a pion was promoted to dame, false otherwise
     */
    public boolean isPromotion() {
        return promotedPiece != null;
    }
    
    @Override
    public String toString() {
        String move = String.format("(%d,%d) → (%d,%d)", fromRow, fromCol, toRow, toCol);
        if (isCapture()) {
            move += " [Capture]";
        }
        if (isPromotion()) {
            move += " [Promotion]";
        }
        return move;
    }
}
