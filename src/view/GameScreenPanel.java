package view;

import java.awt.*;
import javax.swing.*;
import model.Game;
import model.Piece;
import model.Player;

// Game screen with board and scoreboard
public class GameScreenPanel extends JPanel {
    
    private BoardPanel boardPanel;
    private JLabel playerLabel;
    private JLabel scoreLabel;
    private JLabel errorLabel;
    private JTextArea historyArea;
    private JButton menuButton;
    private Game game;
    private GUI gui;
    
    // Selection state (accessed by BoardPanel)
    int selectedRow = -1;
    int selectedCol = -1;
    java.util.List<int[]> normalMoves = new java.util.ArrayList<>();
    java.util.List<int[]> captureMoves = new java.util.ArrayList<>();
    
    public GameScreenPanel(Game game, GUI gui) {
        this.game = game;
        this.gui = gui;
        
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(28, 28, 28));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left: Game board
        boardPanel = new BoardPanel(game, this);
        add(boardPanel, BorderLayout.CENTER);
        
        // Right: Side panel with info
        add(buildSidePanel(), BorderLayout.EAST);
    }
    
    private JPanel buildSidePanel() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(40, 40, 40));
        side.setPreferredSize(new Dimension(280, 400));
        side.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        side.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        
        // Player info
        playerLabel = makeLabel("Turn: ...", Color.WHITE, Font.BOLD, 14);
        scoreLabel = makeLabel(" ", new Color(180, 180, 180), Font.PLAIN, 12);
        errorLabel = makeLabel(" ", new Color(255, 100, 100), Font.ITALIC, 11);
        
        side.add(playerLabel);
        side.add(Box.createVerticalStrut(4));
        side.add(scoreLabel);
        side.add(Box.createVerticalStrut(4));
        side.add(errorLabel);
        side.add(Box.createVerticalStrut(8));
        side.add(makeSeparator());
        side.add(Box.createVerticalStrut(8));
        
        // History
        JLabel histTitle = makeLabel("Move History", new Color(160, 160, 160), Font.BOLD, 12);
        side.add(histTitle);
        side.add(Box.createVerticalStrut(4));
        
        JScrollPane scroll = buildHistoryScroll();
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        side.add(scroll);
        
        side.add(Box.createVerticalGlue());
        side.add(Box.createVerticalStrut(10));
        
        // Menu button
        menuButton = new JButton("Main Menu");
        menuButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(menuButton);
        
        return side;
    }
    
    private JLabel makeLabel(String text, Color color, int style, int size) {
        JLabel l = new JLabel(text);
        l.setForeground(color);
        l.setFont(new Font("SansSerif", style, size));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    
    private JSeparator makeSeparator() {
        JSeparator s = new JSeparator();
        s.setForeground(new Color(70, 70, 70));
        s.setMaximumSize(new Dimension(200, 2));
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        return s;
    }
    
    private JScrollPane buildHistoryScroll() {
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(28, 28, 28));
        historyArea.setForeground(new Color(210, 210, 210));
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        historyArea.setLineWrap(true);
        
        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setPreferredSize(new Dimension(200, 300));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        scroll.setBackground(new Color(28, 28, 28));
        scroll.getViewport().setBackground(new Color(28, 28, 28));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        return scroll;
    }
    
    // Update display after each move
    public void refresh() {
        boardPanel.repaint();
        
        Player current = game.getCurrentPlayer();
        String color = current.getColor() == Piece.Color.WHITE ? "WHITE" : "BLACK";
        playerLabel.setText("Turn: " + current.getName() + "\n(" + color + ")");
        playerLabel.setForeground(
            current.getColor() == Piece.Color.WHITE
                ? new Color(255, 240, 200) : new Color(160, 160, 160));
        
        Player p1 = game.getPlayer1();
        Player p2 = game.getPlayer2();
        scoreLabel.setText("<html>" + p1.getName() + ": " + p1.getTotalPieces()
            + " pieces<br>" + p2.getName() + ": " + p2.getTotalPieces() + " pieces</html>");
        
        errorLabel.setText(" ");
        
        if (game.isGameOver()) {
            showWinner();
        }
    }
    
    private void addToHistory() {
        java.util.List<model.Move> hist = game.getMoveHistory();
        if (hist.isEmpty()) return;
        model.Move last = hist.get(hist.size() - 1);
        historyArea.append(last.getPlayerName() + ": " + last + "\n");
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }
    
    private void showWinner() {
        model.Player winner = game.getWinner();
        if (winner == null) return;
        clearSelection();
        boardPanel.repaint();
        JOptionPane.showMessageDialog(this,
            "Congratulations " + winner.getName() + "! You won!",
            "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Handle board clicks
    void handleCellClick(int row, int col) {
        if (game.isGameOver()) return;
        
        model.Piece clicked = game.getBoard().getPiece(row, col);
        
        if (selectedRow == -1) {
            if (clicked == null) { showError("Empty square"); return; }
            if (clicked.getColor() != game.getCurrentPlayer().getColor()) {
                showError("Not your piece"); return;
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
        clearSelection(); boardPanel.repaint(); showError("Invalid move");
    }
    
    private void select(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        
        normalMoves.clear();
        for (int[] m : game.getValidMoves(row, col)) normalMoves.add(m);
        
        captureMoves.clear();
        for (int[] c : game.getCaptures(row, col)) captureMoves.add(c);
        
        boardPanel.repaint();
        
        if (normalMoves.isEmpty() && captureMoves.isEmpty())
            showError("This piece cannot move");
        else
            errorLabel.setText(" ");
    }
    
    private void play(int fr, int fc, int tr, int tc) {
        boolean ok = game.movePiece(fr, fc, tr, tc);
        clearSelection();
        if (ok) { addToHistory(); refresh(); }
        else { showError(game.getErrorMessage()); boardPanel.repaint(); }
    }
    
    private boolean isHighlighted(int row, int col) {
        for (int[] m : normalMoves) if (m[0]==row && m[1]==col) return true;
        for (int[] c : captureMoves) if (c[0]==row && c[1]==col) return true;
        return false;
    }
    
    private void clearSelection() {
        selectedRow = -1; selectedCol = -1;
        normalMoves.clear(); captureMoves.clear();
    }
    
    private void showError(String msg) { errorLabel.setText(msg); }
    
    public JButton getMenuButton() {
        return menuButton;
    }
    
    public int getSelectedRow() { return selectedRow; }
    public int getSelectedCol() { return selectedCol; }
    public java.util.List<int[]> getNormalMoves() { return normalMoves; }
    public java.util.List<int[]> getCaptureMoves() { return captureMoves; }
}
