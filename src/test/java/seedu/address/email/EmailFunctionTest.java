package seedu.address.email;

//@@author chanyikwai
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EmailFunctionTest {

    private static final String EMAIL_VALID_FUNCTION = "send";
    private static final String EMAIL_INVALID_FUNCTION = "do nothing";

    private EmailFunction emailFunction;

    @Before
    public void setUp() {
        emailFunction = new EmailFunction();
    }

    @Test
    public void valid() {
        emailFunction.setEmailFunction(EMAIL_VALID_FUNCTION);
        assertTrue(emailFunction.isValid());
    }

    @Test
    public void notValid() {
        emailFunction.setEmailFunction(EMAIL_INVALID_FUNCTION);
        assertFalse(emailFunction.isValid());
    }
}
