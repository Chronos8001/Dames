package model;

import java.util.ArrayList;
import java.util.List;

public class Pion extends Piece {

    // Constructor
    /**
     * @param color The color of the pion (WHITE or BLACK)
     */
    public Pion(Color color) {
        super(color); // Call parent Piece constructor
    }

    // Valid moves (non-capture)

    @Override
    public int[][] getValidMoves(Piece[][] board, int row, int col) {
        List<int[]> moves = new ArrayList<>();

        // WHITE moves forward (+1), BLACK moves backward (-1)
        int direction = (getColor() == Color.WHITE) ? 1 : -1;

        // Two possible diagonal directions: left and right
        int[] colOffsets = {-1, 1};

        for (int colOffset : colOffsets) {
            int newRow = row + direction;
            int newCol = col + colOffset;

            // Check board boundaries
            if (isInsideBoard(newRow, newCol)) {
                // Target square must be empty
                if (board[newRow][newCol] == null) {
                    moves.add(new int[]{newRow, newCol});
                }
            }
        }

        // Convert list to 2D array
        return moves.toArray(new int[moves.size()][]);
    }

    // Valid captures (jumps)
    @Override
    public int[][] getCaptures(Piece[][] board, int row, int col) {
        List<int[]> captures = new ArrayList<>();

        // Same direction as valid moves
        int direction = (getColor() == Color.WHITE) ? 1 : -1;

        int[] colOffsets = {-1, 1};

        for (int colOffset : colOffsets) {
            // Position of opponent piece (1 square diagonally)
            int midRow = row + direction;
            int midCol = col + colOffset;

            // Landing position (2 squares diagonally)
            int landRow = row + 2 * direction;
            int landCol = col + 2 * colOffset;

            if (isInsideBoard(midRow, midCol) && isInsideBoard(landRow, landCol)) {
                Piece middle = board[midRow][midCol];
                Piece landing = board[landRow][landCol];

                // Must have opponent piece in middle AND empty landing square
                if (middle != null
                        && middle.getColor() != getColor()
                        && landing == null) {
                    captures.add(new int[]{landRow, landCol});
                }
            }
        }

        return captures.toArray(new int[captures.size()][]);
    }

    // Check if promotion should occur
    /**
     * @param row Current row of pion after move
     * @return true if the pion should be promoted to dame
     */
    // WHITE moves to last row (BOARD_SIZE-1), BLACK moves to first row (0)
    public boolean shouldPromote(int row) {
        if (getColor() == Color.WHITE && row == Piece.BOARD_SIZE - 1) return true;
        if (getColor() == Color.BLACK && row == 0) return true;
        return false;
    }
    
    // Utility method
    /** Checks if position is within board boundaries */
    private boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < Piece.BOARD_SIZE && col >= 0 && col < Piece.BOARD_SIZE;
    }

    @Override
    public String toString() {
        return (getColor() == Color.WHITE) ? "W" : "B";
    }
}