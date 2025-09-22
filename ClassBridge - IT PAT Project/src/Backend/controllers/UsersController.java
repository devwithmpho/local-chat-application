package Backend.controllers;

import Backend.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersController {
    protected Connection conn;
    protected PreparedStatement pst;
    protected ResultSet rs;

    public UsersController() {
        DbController dbCon = new DbController();
        conn = dbCon.getConnection();
    }

    // Getting a users hashed password from the database
    private String getStoredHash(String inEmail) {
        String storedHash = "";

        try {
            String sql = "SELECT HashedPassword FROM tblUsers WHERE EmailAddress = ?";

            pst = conn.prepareStatement(sql);
            pst.setString(1, inEmail);
            rs = pst.executeQuery();

            if (rs.next()) {
                storedHash = rs.getString("HashedPassword");
            }

            rs.close();
            pst.close();
        } catch (SQLException SQLe) {
            System.out.println("Error fetching hash: " + SQLe);
        }

        return storedHash;
    }

    // Checking if a user exists
    public Boolean userExists(String email) {
        boolean doesUserExist = false;

        try {
            if (!email.isEmpty()) {
                String sql = "SELECT EmailAddress FROM tblUsers WHERE EmailAddress = ?";

                pst = conn.prepareStatement(sql);
                pst.setString(1, email);

                rs = pst.executeQuery();

                // if any rows are returned
                if (rs.next()) {
                    doesUserExist = true;
                } else {
                    System.out.println("User does not exist!");
                }
            } else {
                System.out.println("Email address cannot be empty!");
            }

            rs.close();
            pst.close();
        } catch (SQLException SQLe) {
            System.out.println("Error fetching user: " + SQLe);
        }

        return doesUserExist;
    }
    
    // Getting the object of a user after checking that their email address and password match the ones in the table
    public User getAuthenticatedUser(String inEmail, String inPassword) {
        User returnedUser = null;

        try {
            if (userExists(inEmail)) {
                String storedHash = getStoredHash(inEmail);

                // Using the bcrypt library to check the password against the hashed password
                if (BCrypt.checkpw(inPassword, storedHash)) {
                    String sql = "SELECT * FROM tblUsers WHERE EmailAddress = ? AND HashedPassword = ?";

                    pst = conn.prepareStatement(sql);
                    pst.setString(1, inEmail);
                    pst.setString(2, storedHash);

                    rs = pst.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String surname = rs.getString(3);
                        String emailAddress = rs.getString(4);
                        String hashedPassword = rs.getString(5);
                        String type = rs.getString(6);

                        returnedUser = new User(id, name, surname, emailAddress, hashedPassword, type);
                    }
                } else {
                    System.out.println("Password does not match!");
                }
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException SQLe) {
            System.out.println("Error getting user: " + SQLe);
        }

        return returnedUser;
    }

    // Gets the user object but without checking that the passwords match
    public User getUser(String inEmail) {
        User returnedUser = null;

        try {
            if (userExists(inEmail)) {
                String sql = "SELECT * FROM tblUsers WHERE EmailAddress = ?";

                    pst = conn.prepareStatement(sql);
                    pst.setString(1, inEmail);

                    rs = pst.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String surname = rs.getString(3);
                        String emailAddress = rs.getString(4);
                        String hashedPassword = rs.getString(5);
                        String type = rs.getString(6);

                        returnedUser = new User(id, name, surname, emailAddress, hashedPassword, type);
                    }
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException SQLe) {
            System.out.println("Error getting user: " + SQLe);
        }

        return returnedUser;
    }

    // Getting all non sensitive information from a user
    public String getUserPublicData(String email) {
        String publicInfo = "";

        try {
            if (!email.isEmpty() && userExists(email)) {
                String sql = "SELECT FirstName, Surname, emailAddress, Type FROM tblUsers WHERE EmailAddress = ?";

                pst = conn.prepareStatement(sql);
                pst.setString(1, email);

                rs = pst.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("FirstName");
                    String surname = rs.getString("Surname");
                    String emailAddress = rs.getString("EmailAddress");
                    String type = rs.getString("Type");

                    publicInfo = name + "#" + surname + "#" + emailAddress + "#" + type;
                } else {
                    System.out.println("User not found!");
                }
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users public information!");
        }

        return publicInfo;
    }

    // Using the email address of a user to get their ID
    public int getUserId(String email) {
        int userId = 0;

        try {
            if (!email.isEmpty()) {
                if (userExists(email)) {
                    String sql = "SELECT UserID FROM tblUsers WHERE emailAddress = ?";

                    pst = conn.prepareStatement(sql);
                    pst.setString(1, email);

                    rs = pst.executeQuery();

                    if (rs.next()) {
                        userId = rs.getInt(1);
                    } else {
                        System.out.println("Failed to get user ID");
                    }
                } else {
                    System.out.println("User with email '" + email + "' does not exist!");
                }
            } else {
                System.out.println("Email Address cannot be empty");
            }
        } catch (SQLException e) {
            System.out.println("Error getting user ID: " + e);
        }

        return userId;
    }

    // Using the users ID to get their ID
    public String getUserEmailById(int id) {
        String userEmail = "";

        try {
            if (id != 0) {
                String sql = "SELECT EmailAddress FROM tblUsers WHERE UserID = ?";

                pst = conn.prepareStatement(sql);
                pst.setInt(1, id);

                rs = pst.executeQuery();

                if (rs.next()) {
                    userEmail = rs.getString(1);
                } else {
                    System.out.println("Failed to get user email!");
                }
            } else {
                System.out.println("ID cannot be 0");
            }
        } catch (SQLException e) {
            System.out.println("Error getting user ID: " + e);
        }

        return userEmail;
    }

    // Returning an arraylist of all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            String sql = "SELECT * FROM tblUsers";
            pst = conn.prepareStatement(sql);
            
            rs = pst.executeQuery();

            while (rs.next()) {
                users.add(new User(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                ));
            }
        } catch (SQLException SQLe) {
            System.out.println("Error getting all users: " + SQLe);
        }

        return users;
    }

    // Creating a new user in the database
    public void createUser(String name, String surname, String emailAddress, String password, String type) {
        try {
            if (!userExists(emailAddress)) {
                String sql = "INSERT INTO tblUsers (FirstName, Surname, EmailAddress, HashedPassword, Type) VALUES(?, ?, ?, ?, ?)";

                // Hasing the users password using the bcrypt library and a salt of 12
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

                pst = conn.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, surname);
                pst.setString(3, emailAddress);
                pst.setString(4, hashedPassword);
                pst.setString(5, type);

                int rowsAffected = pst.executeUpdate();
                System.out.println(rowsAffected + " row inserted");

                System.out.println("User created successfully!");

                pst.close();
            } else {
                System.out.println("User already exists!");
            }
        } catch (SQLException SQLe) {
            System.out.println("Error creating user: " + SQLe);
        }
    }

    // deleting the user from the databse
    public void deleteUser(int rowID) {
        try {
            String sql = "DELETE FROM tblUsers WHERE UserID = ?";

            pst = conn.prepareStatement(sql);
            pst.setInt(1, rowID);

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User Deleted!");
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException SQLe) {
            System.out.println("Error deleting row: " + SQLe);
        }
    }
}
