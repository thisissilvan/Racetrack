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

    private String testTrackDirectory;

    public TrackTest() {
        Config config = new Config();
        this.testTrackDirectory = config.getTestTrackDirectoryPath();
    }

    /**
     * Are the space-types on the correct positions in the output string with the given track-file?
     * Does the grid have the correct height?
     *
     * @throws FileNotFoundException
     * @throws InvalidTrackFormatException
     */
    @Test
    void toStringTest() throws FileNotFoundException, InvalidTrackFormatException {
        Track track = new Track(new File(this.testTrackDirectory + "challenge.txt"));
        checkChallengeTrack(track);
    }

    /**
     * Are the space-types even on the correct positions, when the file starts with empty lines?
     * Is the text after the grid and a blank line ignored?
     *
     * @throws FileNotFoundException
     * @throws InvalidTrackFormatException
     */
    @Test
    void emptyLinesTest() throws FileNotFoundException, InvalidTrackFormatException {
        Track track = new Track(new File(this.testTrackDirectory + "challenge-empty-lines.txt"));
        checkChallengeTrack(track);
    }

    private void checkChallengeTrack(Track track) {
        String trackString = track.toString();
        assertEquals(trackString.charAt(POSITION_CAR_A), 'a');
        assertEquals(trackString.charAt(POSITION_WALL), Config.SpaceType.WALL.getValue());
        assertEquals(trackString.charAt(POSITION_FINISH_RIGHT), Config.SpaceType.FINISH_RIGHT.getValue());
        assertEquals(trackString.charAt(POSITION_TRACK), Config.SpaceType.TRACK.getValue());
        assertEquals(26, track.getHeight());
    }

    /**
     * Is correct exception thrown, if to many cars are on track?
     */
    @Test
    void maxCarTest() {
        assertThrows(
                InvalidTrackFormatException.class,
                () -> new Track(new File(this.testTrackDirectory + "ten-cars-track.txt"))
        );
    }

    /**
     * Is correct exception thrown, if lengths of lines are unequal?
     */
    @Test
    void unequalLineLengthTest() {
        assertThrows(
                InvalidTrackFormatException.class,
                () -> new Track(new File(this.testTrackDirectory + "unequal-line-length-track.txt"))
        );
    }

    /**
     * Is correct exception thrown, if track-file is empty?
     */
    @Test
    void emptyTrackTest() {
        assertThrows(
                InvalidTrackFormatException.class,
                () -> new Track(new File(this.testTrackDirectory + "empty-track.txt"))
        );
    }

}
