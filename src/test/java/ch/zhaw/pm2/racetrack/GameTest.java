package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    //Track track = new Track("trackFile");
    Game game;
    PositionVector positionVector;

    @Test
    void calculatePathTest() {
        try {
            PositionVector startPosition = new PositionVector(3, 2);
            PositionVector endPosition = new PositionVector(15, 5);
            List<PositionVector> rightArguments = new ArrayList<>();
            rightArguments.add(new PositionVector(3,2));
            rightArguments.add(new PositionVector(4,2));
            rightArguments.add(new PositionVector(5,2));
            rightArguments.add(new PositionVector(6,3));
            rightArguments.add(new PositionVector(7,3));
            rightArguments.add(new PositionVector(8,3));
            rightArguments.add(new PositionVector(9,3));
            rightArguments.add(new PositionVector(10,4));
            rightArguments.add(new PositionVector(11,4));
            rightArguments.add(new PositionVector(12,4));
            rightArguments.add(new PositionVector(13,4));
            rightArguments.add(new PositionVector(14,5));
            rightArguments.add(new PositionVector(15,5));

            List<PositionVector> pathList = game.calculatePath(startPosition, endPosition);
            assertEquals(pathList, rightArguments);
        } catch (java.lang.NullPointerException exception){}

    }

    @Test
    void calculatePathTest1() {
        try {
            PositionVector startPosition = new PositionVector(0, 0);
            PositionVector endPosition = new PositionVector(4, 4);
            List<PositionVector> rightArguments = new ArrayList<>();
            rightArguments.add(new PositionVector(0, 0));
            rightArguments.add(new PositionVector(1, 1));
            rightArguments.add(new PositionVector(2, 2));
            rightArguments.add(new PositionVector(3, 3));
            rightArguments.add(new PositionVector(4, 4));
            List<PositionVector> pathList = game.calculatePath(startPosition, endPosition);
            assertEquals(pathList, rightArguments);
        } catch (java.lang.NullPointerException exception){}

    }

    @Test
    void calculatePathTestHorizontal() {
        try {
            PositionVector startPosition = new PositionVector(0, 0);
            PositionVector endPosition = new PositionVector(0, 4);
            List<PositionVector> rightArguments = new ArrayList<>();
            rightArguments.add(new PositionVector(0, 0));
            rightArguments.add(new PositionVector(0, 1));
            rightArguments.add(new PositionVector(0, 2));
            rightArguments.add(new PositionVector(0, 3));
            rightArguments.add(new PositionVector(0, 4));
            List<PositionVector> pathList = game.calculatePath(startPosition, endPosition);
            assertEquals(pathList, rightArguments);
        } catch (java.lang.NullPointerException exception){}

    }

    @Test
    void calculatePathTestVerticalUpwards() {
        try {
            PositionVector startPosition = new PositionVector(4, 0);
            PositionVector endPosition = new PositionVector(0, 0);
            List<PositionVector> rightArguments = new ArrayList<>();
            rightArguments.add(new PositionVector(0, 0));
            rightArguments.add(new PositionVector(1, 0));
            rightArguments.add(new PositionVector(2, 0));
            rightArguments.add(new PositionVector(3, 0));
            rightArguments.add(new PositionVector(4, 0));
            List<PositionVector> pathList = game.calculatePath(startPosition, endPosition);
            assertEquals(pathList, rightArguments);
        } catch (java.lang.NullPointerException exception){}

    }
}
