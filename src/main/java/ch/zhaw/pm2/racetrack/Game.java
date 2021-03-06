package ch.zhaw.pm2.racetrack;

import java.util.*;

import static ch.zhaw.pm2.racetrack.PositionVector.*;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to move the cars, detect if they are crashed
 * and if we have a winner.
 */
public class Game {
    public static final int NO_WINNER = -1;
    private int currentCar;
    private Track track;
    private PositionVector positionVector;
    //private Direction direction;

    /**
     * Game controller class, receives a track when initialised.
     * @param track which is used for the game
     */
    public Game (Track track){
        this.track = track;
    }

    /**
     * Return the index of the current active car.
     * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
     * @return The zero-based number of the current car
     */
    public int getCurrentCarIndex() {
        return currentCar;
    }

    /**
     * Get the id of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     */
    public char getCarId(int carIndex) {
        return track.getCarId(carIndex);
    }

    /**
     * Get the position of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     */
    public PositionVector getCarPosition(int carIndex) {
        return track.getCarPos(carIndex);
    }

    /**
     * Get the velocity of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    public PositionVector getCarVelocity(int carIndex) {
        return track.getCarVelocity(carIndex);
    }

    /**
     * Return the winner of the game. If the game is still in progress, returns NO_WINNER.
     * @return The winning car's index (zero-based, see getCurrentCar()), or NO_WINNER if the game is still in progress
     */
    public int getWinner() {
        //todo
        return 0;
    }

    /**
     * Execute the next turn for the current active car.
     * <p>This method changes the current car's velocity and checks on the path to the next position,
     * if it crashes (car state to crashed) or passes the finish line in the right direction (set winner state).</p>
     * <p>The steps are as follows</p>
     * <ol>
     *   <li>Accelerate the current car</li>
     *   <li>Calculate the path from current (start) to next (end) position
     *       (see {@link Game#calculatePath(PositionVector, PositionVector)})</li>
     *   <li>Verify for each step what space type it hits:
     *      <ul>
     *          <li>TRACK: check for collision with other car (crashed &amp; don't continue), otherwise do nothing</li>
     *          <li>WALL: car did collide with the wall - crashed &amp; don't continue</li>
     *          <li>FINISH_*: car hits the finish line - wins only if it crosses the line in the correct direction</li>
     *      </ul>
     *   </li>
     *   <li>If the car crashed or wins, set its position to the crash/win coordinates</li>
     *   <li>If the car crashed, also detect if there is only one car remaining, remaining car is the winner</li>
     *   <li>Otherwise move the car to the end position</li>
     * </ol>
     * <p>The calling method must check the winner state and decide how to go on. If the winner is different
     * than {@link Game#NO_WINNER}, or the current car is already marked as crashed the method returns immediately.</p>
     *
     * @param acceleration A Direction containing the current cars acceleration vector (-1,0,1) in x and y direction
     *                     for this turn
     */
    //posToBeChecked = pos + (v + a)
    public PositionVector posToBeChecked(Direction acceleration){
        return positionVector.add(getCarPosition(getCurrentCarIndex()), positionVector.add(getCarVelocity(getCurrentCarIndex()), acceleration.vector));
    }

    public void doCarTurn(Direction acceleration){
        if(willCarCrash(getCurrentCarIndex(), posToBeChecked(acceleration))){
            track.setCarIsCrashed(getCurrentCarIndex());
            //TODO:check if only one car remaining -> winner car
        }
        //TODO: else if (carWins) { }
        else{
            track.getCars().get(getCurrentCarIndex()).accelerate(acceleration); //velocity update
            track.getCars().get(getCurrentCarIndex()).move(); //pos update
        }
    }

    //todo
    public boolean gameIsWon(){
        return track.getCars().size() == 0;
    }

    /**
     * Switches to the next car who is still in the game. Skips crashed cars.
     */
    public void switchToNextActiveCar() {
        int nextCar;
        do {
            nextCar = (currentCar + 1) % track.getCarCount();
        } while (track.getCars().get(nextCar).isCrashed());
        currentCar = nextCar;
    }


    /**
     * Returns all of the grid positions in the path between two positions, for use in determining line of sight.
     * Determine the 'pixels/positions' on a raster/grid using Bresenham's line algorithm.
     * (https://de.wikipedia.org/wiki/Bresenham-Algorithmus)
     * Basic steps are
     * - Detect which axis of the distance vector is longer (faster movement)
     * - for each pixel on the 'faster' axis calculate the position on the 'slower' axis.
     * Direction of the movement has to correctly considered
     * @param startPosition Starting position as a PositionVector
     * @param endPosition Ending position as a PositionVector
     * @return Intervening grid positions as a List of PositionVector's, including the starting and ending positions.
     */
    public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
        List<PositionVector> pathList = new ArrayList<>();
        int x1 = startPosition.getX();
        int y1 = startPosition.getY();
        int x2 = endPosition.getX();
        int y2 = endPosition.getY();
        int newSlope = 2*(y2-y1);
        int newSlopeError = newSlope - (x2-x1);

        for (int x = x1, y = y1; x <= x2; x++) {
            pathList.add(new PositionVector(x, y));
            newSlopeError += newSlope;
            if (newSlopeError >= 0) {
                y++;
                newSlopeError -= 2*(x2-x1);
            }
        }
        return pathList;
    }

    private boolean collisionWithOtherCars(int id, PositionVector position){
        List<PositionVector> pathList = new ArrayList<>();
        pathList = calculatePath(getCarPosition(id), position);
        boolean collision = false;
        for (int i = 0; i < track.getCars().size(); i++) {
            if (pathList.contains(track.getCarPos(i))){
                collision = true;
            }
        }
        return collision;
    }

    /**
     * Does indicate if a car would have a crash with a WALL space or another car at the given position.
     * @param carIndex The zero-based carIndex number
     * @param position A PositionVector of the possible crash position
     * @return A boolean indicator if the car would crash with a WALL or another car.
     */
    public boolean willCarCrash(int carIndex, PositionVector position) {
        return (track.getSpaceType(positionVector.add(track.getCarPos(carIndex), position)) == (Config.SpaceType.WALL) || collisionWithOtherCars(carIndex, position));
    }


}
