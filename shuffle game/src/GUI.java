import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private JPanel mainMenuPanel; 
    private LevelVariables levelVariables;
    private int level;
    
    public GUI() {
    	levelVariables = new LevelVariables();
    	level = 1;
        setTitle("Memory Card Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mainMenuPanel = createMainMenu();
        add(cardPanel);
        cardPanel.add(mainMenuPanel, "Main Menu");
    }


    private JPanel createMainMenu() {
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("background.jpg");
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Title
        JLabel memoryCardLabel = new JLabel("Memory Card Game");
        memoryCardLabel.setFont(new Font("Arial", Font.BOLD, 35));
        memoryCardLabel.setForeground(Color.CYAN); 
        memoryCardLabel.setFont(new Font("Arial", Font.ITALIC, 24));
        memoryCardLabel.setBounds(185, 0, 400, 100);
        backgroundPanel.add(memoryCardLabel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        // Buttons
        JButton startGameButton = new JButton("Start Game");
        JButton selectLevelButton = new JButton("Select Level");
        JButton instructionsButton = new JButton("Instructions");
        JButton exitButton = new JButton("Exit");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        buttonPanel.add(startGameButton, gbc);
        buttonPanel.add(selectLevelButton, gbc);
        buttonPanel.add(instructionsButton, gbc);
        buttonPanel.add(exitButton, gbc);

        // Add button panel to background panel
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        // Action listeners
        startGameButton.addActionListener(e -> startGame(level));
        selectLevelButton.addActionListener(e -> LevelDialog());
        instructionsButton.addActionListener(e -> Instructions());
        exitButton.addActionListener(e -> System.exit(0));

        return backgroundPanel;
    }

    private void startGame(int level) {
        levelVariables = new LevelVariables(level, 0);
        
        GameScreen game = new GameScreen(levelVariables);
        dispose();
    }
    
    private void Instructions() {
        JOptionPane.showMessageDialog(this, "You can go crazy while playing these 3 games with levels. \nI tried to make one of the hardest games in the world. \nWait until you reach the final level :)", 
        		"Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    private void LevelDialog() {
        String[] options = {"Level 1", "Level 2", "Level 3"};
        int choice = JOptionPane.showOptionDialog(this, "Select a Level", "Select Level",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon("swing_icon.png"), options, options[0]);
        if (choice >= 0) {
            levelChoice(choice + 1);
        }
    }
    
    private void levelChoice(int choice) {
        JOptionPane.showMessageDialog(this, "Level "+ choice + " selected!", "Instructions", JOptionPane.INFORMATION_MESSAGE);
        level = choice;
    }
    public static void showGUI() {
        JFrame guiFrame = new GUI();
        guiFrame.setVisible(true);
    }


    public static void main(String[] args) {
    	showGUI();
    }
}
