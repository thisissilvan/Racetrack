package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    //Track track = new Track("trackFile");
    Game game;
    PositionVector positionVector;

    @Test
    void calculatePathTest() {
        PositionVector startPosition = new PositionVector(3, 2);
        PositionVector endPosition = new PositionVector(15, 5);
        List<PositionVector> rightArguments = new ArrayList<>();
        rightArguments.add(new PositionVector(3,2));
        rightArguments.add(new PositionVector(4,3));
        rightArguments.add(new PositionVector(5,3));
        rightArguments.add(new PositionVector(6,3));
        rightArguments.add(new PositionVector(7,3));
        rightArguments.add(new PositionVector(8,4));
        rightArguments.add(new PositionVector(9,4));
        rightArguments.add(new PositionVector(10,4));
        rightArguments.add(new PositionVector(11,4));
        rightArguments.add(new PositionVector(12,5));
        rightArguments.add(new PositionVector(13,5));
        rightArguments.add(new PositionVector(14,5));
        rightArguments.add(new PositionVector(15,5));

        List<PositionVector> pathList = game.calculatePath(startPosition, endPosition);
        assertEquals(pathList, rightArguments);
    }

    @Test
    void calculatePathTest1() {
        PositionVector startPosition = new PositionVector(0, 0);
        PositionVector endPosition = new PositionVector(4, 4);
        List<PositionVector> rightArguments = new ArrayList<>();
        rightArguments.add(new PositionVector(0,0));
        rightArguments.add(new PositionVector(1,1));
        rightArguments.add(new PositionVector(2,2));
        rightArguments.add(new PositionVector(3,3));
        rightArguments.add(new PositionVector(4,4));

        List<PositionVector> pathList = game.calculatePath(startPosition, endPosition);
        assertEquals(pathList, rightArguments);
    }
}
