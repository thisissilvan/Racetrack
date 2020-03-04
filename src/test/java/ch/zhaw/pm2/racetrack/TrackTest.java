package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exception.InvalidTrackFormatException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class TrackTest {

    @Test
    void toStringTest() throws FileNotFoundException, InvalidTrackFormatException {
        Track track = new Track(new File("tracks/challenge.txt"));
        System.out.println(track.toString());
    }

}
