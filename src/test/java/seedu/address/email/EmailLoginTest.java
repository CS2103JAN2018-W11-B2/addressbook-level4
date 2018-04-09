package seedu.address.email;

//@@author chanyikwai
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import seedu.address.email.exceptions.EmailLoginInvalidException;

/**
 * Handles how user logs into email
 */
public class EmailLoginTest {

    private static final String EMAIL_VALID_LOGIN_ACCOUNT = "valid@gmail.com:123";
    private static final String EMAIL_INVALID_LOGIN_ACCOUNT = "invalid@yahoo.com:123";

    private EmailLogin emailLogin;

    @Before
    public void setUp() {
        emailLogin = new EmailLogin();
    }

    @Test
    public void validAccountLogin() throws EmailLoginInvalidException {
        String[] validEmailLogin = EMAIL_VALID_LOGIN_ACCOUNT.split(":");
        emailLogin.loginEmail(validEmailLogin);

        assertTrue(emailLogin.isUserLoggedIn());
    }

    @Test
    public void invalidAccountLogin() {
        try {
            String[] invalidEmailLogin = EMAIL_INVALID_LOGIN_ACCOUNT.split(":");
            emailLogin.loginEmail(invalidEmailLogin);
        } catch (EmailLoginInvalidException e) {
            assertFalse(emailLogin.isUserLoggedIn());
        }
    }

    @Test
    public void retrieveLoginEmail() throws EmailLoginInvalidException {
        String[] validEmailLogin = EMAIL_VALID_LOGIN_ACCOUNT.split(":");
        emailLogin.loginEmail(validEmailLogin);

        assertEquals(emailLogin.getEmailLogin(), validEmailLogin[0]);
    }

    @Test
    public void retrievePassword() throws EmailLoginInvalidException {
        String[] validEmailLogin = EMAIL_VALID_LOGIN_ACCOUNT.split(":");
        emailLogin.loginEmail(validEmailLogin);

        assertEquals(emailLogin.getPassword(), validEmailLogin[1]);
    }

    @Test
    public void logoutUserEmail() throws EmailLoginInvalidException {
        String[] validEmailLogin = EMAIL_VALID_LOGIN_ACCOUNT.split(":");
        emailLogin.loginEmail(validEmailLogin);

        emailLogin.resetData();
        assertFalse(emailLogin.isUserLoggedIn());
    }
}
