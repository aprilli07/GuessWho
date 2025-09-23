import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;                

// This class handles the GUI of the game as well as switching between player turns (implements all the methods from other classes)
public class GUI {

    // Variables
    private String[] characterNames = { // Character names matching image filenames
            "Amy", "David", "Leo", "Gabe", "Katie", "Olivia",
            "Jordan", "Carmen", "Laura", "Joe", "Mike", "Al",
            "Daniel", "Sophia", "Nick", "Lily", "Liz", "Mia",
            "Emma", "Rachel", "Ben", "Eric", "Farah", "Sam"
    };

    // Game variables 
    private ArrayList<Character> characters; // List of characters 
    private ArrayList<Question> questions; // List of questions 
    private ArrayList<Question> guessQuestions; // List of guess character questions
    private Gameboard gameboard; // Gameboard instance 
    private AI aiInstance; // Create an AI object 
    private ArrayList<Character> playerCharacters; // Player's active characters
    private ArrayList<Character> aiCharacters; // AI's active characters
    private Character ai; // AI's selected character 
    private String selectedCharacter; // Stores player's selected character
    private JLabel selectedCharacterLabel; // Displays selected character image
    private boolean characterSelected = false; // Tracks whether a character has been selected
    private Question question; // Stores the selected question that the player asks
    private ArrayList<String> eliminatedCharacters = new ArrayList<>(); // Stores eliminated characters
    private Question lastAIQuestion; // Tracks AI's last question
    private boolean lastAIAnswer;   // Tracks AI's last answer
    private boolean isDarkTheme = false; // Sets initial theme to light 
    private int playerGuesses = 0; // Tracks the number of questions asked by the player
    private String username;
    private ArrayList<Question> originalQuestions; // Original list of questions (used when resetting game in the middle)
    private ArrayList<Question> originalGuessQuestions; // Original list of guess questions (used when resetting game in the middle)

    // GUI components 
    private JFrame boardFrame; 
    private JComboBox<Question> questionDropdown; 
    private JComboBox<Question> guessDropdown; 
    private JButton guessButton; 
    private JButton askButton;
    private JPanel gridPanel;
    private JLabel questionLabel;
    private JPanel sidePanel;
    private JPanel bottomPanel; 
    private JPanel askPanel;
    private JPanel guessPanel;
    private JLabel turn;
    private JButton settingsButton;
    private JLabel score;

    // Music variable 
    private Music music;

    // Constructor 
    public GUI(ArrayList<Character> characters, ArrayList<Question> questions, ArrayList<Question> aiQuestions, ArrayList<Question> guessQuestions, Music music) {
        // Initialize characters and questions 
        this.characters = characters;
        this.questions = questions;
        this.guessQuestions = guessQuestions;
        this.originalQuestions = new ArrayList<>(questions); // Create a copy of the questions for the game (if you reset in the middle of the game)
        this.originalGuessQuestions = new ArrayList<>(guessQuestions); // Create a copy of the character questions for the game (if you reset in the middle of the game)
        this.aiInstance = new AI(new ArrayList<>(aiQuestions)); // Create AI instance with a separate copy of AI questions
        this.music = music;
        
        // Open welcome screen 
        welcomeScreen();
    }

