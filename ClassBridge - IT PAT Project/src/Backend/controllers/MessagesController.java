package Backend.controllers;

import Backend.models.Message;
import Backend.models.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessagesController {
    protected UsersController uc = new UsersController();

    protected Connection conn;
    protected PreparedStatement pst;
    protected ResultSet rs;
    
    private int lastMessageId = 0;

    public MessagesController() {
        DbController dbCon = new DbController();
        conn = dbCon.getConnection();
    }

    // Adding a new message to the databse
    public void sendMessage(int senderId, int recipientId, String message) {
        try {
            if (recipientId != 0 && uc.userExists(uc.getUserEmailById(recipientId))) {
                if (!message.equals("")) {
                    String sql = "INSERT INTO tblMessages (SenderID, RecipientID, Message) VALUES (?, ?, ?)";

                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, senderId);
                    pst.setInt(2, recipientId);
                    pst.setString(3, message);

                    int rowsAffected = pst.executeUpdate();

                    System.out.println(rowsAffected + " row(s) affected!");
                    
                    // getting the newly created message to store the lastCreatedMessage
                    // This is used so that when messages are fetched, only recent ones are fetched rather than all messages
                    sql = "SELECT MessageID FROM tblMessages WHERE SenderID = ? AND RecipientID = ? ORDER BY SentAt";
                    
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, senderId);
                    pst.setInt(2, recipientId);
                    
                    rs = pst.executeQuery();
                    
                    while (rs.next()) {
                        lastMessageId = rs.getInt(1);
                    }
                    
                } else {
                    System.out.println("Message cannot be empty!");
                }
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error sending message: " + e);
        }
    }

    // The actual methood that handled getting recent messages rather than all messages
    public List<Message> getLastMessages(int senderId, int recipientId) {
        List<Message> lastMessages = new ArrayList();
        
        try {
            String sql = "SELECT * FROM tblMessages " + 
                    "WHERE MessageID > ? AND " + 
                    "(SenderID = ? AND RecipientID = ?)" +
                    "ORDER BY MessageID";

            pst = conn.prepareStatement(sql);
            pst.setInt(1, lastMessageId);
            pst.setInt(2, senderId);
            pst.setInt(3, recipientId);

            rs = pst.executeQuery();

            while (rs.next()) {
                String senderEmail = uc.getUserEmailById(rs.getInt(2));
                User sender = uc.getUser(senderEmail);
                
                String recipientEmail = uc.getUserEmailById(rs.getInt(3));
                User recipient = uc.getUser(recipientEmail);
                
                String content = rs.getString(4);
                
                Timestamp ts = rs.getTimestamp(5);
                LocalDateTime sentAt = ts.toLocalDateTime();
                
                lastMessages.add(new Message(
                        sender,
                        recipient,
                        content,
                        sentAt
                ));
                
                lastMessageId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting last messages: " + e);
        }
        
        return lastMessages;
    }
    
    // Getting a list of all the messages between 2 users
    // This is used to get the message history upon startup
    public List<Message> getAllMessagesBetweenUsers(int senderId, int recipientId) {
        List<Message> messages = new ArrayList<>();
        UsersController uc = new UsersController();

        try {
            String sql = "SELECT * FROM tblMessages WHERE (SenderID = ? AND RecipientID = ?) OR (SenderID = ? AND RecipientID = ?)";
            
            pst = conn.prepareStatement(sql);
            pst.setInt(1, senderId);
            pst.setInt(2, recipientId);
            pst.setInt(3, recipientId);
            pst.setInt(4, senderId);
            
            rs = pst.executeQuery();

            while (rs.next()) {
                String senderEmail = uc.getUserEmailById(rs.getInt(2));
                User sender = uc.getUser(senderEmail);
                
                String recipientEmail = uc.getUserEmailById(rs.getInt(3));
                User recipient = uc.getUser(recipientEmail);
                
                String content = rs.getString(4);
                
                Timestamp ts = rs.getTimestamp(5);
                LocalDateTime sentAt = ts.toLocalDateTime();
                
                messages.add(new Message(
                        sender,
                        recipient,
                        content,
                        sentAt
                ));
            }
        } catch (SQLException SQLe) {
            System.out.println("Error getting all messages: " + SQLe);
        }

        return messages;
    }
    
    // Checking whether users already have existing messages
    // This is used when a user is logged in to establish which other users the logged in user has communicated with
    public Boolean doUsersHaveMessages(int senderId, int recipientId) {
        Boolean doUsersHaveMessages = false;
        
        try {
            if ((senderId != 0 && uc.userExists(uc.getUserEmailById(senderId))) && (recipientId != 0 && uc.userExists(uc.getUserEmailById(recipientId)))) {
                String sql = "SELECT * from tblMessages WHERE (SenderID = ? AND RecipientID = ?) OR (SenderID = ? AND RecipientID = ?)";
                
                pst = conn.prepareStatement(sql);
                pst.setInt(1, senderId);
                pst.setInt(2, recipientId);
                pst.setInt(3, recipientId);
                pst.setInt(4, senderId);
                
                rs = pst.executeQuery();
                
                if (rs.next()) {
                    doUsersHaveMessages = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking if users have messages: " + e);
        }
        
        return doUsersHaveMessages;
    }
}
