package view;

import java.awt.*;
import javax.swing.*;

// Screen to select game variant
public class VariantSelectorPanel extends JPanel {
    
    private ButtonGroup variantGroup;
    private JButton continueButton;
    private JButton backButton;
    
    public VariantSelectorPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(28, 28, 28));
        setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        
        // Title
        JLabel titleLabel = new JLabel("Select Game Variant");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 240, 200));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        
        add(Box.createVerticalStrut(40));
        
        // Variant options
        variantGroup = new ButtonGroup();
        for (GameVariant variant : GameVariant.values()) {
            JRadioButton radioButton = new JRadioButton(variant.getDisplayName());
            radioButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
            radioButton.setForeground(new Color(210, 210, 210));
            radioButton.setBackground(new Color(40, 40, 40));
            radioButton.setActionCommand(variant.name());
            if (variant == GameVariant.INTERNATIONAL) {
                radioButton.setSelected(true);
            }
            radioButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            variantGroup.add(radioButton);
            add(radioButton);
            add(Box.createVerticalStrut(15));
        }
        
        add(Box.createVerticalStrut(40));
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(28, 28, 28));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        backButton = new JButton("Back");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(100, 40));
        buttonPanel.add(backButton);
        
        continueButton = new JButton("Continue");
        continueButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        continueButton.setPreferredSize(new Dimension(120, 40));
        continueButton.setBackground(new Color(50, 150, 50));
        continueButton.setForeground(Color.WHITE);
        continueButton.setFocusPainted(false);
        buttonPanel.add(continueButton);
        
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }
    
    public GameVariant getSelectedVariant() {
        String selected = variantGroup.getSelection().getActionCommand();
        return GameVariant.valueOf(selected);
    }
    
    public JButton getContinueButton() {
        return continueButton;
    }
    
    public JButton getBackButton() {
        return backButton;
    }
}
