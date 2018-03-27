package seedu.address.email.message;

/**
 * Represents a Email Draft Message in MVP.
 */
public class MessageDraft implements ReadOnlyMessageDraft {

    private String message;
    private String subject;
    private String recipient;

    public MessageDraft() {
        message = "";
        subject = "";
    }

    public MessageDraft(String message, String subject, String recipient) {
        this.message = message;
        this.subject = subject;
        this.recipient = recipient;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getRecipient() {
        return recipient;
    }

    public void setRecipients(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public boolean containsContent() {
        if (message.isEmpty() || subject.isEmpty() || recipient.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyMessageDraft // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyMessageDraft) other));
    }
}
