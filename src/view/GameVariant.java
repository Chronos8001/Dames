package view;

// Enumeration for different checkers game variants
public enum GameVariant {
    INTERNATIONAL("International (10x10)", 10, 20),
    ENGLISH("English Draughts (8x8)", 8, 12);
    
    private final String displayName;
    private final int boardSize;
    private final int piecesPerPlayer;
    
    GameVariant(String displayName, int boardSize, int piecesPerPlayer) {
        this.displayName = displayName;
        this.boardSize = boardSize;
        this.piecesPerPlayer = piecesPerPlayer;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getBoardSize() {
        return boardSize;
    }
    
    public int getPiecesPerPlayer() {
        return piecesPerPlayer;
    }
}
