package Backend.models;

public class User {
    private int userId;
    private String name;
    private String surname;
    private String emailAddress;
    private String hashedPassword;
    private String type;

    public static User LOGGED_IN_USER;
    
    public User(int inId, String inName, String inSurname, String inEmailAddress, String inHashedPassword, String inType) {
        userId = inId;
        name = inName;
        surname = inSurname;
        emailAddress = inEmailAddress;
        hashedPassword = inHashedPassword;
        type = inType;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullname() {
        return getName() + " " + getSurname();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Full Name: " + getFullname() + "\n"
                + "Email Address: " + getEmailAddress() + "\n"
                + "Type: " + getType();
    }
}
