package seedu.address.email;

/**
 * Keeps track of called Email Function
 */
public class EmailFunction {
    public static final String EMAIL_FUNCTION_SEND = "send";
    public static final String EMAIL_FUNCTION_CLEAR = "clear";

    private String emailFunction;

    public EmailFunction() {
        emailFunction = "";
    }

    public EmailFunction(String task) {
        this.emailFunction = task;
    }

    public String getEmailFunction() {
        return emailFunction;
    }

    public void setEmailFunction(String task) {
        this.emailFunction = task;
    }

    /**
     * Returns true if task is valid
     */
    public boolean isValid() {
        switch (emailFunction) {
        case EMAIL_FUNCTION_SEND:
        case EMAIL_FUNCTION_CLEAR:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailFunction // instanceof handles nulls
                && this.emailFunction.equals(((EmailFunction) other).emailFunction));
    }
}
