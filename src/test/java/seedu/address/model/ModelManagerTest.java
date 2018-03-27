package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_COMEDY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SUPERHERO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_UNUSED;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CINEMAS;
import static seedu.address.testutil.TypicalCinemas.ALJUNIED;
import static seedu.address.testutil.TypicalCinemas.BEDOK;
import static seedu.address.testutil.TypicalCinemas.SENGKANG;
import static seedu.address.testutil.TypicalMovies.ABTM4;
import static seedu.address.testutil.TypicalMovies.BLACK_PANTHER;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.email.EmailManager;
import seedu.address.model.cinema.NameContainsKeywordsPredicate;
import seedu.address.model.movie.Movie;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;
import seedu.address.testutil.MovieBuilder;
import seedu.address.testutil.MoviePlannerBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getFilteredCinemaList_modifyList_throwsUnsupportedOperationException() {
        ModelManager modelManager = new ModelManager();
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredCinemaList().remove(0);
    }

    @Test
    public void equals() {
        MoviePlanner moviePlanner = new MoviePlannerBuilder().withCinema(ALJUNIED).withCinema(BEDOK).build();
        MoviePlanner differentMoviePlanner = new MoviePlanner();
        UserPrefs userPrefs = new UserPrefs();
        EmailManager emailManager = new EmailManager();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(moviePlanner, userPrefs, emailManager);
        ModelManager modelManagerCopy = new ModelManager(moviePlanner, userPrefs, emailManager);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different moviePlanner -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentMoviePlanner, userPrefs, emailManager)));

        // different filteredList -> returns false
        String[] keywords = ALJUNIED.getName().fullName.split("\\s+");
        modelManager.updateFilteredCinemaList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(moviePlanner, userPrefs, emailManager)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredCinemaList(PREDICATE_SHOW_ALL_CINEMAS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setMoviePlannerName("differentName");
        assertTrue(modelManager.equals(new ModelManager(moviePlanner, differentUserPrefs, emailManager)));
    }

    @Test
    public void deleteTag_tagNotInUse_modelNotChanged() throws Exception {
        MoviePlanner moviePlannerWithAmy = new MoviePlannerBuilder().withCinema(SENGKANG).build();
        UserPrefs userPrefs = new UserPrefs();
        EmailManager emailManager = new EmailManager();

        ModelManager modelManager = new ModelManager(moviePlannerWithAmy, userPrefs, emailManager);
        thrown.expect(TagNotFoundException.class);
        modelManager.deleteTag(new Tag(VALID_TAG_UNUSED));

        assertEquals(new ModelManager(moviePlannerWithAmy, userPrefs, emailManager), modelManager);
    }

    @Test
    public void deleteTag_tagInUseByOneCinema_tagRemoved() throws Exception {
        MoviePlanner moviePlannerWithAmyAndBob = new MoviePlannerBuilder().withMovie(ABTM4).withMovie(BLACK_PANTHER)
                .build();
        UserPrefs userPrefs = new UserPrefs();
        EmailManager emailManager = new EmailManager();

        ModelManager modelManager = new ModelManager(moviePlannerWithAmyAndBob, userPrefs, emailManager);
        modelManager.deleteTag(new Tag(VALID_TAG_SUPERHERO));

        Movie bpSuperheroTagRemoved = new MovieBuilder(BLACK_PANTHER).withTags(VALID_TAG_COMEDY).build();

        MoviePlanner expectedMoviePlanner = new MoviePlannerBuilder().withMovie(ABTM4)
                                                                  .withMovie(bpSuperheroTagRemoved).build();

        assertEquals(new ModelManager(expectedMoviePlanner, userPrefs, emailManager), modelManager);
    }

    @Test
    public void deleteTag_tagInUseByMultipleMovie_tagRemoved() throws Exception {
        MoviePlanner moviePlannerWithAbtm44AndBp = new MoviePlannerBuilder().withMovie(ABTM4).withMovie(BLACK_PANTHER)
                .build();
        UserPrefs userPrefs = new UserPrefs();
        EmailManager emailManager = new EmailManager();

        ModelManager modelManager = new ModelManager(moviePlannerWithAbtm44AndBp, userPrefs, emailManager);
        modelManager.deleteTag(new Tag(VALID_TAG_COMEDY));

        Movie abtm4ComedyTagRemoved = new MovieBuilder(ABTM4).withTags().build();
        Movie bpComedyTagRemoved = new MovieBuilder(BLACK_PANTHER).withTags(VALID_TAG_SUPERHERO).build();
        MoviePlanner expectedMoviePlanner = new MoviePlannerBuilder().withMovie(abtm4ComedyTagRemoved)
                                                                  .withMovie(bpComedyTagRemoved).build();

        assertEquals(new ModelManager(expectedMoviePlanner, userPrefs, emailManager), modelManager);
    }
}
