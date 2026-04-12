package model;

import java.util.ArrayList;
import java.util.List;

public class Dame extends Piece {

    // Constructor
    /**
     * @param color The color of the dame (WHITE or BLACK)
     */
    public Dame(Color color) {
        super(color);          // Call parent Piece constructor
        setDame(true);         // A dame is always marked as such
    }

    // Valid moves (non-capture)
   
    @Override
    public int[][] getValidMoves(Piece[][] board, int row, int col) {
        List<int[]> moves = new ArrayList<>();

        // Four diagonal directions: (up-left, up-right, down-left, down-right)
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            // Move square by square in this direction
            while (isInsideBoard(r, c)) {
                if (board[r][c] == null) {
                    // Empty square: move is possible, continue
                    moves.add(new int[]{r, c});
                } else {
                    // Occupied square: stop (whether friend or enemy)
                    break;
                }
                r += dir[0];
                c += dir[1];
            }
        }

        return moves.toArray(new int[0][]);
    }

    // Valid captures (jumps)
   
    @Override
    public int[][] getCaptures(Piece[][] board, int row, int col) {
        List<int[]> captures = new ArrayList<>();

        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            // Find first piece in this direction
            while (isInsideBoard(r, c) && board[r][c] == null) {
                // Empty squares before opponent piece: continue searching
                r += dir[0];
                c += dir[1];
            }

            // Found opponent piece?
            if (isInsideBoard(r, c)
                    && board[r][c] != null
                    && board[r][c].getColor() != getColor()) {

                // Dame can land on all empty squares behind opponent piece
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

    // Utility method
    private boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < Piece.BOARD_SIZE && col >= 0 && col < Piece.BOARD_SIZE;
    }

    @Override
    public String toString() {
        return (getColor() == Color.WHITE) ? "WD" : "BD";
    }
}