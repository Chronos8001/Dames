package view;

import java.awt.*;
import javax.swing.*;

// Screen to input player names
public class PlayerSetupPanel extends JPanel {
    
    private JTextField player1NameField;
    private JTextField player2NameField;
    private JButton startButton;
    private JButton backButton;
    
    public PlayerSetupPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(28, 28, 28));
        setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        
        // Title
        JLabel titleLabel = new JLabel("Enter Player Names");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 240, 200));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        
        add(Box.createVerticalStrut(50));
        
        // Player 1 (White)
        JLabel player1Label = new JLabel("Player 1 (White):");
        player1Label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        player1Label.setForeground(new Color(255, 240, 200));
        player1Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(player1Label);
        
        player1NameField = new JTextField("Alice");
        player1NameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        player1NameField.setMaximumSize(new Dimension(300, 40));
        player1NameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(player1NameField);
        
        add(Box.createVerticalStrut(30));
        
        // Player 2 (Black)
        JLabel player2Label = new JLabel("Player 2 (Black):");
        player2Label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        player2Label.setForeground(new Color(160, 160, 160));
        player2Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(player2Label);
        
        player2NameField = new JTextField("Bob");
        player2NameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        player2NameField.setMaximumSize(new Dimension(300, 40));
        player2NameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(player2NameField);
        
        add(Box.createVerticalStrut(50));
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(28, 28, 28));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(100, 40));
        buttonPanel.add(backButton);
        
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        startButton.setPreferredSize(new Dimension(140, 40));
        startButton.setBackground(new Color(50, 150, 50));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        buttonPanel.add(startButton);
        
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }
    
    public String getPlayer1Name() {
        String name = player1NameField.getText().trim();
        return name.isEmpty() ? "Player 1" : name;
    }
    
    public String getPlayer2Name() {
        String name = player2NameField.getText().trim();
        return name.isEmpty() ? "Player 2" : name;
    }
    
    public JButton getStartButton() {
        return startButton;
    }
    
    public JButton getBackButton() {
        return backButton;
    }
}
