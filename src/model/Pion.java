package model;

import java.util.ArrayList;
import java.util.List;

public class Pion extends Piece {

    // Constructeur
    /**
     * @param color La couleur du pion (WHITE ou BLACK)
     */
    public Pion(Color color) {
        super(color); // Appel du constructeur de Piece
    }

    // Déplacements valides (sans capture)

    @Override
    public int[][] getValidMoves(Piece[][] board, int row, int col) {
        List<int[]> moves = new ArrayList<>();

        // CORRECTION : WHITE (rows 0-3) avance vers les rows croissantes (+1)
        //              BLACK (rows 6-9) avance vers les rows décroissantes (-1)
        int direction = (getColor() == Color.WHITE) ? 1 : -1;

        // Deux diagonales avant possibles : gauche et droite
        int[] colOffsets = {-1, 1};

        for (int colOffset : colOffsets) {
            int newRow = row + direction;
            int newCol = col + colOffset;

            // Vérification des limites du plateau (0 à 9)
            if (isInsideBoard(newRow, newCol)) {
                // La case doit être vide
                if (board[newRow][newCol] == null) {
                    moves.add(new int[]{newRow, newCol});
                }
            }
        }

        // Convertit la liste en tableau 2D
        return moves.toArray(new int[moves.size()][]);
    }

    // Captures valides
    @Override
    public int[][] getCaptures(Piece[][] board, int row, int col) {
        List<int[]> captures = new ArrayList<>();

        // CORRECTION : même direction que getValidMoves
        int direction = (getColor() == Color.WHITE) ? 1 : -1;

        int[] colOffsets = {-1, 1};

        for (int colOffset : colOffsets) {
            // Position de la pièce adverse (1 case en diagonale)
            int midRow = row + direction;
            int midCol = col + colOffset;

            // Position d'atterrissage (2 cases en diagonale)
            int landRow = row + 2 * direction;
            int landCol = col + 2 * colOffset;

            if (isInsideBoard(midRow, midCol) && isInsideBoard(landRow, landCol)) {
                Piece middle = board[midRow][midCol];
                Piece landing = board[landRow][landCol];

                // Il doit y avoir une pièce adverse au milieu ET la case d'atterrissage vide
                if (middle != null
                        && middle.getColor() != getColor()
                        && landing == null) {
                    captures.add(new int[]{landRow, landCol});
                }
            }
        }

        return captures.toArray(new int[captures.size()][]);
    }

    // Vérification de la promotion
    /**
     * @param row La ligne actuelle du pion après son déplacement
     * @return true si le pion doit être promu
     */
    // CORRECTION : WHITE avance vers row 9 → promu à row 9
    //              BLACK avance vers row 0  → promu à row 0
    public boolean shouldPromote(int row) {
        if (getColor() == Color.WHITE && row == 9) return true;
        if (getColor() == Color.BLACK && row == 0) return true;
        return false;
    }
    // Méthode utilitaire privée
    /** Vérifie que la position est bien dans les limites du plateau 10x10 */
    private boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }

    @Override
    public String toString() {
        return (getColor() == Color.WHITE) ? "W" : "B";
    }
}