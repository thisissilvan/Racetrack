package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.exception.CarIsCrashedException;

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
    private Game game;

    /**
     * Car Constructor.
     * @param id        the id of the car.
     * @param position  a PositionVector that shows the position of the car on the track.
     * @param velocity  the velocity as PositionVector, with the position and velocity the next turn can be calculated.
     */
    public Car(char id, PositionVector position, PositionVector velocity) {
        this.id = id;
        this.position = position;
        this.velocity = velocity;
        this.isCrashed = false;
        game = new Game();
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

    /**
     * This method calculates the nextPosition. While someone gives two PositionVectors, one for the position and one
     * for the velocity, this method returns the nextPosition which gets calculated in the PositionVector class.
     * @return  the next position where the car goes after passing in the position and the velocity PositionVector.
     */
    public PositionVector nextPosition(){
        return PositionVector.add(position, velocity);
    }

    /**
     * This method simply moves a car to the next position while calling the nextPosition() method.
     * If the car is crashed, a RuntimeException will get thrown.
     * @throws CarIsCrashedException     an unchecked Exception that tells if a car is crashed and can not do the move.
     */
    public void move(){
        if (isCrashed){
            throw new CarIsCrashedException("Can not move car. Car is crashed.");
        }
        position = nextPosition();
    }

    /**
     * Method change the state of the isCrashed boolean to true. This can happen when:
     *  - After a move() from one player  the car holds on the field where another car stands
     *  - While the move gets calculated, a car will drive over another car
     *  - The actual turn will end in the wall
     */
    public void crash(){
        if (game.willCarCrash(id, position)) {
            isCrashed = true;
        }
    }

    /**
     * The method accelerate calculates the velocity. It takes the direction as a parameter.
     * @param direction the direction
     */
    public void accelerate(Direction direction){
        velocity = PositionVector.add(direction.vector, velocity);
    }

    /**
     * Method shows if a car is crashed or not. A car can crash when driving in or over another vehicle or driving into
     * the walls.
     * @return a boolean value that shows if the car is crashed.
     */
    public boolean isCrashed(){
        return isCrashed;
    }


}
