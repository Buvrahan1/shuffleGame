import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameScreen extends JFrame {
    private int score;
    private int previousCardIndex;
    private int matchesFound;
    private JButton previousButton;
    public static int triesLeft;
    private boolean timerRunning;
    JLabel triesLeftLabel;
    static LevelVariables levelVariables;
    private ArrayList<Integer> correctMatches = new ArrayList<>();

    
    public GameScreen(LevelVariables lvlVar) {
    	GameScreen.levelVariables = lvlVar;
    	score = levelVariables.score;
        previousCardIndex = -1;
        matchesFound = 0;
        previousButton = null;
        triesLeft = levelVariables.getTries();
        timerRunning = false;
        triesLeftLabel = new JLabel("    "+ " Tries Left : " + triesLeft);
    	
        setTitle("Flash Cards Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        setJMenuBar(MenuBar());
        add(createTopBar(), BorderLayout.NORTH);
        add(createGamePanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    public JMenuBar MenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = createGameMenu();
        menuBar.add(Box.createHorizontalStrut(170));
        menuBar.add(gameMenu);

        JMenu aboutMenu = createAboutMenu();
        menuBar.add(aboutMenu);

        JMenu exitMenu = createExitMenu();
        menuBar.add(exitMenu);

        return menuBar;
    }

    private JMenu createGameMenu() {
        JMenu gameMenu = new JMenu("Game");

        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameScreen(levelVariables);
            }
        });

        JMenuItem highScoresItem = new JMenuItem("High Scores");
        highScoresItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHighScores();
            }
        });

        gameMenu.add(restartItem);
        gameMenu.add(highScoresItem);

        return gameMenu;
    }

    private JMenu createAboutMenu() {
        JMenu aboutMenu = new JMenu("About");

        JMenuItem aboutDeveloperItem = new JMenuItem("About the Developer");
        aboutDeveloperItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAboutDeveloper();
            }
        });

        JMenuItem aboutGameItem = new JMenuItem("About the Game");
        aboutGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAboutGame();
            }
        });

        aboutMenu.add(aboutDeveloperItem);
        aboutMenu.add(aboutGameItem);

        return aboutMenu;
    }

    private JMenu createExitMenu() {
        JMenu exitMenu = new JMenu("Exit");

        JMenuItem returnToMenu = new JMenuItem("Return to Menu");
        returnToMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                GUI.showGUI();
                triesLeft = levelVariables.getTries(); 
            }
        });

        exitMenu.add(returnToMenu);

        return exitMenu;
    }
    
    
    public JPanel createTopBar() {
    	JPanel levelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Color customColor = levelVariables.getColor();
        levelPanel.setBackground(customColor);
        JLabel levelLabel = new JLabel(" LEVEL "+ levelVariables.getLevel() +" ");
        levelLabel.setFont(new Font("Arial", Font.BOLD, 27));
        triesLeftLabel.setFont(new Font("Arial", Font.BOLD, 27));
        levelLabel.setForeground(Color.WHITE);
        triesLeftLabel.setForeground(Color.WHITE);
        levelPanel.add(levelLabel);
        levelPanel.add(triesLeftLabel);
        
        return levelPanel;
    }
    
    public JPanel createGamePanel() {
    	GridLayout grid = new GridLayout(4,4);
        JPanel gamePanel = new JPanel(grid);
        
        Integer temp[] = {0,1,2,3,4,5,6,7,8};
        List<Integer> cardIndexes = new ArrayList<>(Arrays.asList(temp));
        Collections.shuffle(cardIndexes);


        ArrayList<Integer> selectedIndexes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int randomIndex = cardIndexes.get(i);
            selectedIndexes.add(randomIndex);
            selectedIndexes.add(randomIndex);

            Collections.shuffle(selectedIndexes);
        }

        for (int i = 0; i < 16; i++) {
            int cardIndex = selectedIndexes.get(i);
            
            JButton cardButton = new JButton(new ImageIcon(levelVariables.folderName() + "no_image.png"));
            cardButton.putClientProperty("cardIndex", cardIndex); 
            cardButton.addActionListener(new CardButtonListener(cardIndex));
            gamePanel.add(cardButton);
        }
        return gamePanel;
    }
    
    private class CardButtonListener implements ActionListener {
        private int cardIndex;

        public CardButtonListener(int cardIndex) {
            this.cardIndex = cardIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();

            if (timerRunning) {
                return;
            }

            button.setIcon(new ImageIcon(levelVariables.folderName() + cardIndex + ".png"));

            if (previousCardIndex != -1 /*&& previousButton != button*/) {
                if (isCorrectMatch(cardIndex, previousCardIndex)) {
                    matchesFound++;
                    score += levelVariables.getCorrectPoint();
                    if (matchesFound == 8) {
                        saveScore(score); 
                        showLevelUpMessage();
                    }

                    button.setEnabled(false);
                    previousButton.setEnabled(false);
                    correctMatches.add(cardIndex);
                    correctMatches.add(previousCardIndex);
                } else {
                    score -= levelVariables.getWrongPoint();
                    triesLeft--; 
                    triesLeftLabel.setText("     Tries Left : " + triesLeft);
                    Timer timer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            button.setIcon(new ImageIcon(levelVariables.folderName() + "no_image.png"));
                            previousButton.setIcon(new ImageIcon(levelVariables.folderName() + "no_image.png"));
                            if(levelVariables.getLevel() == 3) { 
                            	resetCards();
                            	}
                            timerRunning = false; // Update timer status after timer ends
                        }
                    });
                    timer.setRepeats(false); 
                    timerRunning = true; // Update timer status when timer starts
                    timer.start();
                    if (triesLeft == 0) {
                        showLostMessage(); // Show lost message when tries left reaches 0
                        dispose();
                        levelVariables = new LevelVariables(1, 0);
                        new GameScreen(levelVariables);
                    }
                }
                previousCardIndex = -1; 
            } else {
                previousCardIndex = cardIndex; 
                previousButton = button; 
            }
        }
    }


    private boolean isCorrectMatch(int firstCardIndex, int secondCardIndex) {
        return firstCardIndex == secondCardIndex;
    }

    private void saveScore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("high_scores.txt", true))) {
            writer.write(score + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showHighScores() {
        File highScoresFile = new File("high_scores.csv");

        if (!highScoresFile.exists()) {
            JOptionPane.showMessageDialog(this, "No high scores found!", "High Scores", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(highScoresFile))) {
            ArrayList<String> scores = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }

            // Puanlara göre sırala
            scores.sort((s1, s2) -> {
                int score1 = Integer.parseInt(s1.split(",")[1]);
                int score2 = Integer.parseInt(s2.split(",")[1]);
                return Integer.compare(score2, score1); // Büyükten küçüğe doğru sırala
            });

            // En fazla 10 kullanıcıyı al
            StringBuilder highScoresText = new StringBuilder();
            for (int i = 0; i < Math.min(10, scores.size()); i++) {
                String[] parts = scores.get(i).split(",");
                String username = parts[0];
                int score = Integer.parseInt(parts[1]);
                highScoresText.append(username).append(": ").append(score).append("\n");
            }

            JOptionPane.showMessageDialog(this, highScoresText.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }



    private void showAboutGame() {
        JOptionPane.showMessageDialog(this, "This is a simple memory card game.\nMatch pairs of cards to win!", "About the Game", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDeveloper() {
        JOptionPane.showMessageDialog(this, "Developer: BUGRAHAN AYDIN \nStudent Number: 20210702018 ", "About the Developer", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showLevelUpMessage() {
        
            if(levelVariables.getLevel() == 3) {
                String username = JOptionPane.showInputDialog(this, "Congratulations, you won!\nEnter your username:");
            	if (username != null && !username.isEmpty()) {
            		JOptionPane.showMessageDialog(this, "Congrats you won!!" + "\nScore: " + score , "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                	saveScoreToCSV(username, score);
                	dispose();
                	GUI.showGUI();
            	}
                else {
                    JOptionPane.showMessageDialog(this, "Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Congrats you won!!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new GameScreen(new LevelVariables(levelVariables.getLevel()+1, score));
            }
        } 



    private void showLostMessage() {
        JOptionPane.showMessageDialog(this, "You lost, try again!", "Try Again", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void resetCards() {
        JPanel gamePanel = (JPanel) getContentPane().getComponent(1); 
        ArrayList<JButton> closedCards = new ArrayList<>();
        ArrayList<JButton> openCards = new ArrayList<>();

        // Açılmamış kartları ve açık kartları ayır
        for (Component component : gamePanel.getComponents()) {
            if (component instanceof JButton) {
                JButton cardButton = (JButton) component;
                int cardIndex = (int) cardButton.getClientProperty("cardIndex");
                if (cardButton.isEnabled() && !correctMatches.contains(cardIndex)) {
                    closedCards.add(cardButton);
                } else {
                    openCards.add(cardButton);
                }
            }
        }

        
        Thread shuffleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int n = closedCards.size();
                for (int i = n - 1; i > 0; i--) {
                    int j = (int) (Math.random() * (i + 1));
                    JButton temp = closedCards.get(i);
                    closedCards.set(i, closedCards.get(j));
                    closedCards.set(j, temp);
                }

                gamePanel.removeAll();
                for (JButton card : closedCards) {
                    gamePanel.add(card);
                }
                for (JButton card : openCards) {
                    gamePanel.add(card);
                }

                gamePanel.revalidate();
                gamePanel.repaint();
            }
        });

        shuffleThread.start(); 
    }

    private void saveScoreToCSV(String username, int score) {
        try (FileWriter writer = new FileWriter("high_scores.csv", true)) {
            writer.append(username).append(",").append(String.valueOf(score)).append("\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
    	new GameScreen(new LevelVariables(1, 0));
    }
}
