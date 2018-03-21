package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_COMEDY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SUPERHERO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_UNUSED;
import static seedu.address.testutil.TypicalCinemas.GV;
import static seedu.address.testutil.TypicalCinemas.GV_PAYA_LEBAR;
import static seedu.address.testutil.TypicalCinemas.SHAW;
import static seedu.address.testutil.TypicalCinemas.getTypicalMoviePlanner;
import static seedu.address.testutil.TypicalMovies.ABTM4;
import static seedu.address.testutil.TypicalMovies.BLACK_PANTHER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.Theater;
import seedu.address.model.movie.Movie;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;
import seedu.address.testutil.MovieBuilder;
import seedu.address.testutil.MoviePlannerBuilder;

public class MoviePlannerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final MoviePlanner moviePlanner = new MoviePlanner();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), moviePlanner.getCinemaList());
        assertEquals(Collections.emptyList(), moviePlanner.getTagList());
        assertEquals(Collections.emptyList(), moviePlanner.getTheaterList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        moviePlanner.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyMoviePlanner_replacesData() {
        MoviePlanner newData = getTypicalMoviePlanner();
        moviePlanner.resetData(newData);
        assertEquals(newData, moviePlanner);
    }

    @Test
    public void resetData_withDuplicateCinemas_throwsAssertionError() {
        // Repeat GV_PAYA_LEBAR twice
        List<Cinema> newCinemas = Arrays.asList(GV_PAYA_LEBAR, GV_PAYA_LEBAR);
        List<Movie> newMovies = new ArrayList<>();
        List<Tag> newTags = new ArrayList<>(BLACK_PANTHER.getTags());
        List<Theater> newTheaters = new ArrayList<>(GV_PAYA_LEBAR.getTheaters());
        MoviePlannerStub newData = new MoviePlannerStub(newCinemas, newTheaters, newTags, newMovies);

        thrown.expect(AssertionError.class);
        moviePlanner.resetData(newData);
    }

    @Test
    public void getCinemaList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        moviePlanner.getCinemaList().remove(0);
    }

    @Test
    public void getTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        moviePlanner.getTagList().remove(0);
    }

    @Test
    public void updateCinema_modifyDetails_cinemaAndTagListUpdated() throws Exception {
        MoviePlanner moviePlannerBobChangeToAmy = new MoviePlannerBuilder().withCinema(SHAW).build();
        MoviePlanner moviePlannerWithAmy = new MoviePlannerBuilder().withCinema(GV).build();

        moviePlannerBobChangeToAmy.updateCinema(SHAW, GV);

        assertEquals(moviePlannerWithAmy, moviePlannerBobChangeToAmy);
    }

    @Test
    public void removeTag_tagNotInUse_moviePlannerNotChanged() throws Exception {
        MoviePlanner moviePlannerWithAbtm4 = new MoviePlannerBuilder().withMovie(ABTM4).build();
        thrown.expect(TagNotFoundException.class);
        moviePlannerWithAbtm4.removeTag(new Tag(VALID_TAG_UNUSED));
        MoviePlanner expectedMoviePlanner = new MoviePlannerBuilder().withMovie(ABTM4).build();

        assertEquals(expectedMoviePlanner, moviePlannerWithAbtm4);
    }

    @Test
    public void removeTag_tagInUseByOneMovie_tagRemoved() throws Exception {
        MoviePlanner moviePlannerWithAbtm4andBp = new MoviePlannerBuilder().withMovie(ABTM4)
                .withMovie(BLACK_PANTHER).build();
        moviePlannerWithAbtm4andBp.removeTag(new Tag(VALID_TAG_SUPERHERO));

        Movie bpSuperheroTagRemoved = new MovieBuilder(BLACK_PANTHER).withTags(VALID_TAG_COMEDY).build();
        MoviePlanner expectedMoviePlanner = new MoviePlannerBuilder().withMovie(ABTM4)
                                                                  .withMovie(bpSuperheroTagRemoved).build();

        assertEquals(expectedMoviePlanner, moviePlannerWithAbtm4andBp);
    }

    @Test
    public void removeTag_tagInUseByMultipleMovie_tagRemoved() throws Exception {
        MoviePlanner moviePlannerWithAbtm4AndBp = new MoviePlannerBuilder().withMovie(ABTM4)
                .withMovie(BLACK_PANTHER).build();
        moviePlannerWithAbtm4AndBp.removeTag(new Tag(VALID_TAG_COMEDY));

        Movie abtm4ComedyTagRemoved = new MovieBuilder(ABTM4).withTags().build();
        Movie bpComedyTagRemoved = new MovieBuilder(BLACK_PANTHER).withTags(VALID_TAG_SUPERHERO).build();

        MoviePlanner expectedMoviePlanner = new MoviePlannerBuilder().withMovie(abtm4ComedyTagRemoved)
                                                                  .withMovie(bpComedyTagRemoved).build();

        assertEquals(expectedMoviePlanner, moviePlannerWithAbtm4AndBp);
    }

    /**
     * A stub ReadOnlyMoviePlanner whose cinemas and tags lists can violate interface constraints.
     */
    private static class MoviePlannerStub implements ReadOnlyMoviePlanner {
        private final ObservableList<Cinema> cinemas = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();
        private final ObservableList<Movie> movies = FXCollections.observableArrayList();
        private final ObservableList<Theater> theaters = FXCollections.observableArrayList();

        MoviePlannerStub(Collection<Cinema> cinemas, Collection<Theater> theaters,
                         Collection<? extends Tag> tags, Collection<Movie> movies) {
            this.cinemas.setAll(cinemas);
            this.movies.setAll(movies);
            this.tags.setAll(tags);
            this.theaters.setAll(theaters);
        }

        @Override
        public ObservableList<Cinema> getCinemaList() {
            return cinemas;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }

        @Override
        public ObservableList<Movie> getMovieList() {
            return movies;
        }

        @Override
        public ObservableList<Theater> getTheaterList() {
            return theaters;
        }
    }

}
