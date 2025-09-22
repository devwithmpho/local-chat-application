package Backend.models;

import java.time.LocalDateTime;

public class Message {
    private String messageID;
    private User sender;
    private User recipient;
    private String content;
    private LocalDateTime sentAt;

    public Message (User inSender, User inRecipient, String inContent, LocalDateTime inSentAt) {
        sender = inSender;
        recipient = inRecipient;
        content = inContent;
        sentAt = inSentAt;
    }

    public int getSenderId() {
        return sender.getUserId();
    }
    public int getRecipientID() {
        return recipient.getUserId();
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    @Override
    public String toString() {
        return "Sender ID: " + getSenderId() + "\n"
                + "Recipient ID: " + getRecipientID() + "\n"
                + "Message: " + getContent() + "\n"
                + "Sent At: " + getSentAt();
    }
}
