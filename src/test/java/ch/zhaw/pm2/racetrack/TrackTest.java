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

    @Test
    void toStringTest() throws FileNotFoundException, InvalidTrackFormatException {
        Track track = new Track(new File("tracks/tests/test-track.txt"));
        String trackString = track.toString();
        assertEquals(trackString.charAt(POSITION_CAR_A), 'a');
        assertEquals(trackString.charAt(POSITION_WALL), Config.SpaceType.WALL.getValue());
        assertEquals(trackString.charAt(POSITION_FINISH_RIGHT), Config.SpaceType.FINISH_RIGHT.getValue());
        assertEquals(trackString.charAt(POSITION_TRACK), Config.SpaceType.TRACK.getValue());
    }

}
