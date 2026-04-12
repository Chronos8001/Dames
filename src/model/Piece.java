
package model;

public abstract class Piece {

    // Current board size (set by Game when a variant is selected)
    public static int BOARD_SIZE = 10;
    
    // Color enumeration (WHITE = bottom player, BLACK = top player)
    public enum Color {
        WHITE, BLACK
    }

    // Attributes (private = encapsulation)
    private Color color;      // Color of the piece
    private boolean isDame;   // true if promoted to Dame (king)

    // Constructor
    /**
     * @param color The color of the piece (WHITE or BLACK)
     */
    public Piece(Color color) {
        this.color  = color;
        this.isDame = false; // Initially all pieces are Pions
    }

    // Getters / Setters

    /** Returns the color of the piece */
    public Color getColor() {
        return color;
    }

    /** Indique si la pièce est une Dame */
    public boolean isDame() {
        return isDame;
    }

    /** Marque la pièce comme Dame (appelé par Board lors de la promotion) */
    public void setDame(boolean isDame) {
        this.isDame = isDame;
    }

    // Méthodes abstraites (polymorphisme)

    /**
     * Calcule les déplacements valides pour cette pièce depuis la position (row, col).
     *
     * @param board Le plateau de jeu (tableau 2D de Piece)
     * @param row   Ligne actuelle de la pièce (0-9)
     * @param col   Colonne actuelle de la pièce (0-9)
     * @return Un tableau de positions valides [ [destRow, destCol], ... ]
     */
    public abstract int[][] getValidMoves(Piece[][] board, int row, int col);

    /**
     * Calcule les captures valides pour cette pièce depuis (row, col).
     *
     * @param board Le plateau de jeu
     * @param row   Ligne actuelle
     * @param col   Colonne actuelle
     * @return Un tableau de positions de capture [ [destRow, destCol], ... ]
     */
    public abstract int[][] getCaptures(Piece[][] board, int row, int col);

    // Méthode utilitaire

    @Override
    public String toString() {
        String s = (color == Color.WHITE) ? "W" : "B";
        if (isDame) s += "D";
        return s;
    }
}