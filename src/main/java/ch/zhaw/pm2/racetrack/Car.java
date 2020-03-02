package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.PositionVector.Direction;

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
    private boolean isCrashed;

    public Car(char id, PositionVector position, PositionVector velocity, boolean isCrashed) {
        this.id = id;
        this.position = position;
        this.velocity = velocity;
        this.isCrashed = isCrashed;
        isCrashed = false;
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

    public PositionVector nextPosition(){
        return PositionVector.add(position, velocity);
    }

    public void move(){
        position = nextPosition();
    }

    public void crash(){
    }

    public void accelerate(Direction direction){
        velocity = PositionVector.add(direction.vector, velocity);
    }

    public boolean isCrashed(){
        return false;
    }


}
