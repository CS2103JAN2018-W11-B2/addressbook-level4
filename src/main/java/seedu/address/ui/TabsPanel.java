package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.cinema.Cinema;

/**
 * Left Tab Panel containing cinemaListPanel and EmailDraftPanel
 */
public class TabsPanel extends UiPart<Region> {
    private static final String FXML = "TabsPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TabsPanel.class);

    private CinemaListPanel cinemaListPanel;
    private EmailMessagePanel emailMessageDisplay;
    private int tabIndex;

    @FXML
    private TabPane tabsPane;

    @FXML
    private Tab cinemaListTab;

    @FXML
    private Tab emailDraftTab;

    @FXML
    private StackPane cinemaListPanelPlaceholder;

    @FXML
    private StackPane messageDraftPanelPlaceholder;

    public TabsPanel(ObservableList<Cinema> cinemaList) {
        super(FXML);

        cinemaListPanel = new CinemaListPanel(cinemaList);
        cinemaListPanelPlaceholder.getChildren().add(cinemaListPanel.getRoot());

        emailMessageDisplay = new EmailMessagePanel();
        messageDraftPanelPlaceholder.getChildren().add(emailMessageDisplay.getRoot());

        tabIndex = 0;
    }

    /**
     * Toggles Tabs in Main Window by referencing index
     */
    public void toggleTabs(int index) {
        if (index >= 0) {
            tabIndex = index;
        } else {
            tabIndex = (tabIndex + 1) % 2;
        }

        switch (tabIndex) {
        case 0:
            tabsPane.getSelectionModel().select(cinemaListTab);
            break;
        case 1:
            tabsPane.getSelectionModel().select(emailDraftTab);
            break;
        default:
            assert false : "It should not be possible to reach here.";
            break;
        }
    }

    public CinemaListPanel getCinemaListPanel() {
        return this.cinemaListPanel;
    }
}
