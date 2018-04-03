package seedu.address.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.DetailedDayView;
import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.CinemaPanelSelectionChangedEvent;
import seedu.address.commons.events.ui.JumpToDateRequestEvent;
import seedu.address.commons.events.ui.ReloadBrowserPanelEvent;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.Theater;
import seedu.address.model.screening.Screening;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String FXML = "BrowserPanel.fxml";
    private static final String DATE_FORMAT = "dd/MM/uu";
    private static final String TIME_FORMAT = "HH:mm";
    private static final String SCREENING_DISPLAY_FORMAT = "%s\n%s - %s";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private StackPane browserPane;
    @FXML
    private Label date;

    private Cinema currentCinema = null;
    private LocalDate currentDate = null;

    public BrowserPanel() {
        super(FXML);
        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);
        registerAsAnEventHandler(this);
    }

    /**
     * Loads the schedule of the provided cinema
     * @param cinema
     */
    private void loadCinemaSchedule(Cinema cinema, LocalDate providedDate) {
        DetailedDayView detailedDayView = new DetailedDayView();
        setUpDayView(detailedDayView, providedDate);

        ArrayList<Theater> theaterList = cinema.getTheaters();
        CalendarSource theatersSchedule = new CalendarSource("Theaters");

        int count = 1;

        for (Theater t : theaterList) {
            Calendar c = new Calendar(Integer.toString(t.getTheaterNumber()));
            c.setShortName(Integer.toString(t.getTheaterNumber()));
            setColorStyle(c, count);
            c.setReadOnly(true);
            theatersSchedule.getCalendars().add(c);

            ArrayList<Screening> screeningList = t.getScreeningList();

            // add entry
            for (Screening s : screeningList) {
                Entry<String> movieScreening = new Entry<>(s.getMovieName());
                String startTime = s.getScreeningDateTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT));
                String endTime = s.getScreeningEndDateTime().format(DateTimeFormatter.ofPattern(TIME_FORMAT));
                movieScreening.setInterval(s.getScreeningDateTime(), s.getScreeningEndDateTime());
                movieScreening.setTitle(String.format(SCREENING_DISPLAY_FORMAT, s.getMovieName(), startTime, endTime));
                c.addEntry(movieScreening);
            }
            count++;
        }

        detailedDayView.getCalendarSources().setAll(theatersSchedule);
        browserPane.getChildren().add(detailedDayView);
        browserPane.getChildren().add(date);
    }

    /**
     * Sets the color style for the provided calendar
     */
    private void setColorStyle(Calendar c, int count) {
        switch (count % 7) {
        case 1:
            c.setStyle(Calendar.Style.STYLE1);
            break;
        case 2:
            c.setStyle(Calendar.Style.STYLE2);
            break;
        case 3:
            c.setStyle(Calendar.Style.STYLE3);
            break;
        case 4:
            c.setStyle(Calendar.Style.STYLE4);
            break;
        case 5:
            c.setStyle(Calendar.Style.STYLE5);
            break;
        case 6:
            c.setStyle(Calendar.Style.STYLE6);
            break;
        case 7:
            c.setStyle(Calendar.Style.STYLE7);
            break;
        default:
            c.setStyle(Calendar.Style.STYLE1);
        }
    }

    /**
     * Sets up the day view for scheduler
     */
    private void setUpDayView(DetailedDayView detailedDayView, LocalDate providedDate) {
        date.setText(providedDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        detailedDayView.setLayout(DateControl.Layout.SWIMLANE);
        detailedDayView.setDate(providedDate);
        detailedDayView.setMouseTransparent(true);
        detailedDayView.setShowAllDayView(false);
        detailedDayView.setShowScrollBar(false);
        detailedDayView.setVisibleHours(24);

        detailedDayView.setBackground(new Background(new BackgroundFill(Color.web("#ffffff"),
                CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * Checks if a cinema exist in a given list of cinemas
     */
    private boolean hasCinema(ObservableList<Cinema> cinemas) {
        for (Cinema c : cinemas) {
            if (c.getName().equals(currentCinema.getName())) {
                currentCinema = c;
                return true;
            }
        }
        return false;
    }

    /**
     * Handles CinemaPanelSelectionChangedEvent
     * Reloads the schedule of newly selected cinema
     */
    @Subscribe
    private void handleCinemaPanelSelectionChangedEvent(CinemaPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        browserPane.getChildren().clear();
        currentDate = LocalDate.now();
        currentCinema = event.getNewSelection().cinema;
        loadCinemaSchedule(currentCinema, currentDate);
    }

    /**
     * Reloads the schedule of the cinema provided
     */
    @Subscribe
    private void handleReloadBrowserPanelEvent(ReloadBrowserPanelEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, refreshing browser view"));
        browserPane.getChildren().clear();

        if (event.getMoviePlanner() != null) { // handling undo, redo, clear all
            if (hasCinema(event.getMoviePlanner().getCinemaList())) {
                loadCinemaSchedule(currentCinema, currentDate);
            }
        } else { // handling new screening
            currentCinema = event.getCinema();
            currentDate = event.getDate().toLocalDate();
            loadCinemaSchedule(currentCinema, currentDate);
        }
    }

    /**
     * Handles JumpToDateRequestEvent
     * Jumps to specified date in the scheduler
     */
    @Subscribe
    private void handleJumpCommandEvent(JumpToDateRequestEvent event) {
        try {
            logger.info(LogsCenter.getEventHandlingLogMessage(event, "Jumping to date: " + event.getDate()));
            currentDate = event.getDate();
            browserPane.getChildren().clear();
            loadCinemaSchedule(currentCinema, currentDate);
        } catch (NullPointerException npe) {
            logger.severe(LogsCenter.getEventHandlingLogMessage(event, "Null cinema card."));
        }
    }
}
