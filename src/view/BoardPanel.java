package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.Game;
import model.Piece;

// Panel that draws the game board and handles click events
public class BoardPanel extends JPanel {

    // Board constants
    static final int CELL_SIZE  = 65;
    static final int BOARD_SIZE = 10;
    private static final int PIECE_MARGIN = 7;

    // Board colors (light and dark squares)
    private static final Color LIGHT = new Color(240, 217, 181);
    private static final Color DARK  = new Color(181, 136,  99);

    // Highlight colors (selection, moves, captures)
    private static final Color SEL     = new Color( 50, 200,  50, 160);
    private static final Color MOVE    = new Color(  0, 200,   0,  70);
    private static final Color CAPTURE = new Color(220,  50,  50,  90);

    // Piece colors (white and black)
    private static final Color W_FILL   = new Color(255, 248, 230);
    private static final Color W_BORDER = new Color(180, 150, 100);
    private static final Color B_FILL   = new Color( 45,  45,  45);
    private static final Color B_BORDER = new Color( 90,  90,  90);
    private static final Color CROWN    = new Color(255, 200,   0);

    private final Game         game;
    private final GUI          gui;

    // Initialize the board panel with click listener
    BoardPanel(Game game, GUI gui) {
        this.game = game;
        this.gui  = gui;
        setPreferredSize(new Dimension(CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;
                if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
                    gui.handleCellClick(row, col);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawCases(g2);
        drawHighlights(g2);
        drawPieces(g2);
    }

    // Draw the checkerboard pattern
    private void drawCases(Graphics2D g2) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                g2.setColor((row + col) % 2 == 1 ? DARK : LIGHT);
                g2.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    // Draw highlights for selected piece and valid moves
    private void drawHighlights(Graphics2D g2) {
        int selRow = gui.selectedRow;
        int selCol = gui.selectedCol;

        if (selRow != -1) {
            g2.setColor(SEL);
            g2.fillRect(selCol * CELL_SIZE, selRow * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        for (int[] m : gui.normalMoves) {
            g2.setColor(MOVE);
            g2.fillRect(m[1] * CELL_SIZE, m[0] * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g2.setColor(new Color(0, 150, 0, 180));
            g2.fillOval(m[1] * CELL_SIZE + CELL_SIZE/2 - 10,
                        m[0] * CELL_SIZE + CELL_SIZE/2 - 10, 20, 20);
        }

        for (int[] c : gui.captureMoves) {
            g2.setColor(CAPTURE);
            g2.fillRect(c[1] * CELL_SIZE, c[0] * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g2.setColor(new Color(200, 0, 0, 180));
            g2.fillOval(c[1] * CELL_SIZE + CELL_SIZE/2 - 10,
                        c[0] * CELL_SIZE + CELL_SIZE/2 - 10, 20, 20);
        }
    }

    // Draw all pieces on the board
    private void drawPieces(Graphics2D g2) {
        Piece[][] arr = game.getBoard().getBoardArray();
        for (int row = 0; row < BOARD_SIZE; row++)
            for (int col = 0; col < BOARD_SIZE; col++)
                if (arr[row][col] != null)
                    drawOnePiece(g2, arr[row][col], row, col);
    }

    // Draw a single piece with optional dame crown
    private void drawOnePiece(Graphics2D g2, Piece p, int row, int col) {
        int x    = col * CELL_SIZE + PIECE_MARGIN;
        int y    = row * CELL_SIZE + PIECE_MARGIN;
        int size = CELL_SIZE - 2 * PIECE_MARGIN;
        boolean isWhite = p.getColor() == Piece.Color.WHITE;

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillOval(x + 3, y + 3, size, size);

        g2.setColor(isWhite ? W_FILL : B_FILL);
        g2.fillOval(x, y, size, size);

        g2.setColor(isWhite ? W_BORDER : B_BORDER);
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawOval(x, y, size, size);
        g2.setStroke(new BasicStroke(1f));

        if (p.isDame()) {
            g2.setColor(CROWN);
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(x + 7, y + 7, size - 14, size - 14);
            g2.setStroke(new BasicStroke(1f));
            g2.fillOval(x + size/2 - 5, y + size/2 - 5, 10, 10);
        }
    }
}