    // Welcome screen GUI components  
    private void welcomeScreen() {
        // AI Selects a Character
        ai = aiInstance.aiCharacter(characters); // AI randomly selects a character
        System.out.println("AI selected: " + ai.getName()); // Debugging output
        playerCharacters = new ArrayList<>(characters); // Initialize player's character list
        aiCharacters = new ArrayList<>(characters);  // Initialize AI's character list 
        gameboard = new Gameboard(); // Initialize gameboard 

        // Create the Welcome Frame
        JFrame frame = new JFrame("Guess Who - Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 350);
        frame.setBackground(new Color(66, 121, 161));
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        // Title Label
        JLabel titleLabel = new JLabel("Welcome to Guess Who!", JLabel.CENTER);
        titleLabel.setBackground(new Color(66, 121, 161));
        titleLabel.setForeground(new Color(255, 210, 8));
        titleLabel.setFont(new Font("Futura", Font.BOLD, 45));
        titleLabel.setOpaque(true);
        titleLabel.setBorder(new EmptyBorder(20, 20, 0, 20));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(66, 121, 161));
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 20));
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Start Button
        JButton startButton = new JButton("Start");
        startButton.setForeground(new Color(38, 20, 71));
        startButton.setFont(new Font("Futura", Font.PLAIN, 30));
        startButton.setFocusPainted(false);
        buttonPanel.add(startButton);

        // Rules Button
        JButton rulesButton = new JButton("Rules / How to Play");
        rulesButton.setForeground(new Color(38, 20, 71));
        rulesButton.setFont(new Font("Futura", Font.PLAIN, 25));
        rulesButton.setFocusPainted(false);
        buttonPanel.add(rulesButton);

        // Leaderboard Button
        JButton leaderBoardButton = new JButton("Leaderboard");
        leaderBoardButton.setForeground(new Color(38, 20, 71));
        leaderBoardButton.setFont(new Font("Futura", Font.PLAIN, 30));
        leaderBoardButton.setFocusPainted(false);
        buttonPanel.add(leaderBoardButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        // Start Button Action
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                music.buttonClick("buttonClick.wav"); // Button sound 
                frame.dispose(); // Close welcome window
                openGameBoard(); // Open the gameboard
            }
        });

        // Rules Button Action
        rulesButton.addActionListener(new ActionListener() {
            // Display rules using a JOptionPane 
            @Override
            public void actionPerformed(ActionEvent e) {
                music.buttonClick("buttonClick.wav");
                JOptionPane.showMessageDialog(frame,
                        "How to Play:\n\n1. Choose a character for the computer to guess.\n" +
                                "2. Take turns asking yes/no questions to eliminate characters.\n" +
                                "3. The first to correctly guess the opponent's character wins!\n" +
                                "4. You can guess the character at any turn, but be careful! \n    If you guess wrong, you lose automatcially!\n" +
                                "5. Each question asked is will be added to your total guesses. \n    Ask the least amount of questions to be on the leaderboard!",
                        "Game Rules",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Leaderboard Button Action
        leaderBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                music.buttonClick("buttonClick.wav");
                // Display leaderboard by calling methods 
                Leaderboard.loadGuesses();
                Leaderboard.displayLeaderboard();
            }
        });

        // Display the Welcome Frame
        frame.setVisible(true);
    }

    // Gameboard Window 
    private void openGameBoard() {
        music.backgroundMusic("backgroundMusic.wav");
        // Create the Board Frame
        boardFrame = new JFrame("Guess Who - Gameboard");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setBackground(new Color(66, 121, 161));
        boardFrame.setSize(1100, 900); // Enlarged window size
        boardFrame.setLayout(new BorderLayout());

        // Title Label
        JLabel title = new JLabel("Guess Who - Gameboard", JLabel.CENTER);
        title.setBackground(new Color(66, 121, 161));
        title.setForeground(new Color(255, 210, 8));
        title.setFont(new Font("Futura", Font.BOLD, 65));
        title.setOpaque(true);
        title.setBorder(new EmptyBorder(20, 20, 20, 20));
        boardFrame.add(title, BorderLayout.NORTH);

        // Gameboard Panel - 6x4 Grid 
        gridPanel = new JPanel();
        gridPanel.setBackground(new Color(66, 121, 161));
        gridPanel.setLayout(new GridLayout(4, 6, 15, 15)); // 4 rows, 6 columns
        gridPanel.setBorder(new EmptyBorder(0, 20, 0, 5));

        // Side Panel for displaying the selected character
        sidePanel = new JPanel();
        sidePanel.setBackground(new Color(66, 121, 161));
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBorder(new EmptyBorder(20, 5, 20, 10)); 

        selectedCharacterLabel = new JLabel(); // Placeholder for selected character image
        selectedCharacterLabel.setHorizontalAlignment(JLabel.CENTER);
        sidePanel.add(selectedCharacterLabel, BorderLayout.CENTER);
        boardFrame.add(sidePanel, BorderLayout.EAST);

        // Add buttons with images for each character
        for (int i = 0; i < characterNames.length; i++) { 
            String name = characterNames[i]; // Access each name by index
            JButton charButton = new JButton();

            // Load and resize the image for each character
            ImageIcon icon = new ImageIcon("Characters/" + name + ".png"); // Load image (e.g. "Amy.png")
            Image img = icon.getImage().getScaledInstance(100, 120, Image.SCALE_SMOOTH); // Larger size
            charButton.setIcon(new ImageIcon(img)); // Set the resized image as the button icon
            charButton.setToolTipText(name); // Tooltip with character name
            charButton.setPreferredSize(new Dimension(120, 150)); 

            // Style the button (no border for cleaner look)
            charButton.setBorderPainted(false);
            charButton.setFocusPainted(false);
            charButton.setContentAreaFilled(false);

            // Add button to grid panel
            gridPanel.add(charButton);

            // Action Listener for Button Click
            charButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { 
                    music.buttonClick("buttonClick.wav");
                    // If player already chose a character and the button is already disabled, exit early (no error message)
                    if (!charButton.isEnabled()) {
                        return; // Simply ignore further clicks 
                    }
                    
                    // Save the selected character
                    selectedCharacter = name;
                    characterSelected = true; // Mark a character as selected
                    askButton.setEnabled(true); // Enable the Ask button
                    guessButton.setEnabled(true); // Enable the guess character button 

                    // Display the selected character's image on the right panel
                    ImageIcon selectedIcon = new ImageIcon("Characters/" + name + ".png");
                    Image selectedImg = selectedIcon.getImage().getScaledInstance(200, 250, Image.SCALE_SMOOTH); 
                    turn = new JLabel("Your Turn", JLabel.CENTER);
                    turn.setForeground(new Color(255, 210, 8));
                    turn.setFont(new Font("Futura", Font.BOLD, 45));
                    score = new JLabel(" Guesses: " + Integer.toString(playerGuesses), JLabel.CENTER);
                    score.setForeground(new Color(255, 210, 8));
                    score.setFont(new Font("Futura", Font.BOLD, 45));
                    sidePanel.add(turn, BorderLayout.NORTH);
                    sidePanel.add(score, BorderLayout.SOUTH);
                    selectedCharacterLabel.setIcon(new ImageIcon(selectedImg));
                    sidePanel.setBorder(new EmptyBorder(20, 5, 20, 20));

                    // Confirmation popup
                    JOptionPane.showMessageDialog(boardFrame,
                            "You chose: " + selectedCharacter,
                            "Character Selected",
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    // Switch to AI's turn
                    aiTurn();
                     
                    // Disable all buttons after character selection
                    for (int i = 0; i < gridPanel.getComponentCount(); i++) {
                        JButton button = (JButton) gridPanel.getComponent(i); // Directly cast to JButton
                        button.setDisabledIcon(button.getIcon()); // Keep the icon visible (used so that the button doesn't turn grey)
                        button.setEnabled(false); // Disable the button
                    }

                    // Next turn popup 
                    JOptionPane.showMessageDialog(boardFrame, 
                            "It's your turn! Choose a question from the dropdown to ask.",
                            "Your Turn",
                            JOptionPane.INFORMATION_MESSAGE); 
                }
            });
        }

        // Add grid panel to the board frame
        boardFrame.add(gridPanel, BorderLayout.CENTER);

        // Dropdown for Questions
        bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(66, 121, 161));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.setLayout(new GridLayout(2, 1, 10, 10));

        askPanel = new JPanel();
        askPanel.setBackground(new Color(66, 121, 161));
        
        questionLabel = new JLabel("Ask a Question:");   
        questionLabel.setFont(new Font("Futura", Font.PLAIN, 30));
        questionLabel.setForeground(Color.white);
        
        questionDropdown = new JComboBox<>(questions.toArray(new Question[0]));
        questionDropdown.setFont(new Font("Futura", Font.PLAIN, 20));
        questionDropdown.setForeground(new Color(38, 20, 71));
        
        askButton = new JButton("Ask");
        askButton.setFont(new Font("Futura", Font.PLAIN, 20));
        askButton.setForeground(new Color(38, 20, 71));
        askButton.setEnabled(false); // Disable initially (You don't want the user to ask a question before choosing a character)

        askPanel.add(questionLabel);
        askPanel.add(questionDropdown);
        askPanel.add(askButton);

        // Dropdown for guessing characters
        guessPanel = new JPanel();
        guessPanel.setBackground(new Color(66, 121, 161));
 
        JLabel guessLabel = new JLabel("Guess a Character:");
        guessLabel.setFont(new Font("Futura", Font.PLAIN, 30));
        guessLabel.setForeground(Color.white);

        guessDropdown = new JComboBox<>(guessQuestions.toArray(new Question[0])); // Populate dropdown with all character names
        guessDropdown.setFont(new Font("Futura", Font.PLAIN, 20));
        guessDropdown.setForeground(new Color(38, 20, 71));

        guessButton = new JButton("Guess");
        guessButton.setFont(new Font("Futura", Font.PLAIN, 20));
        guessButton.setForeground(new Color(38, 20, 71));
        guessButton.setEnabled(false); // Initially disabled until character is selected

        guessPanel.add(guessLabel);
        guessPanel.add(guessDropdown);
        guessPanel.add(guessButton);

        // Settings Button
        settingsButton = new JButton();
        ImageIcon icon = new ImageIcon("ButtonIcons/blackSettingIcon.png"); // Load icon from file

        // Resize icon
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        settingsButton.setIcon(resizedIcon);

        // Style button for cleaner look 
        settingsButton.setOpaque(false); 
        settingsButton.setContentAreaFilled(false); 
        settingsButton.setBorderPainted(false);
        settingsButton.setFocusPainted(false);

        bottomPanel.add(askPanel); 
        bottomPanel.add(guessPanel); 
        guessPanel.add(settingsButton);

        boardFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Settings button action listener
        settingsButton.addActionListener(e -> openSettingsWindow());

        // Player turn to ask question 
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                music.buttonClick("buttonClick.wav");
                // Increment question count and update score for both actions
                playerGuesses++;
                score.setText(" Guesses: " + playerGuesses);
        
                // Check if the askButton was clicked
                if (e.getSource() == askButton) {
                    // Store selected question
                    Question selectedQuestion = (Question) questionDropdown.getSelectedItem();
        
                    // Display confirmation message 
                    JOptionPane.showMessageDialog(boardFrame,
                            "You asked: " + selectedQuestion.getQuestion(),
                            "Question Asked",
                            JOptionPane.INFORMATION_MESSAGE);
        
                    //Remove question and update dropdown
                    questions.remove(selectedQuestion);
                    updateDropdown();
        
                    // Compare player's question with AI's chosen character
                    boolean match = compareWithAICharacter(selectedQuestion);

                     // Remove characters based on question match
                    playerCharacters = Gameboard.removeCharacter(playerCharacters, selectedQuestion.getAttribute(), selectedQuestion.getValue(), match);
                    
                    // Track eliminated characters using a counted loop
                    for (int i = 0; i < characters.size(); i++) { // Loop through all characters
                        Character c = characters.get(i); // Get the character by index

                        // If the character is NOT in playerCharacters, mark as eliminated
                        boolean eliminated = true;
                        for (int j = 0; j < playerCharacters.size(); j++) {
                            if (c.equals(playerCharacters.get(j))) { // Check if character still exists
                                eliminated = false; // It's not eliminated
                                break; // Exit the inner loop early
                            }
                        }

                        // Add eliminated characters to the list
                        if (eliminated && !eliminatedCharacters.contains(c.getName())) { // Avoid duplicates
                            eliminatedCharacters.add(c.getName()); // Add eliminated character
                        }
                    }

                    // Update eliminated images and guess character dropdown 
                    updateEliminatedCharacterButtons();
                    updateGuessDropdown();

                    // Print remaining player characters
                    System.out.println("Remaining Player Characters:");
                    for (int i = 0; i < playerCharacters.size(); i++) { 
                        System.out.println(playerCharacters.get(i).getName()); 
                    }
        
                    // Check win condition
                    if (gameboard.checkWinCondition(playerCharacters)) {
                        Leaderboard.updateGuesses(username, playerGuesses); // Update leaderboard
                        gameboard.endGame("Player", ai, selectedCharacter); // Player wins
                        return; // Stop further execution
                    }
                }
        
                // Check if the guessButton was clicked
                else if (e.getSource() == guessButton) {
                    // Get selected character from dropdown
                    Question guess = (Question) guessDropdown.getSelectedItem();
                    String guessedCharacter = guess.getValue(); // Extract the character name
        
                    if (guessedCharacter.equals(ai.getName())) { // Player guessed AI's character correctly
                        Leaderboard.updateGuesses(username, playerGuesses); // Update leaderboard
                        gameboard.endGame("Player", ai, selectedCharacter); // Player wins
                    } else {
                        // Player guessed wrong 
                        gameboard.endGame("AI - wrong Guess", ai, selectedCharacter); //AI wins
                    }
                    return; // Exit immediately after guessing
                }
        
                // Switch to AI's turn if no win condition met
                gameboard.switchTurn();
                aiTurn();
            }
        };
        
        // Add ActionListener to both buttons
        askButton.addActionListener(buttonListener);
        guessButton.addActionListener(buttonListener);

        // Display Board Frame
        boardFrame.setVisible(true);

        // Prompt the user for their username with validation loop at the beginning of game 
        username = null;
        while (username == null || username.trim().isEmpty()) {
            username = JOptionPane.showInputDialog(
                    boardFrame,
                    "Enter your username:",
                    "Username Entry",
                    JOptionPane.QUESTION_MESSAGE);

            // Validate username input
            if (username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        boardFrame,
                        "Username cannot be empty. Please enter a valid username.",
                        "Invalid Username",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // Popup for initial character selection
        JOptionPane.showMessageDialog(boardFrame,
                "Please choose a character for the computer to guess!",
                "Character Selection",
                JOptionPane.INFORMATION_MESSAGE);

    }

    // AI Turn
    private void aiTurn() {
        if (!gameboard.isPlayerTurn()) { // AI's turn
            turn.setText(" AI's Turn "); // Change display text to show that it's AI's turn

            // AI selects a question 
            Question aiQuestion = aiInstance.selectQuestion(aiCharacters, lastAIQuestion, lastAIAnswer);

            // Keep showing the dialog until the correct answer is selected (Cannot lie when answering question)
            boolean validAnswer = false; // Tracks whether the answer is valid
            boolean answer = false; 
            while (!validAnswer) { // Loop until valid input
                // Display AI's question with Yes/No options
                int userInput = JOptionPane.showOptionDialog(
                    boardFrame,
                    "AI asks: " + aiQuestion.getQuestion(), 
                    "AI's Turn", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE,
                    null, 
                    null, 
                    null 
                );

                // Set answer to true if YES is selected, otherwise false
                answer = (userInput == JOptionPane.YES_OPTION);

                // Check if the player's response is valid
                validAnswer = isValidAnswer(aiQuestion, answer);

                // If invalid, stay in the loop (dialog won't close until valid)
            }

            lastAIQuestion = aiQuestion; // Store last question
            lastAIAnswer = answer;       // Store last answer

            // Remove characters based on question match 
            aiCharacters = Gameboard.removeCharacter(aiCharacters, aiQuestion.getAttribute(), aiQuestion.getValue(), answer);

            // Print remaining AI characters using a counted loop
            System.out.println("Remaining AI characters:");
            for (int i = 0; i < aiCharacters.size(); i++) { 
                Character character = aiCharacters.get(i); 
                System.out.println(character.getName());   
            }

            // Check win condition for AI
            if (gameboard.checkWinCondition(aiCharacters)) { 
                gameboard.endGame("AI", ai, selectedCharacter); // AI wins
                return; // Stop further execution
            }

            // Once a valid answer is selected, switch back to the player's turn
            gameboard.switchTurn();
            turn.setText("Your Turn");
        }
    }
    
    /*
     * Compares the player's question with the AI's chosen character's attributes.
     *
     * @param question The question asked by the player, containing an attribute and a value to check.
     * @return `true` if the AI's character matches the attribute and value in the question, otherwise `false`.
     */
    private boolean compareWithAICharacter(Question question) {
        // Check if question is about gender (the same for all other attributes)
        if (question.getAttribute().equals("gender")) {
            // Compare AI character's gender with the question value
            return ai.getGender().equalsIgnoreCase(question.getValue());
        } else if (question.getAttribute().equals("hair")) {
            return ai.getHairColour().equalsIgnoreCase(question.getValue());
        } else if (question.getAttribute().equals("eye")) {
            return ai.getEyeColour().equalsIgnoreCase(question.getValue());
        } else if (question.getAttribute().equals("glasses")) {
            return ai.hasGlasses(); 
        } else if (question.getAttribute().equals("hat")) {
            return ai.hasHat();
        } else if (question.getAttribute().equals("jewelry")) {
            return ai.hasJewelry();
        } else if (question.getAttribute().equals("beard")) {
            return ai.hasBeard();
        } else if (question.getAttribute().equals("mustache")) {
            return ai.hasMustache();
        }
        return false; // Default case (should never happen)
    }

    /*
    * Validates the AI's question by checking if the player's selected character's attributes match the question's condition.
    *
    * @param question The question asked by the AI, containing an attribute and a value to verify.
    * @param answer The player's response to the AI's question (true for "Yes", false for "No").
    * @return `true` if the answer matches the player's selected character's attribute, otherwise `false`.
    */
    private boolean isValidAnswer(Question question, boolean answer) {
        // Retrieve the player's selected character based on their choice
        Character playerCharacter = Gameboard.chosenCharacter(characters, selectedCharacter);
        // Check if question is about gender (the same for all other attributes)
        if (question.getAttribute() == "gender") {
            // Compare player's gender with the question value and match it with the given answer
            return answer == (playerCharacter.getGender().equals(question.getValue()));
        } else if (question.getAttribute() == "hair") {
            return answer == (playerCharacter.getHairColour().equals(question.getValue()));
        } else if (question.getAttribute() == "eye") {
            return answer == (playerCharacter.getEyeColour().equals(question.getValue()));
        } else if (question.getAttribute() == "glasses") {
            return answer == playerCharacter.hasGlasses();
        } else if (question.getAttribute() == "hat") {
            return answer == playerCharacter.hasHat();
        } else if (question.getAttribute() == "jewelry") {
            return answer == playerCharacter.hasJewelry();
        } else if (question.getAttribute() == "beard") {
            return answer == playerCharacter.hasBeard();
        } else if (question.getAttribute() == "mustache") {
            return answer == playerCharacter.hasMustache();
        }

        return false;
    }   

    // Remove questions player already asked in dropdown 
    private void updateDropdown() {
        questionDropdown.removeAllItems(); // Clear dropdown
    
        // Repopulate with remaining questions  
        for (int i = 0; i < questions.size(); i++) { 
            questionDropdown.addItem(questions.get(i)); // Add each question to dropdown
        }
    }

    // Remove characters that are eliminated in the guess character dropdown 
    private void updateGuessDropdown() {
        guessDropdown.removeAllItems(); // Clear dropdown
    
        // Repopulate with remaining characters 
        for (int i = 0; i < playerCharacters.size(); i++) {
            Character c = playerCharacters.get(i); 
            guessDropdown.addItem(new Question("Is " + c.getName() + " your character?", "guess", c.getName())); // Add each question to dropdown
        }
    }
    
    // Updates eliminated character buttons with new icons 
    private void updateEliminatedCharacterButtons() {
        for (int i = 0; i < characterNames.length; i++) { // Loop through all characters 
            String name = characterNames[i]; // Get the name 
            // If the name is found within the elimated characters, change the button icon 
            if (eliminatedCharacters.contains(name)) {
                JButton charButton = (JButton) gridPanel.getComponent(i); // Get the corresponding button 
                ImageIcon icon = new ImageIcon("Remove_Characters/" + name + "X.png"); // Replace with new image 
                Image img = icon.getImage().getScaledInstance(100, 120, Image.SCALE_SMOOTH);
                charButton.setPreferredSize(new Dimension(120, 150)); 
                charButton.setIcon(new ImageIcon(img)); 
                charButton.setDisabledIcon(new ImageIcon(img)); 
                charButton.setEnabled(false); // Ensure button remains disabled
            }
        }
    }

    // Restart the game in the middle of a current game
    private void resetGame(){
        // Reset game variables
        selectedCharacter = null;
        selectedCharacterLabel.setIcon(null);
        characterSelected = false;
        eliminatedCharacters.clear();
        playerGuesses = 0;

        // Reinitialize player and AI character lists
        playerCharacters = new ArrayList<>(characters);
        aiCharacters = new ArrayList<>(characters);

        // Reset player questions 
        questions = new ArrayList<>(originalQuestions);
        guessQuestions = new ArrayList<>(originalGuessQuestions);

        // Reset AI questions
        aiInstance = new AI(new ArrayList<>(originalQuestions));

        // Recreate the gameboard
        gameboard = new Gameboard();

        // Re-enable grid panel buttons
        for (int i = 0; i < gridPanel.getComponentCount(); i++) {
            JButton button = (JButton) gridPanel.getComponent(i);
            button.setEnabled(true);
            button.setIcon(new ImageIcon("Characters/" + characterNames[i] + ".png"));
        }

        // Reset question dropdown
        updateDropdown();
        updateGuessDropdown();
    }

    // Settings button frame  
    private void openSettingsWindow(){
        // Create a dialog for settings 
        JDialog settingsDialog = new JDialog(boardFrame, "Settings", true);
        settingsDialog.setSize(300,200);
        settingsDialog.setLayout(new BorderLayout());
        settingsDialog.setResizable(false);

        // Title label for settings 
        JLabel settingsLabel = new JLabel("Settings", JLabel.CENTER);
        settingsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        settingsDialog.add(settingsLabel, BorderLayout.NORTH);

        // Panel to hold the buttons (Restart, Theme Toggle, Music Toggle)
        JPanel buttonPanel = new JPanel();

        // Restart button
        JButton restartButton = new JButton("Restart Game");
        buttonPanel.add(restartButton);

        // Theme toggle button
        JToggleButton themeToggle = new JToggleButton("Enable Dark Theme");
        buttonPanel.add(themeToggle);

        // Music toggle button
        JToggleButton musicToggle = new JToggleButton("Toggle Music");
        buttonPanel.add(musicToggle);

        // Apply initial theme state
        themeToggle.setSelected(isDarkTheme);
        if (isDarkTheme){
            themeToggle.setText("Enable Light Theme");
        }else{
            themeToggle.setText("Enable Dark Theme");
        }

        settingsDialog.add(buttonPanel, BorderLayout.CENTER);

        // Restart button action listener
        restartButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                music.buttonClick("buttonClick.wav");
                boardFrame.dispose(); // Close the gameboard window
                resetGame(); // Reset all variables and restart
                welcomeScreen(); // Opens the welcome screen
                settingsDialog.dispose(); // Close the settings dialog
            }
        });

        // Theme toggle action listener
        themeToggle.addActionListener(new ActionListener() {
            @Override
            public  void actionPerformed(ActionEvent e){
                music.buttonClick("buttonClick.wav"); // Button sound 
                isDarkTheme = themeToggle.isSelected(); 
                if(isDarkTheme){
                    themeToggle.setText("Enable Light Theme"); // Light mode
                }else{
                    themeToggle.setText("Enable Dark Theme"); // Dark mode 
                }
                applyTheme();
            }
        });

        // Music toggle button action listener
        musicToggle.addActionListener(e -> {
            music.buttonClick("buttonClick.wav"); // Button sound 
            music.toggleMusic(); // Toggle music 
            // Update the button text based on the current state
            if (musicToggle.isSelected()) {
                musicToggle.setText("Toggle Music Off"); // Show "Off" when music is playing
            } else {
                musicToggle.setText("Toggle Music On"); // Show "On" when music is stopped
            }
        });

        settingsDialog.setVisible(true);
    }

    // Method to apply the selected theme to the game
    private void applyTheme() {
        // Declare theme colors
        Color bgColor;

        // Define light and dark theme colors
        if (isDarkTheme) {
            bgColor = Color.BLACK;
        } else {
            bgColor = new Color(66, 121, 161);
        }

        // Update background colors
        boardFrame.getContentPane().setBackground(bgColor);
        gridPanel.setBackground(bgColor);
        bottomPanel.setBackground(bgColor);
        sidePanel.setBackground(bgColor);
        askPanel.setBackground(bgColor);
        guessPanel.setBackground(bgColor);

        // Update title label background
        Component[] components = boardFrame.getContentPane().getComponents(); // Store all components of boardFrame into an array 
        for (Component comp : components) { // Loop through all components 
            if (comp instanceof JLabel) { // Link: https://www.javatpoint.com/downcasting-with-instanceof-operator
                JLabel label = (JLabel) comp;
                if ("Guess Who - Gameboard".equals(label.getText())) {
                    label.setBackground(bgColor); // Update background colour 
                }
            }
        }

        // Update settings icon theme
        String iconPath;
        if(isDarkTheme){
            iconPath = "ButtonIcons/whiteSettingIcon.png"; // Light theme settings icon
        }else{
            iconPath = "ButtonIcons/blackSettingIcon.png"; // Dark theme settings icon
        }

        // Resize the icon and set it to the settings button
        ImageIcon newIcon = new ImageIcon(iconPath);
        Image img = newIcon.getImage();
        Image resizedImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImg);

        settingsButton.setIcon(resizedIcon);
    }
    
}
