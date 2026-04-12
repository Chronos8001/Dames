package view;

import javax.swing.*;

// Dialog displaying the rules of checkers
public class RulesDialog {
    
    public static void show() {
        String rulesText = 
            "CHECKERS RULES\n" +
            "===============\n\n" +
            "SETUP:\n" +
            "• Played on a 10x10 board (International Checkers)\n" +
            "• Each player starts with 20 pieces on the first 4 rows\n" +
            "• Players alternate turns, White always moves first\n\n" +
            
            "MOVEMENT:\n" +
            "• Regular pieces (pawns) move diagonally forward one square\n" +
            "• A piece that reaches the opposite end becomes a DAME (king)\n" +
            "• Dames can move diagonally backward and forward\n" +
            "• Dames can jump multiple squares diagonally\n\n" +
            
            "CAPTURING:\n" +
            "• To capture: jump over an opponent's piece diagonally\n" +
            "• The jumped piece is removed from the board\n" +
            "• If multiple captures are possible, you must continue jumping\n" +
            "• Dames can capture in any diagonal direction\n\n" +
            
            "WINNING:\n" +
            "• Capture all opponent's pieces to win\n" +
            "• If no moves are available, you lose\n\n" +
            
            "SPECIAL RULES:\n" +
            "• Capturing is mandatory when available\n" +
            "• Multi-jump sequences are allowed (chain captures)\n" +
            "• A piece becomes a Dame immediately upon reaching the end\n";
        
        JTextArea textArea = new JTextArea(rulesText);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 12));
        textArea.setBackground(new java.awt.Color(240, 240, 240));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 600));
        
        JOptionPane.showMessageDialog(
            null,
            scrollPane,
            "Checkers Rules",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
