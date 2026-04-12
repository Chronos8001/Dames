package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.Game;
import model.Move;
import model.Piece;
import model.Player;

// Main window for the checkers game
public class GUI extends JFrame {

    private final Game game;

    private BoardPanel boardPanel;
    private JLabel     playerLabel;
    private JLabel     scoreLabel;
    private JLabel     errorLabel;
    private JTextArea  historyArea;

    int         selectedRow  = -1;
    int         selectedCol  = -1;
    List<int[]> normalMoves  = new ArrayList<>();
    List<int[]> captureMoves = new ArrayList<>();

    // Initialize the GUI with a game instance
    public GUI(Game game) {
        this.game = game;
        setTitle("Jeu de Dames");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        buildUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        refresh();
    }

    // Build the main UI layout with board and side panel
    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(10, 0));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.setBackground(new Color(28, 28, 28));

        boardPanel = new BoardPanel(game, this);
        main.add(boardPanel, BorderLayout.CENTER);
        main.add(buildSidePanel(), BorderLayout.EAST);
        add(main);
    }

    // Create the right panel with player info, score, and history
    private JPanel buildSidePanel() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(40, 40, 40));
        side.setPreferredSize(new Dimension(220, BoardPanel.CELL_SIZE * BoardPanel.BOARD_SIZE));
        side.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        playerLabel = makeLabel("Tour de : ...", Color.WHITE, Font.BOLD, 14);
        scoreLabel  = makeLabel(" ", new Color(180, 180, 180), Font.PLAIN, 12);
        errorLabel  = makeLabel(" ", new Color(255, 100, 100), Font.ITALIC, 11);

        side.add(playerLabel);
        side.add(Box.createVerticalStrut(4));
        side.add(scoreLabel);
        side.add(Box.createVerticalStrut(4));
        side.add(errorLabel);
        side.add(Box.createVerticalStrut(8));
        side.add(makeSeparator());
        side.add(Box.createVerticalStrut(8));

        JLabel histTitle = makeLabel("Historique", new Color(160, 160, 160), Font.BOLD, 12);
        side.add(histTitle);
        side.add(Box.createVerticalStrut(4));
        side.add(buildHistoryScroll());
        side.add(Box.createVerticalStrut(10));
        side.add(buildNewGameButton());

        return side;
    }

    // Create a styled label
    private JLabel makeLabel(String text, Color color, int style, int size) {
        JLabel l = new JLabel(text);
        l.setForeground(color);
        l.setFont(new Font("SansSerif", style, size));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    // Create a horizontal separator
    private JSeparator makeSeparator() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(70, 70, 70));
        s.setMaximumSize(new Dimension(200, 2));
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        return s;
    }

    // Create a scrollable text area for move history
    private JScrollPane buildHistoryScroll() {
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(28, 28, 28));
        historyArea.setForeground(new Color(210, 210, 210));
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        historyArea.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setPreferredSize(new Dimension(200, 400));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        return scroll;
    }

    // Create the "New Game" button
    private JButton buildNewGameButton() {
        JButton btn = new JButton("Nouvelle partie");
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addActionListener(e -> onNewGame());
        return btn;
    }

    // Update all UI elements after each move
    void refresh() {
        boardPanel.repaint();

        Player current = game.getCurrentPlayer();
        String col = current.getColor() == Piece.Color.WHITE ? "BLANC" : "NOIR";
        playerLabel.setText("Tour de : " + current.getName() + " (" + col + ")");
        playerLabel.setForeground(
            current.getColor() == Piece.Color.WHITE
                ? new Color(255, 240, 200) : new Color(160, 160, 160));

        Player p1 = game.getPlayer1();
        Player p2 = game.getPlayer2();
        scoreLabel.setText("<html>" + p1.getName() + " : " + p1.getTotalPieces()
            + " pièces<br>" + p2.getName() + " : " + p2.getTotalPieces() + " pièces</html>");

        errorLabel.setText(" ");

        if (game.isGameOver()) showWinner();
    }

    // Append the last move to the history display
    private void addToHistory() {
        List<Move> hist = game.getMoveHistory();
        if (hist.isEmpty()) return;
        Move last = hist.get(hist.size() - 1);
        historyArea.append(last.getPlayerName() + " : " + last + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    // Display the winner message when game ends
    private void showWinner() {
        Player winner = game.getWinner();
        if (winner == null) return;
        clearSelection();
        boardPanel.repaint();
        JOptionPane.showMessageDialog(this,
            "Félicitations " + winner.getName() + " ! Vous avez gagné !",
            "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
    }

    // Handle board cell clicks: select piece or execute move
    void handleCellClick(int row, int col) {
        if (game.isGameOver()) return;

        Piece clicked = game.getBoard().getPiece(row, col);

        if (selectedRow == -1) {
            if (clicked == null) { showError("Case vide."); return; }
            if (clicked.getColor() != game.getCurrentPlayer().getColor()) {
                showError("Ce n'est pas votre pièce."); return;
            }
            select(row, col);
            return;
        }

        if (row == selectedRow && col == selectedCol) {
            clearSelection(); boardPanel.repaint(); return;
        }
        if (clicked != null && clicked.getColor() == game.getCurrentPlayer().getColor()) {
            select(row, col); return;
        }
        if (isHighlighted(row, col)) {
            play(selectedRow, selectedCol, row, col); return;
        }
        clearSelection(); boardPanel.repaint(); showError("Mouvement invalide.");
    }

    // Select a piece and display its valid moves
    private void select(int row, int col) {
        selectedRow = row;
        selectedCol = col;

        normalMoves.clear();
        for (int[] m : game.getValidMoves(row, col)) normalMoves.add(m);

        captureMoves.clear();
        for (int[] c : game.getCaptures(row, col))   captureMoves.add(c);

        boardPanel.repaint();

        if (normalMoves.isEmpty() && captureMoves.isEmpty())
            showError("Cette pièce ne peut pas bouger.");
        else
            errorLabel.setText(" ");
    }

    // Execute a move from source to destination
    private void play(int fr, int fc, int tr, int tc) {
        boolean ok = game.movePiece(fr, fc, tr, tc);
        clearSelection();
        if (ok) { addToHistory(); refresh(); }
        else    { showError(game.getErrorMessage()); boardPanel.repaint(); }
    }

    // Check if a cell is a valid move destination
    private boolean isHighlighted(int row, int col) {
        for (int[] m : normalMoves)  if (m[0]==row && m[1]==col) return true;
        for (int[] c : captureMoves) if (c[0]==row && c[1]==col) return true;
        return false;
    }

    // Clear the selected piece and its move highlights
    private void clearSelection() {
        selectedRow = -1; selectedCol = -1;
        normalMoves.clear(); captureMoves.clear();
    }

    // Display an error message
    private void showError(String msg) { errorLabel.setText(msg); }

    // Reset the game and clear the board
    private void onNewGame() {
        int ok = JOptionPane.showConfirmDialog(this,
            "Recommencer une nouvelle partie ?", "Nouvelle partie",
            JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            game.resetGame();
            historyArea.setText("");
            clearSelection();
            refresh();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI(new Game("Alice", "Bob")));
    }
}
