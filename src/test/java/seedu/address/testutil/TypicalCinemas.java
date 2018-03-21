package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUMOFTHEATERS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_SHAW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.MoviePlanner;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;

/**
 * A utility class containing a list of {@code Cinema} objects to be used in tests.
 */
public class TypicalCinemas {

    public static final Cinema GV_PAYA_LEBAR = new CinemaBuilder().withName("GV Paya Lebar")
            .withAddress("SingPost Centre, 10 Eunos Road 8, #03-107, Singapore 408600")
            .withEmail("customersvc@goldenvillage.com.sg").withPhone("66538100").withTheater(8).build();
    public static final Cinema GV_TIONG_BAHRU = new CinemaBuilder().withName("GV Tiong Bahru")
            .withAddress("302 Tiong Bahru Road, #04-105, Tiong Bahru Plaza, Singapore 168732")
            .withEmail("customersvc@goldenvillage.com.sg").withPhone("66538100").withTheater(5).build();
    public static final Cinema CATHAY_AMK_HUB = new CinemaBuilder().withName("Cathay Cineplex AMK Hub")
            .withPhone("67367355").withEmail("corporate_services@cathaycineplexes.com.sg")
            .withAddress("53 Ang Mo Kio Ave 3 Level 4, AMK Hub Singapore 569933").withTheater(8).build();
    public static final Cinema CATHAY_CAUSEWAY_POINT = new CinemaBuilder().withName("Cathay Cineplex Causeway Point")
            .withPhone("67367355").withEmail("corporate_services@cathaycineplexes.com.sg")
            .withAddress("1 Woodlands Square Level 7, Causeway Point Singapore 738099").withTheater(7).build();
    public static final Cinema CATHAY_CINELEISURE_ORCHARD = new CinemaBuilder()
            .withName("Cathay Cineplex Cineleisure Orchard").withPhone("67367355")
            .withEmail("corporate_services@cathaycineplexes.com.sg")
            .withAddress("8 Grange Road Levels 4, 5 & 6, Cathay Cineleisure Orchard Singapore 239695")
            .withTheater(12).build();
    public static final Cinema SHAW_LIDO = new CinemaBuilder().withName("Shaw Theatres Lido").withPhone("62352077")
            .withEmail("info@shaw.sg").withAddress("350, Orchard Road, 5th/6th Floor, Shaw House, Singapore 238868")
            .withTheater(11).build();
    public static final Cinema SHAW_JCUBE = new CinemaBuilder().withName("Shaw Theatres JCube").withPhone("62352077")
            .withEmail("info@shaw.sg").withAddress("2, Jurong East Central 1, JCube, #04-11, Singapore 609731")
            .withTheater(7).build();

    // Manually added
    public static final Cinema GV_VIVOCITY = new CinemaBuilder().withName("GV Vivocity").withPhone("66538100")
            .withEmail("customersvc@goldenvillage.com.sg")
            .withAddress("1 HarbourFront Walk, #02-30, VivoCity, Singapore 098585").withTheater(15).build();
    public static final Cinema CATHAY_JEM = new CinemaBuilder().withName("Cathay Cineplex Jem").withPhone("67367355")
            .withEmail("corporate_services@cathaycineplexes.com.sg")
            .withAddress("50 Jurong Gateway Road, Level 5, Jem Singapore 608549").withTheater(10).build();

    // Manually added - Cinema's details found in {@code CommandTestUtil}
    public static final Cinema GV = new CinemaBuilder().withName(VALID_NAME_GV).withPhone(VALID_PHONE_GV)
            .withEmail(VALID_EMAIL_GV).withAddress(VALID_ADDRESS_GV).withTheater(VALID_NUMOFTHEATERS).build();
    public static final Cinema SHAW = new CinemaBuilder().withName(VALID_NAME_SHAW).withPhone(VALID_PHONE_SHAW)
            .withEmail(VALID_EMAIL_SHAW).withAddress(VALID_ADDRESS_SHAW).withTheater(VALID_NUMOFTHEATERS).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalCinemas() {} // prevents instantiation

    /**
     * Returns an {@code MoviePlanner} with all the typical cinemas.
     */
    public static MoviePlanner getTypicalMoviePlanner() {
        MoviePlanner ab = new MoviePlanner();
        for (Cinema cinema : getTypicalCinemas()) {
            try {
                ab.addCinema(cinema);
            } catch (DuplicateCinemaException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }

    public static List<Cinema> getTypicalCinemas() {
        return new ArrayList<>(Arrays.asList(GV_PAYA_LEBAR, GV_TIONG_BAHRU, CATHAY_AMK_HUB,
                CATHAY_CAUSEWAY_POINT, CATHAY_CINELEISURE_ORCHARD, SHAW_LIDO, SHAW_JCUBE));
    }
}
