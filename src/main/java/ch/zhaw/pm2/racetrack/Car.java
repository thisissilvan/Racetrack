package ch.zhaw.pm2.racetrack;

/**
 * Class representing a car on the racetrack.
 * Uses {@link PositionVector} to store current position on the track grid and current velocity vector.
 * Each car has an identifier character which represents the car on the race track board.
 * Also keeps the state, if the car is crashed (not active anymore). The state can not be changed back to uncrashed.
 * The velocity is changed by providing an acelleration vector.
 * The car is able to calculate the endpoint of its next position and on request moves to it.
 */
public class Car {

    private char id;

    private PositionVector position;

    private PositionVector velocity;

    public Car(char id, PositionVector position, PositionVector velocity) {
        this.id = id;
        this.position = position;
        this.velocity = velocity;
    }

    public char getId() {
        return id;
    }

    public PositionVector getPosition() {
        return position;
    }

    public PositionVector getVelocity() {
        return velocity;
    }
}
