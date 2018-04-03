package seedu.address.commons.events.ui;

import java.time.LocalDateTime;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyMoviePlanner;
import seedu.address.model.cinema.Cinema;

/**
 * Indicates that there is a need to reload browser panel
 */
public class ReloadBrowserPanelEvent extends BaseEvent {
    private Cinema cinema;
    private LocalDateTime date;
    private ReadOnlyMoviePlanner moviePlanner;

    public ReloadBrowserPanelEvent(ReadOnlyMoviePlanner moviePlanner) {
        this.moviePlanner = moviePlanner;
    }
    public ReloadBrowserPanelEvent(Cinema cinema, LocalDateTime date) {
        this.cinema = cinema;
        this.date = date;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public Cinema getCinema() {
        return cinema;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ReadOnlyMoviePlanner getMoviePlanner() {
        return moviePlanner;
    }
}
