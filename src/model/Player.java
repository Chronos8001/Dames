package model;

public class Player {
    private final String name;
    private final Piece.Color color;
    private int piecesCount;
    private int damesCount;
    private boolean isActive;
    
    /**
     * @param name The player's name
     * @param color The color of the player's pieces
     */
    public Player(String name, Piece.Color color) {
        this.name = name;
        this.color = color;
        this.piecesCount = 20; // International checkers: 20 pieces
        this.damesCount = 0;
        this.isActive = color == Piece.Color.WHITE; // White player starts
    }
    
    /**
     * Gets the player's name.
     * 
     * @return The player's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the player's color.
     * 
     * @return The player's color
     */
    public Piece.Color getColor() {
        return color;
    }
    
    /**
     * Gets the number of regular pieces (pions).
     * 
     * @return Number of pions
     */
    public int getPiecesCount() {
        return piecesCount;
    }
    
    /**
     * Gets the number of promoted pieces (dames).
     * 
     * @return Number of dames
     */
    public int getDamesCount() {
        return damesCount;
    }
    
    /**
     * Checks if this player's turn is active.
     * 
     * @return true if it's this player's turn, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Sets whether it's this player's turn.
     * 
     * @param active true to activate this player's turn, false otherwise
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    /**
     * Decrements the pion count (called when a pion is captured or promoted).
     */
    public void decrementPiecesCount() {
        this.piecesCount--;
    }
    
    /**
     * Increments the dame count (called when a pion is promoted).
     */
    public void incrementDamesCount() {
        this.damesCount++;
    }
    
    /**
     * Gets the total number of pieces (pions + dames).
     * 
     * @return Total pieces count
     */
    public int getTotalPieces() {
        return piecesCount + damesCount;
    }
    
    @Override
    public String toString() {
        return name + " (" + color + ") - Pions: " + piecesCount + ", Dames: " + damesCount;
    }
}
