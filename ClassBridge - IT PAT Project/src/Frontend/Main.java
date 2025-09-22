package Frontend;

import Backend.controllers.UsersController;
import Backend.models.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    // On startup, the application checks if there is a previously logged in user using a text file
    // If there is no email address or the email address does not belong to any users in the users table in the database, then the signup page is shown
    // If there is an email and it belongs to a user in the users table, then that user will automatically be logged in
    public static void main(String[] args) {
        UsersController uc = new UsersController();
        
        // reading the file that contains logged in users to do the check of whether to have a user already logged in or make the user sign up
        try {
            File loggedInUserFile = new File("loggedInUser.txt");
            Scanner scFile = new Scanner(loggedInUserFile);
            
            if (scFile.hasNext()) {
                String line = scFile.nextLine();
                Scanner scLine = new Scanner(line).useDelimiter("#");
                
                if (scLine.hasNext()) {
                    String emailAddress = scLine.next();
                    
                    // checking if the user exists
                    if (uc.getUser(emailAddress) != null) {
                        // setting the global logged in user to the user that was found
                        User.LOGGED_IN_USER = uc.getUser(emailAddress);
                        System.out.println("Logged in!");
                        Home home = new Home();
                        home.setVisible(true);
                    } else {
                        System.out.println("Unable to login to: " + emailAddress);
                        Signup su = new Signup();
                        su.setVisible(true);
                    }
                } else {
                    System.out.println("No logged in user found!");
                    Signup su = new Signup();
                    su.setVisible(true);
                }
            } else {
                System.out.println("No logged in user found!");
                Signup su = new Signup();
                su.setVisible(true);
            }
            
            scFile.close();
        } catch (IOException IOe) {
            System.out.println("Error reading file: " + IOe);
        }
    }
    
    // Writing a users email to the text file
    // This is used on startup, if there is already an email in the text file, then there is likely a previously logged in user
    public static void writeUserToFile(String email) {
        try {
            FileWriter fileWriter = new FileWriter(new File("loggedInUser.txt"));
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(email);
            
            writer.close();
            fileWriter.close();
            
            System.out.println("Wrote user to file;");
        } catch (IOException IOe) {
            System.out.println("Error writing user to file: " + IOe);
        }
    }
}
