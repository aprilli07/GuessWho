/*
 * Name: April, Lucas, Jerry, Ponnavaddn
 * Due Date: Jan 15, 2025 
 * Teacher: Mr. Chu
 * Course: ISC4U    
 * Assignemnt: Guess who ISP - Main class
 */

import java.util.ArrayList; 

// This is the main class that instatiates everything and runs the code
public class Main {
    // Initialize objects: Make characters and questions static so they can be reused
    public static ArrayList<Character> characters = new ArrayList<>();
    public static ArrayList<Question> questions = new ArrayList<>();
    public static ArrayList<Question> aiQuestions = new ArrayList<>();
    public static ArrayList<Question> guessQuestions = new ArrayList<>();
    public static Music music; 

    public static void main(String[] args) {
        // Initialize characters and questions
        initializeCharacters(); 
        initializeQuestions();  
        initializeAIQuestions(); 
        initializeGuessQuestions();

        music = new Music(); // Initialize Music 

        // Launch GUI
        new GUI(characters, questions, aiQuestions, guessQuestions, music);

    }   

    // Initialize characters
    public static void initializeCharacters() {
        characters.clear(); // Clear any existing data (important for restarting)

        characters.add(new Character("Amy", "Female", "black", "brown", true, false, false, false, false));
        characters.add(new Character("David", "male", "blonde", "brown", false, true, false, false, true));
        characters.add(new Character("Leo", "male", "white", "brown", false, false, false, false, true));
        characters.add(new Character("Gabe", "male", "brown", "brown", false, false, false, false, false));
        characters.add(new Character("Katie", "female", "blonde", "blue", false, true, false, false, false));
        characters.add(new Character("Olivia", "female", "brown", "brown", false, false, false, false, false));
        characters.add(new Character("Jordan", "male", "brown", "brown", false, false, true, true, true));
        characters.add(new Character("Carmen", "female", "white", "brown", false, false, true, false, false));
        characters.add(new Character("Laura", "female", "black", "green", false, false, true, false, false));
        characters.add(new Character("Joe", "male", "bald", "brown", true, false, false, false, true));
        characters.add(new Character("Mike", "male", "black", "brown", false, true, false, false, false));
        characters.add(new Character("Al", "male", "white", "green", true, false, false, true, true));
        characters.add(new Character("Daniel", "male", "ginger", "green", false, false, false, true, true));
        characters.add(new Character("Sophia", "female", "brown", "green", false, false, true, false, false));
        characters.add(new Character("Nick", "male", "blonde", "brown", false, false, true, false, false));
        characters.add(new Character("Lily", "female", "brown", "green", false, true, false, false, false));
        characters.add(new Character("Liz", "female", "white", "blue", true, false, false, false, false));
        characters.add(new Character("Mia", "female", "black", "brown", false, false, false, false, false));
        characters.add(new Character("Emma", "female", "ginger", "brown", false, false, false, false, false));
        characters.add(new Character("Rachel", "female", "brown", "blue", true, false, true, false, false));
        characters.add(new Character("Ben", "male", "brown", "brown", true, false, true, false, false));
        characters.add(new Character("Eric", "male", "blue", "blue", false, false, false, false, false));
        characters.add(new Character("Farah", "female", "black", "blue", false, false, false, false, false));
        characters.add(new Character("Sam", "male", "black", "green", false, true, false, false, false));
    }

    // Initialize questions
    public static void initializeQuestions() {
        questions.clear(); // Clear any existing data (important for restarting)

        questions.add(new Question("Is your character male?", "gender", "male"));
        questions.add(new Question("Is your character female?", "gender", "female"));
        questions.add(new Question("Does your character have black hair?", "hair", "black"));
        questions.add(new Question("Does your character have blonde hair?", "hair", "blonde"));
        questions.add(new Question("Does your character have white hair?", "hair", "white"));
        questions.add(new Question("Does your character have ginger hair?", "hair", "ginger"));
        questions.add(new Question("Does your character have blue hair?", "hair", "blue"));
        questions.add(new Question("Does your character have brown hair?", "hair", "brown"));
        questions.add(new Question("Does your character have brown eyes?", "eye", "brown"));
        questions.add(new Question("Does your character have blue eyes?", "eye", "blue"));
        questions.add(new Question("Does your character have green eyes?", "eye", "green"));
        questions.add(new Question("Does your character wear glasses?", "glasses", ""));
        questions.add(new Question("Is your character wearing a hat?", "hat", ""));
        questions.add(new Question("Is your character wearing jewelry?", "jewelry", ""));
        questions.add(new Question("Does your character have a beard?", "beard", ""));
        questions.add(new Question("Does your character have a mustache?", "mustache", ""));
    }

    // Initialize questions for AI (separate list)
    public static void initializeAIQuestions() {
        aiQuestions = new ArrayList<>(questions); // Make a copy of player questions for AI
    }
    
    // CODED BY: LUCAS 
    // Initialize guess questions
    public static void initializeGuessQuestions() {
        guessQuestions.clear(); // Clear any existing data (important for restarting)

        // Counted for loop to add guess questions for each character
        for (int i = 0; i < characters.size(); i++) {
            Character c = characters.get(i); // Access character by index
            String questionText = "Is " + c.getName() + " your character?"; // Format the question
            guessQuestions.add(new Question(questionText, "guess", c.getName())); // Create and add question
        }
    }
        
}






