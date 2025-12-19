 // This class handles the questions that the player and AI can ask
public class Question {
    
    //Private attributes 
    private String question; 
    private String attribute;
    private String value; 

    //Constructor 
    public Question(String question, String attribute, String value) {
        this.question = question;
        this.attribute = attribute;
        this.value = value;
    }

    //Getter methods 
    public String getQuestion() {
        return question;
    } 

    public String getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }

    // Override toString to display question text in dropdown
    @Override
    public String toString() {
        return question; // Show question text in dropdown
    }
}



