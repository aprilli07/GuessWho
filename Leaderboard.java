/*
 * Name: April, Lucas, Jerry, Ponnavaddn
 * Due Date: Jan 15, 2025 
 * Teacher: Mr. Chu
 * Course: ISC4U 
 * Assignemnt: Guess who ISP - Leaderboard class   
 */

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;

// This class handles the leaderboard GUI and updating the leaderboard after each game 
public class Leaderboard {

    // File to store all leaderboard data 
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    
    // Maintain guesses in an ArrayList
    private static ArrayList<PlayerGuesses> guessesList = new ArrayList<>();

    // Display leaderboard GUI 
    public static void displayLeaderboard() {
        // Create the frame
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Title panel with background color and title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(45, 85, 135)); // Blue background
        JLabel titleLabel = new JLabel("Leaderboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Add title panel to frame
        frame.add(titlePanel, BorderLayout.NORTH);

        // Panel for leaderboard entries
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leaderboardPanel.setBackground(new Color(235, 240, 250)); // Light background

        // Get sorted guesses
        List<PlayerGuesses> sortedGuesses = getAllGuessesSorted();

        // Add each player's guesses with styled labels
        for (int i = 0; i < sortedGuesses.size(); i++) {
            PlayerGuesses ps = sortedGuesses.get(i);

            // Create a panel for each player entry
            JPanel playerPanel = new JPanel();
            playerPanel.setLayout(new BorderLayout());
            playerPanel.setBackground(new Color(255, 255, 255)); // White background for entries
            playerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1), // Light border
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding
            ));

            // Player's rank
            JLabel rankLabel = new JLabel((i + 1) + ". ", JLabel.LEFT);
            rankLabel.setFont(new Font("Arial", Font.BOLD, 18));
            rankLabel.setForeground(new Color(45, 85, 135)); 

            // Player's name
            JLabel nameLabel = new JLabel(ps.getPlayerName(), JLabel.LEFT);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nameLabel.setForeground(Color.DARK_GRAY);

            // Player's guesses
            JLabel guessesLabel = new JLabel(String.valueOf(ps.getGuesses()), JLabel.RIGHT);
            guessesLabel.setFont(new Font("Arial", Font.BOLD, 16));
            guessesLabel.setForeground(new Color(34, 139, 34)); 

            // Add labels to player panel
            playerPanel.add(rankLabel, BorderLayout.WEST);
            playerPanel.add(nameLabel, BorderLayout.CENTER);
            playerPanel.add(guessesLabel, BorderLayout.EAST);

            // Add player panel to leaderboard panel
            leaderboardPanel.add(playerPanel);
            leaderboardPanel.add(Box.createVerticalStrut(10)); // Add spacing between entries
        }

        // Add leaderboard panel to a scroll pane just in case the list is long
        JScrollPane scrollPane = new JScrollPane(leaderboardPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane, BorderLayout.CENTER);

        // Footer panel with a close button
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(45, 85, 135)); 
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        closeButton.setForeground(Color.BLACK);
        closeButton.setBackground(new Color(200, 50, 50)); 
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> frame.dispose());
        footerPanel.add(closeButton);

        // Add footer panel to frame
        frame.add(footerPanel, BorderLayout.SOUTH);

        // Show the frame
        frame.setVisible(true);

    }

    // Loads guesses from a file using Scanner
    public static void loadGuesses() {
        guessesList.clear(); // Clear the list before loading 
        File file = new File(LEADERBOARD_FILE);
        if (!file.exists()) { // If file doesn't exist, do nothing
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim(); // Read and trim each line
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\s+"); // Split line into player name and guesses
                    if (parts.length == 2) {
                        String playerName = parts[0];
                        int guesses = Integer.parseInt(parts[1]); // Convert guesses to an integer 
                        guessesList.add(new PlayerGuesses(playerName, guesses)); // Add to the list
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Saves guesses to a file using PrintWriter and overwrites old data in the file
    public static void saveGuesses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEADERBOARD_FILE))) {
            for (PlayerGuesses playerGuesses : guessesList) {
                writer.println(playerGuesses.getPlayerName() + " " + playerGuesses.getGuesses()); // Write name and guesses
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Updates the leaderboard by adding the players name and guesses 
     *
     * @param playerName The name of the player to update or add.
     * @param newGuesses The number of guesses to record for the player.
     */
    public static void updateGuesses(String playerName, int guesses) {
        guessesList.add(new PlayerGuesses(playerName, guesses)); // Add a new entry for the player with their guesses
        saveGuesses(); // Save the updated leaderboard to the file
    }

    /*
    * Retrieves the number of guesses for a specific player.
    *
    * @param playerName The name of the player whose guesses are to be retrieved.
    * @return The number of guesses the player has made, or 0 if the player is not found.
    */
    public static int getGuesses(String playerName) {
        for (PlayerGuesses playerGuesses : guessesList) {
            if (playerGuesses.getPlayerName().equalsIgnoreCase(playerName)) {
                return playerGuesses.getGuesses(); 
            }
        }
        return 0;
    }

    // Returns a sorted copy of the leaderboard in ascending order by guesses
    public static List<PlayerGuesses> getAllGuessesSorted() {
        List<PlayerGuesses> sortedList = new ArrayList<>(guessesList); // Create a copy of the guesses

        // Bubble Sort Algorithm to sort in ascending order
        for (int i = 0; i < sortedList.size() - 1; i++) {
            for (int j = 0; j < sortedList.size() - i - 1; j++) {
                if (sortedList.get(j).getGuesses() > sortedList.get(j + 1).getGuesses()) {
                    // Swap elements
                    PlayerGuesses temp = sortedList.get(j);
                    sortedList.set(j, sortedList.get(j + 1));
                    sortedList.set(j + 1, temp);
                }
            }
        }

        return sortedList; // Return sorted list
    }
}