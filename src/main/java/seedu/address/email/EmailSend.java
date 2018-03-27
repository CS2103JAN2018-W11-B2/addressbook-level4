package seedu.address.email;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.ReadOnlyMessageDraft;

/**
 * Handles the process of sending email via JavaAPI
 */
public class EmailSend {
    private Properties props;

    /**
     * Creates an EmailSend with an default properties
     */
    public EmailSend() {
        setUpEmailProperties();
    }

    /**
     * Sets up the default email properties
     */
    private void setUpEmailProperties() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port", "465");
    }

    /**
     * Handles sending email process
     *
     * @param emailCompose contains message to be send
     * @param emailLogin contains login details of user
     * @throws EmailLoginInvalidException if login details is empty
     * @throws EmailMessageEmptyException if message is empty
     * @throws EmailRecipientsEmptyException if recipients list is empty
     * @throws AuthenticationFailedException if gmail account can't be logged in
     */
    public void sendEmail(EmailCompose emailCompose, EmailLogin emailLogin) throws EmailLoginInvalidException,
            EmailMessageEmptyException, EmailRecipientsEmptyException, AuthenticationFailedException {

        // Verify that the email draft consists of message and subject
        if (!emailCompose.getMessage().containsContent()) {
            throw new EmailMessageEmptyException();
        }

        // Verify that the user is logged in strictly using only Gmail account.
        if (!emailLogin.isUserLoggedIn()) {
            throw new EmailLoginInvalidException();
        }

        // Verify that Recipient field is not empty
        if (emailCompose.getMessage().getRecipient().isEmpty()) {
            throw new EmailRecipientsEmptyException();
        }

        // Send out email using JavaMail API
        sendingEmail(emailLogin.getEmailLogin(), emailLogin.getPassword(), emailCompose.getMessage());
    }

    /**
     * Sends email out using JavaMail API
     *
     * @param login email login account
     * @param pass email login password
     * @param message message to send
     * @throws AuthenticationFailedException if gmail account can't be logged in
     */
    private void sendingEmail(String login, String pass, ReadOnlyMessageDraft message)
            throws AuthenticationFailedException {
        final String username = login;
        final String password = pass;

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message newMessage = new MimeMessage(session);
            newMessage.setFrom(new InternetAddress(username));
            InternetAddress recipientEmail = new InternetAddress(message.getRecipient());
            newMessage.setRecipient(Message.RecipientType.TO, recipientEmail);
            newMessage.setSubject(message.getSubject());
            newMessage.setText(message.getMessage());

            Transport.send(newMessage);
        } catch (AuthenticationFailedException e) {
            throw new AuthenticationFailedException();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
