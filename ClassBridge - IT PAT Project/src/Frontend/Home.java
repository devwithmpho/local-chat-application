/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Frontend;

import Backend.controllers.MessagesController;
import Backend.controllers.UsersController;
import Backend.models.Message;
import Backend.models.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Administrator
 */
public class Home extends javax.swing.JFrame {

    UsersController uc = new UsersController();
    MessagesController mc = new MessagesController();
    
    private int recipientsId;

    /**
     * Creates new form Home
     */
    public Home() {
        initComponents();
        this.setTitle("ClassBridge: " + User.LOGGED_IN_USER.getFullname());
        
        // The area where chat messages show up
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        
        jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));
        jPanel2.setPreferredSize(new Dimension(200, 40));
        
        List<User> userList = uc.getAllUsers();
        
        // Showing already existing chats between users on startup
        userList.forEach(user -> {
            if (mc.doUsersHaveMessages(User.LOGGED_IN_USER.getUserId(), user.getUserId())) {
                createUserElement(user);
            }
        });
    }
    
    // Method to regularly get the last messages from the database
    public void pollMessages(int senderId, int recipientId) {
        List<Message> newMessages = mc.getLastMessages(senderId, recipientId);
        
        newMessages.forEach(newMessage -> {
            if (User.LOGGED_IN_USER.getUserId() == newMessage.getSenderId()) {
                addMessage(newMessage.getSentAt(), newMessage.getContent());
            } else {
                String userEmail = uc.getUserEmailById(recipientId);
                User recipient = uc.getUser(userEmail);
                addMessage(newMessage.getSentAt(), recipient.getName(), newMessage.getContent());
            }
        });
    }
    
    // Method to create the UI 
    public void createUserElement(User user) {
        // creating the container that for the actual contact element
        JPanel contactContainer = new JPanel();
        contactContainer.setName(user.getEmailAddress());
        contactContainer.setBackground(new java.awt.Color(230, 230, 230));
        contactContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        contactContainer.setLayout(new BorderLayout());

        JLabel contactName = new JLabel(user.getType() + ": " + user.getFullname());
        contactContainer.add(contactName, BorderLayout.CENTER);

        int height = 40;
        int width = 200;
        contactContainer.setPreferredSize(new Dimension(width, height));
        contactContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));

        Color normalColor = new java.awt.Color(230, 230, 230);
        Color hoverColor = new java.awt.Color(200, 200, 255);

        // Creating methods to add a hover effect to the actual container
        contactContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                contactContainer.setBackground(hoverColor);
                contactContainer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                contactContainer.setBackground(normalColor);
                contactContainer.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });

        // displaying the chats between the logged in user and the selected user when their contact card is created
        contactContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                chatArea.setText("");
                jLabel7.setText(user.getFullname());
                String userEmail = contactContainer.getName();
                recipientsId = uc.getUserId(userEmail);

                // Getting all the messages between the users and displaying them in the chat display
                List<Message> messages = mc.getAllMessagesBetweenUsers(User.LOGGED_IN_USER.getUserId(), recipientsId);
                messages.forEach(message -> {
                    if (User.LOGGED_IN_USER.getUserId() == message.getSenderId()) {
                        addMessage(message.getSentAt(), message.getContent());
                    } else {
                        addMessage(message.getSentAt(), user.getName(), message.getContent());
                    }
                });
            }
        });

        jPanel2.add(contactContainer);
        
        jPanel2.revalidate();
        jPanel2.repaint();
    }
    
    /* 
        The methods that follow handle displaying the messages in the chat area
    */
    
    // Adding the users message in the database to the UI
    // This adds messages sent by the current user that already exist in the databse
    public void addMessage(LocalDateTime sentAt, String message) {
        chatArea.append("[" + sentAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ") + "] Me: " + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
    
    // Adding the users message to the UI and database
    // This adds messages that the current user that they want to send to the user now
    public void addMessage(LocalDateTime sentAt, String message, int recipientId) {
        chatArea.append("[" + sentAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ") + "] Me: " + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
        mc.sendMessage(User.LOGGED_IN_USER.getUserId(), recipientsId, message);
    }
    
    // Adding the recipients message in the database to the UI
    // This adds messages that the user being chatted with has already sent
    public void addMessage(LocalDateTime sentAt, String firstName, String message) {
        chatArea.append("[" + sentAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ") + "] " + firstName + ": " + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        inputField = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        userToolTip = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel7)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane3.setViewportView(chatArea);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(inputField))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        userToolTip.setText("User");
        userToolTip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userToolTipActionPerformed(evt);
            }
        });

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setText("Add New Chat");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        userToolTip.add(jMenuItem2);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setText("Sign Out");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        userToolTip.add(jMenuItem1);

        jMenuBar1.add(userToolTip);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // The action for logging out of the application
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        User.LOGGED_IN_USER = null;
        
        this.dispose();
        
        Login login = new Login();
        login.setVisible(true);
        
        System.out.println("Signed out!");
        
        // Removing any emails from the textFile
        Main.writeUserToFile("");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    // The action for chatting to a new user in the application
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        String emailAddress = JOptionPane.showInputDialog("Enter the users Email Address:");
        
        UsersController uc = new UsersController();
        
        // checking that the user exists
        if (emailAddress != null && uc.userExists(emailAddress)) {
            createUserElement(uc.getUser(emailAddress));
        } else if (emailAddress == null ) {
            System.out.println("No email address entered");
        } else {
            JOptionPane.showMessageDialog(this, "User not found!");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void userToolTipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userToolTipActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userToolTipActionPerformed

    // actually sending messages to recipient
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        // TODO add your handling code here:
        String message = inputField.getText().trim();
        
        if (!message.isEmpty()) {
            addMessage(LocalDateTime.now(), message, recipientsId);
            inputField.setText("");
            
            // checking for any previous sent messages by the recipient to display them
            // This is done by constantly doing the check every minute
            new Timer(1000, event -> pollMessages(User.LOGGED_IN_USER.getUserId(), recipientsId)).start();
        }
    }//GEN-LAST:event_sendButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatArea;
    private javax.swing.JTextField inputField;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton sendButton;
    private javax.swing.JMenu userToolTip;
    // End of variables declaration//GEN-END:variables
}
