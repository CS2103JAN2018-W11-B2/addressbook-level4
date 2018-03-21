package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalCinemas.CATHAY_JEM;
import static seedu.address.testutil.TypicalCinemas.GV_PAYA_LEBAR;
import static seedu.address.testutil.TypicalCinemas.GV_VIVOCITY;
import static seedu.address.testutil.TypicalCinemas.getTypicalMoviePlanner;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.MoviePlanner;
import seedu.address.model.ReadOnlyMoviePlanner;

public class XmlMoviePlannerStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlMoviePlannerStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readMoviePlanner_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readMoviePlanner(null);
    }

    private java.util.Optional<ReadOnlyMoviePlanner> readMoviePlanner(String filePath) throws Exception {
        return new XmlMoviePlannerStorage(filePath).readMoviePlanner(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readMoviePlanner("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readMoviePlanner("NotXmlFormatMoviePlanner.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readMoviePlanner_invalidCinemaMoviePlanner_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readMoviePlanner("invalidCinemaMoviePlanner.xml");
    }

    @Test
    public void readMoviePlanner_invalidAndValidCinemaMoviePlanner_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readMoviePlanner("invalidAndValidCinemaMoviePlanner.xml");
    }

    @Test
    public void readAndSaveMoviePlanner_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempMoviePlanner.xml";
        MoviePlanner original = getTypicalMoviePlanner();
        XmlMoviePlannerStorage xmlMoviePlannerStorage = new XmlMoviePlannerStorage(filePath);

        //Save in new file and read back
        xmlMoviePlannerStorage.saveMoviePlanner(original, filePath);
        ReadOnlyMoviePlanner readBack = xmlMoviePlannerStorage.readMoviePlanner(filePath).get();
        assertEquals(original, new MoviePlanner(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addCinema(GV_VIVOCITY);
        original.removeCinema(GV_PAYA_LEBAR);
        xmlMoviePlannerStorage.saveMoviePlanner(original, filePath);
        readBack = xmlMoviePlannerStorage.readMoviePlanner(filePath).get();
        assertEquals(original, new MoviePlanner(readBack));

        //Save and read without specifying file path
        original.addCinema(CATHAY_JEM);
        xmlMoviePlannerStorage.saveMoviePlanner(original); //file path not specified
        readBack = xmlMoviePlannerStorage.readMoviePlanner().get(); //file path not specified
        assertEquals(original, new MoviePlanner(readBack));

    }

    @Test
    public void saveMoviePlanner_nullMoviePlanner_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveMoviePlanner(null, "SomeFile.xml");
    }

    /**
     * Saves {@code moviePlanner} at the specified {@code filePath}.
     */
    private void saveMoviePlanner(ReadOnlyMoviePlanner moviePlanner, String filePath) {
        try {
            new XmlMoviePlannerStorage(filePath).saveMoviePlanner(moviePlanner, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveMoviePlanner_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveMoviePlanner(new MoviePlanner(), null);
    }


}
