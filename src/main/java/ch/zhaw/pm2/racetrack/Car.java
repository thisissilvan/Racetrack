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
    private boolean isCrashed;


    public boolean isCrashed (){
        return false;
    }

    public void setIsCrashed() {
        isCrashed = true;
    }

    public char getId () { return id; }

    public PositionVector getPosition () { return position; }

    public void setPosition (PositionVector newPosition) {
        position = newPosition;
    }

    public PositionVector getVelocity () { return velocity; }

    public void setVelocity (PositionVector newVelocity) {
        velocity = newVelocity;
    }
}
