package seedu.address.email;

//@@author chanyikwai
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import seedu.address.email.exceptions.EmailLoginInvalidException;

import org.junit.Before;
import org.junit.Test;

/**
 * Handles how user logs into email
 */
public class EmailLoginTest {
    private static final String EMAIL_VALID_LOGIN_ACCOUNT = "valid@gmail.com:123";
    private static final String EMAIL_INVALID_LOGIN_ACCOUNT = "invalid@yahoo.com:123";

    private EmailLogin emailLogin;

    @Before
    public void setUp() { emailLogin = new EmailLogin(); }

    @Test
    public void validAccountLogin() throws EmailLoginInvalidException {
        String[] EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS = EMAIL_VALID_LOGIN_ACCOUNT.split(":");
        emailLogin.loginEmail(EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS);

        assertTrue(emailLogin.isUserLoggedIn());
    }

    @Test
    public void invalidAccountLogin() {
        try {
            String[] EMAIL_INVALID_LOGIN_ACCOUNT_PARAMETERS = EMAIL_INVALID_LOGIN_ACCOUNT.split(":");
            emailLogin.loginEmail(EMAIL_INVALID_LOGIN_ACCOUNT_PARAMETERS);
        } catch (EmailLoginInvalidException e) {
            assertFalse(emailLogin.isUserLoggedIn());
        }
    }

    @Test
    public void retrieveLoginEmail() throws EmailLoginInvalidException {
        String[] EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS = EMAIL_VALID_LOGIN_ACCOUNT.split(":");
        emailLogin.loginEmail(EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS);

        assertEquals(emailLogin.getEmailLogin(), EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS[0]);
    }

    @Test
    public void retrievePassword() throws EmailLoginInvalidException {
        String[] EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS = EMAIL_VALID_LOGIN_ACCOUNT.split(":");
        emailLogin.loginEmail(EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS);

        assertEquals(emailLogin.getPassword(), EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS[1]);
    }

    @Test
    public void logoutUserEmail() throws EmailLoginInvalidException {
        String[] EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS = EMAIL_VALID_LOGIN_ACCOUNT.split(":");
        emailLogin.loginEmail(EMAIL_VALID_LOGIN_ACCOUNT_PARAMETERS);

        emailLogin.resetData();
        assertFalse(emailLogin.isUserLoggedIn());
    }
}
