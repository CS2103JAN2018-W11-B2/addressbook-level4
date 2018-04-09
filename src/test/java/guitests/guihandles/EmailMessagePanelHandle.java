package guitests.guihandles;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import seedu.address.model.cinema.Cinema;
import seedu.address.ui.CinemaCard;

/**
 * Provides a handle for {@code CinemaListPanel} containing the list of {@code CinemaCard}.
 */
public class EmailMessagePanelHandle extends NodeHandle<TextArea> {
    public static final String EMAIL_PANE_VIEW_ID = "#emailPane";
    public static final String EMAIL_MESSAGE_VIEW_ID = "#messageArea";
    public static final String EMAIL_SUBJECT_VIEW_ID = "#subjectArea";
    public static final String EMAIL_RECIPIENTS_VIEW_ID = "#recipientsArea";

    private final TextArea messageArea;
    private final TextArea subjectArea;
    private final TextArea recipientsArea;

    public EmailMessagePanelHandle(TextArea emailMessagePanelNode) {
        super(emailMessagePanelNode);

        this.messageArea = getChildNode(EMAIL_MESSAGE_VIEW_ID);
        this.subjectArea = getChildNode(EMAIL_SUBJECT_VIEW_ID);
        this.recipientsArea = getChildNode(EMAIL_RECIPIENTS_VIEW_ID);
    }

    /**
     * Returns the text in the message text area.
     */
    public String getMessage() { return messageArea.getText(); }

    public String getSubject() { return subjectArea.getText(); }

    public String getRecipients() { return recipientsArea.getText(); }

}
