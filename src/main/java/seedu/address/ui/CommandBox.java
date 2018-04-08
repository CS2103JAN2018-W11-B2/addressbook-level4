package seedu.address.ui;

import java.util.HashMap;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.logic.ListElementPointer;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddMovieCommand;
import seedu.address.logic.commands.AddScreeningCommand;
import seedu.address.logic.commands.AddTheaterCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DecryptCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteMovieCommand;
import seedu.address.logic.commands.DeleteTheaterCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EmailCommand;
import seedu.address.logic.commands.EncryptCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindMovieCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.JumpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private static final String KEYWORD_LABEL_BACKGROUND_COLOR = "rgba(128, 0, 128, 1)";
    private static final String DEFAULT_KEYWORD_COLOR = "white";
    private static final int KEYWORD_LABEL_FONT_SIZE = 17;
    private static final int DEFAULT_TAG_OFFSET_VALUE = 12;
    private static final double OFFSET_MULTIPLIER = 4.65;


    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;
    private ListElementPointer historySnapshot;

    private HashMap<String, String> keywordColorCode;
    private String defaultFontSize = "-fx-font-size: " + KEYWORD_LABEL_FONT_SIZE + ";";

    @FXML
    private TextField commandTextField;

    @FXML
    private Text defaultTextSetting;

    @FXML
    private StackPane stackPane;

    @FXML
    private Label keywordLabel;

    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        historySnapshot = logic.getHistorySnapshot();
        keywordColorCode = initializeKeywordColorCoding();
    }

    /**
     * Handles the key press event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
        case UP:
            // As up and down buttons will alter the position of the caret,
            // consuming it causes the caret's position to remain unchanged
            keyEvent.consume();

            navigateToPreviousInput();
            break;
        case DOWN:
            keyEvent.consume();
            navigateToNextInput();
            break;
        default:
            // let JavaFx handle the keypress
        }
    }

    /**
     * Handles the key press released event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyReleased(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
        default:
            checkChangesInCommandBoxInput();
            break;
        }
    }

    //@@author chanyikwai
    /**
     * Split text in text field into respective components for processing
     */
    private void checkChangesInCommandBoxInput() {
        String textFromTextField = commandTextField.getText();
        String[] allWordsFromText = textFromTextField.split(" ");
        String commandKeyWord = "";
        int index = 0;

        while (allWordsFromText.length > 0 && index < allWordsFromText.length && allWordsFromText[index].equals("")) {
            if (index < allWordsFromText.length) {
                index++;
            } else {
                break;
            }
            if (index < allWordsFromText.length && (!allWordsFromText[index].equals(""))) {
                break;
            }
        }

        if (index < allWordsFromText.length && allWordsFromText.length > 0) {
            commandKeyWord = allWordsFromText[index];
        }

        makeKeywordLabelNonVisible();

        if (isValidCommandKeyword(commandKeyWord)) {
            makeKeywordLabelVisible(commandKeyWord, index);
        }
        commandTextField.setStyle(defaultFontSize);
    }

    //@@author chanyikwai
    /**
     * Creates a label to replace the command keyword
     */
    private void makeKeywordLabelVisible(String commandKeyword, int offset) {
        keywordLabel.setId("commandKeywordLabel");
        keywordLabel.setText(commandKeyword);
        keywordLabel.setVisible(true);
        keywordLabel.getStyleClass().clear();
        Insets textFieldOffsets = new Insets(0, 0, 0, (offset * OFFSET_MULTIPLIER) + DEFAULT_TAG_OFFSET_VALUE);

        stackPane.setAlignment(keywordLabel, Pos.CENTER_LEFT);
        stackPane.setMargin(keywordLabel, textFieldOffsets);

        String keywordTextColor = keywordColorCode.get(commandKeyword);
        keywordLabel.setStyle("-fx-text-fill: " + keywordTextColor + ";\n"
            + "-fx-background-radius: 2;\n"
            + "-fx-font-size: " + KEYWORD_LABEL_FONT_SIZE + ";\n"
            + "-fx-background-color: " + KEYWORD_LABEL_BACKGROUND_COLOR + ";");
        keywordLabel.toFront();
    }

    /**
     * Set the keyword label to be non-visible
     */
    private void makeKeywordLabelNonVisible() {
        keywordLabel.setVisible(false);
    }

    /**
     * Check if input keyword is a valid command keyword
     * @param commandKeyword
     */
    private boolean isValidCommandKeyword(String commandKeyword) {
        return keywordColorCode.containsKey(commandKeyword);
    }

    /**
     * Updates the text field with the previous input in {@code historySnapshot},
     * if there exists a previous input in {@code historySnapshot}
     */
    private void navigateToPreviousInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasPrevious()) {
            return;
        }

        replaceText(historySnapshot.previous());
    }

    /**
     * Updates the text field with the next input in {@code historySnapshot},
     * if there exists a next input in {@code historySnapshot}
     */
    private void navigateToNextInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasNext()) {
            return;
        }

        replaceText(historySnapshot.next());
    }

    /**
     * Sets {@code CommandBox}'s text field with {@code text} and
     * positions the caret to the end of the {@code text}.
     */
    private void replaceText(String text) {
        commandTextField.setText(text);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandInputChanged() {
        try {
            CommandResult commandResult = logic.execute(commandTextField.getText());
            initHistory();
            historySnapshot.next();
            // process result of the command
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));
            makeKeywordLabelNonVisible();
        } catch (CommandException | ParseException e) {
            initHistory();
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    /**
     * Initializes the history snapshot.
     */
    private void initHistory() {
        historySnapshot = logic.getHistorySnapshot();
        // add an empty string to represent the most-recent end of historySnapshot, to be shown to
        // the user if she tries to navigate past the most-recent end of the historySnapshot.
        historySnapshot.add("");
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    //@@author chanyikwai
    /**
     * Assign keywords with color coding
     */
    public HashMap<String, String> initializeKeywordColorCoding() {
        HashMap<String, String> keywordColorCode = new HashMap<>();
        keywordColorCode.put(AddCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(AddCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(AddMovieCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(AddMovieCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(AddTheaterCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(AddTheaterCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(AddScreeningCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(AddScreeningCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(ClearCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(ClearCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(DeleteCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(DeleteCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(DeleteMovieCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(DeleteMovieCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(DeleteTheaterCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(DeleteTheaterCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(DecryptCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(DecryptCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(EditCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(EditCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(EncryptCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(EncryptCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(EmailCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(EmailCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(FindCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(FindCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(FindMovieCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(FindMovieCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(JumpCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(JumpCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(SelectCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(SelectCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(ExitCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(HelpCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(HelpCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(ListCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(ListCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(RedoCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(RedoCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(UndoCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(UndoCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(HistoryCommand.COMMAND_WORD, DEFAULT_KEYWORD_COLOR);
        keywordColorCode.put(HistoryCommand.COMMAND_ALIAS, DEFAULT_KEYWORD_COLOR);
        return keywordColorCode;
    }

}
