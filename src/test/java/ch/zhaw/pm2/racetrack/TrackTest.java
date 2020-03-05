package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exception.InvalidTrackFormatException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;

public class TrackTest {

    private static final int POSITION_CAR_A = 22 * 64 + 24;
    private static final int POSITION_WALL = 17 * 64 + 24;
    private static final int POSITION_TRACK = 22 * 64 + 30;
    private static final int POSITION_FINISH_RIGHT = 22 * 64 + 22;


    /**
     * Are the space-types on the correct positions in the output string with the given track-file?
     *
     * @throws FileNotFoundException
     * @throws InvalidTrackFormatException
     */
    @Test
    void toStringTest() throws FileNotFoundException, InvalidTrackFormatException {
        Track track = new Track(new File("tracks/tests/challenge-test-track.txt"));
        String trackString = track.toString();
        assertEquals(trackString.charAt(POSITION_CAR_A), 'a');
        assertEquals(trackString.charAt(POSITION_WALL), Config.SpaceType.WALL.getValue());
        assertEquals(trackString.charAt(POSITION_FINISH_RIGHT), Config.SpaceType.FINISH_RIGHT.getValue());
        assertEquals(trackString.charAt(POSITION_TRACK), Config.SpaceType.TRACK.getValue());
    }

    /**
     * Is correct exception thrown, if to many cars are on track?
     */
    @Test
    void maxCarTest() {
        assertThrows(
                InvalidTrackFormatException.class,
                () -> new Track(new File("tracks/tests/ten-cars-track.txt"))
        );
    }

    /**
     * Is correct exception thrown, if lengths of lines are unequal?
     */
    @Test
    void unequalLineLengthTest() {
        assertThrows(
                InvalidTrackFormatException.class,
                () -> new Track(new File("tracks/tests/unequal-line-length-track.txt"))
        );
    }

}
