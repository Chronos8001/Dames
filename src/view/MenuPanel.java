package view;

import java.awt.*;
import javax.swing.*;

// Main menu screen with Play button
public class MenuPanel extends JPanel {
    
    private JButton playButton;
    private JButton rulesButton;
    private JLabel titleLabel;
    
    public MenuPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(28, 28, 28));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Title
        titleLabel = new JLabel("Welcome to Checkers");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        titleLabel.setForeground(new Color(255, 240, 200));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(50));
        add(titleLabel);
        
        // Description
        JLabel descLabel = new JLabel("Classic strategy game for two players");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        descLabel.setForeground(new Color(180, 180, 180));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(descLabel);
        
        // Play button
        add(Box.createVerticalStrut(80));
        playButton = new JButton("PLAY");
        playButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        playButton.setPreferredSize(new Dimension(200, 60));
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setBackground(new Color(50, 150, 50));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        add(playButton);
        
        // Rules button
        add(Box.createVerticalStrut(20));
        rulesButton = new JButton("RULES");
        rulesButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        rulesButton.setPreferredSize(new Dimension(200, 50));
        rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rulesButton.setBackground(new Color(80, 120, 150));
        rulesButton.setForeground(Color.WHITE);
        rulesButton.setFocusPainted(false);
        add(rulesButton);
        
        add(Box.createVerticalGlue());
    }
    
    public JButton getPlayButton() {
        return playButton;
    }
    
    public JButton getRulesButton() {
        return rulesButton;
    }
}
