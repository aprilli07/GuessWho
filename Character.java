// This class is for all the characters in the game 
public class Character {
    // Private Attributes
    private String name, gender, hairColour, eyeColour;
    private boolean glasses, hat, jewelry, beard, mustache;

    // Constructor
    public Character(String name, String gender, String hairColour, String eyeColour, boolean glasses, boolean hat, boolean jewelry, boolean beard, boolean mustache) {
        this.name = name;
        this.gender = gender;
        this.hairColour = hairColour;
        this.eyeColour = eyeColour;
        this.glasses = glasses;
        this.hat = hat;
        this.jewelry = jewelry;
        this.beard = beard;
        this.mustache = mustache;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getHairColour() {
        return hairColour;
    }

    public String getEyeColour() {
        return eyeColour;
    }

    public boolean hasGlasses() {  
        return glasses;
    }

    public boolean hasHat() {  
        return hat;
    }

    public boolean hasJewelry() {
        return jewelry;
    }

    public boolean hasBeard() {
        return beard;
    }

    public boolean hasMustache() {
        return mustache;
    }
    
} 




