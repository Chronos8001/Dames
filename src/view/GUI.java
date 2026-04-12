package view;

import java.awt.*;
import javax.swing.*;
import model.Game;

// Main application window managing game flow
public class GUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    private MenuPanel menuPanel;
    private VariantSelectorPanel variantPanel;
    private PlayerSetupPanel playerSetupPanel;
    private GameScreenPanel gameScreenPanel;
    
    private GameVariant selectedVariant;
    
    public GUI() {
        setTitle("Checkers Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(900, 750));
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(28, 28, 28));
        
        // Create all screens
        menuPanel = new MenuPanel();
        variantPanel = new VariantSelectorPanel();
        playerSetupPanel = new PlayerSetupPanel();
        
        // Add screens to card panel
        cardPanel.add(menuPanel, "menu");
        cardPanel.add(variantPanel, "variant");
        cardPanel.add(playerSetupPanel, "playerSetup");
        
        add(cardPanel);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Setup event handlers
        setupEventHandlers();
        
        // Show menu screen first
        cardLayout.show(cardPanel, "menu");
    }
    
    private void setupEventHandlers() {
        // Menu: Play button
        menuPanel.getPlayButton().addActionListener(e -> showVariantSelection());
        
        // Menu: Rules button
        menuPanel.getRulesButton().addActionListener(e -> RulesDialog.show());
        
        // Variant: Continue button
        variantPanel.getContinueButton().addActionListener(e -> showPlayerSetup());
        variantPanel.getBackButton().addActionListener(e -> showMenu());
        
        // Player Setup: Start button
        playerSetupPanel.getStartButton().addActionListener(e -> startGame());
        playerSetupPanel.getBackButton().addActionListener(e -> showVariantSelection());
    }
    
    private void showMenu() {
        cardLayout.show(cardPanel, "menu");
    }
    
    private void showVariantSelection() {
        cardLayout.show(cardPanel, "variant");
    }
    
    private void showPlayerSetup() {
        selectedVariant = variantPanel.getSelectedVariant();
        cardLayout.show(cardPanel, "playerSetup");
    }
    
    private void startGame() {
        String player1Name = playerSetupPanel.getPlayer1Name();
        String player2Name = playerSetupPanel.getPlayer2Name();
        
        // Create new game with selected variant
        Game game = new Game(player1Name, player2Name, selectedVariant);
        
        // Create game screen
        gameScreenPanel = new GameScreenPanel(game, this);
        cardPanel.add(gameScreenPanel, "game");
        
        // Setup return to menu
        gameScreenPanel.getMenuButton().addActionListener(e -> returnToMenu());
        
        // Initialize and show
        gameScreenPanel.refresh();
        cardLayout.show(cardPanel, "game");
    }
    
    private void returnToMenu() {
        // Reset for new game
        cardPanel.remove(gameScreenPanel);
        gameScreenPanel = null;
        selectedVariant = null;
        
        // Show menu
        cardLayout.show(cardPanel, "menu");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
