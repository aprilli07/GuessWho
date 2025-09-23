import javax.sound.sampled.*; // Audio components
import java.io.File; // File handling

// This class handles sounds 
public class Music {
    private Clip clip; // Sound effects 
    private Clip backgroundClip; // Background music 
    private boolean isPlaying; // Boolean to track whether background music is currently playing

    // Constructor 
    public Music() {
        // Initialize and play background music 
        backgroundMusic("backgroundMusic.wav"); 
    }

    // Link to how to play audio: https://www.geeksforgeeks.org/play-audio-file-using-java/ and https://www.baeldung.com/java-play-sound
    /*
     * Method to play background music
     * @param file path of the music file 
     */
    public void backgroundMusic(String filePath) {
        try {
            // if music is already playing, exit 
            if (isPlaying) return; 

            // Load the sound file
            File musicFile = new File(filePath); // Path to the sound file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile); // Create an audio stream 

            // Create and open the audio clip
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);

            // Loop the clip continuously
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start(); // Start playing the music

            // Music is playing 
            isPlaying = true;

        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    // Method to stop the background music
    public void stopMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) { // Check if the music is running 
            backgroundClip.stop(); // Stop the clip
            isPlaying = false; // Update state
        }
    }

    // Method to toggle music
    public void toggleMusic() {
        if (isPlaying) {
            stopMusic(); // Stop music if its currently playing 
        } else {
            backgroundMusic("backgroundMusic.wav"); // Start playing music if it's not already playing 
        }
    }

    /* 
     * Mehthod for button click sound effect 
     * @param file path of sound you want to play 
    */
    public void buttonClick(String filePath) {
        try {
            // Load the sound file
            File musicFile = new File(filePath); // Path to the sound file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile); // Create an audio stream 

            // Create and open the audio clip
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // Play sound once 

        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    } 
}


