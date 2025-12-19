 import java.awt.Window;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;   

// This class handles the basic game functions 
public class Gameboard {
    
    //Variables 
    private boolean isPlayerTurn = true; // Player starts first
    private boolean gameOver = false;   // Track if the game has ended

    // Constructor
    public Gameboard() {
        this.isPlayerTurn = true; // Player starts first
        this.gameOver = false;   // Game is not over initially
    }

    // Switch Turns
    public void switchTurn() {
        isPlayerTurn = !isPlayerTurn; // Toggle between player and AI
    }

    // Check if it's Player's Turn
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    /*
     * Void method that ends the game and the player can either restart, view the leaderboard, or exit the game.
     *
     * @param winner The winner of the game 
     * @param ai The AI's selected character object, used to display the AI's character name if the player wins.
     * @param selectedCharacter The player's selected character, used to display in the message if the AI wins.
     */
    public void endGame(String winner, Character ai, String selectedCharacter) {
        gameOver = true; // Mark the game as over
        String winMessage = "";

        //Show winning message 
        if (winner.equalsIgnoreCase("AI")) { // AI wins 
            winMessage = "AI wins! They gessed your character " + selectedCharacter;
        } else if (winner.equalsIgnoreCase("AI - wrong Guess")) {
            winMessage = "You guessed wrong! You lose! The AI's character was " + ai.getName(); // AI wins because player guessed wrong character 
        } else if (winner.equalsIgnoreCase("Player")) { // Player wins 
            winMessage = "You win! You guessed the AI's character " + ai.getName();
        } 

        JOptionPane.showMessageDialog(null,
                winMessage,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        
        // Create an array of options for the dialog
        Object[] options = {"Restart", "Exit", "Leaderboard"};
        
        // Show a JOptionPane with the given options and store the user's choice
        int choice = JOptionPane.showOptionDialog(null,
                "Do you want to play again?",
                "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]); // Default selection is "Restart"
    
        // Handle user's choice
        if (choice == 0) { // Restart
            // Close the current GUI window
            Window[] windows = Window.getWindows(); // Get all open windows
            for (int i = 0; i < windows.length; i++) { 
                if (windows[i] instanceof JFrame) {
                    windows[i].dispose(); // Close each JFrame (so that the game starts fresh)
                }
            }
    
            // Reinitialize characters and questions
            Main.initializeCharacters();
            Main.initializeQuestions();  
    
            // Restart the game - reinitialize GUI with the same characters and questions
            SwingUtilities.invokeLater(() -> {
                new GUI(Main.characters, Main.questions, Main.aiQuestions, Main.guessQuestions, Main.music); // Reinitialize GUI using Main's lists
            });
        } else if (choice == 1 || choice == JOptionPane.CLOSED_OPTION ) { // Exit
            System.exit(0); // Exit the program
        } else if (choice == 2 ) { // Leaderboard
            Leaderboard.loadGuesses();
            Leaderboard.displayLeaderboard();
        }

    }

    /*
     * Checks if the win condition is met and retrieves the player's/AI's chosen character
     * 
     * @param characters The list of characters to check for the win condition
     * @return returns true if there is only one character remaining in the list, indicating the win condition is met, else return false
     */
    public boolean checkWinCondition(ArrayList<Character> characters) {
        return characters.size() == 1; // True if only 1 character remains
    }

    /*
     * Retrieves the `Character` object that matches the given name from a list of characters.
     * This is needed so that you can compare attributes with questions asked 
     *
     * @param characters The list of `Character` objects to search through.
     * @param selectedName The name of the character to find
     * @return The `Character` object that matches the `selectedName`, or `null` if no match is found.
     */
    public static Character chosenCharacter(ArrayList<Character> characters, String selectedName) {
        // Loop through all characters using a counted for loop
        for (int i = 0; i < characters.size(); i++) { 
            Character c = characters.get(i); // Get character at index i
            
            // Check if the character's name matches the selected name
            if (c.getName().equalsIgnoreCase(selectedName)) { 
                return c; // Return the matched character
            }
        }
        return null; // Return null if no match is found
    } 

    /*
     * Filters a list of characters based on a specified attribute and value.
     *
     * @param characters The list of `Character` objects to be filtered.
     * @param attribute  The attribute to check (e.g., "gender", "hair", "eye").
     * @param value      The value of the attribute to match (e.g., "male", "blonde", "blue").
     * @param matches    A boolean indicating whether to keep characters that match (`true`) or don't match (`false`) the criteria.
     * @return A filtered `ArrayList<Character>` containing only the characters that are left after elimination 
     */
    // Method to remove characters depending on the question asked and the characteristics of character chosen 
    public static ArrayList<Character> removeCharacter(ArrayList<Character> characters, String attribute, String value, boolean matches) {
        // Create a temporary list to store characters to remove
        ArrayList<Character> toRemove = new ArrayList<>();
    
        // Loop through all characters using a regular for loop
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i); // Get the current character
            boolean match = false;
    
            // Check the attribute for each character
            if (attribute.equalsIgnoreCase("gender")) {
                match = c.getGender().equalsIgnoreCase(value);
            } else if (attribute.equalsIgnoreCase("hair")) {
                match = c.getHairColour().equalsIgnoreCase(value);
            } else if (attribute.equalsIgnoreCase("eye")) {
                match = c.getEyeColour().equalsIgnoreCase(value);
            } else if (attribute.equalsIgnoreCase("glasses")) {
                match = c.hasGlasses();
            } else if (attribute.equalsIgnoreCase("hat")) {
                match = c.hasHat();
            } else if (attribute.equalsIgnoreCase("jewelry")) {
                match = c.hasJewelry();
            } else if (attribute.equalsIgnoreCase("beard")) {
                match = c.hasBeard();
            } else if (attribute.equalsIgnoreCase("mustache")) {
                match = c.hasMustache();
            }
    
            // Remove characters that don't meet the match condition
            if ((matches && !match) || (!matches && match)) {

                toRemove.add(c); // Add to removal list
            }
        }
    
        // Remove all characters marked for removal
        characters.removeAll(toRemove);
    
        // Return the filtered list
        return characters;
    }

}