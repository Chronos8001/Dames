package model;

import java.util.ArrayList;
import java.util.List;

public class Dame extends Piece {

    // Constructeur
    /**
     * @param color La couleur de la Dame (WHITE ou BLACK)
     */
    public Dame(Color color) {
        super(color);          // Appel du constructeur de Piece
        setDame(true);         // Une Dame est toujours marquée comme telle
    }

    // Déplacements valides (sans capture)
   
    @Override
    public int[][] getValidMoves(Piece[][] board, int row, int col) {
        List<int[]> moves = new ArrayList<>();

        // Les 4 directions diagonales : (haut-gauche, haut-droite, bas-gauche, bas-droite)
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            // On avance case par case dans cette direction
            while (isInsideBoard(r, c)) {
                if (board[r][c] == null) {
                    // Case vide : déplacement possible, on continue
                    moves.add(new int[]{r, c});
                } else {
                    // Case occupée : on s'arrête (que ce soit ami ou ennemi)
                    break;
                }
                r += dir[0];
                c += dir[1];
            }
        }

        return moves.toArray(new int[0][]);
    }

    // Captures valides
   
    @Override
    public int[][] getCaptures(Piece[][] board, int row, int col) {
        List<int[]> captures = new ArrayList<>();

        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            // Cherche la première pièce dans cette direction
            while (isInsideBoard(r, c) && board[r][c] == null) {
                // Cases vides avant la pièce adverse : on continue à chercher
                r += dir[0];
                c += dir[1];
            }

            // On a trouvé une pièce adverse ?
            if (isInsideBoard(r, c)
                    && board[r][c] != null
                    && board[r][c].getColor() != getColor()) {

                // La Dame peut atterrir sur toutes les cases vides derrière
                int landR = r + dir[0];
                int landC = c + dir[1];

                while (isInsideBoard(landR, landC) && board[landR][landC] == null) {
                    captures.add(new int[]{landR, landC});
                    landR += dir[0];
                    landC += dir[1];
                }
            }
        }

        return captures.toArray(new int[0][]);
    }

    // Méthode utilitaire privée
    private boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }

    @Override
    public String toString() {
        return (getColor() == Color.WHITE) ? "WD" : "BD";
    }
}