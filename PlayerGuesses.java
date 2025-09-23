// This class helps track name and number of guesses used in the leaderboard 
public class PlayerGuesses {
    private String playerName; // Player name 
    private int guesses; // Number of player guesses / questions asked 

    // Constructor 
    public PlayerGuesses(String playerName, int guesses) {
        this.playerName = playerName; // Initialize the player's name
        this.guesses = guesses; // Initialize the player's guess count 
    }

    // Getter methods 
    public String getPlayerName() {
        return playerName;
    }

    public int getGuesses() {
        return guesses;
    }

    // Setter method 
    public void setGuesses(int guesses) {
        this.guesses = guesses;
    }
}


