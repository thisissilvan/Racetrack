package ch.zhaw.pm2.racetrack;

import java.util.*;

import static ch.zhaw.pm2.racetrack.PositionVector.*;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to move the cars, detect if they are crashed
 * and if we have a winner.
 */
public class Game {

    public interface CarCrashListener {
        public void onCarCrash();
    }

    public static final int NO_WINNER = -1;
    private int WINNER = NO_WINNER;
    private int currentCar;
    private Track track;
    private PositionVector positionVector;
    private CarCrashListener carCrashListener;

    /**
     * Game controller class, receives a track when initialised.
     *
     * @param track which is used for the game
     */
    public Game(Track track, CarCrashListener carCrashListener) {
        this.track = track;
        this.carCrashListener = carCrashListener;
    }
    
    /**
     * Return the index of the current active car.
     * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
     *
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
     * Get all the cars from the game.
     * @return A List containing all cars
     */
    public List <Car> getCarsList() { return track.getCars(); }

    /**
     * Return the winner of the game. If the game is still in progress, returns NO_WINNER.
     * @return The winning car's index (zero-based, see getCurrentCar()), or NO_WINNER if the game is still in progress
     */
    public int getWinner() {
        return WINNER;
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
    //posToBeChecked = curr_pos + (v + a)
    private PositionVector posToBeChecked(Direction acceleration) {
        return add(getCarPosition(currentCar), add(getCarVelocity(currentCar), acceleration.vector));
    }

    //checks after crash if only one car remaining -> this will be the winner
    private void remainingCarCheck() {
        int count = 0;
        for (int i = 0; i < track.getCarCount(); i++) {
            if (!track.getCars().get(i).isCrashed()) {
                count++;
                WINNER = i;
            }
        }
        if (count != 1) {
            WINNER = NO_WINNER;
        }
    }

    //checks after new acceleration and new position, if car reaches/crosses winning line
    private void carCrossesLineCheck(Direction acceleration) {
        List<PositionVector> pathList = new ArrayList<>();
        pathList = calculatePath(getCarPosition(currentCar), posToBeChecked(acceleration));
        int x1 = getCarPosition(currentCar).getX();
        int y1 = getCarPosition(currentCar).getY();
        int x2 = posToBeChecked(acceleration).getX();
        int y2 = posToBeChecked(acceleration).getY();
        for (int i = 0; i < pathList.size(); i++) {
            if (track.getSpaceType(pathList.get(i)) != Config.SpaceType.TRACK
                    && (    (track.getSpaceType(pathList.get(i)) == Config.SpaceType.FINISH_UP && x1 >= x2) ||
                    (track.getSpaceType(pathList.get(i)) == Config.SpaceType.FINISH_DOWN && x2 >= x1) ||
                    (track.getSpaceType(pathList.get(i)) == Config.SpaceType.FINISH_RIGHT && y2 >= y1) ||
                    (track.getSpaceType(pathList.get(i)) == Config.SpaceType.FINISH_LEFT && y1 >= y2))
            ) {
                WINNER = currentCar;
                break;
            }
        }
    }

    public void doCarTurn(Direction acceleration){
        if(willCarCrash(currentCar, posToBeChecked(acceleration))){
            track.setCarIsCrashed(currentCar);
            remainingCarCheck();
        } else{
            track.getCars().get(currentCar).accelerate(acceleration); //velocity update
            track.getCars().get(currentCar).move(); //pos update
            //check if this car crosses winning line
            carCrossesLineCheck(acceleration);
        }
        //TODO: Check how to set end of game if winner determined
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
        int x1 = startPosition.getX(), y1 = startPosition.getY();
        int x2 = endPosition.getX(), y2 = endPosition.getY();
        int dX = x2-x1, dY = y2-y1;
        int distX = Math.abs(dX), distY = Math.abs(dY);
        int dirX = Integer.signum(dX), dirY = Integer.signum(dY);
        int parallelX = 0, parallelY = dirY;
        int dSlow = distX, dFast = distY;
        int diagonalX = dirX, diagonalY = dirY;
        if (distX > distY) {
            parallelX = dirX; parallelY = 0;
            dSlow = distY;
            dFast = distX;
        }
        int slopeError = dFast/2;
        pathList.add(new PositionVector(x1, y1));
        for (int i = 0; i < dFast; i++) {
            slopeError -= dSlow;
            if (slopeError < 0) {
                slopeError += dFast;
                x1 += diagonalX;
                y1 += diagonalY;
            } else {
                x1 += parallelX;
                y1 += parallelY;
            }
            pathList.add(new PositionVector(x1, y1));
        }
        return pathList;
    }

    /*public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
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
*/
    private boolean collisionWithOtherCars(int carIndex, PositionVector position){
        List<PositionVector> pathList = calculatePath(getCarPosition(carIndex), position);
        int count = 0;
        for (int i = 0; i < track.getCars().size(); i++) {
            if (pathList.contains(track.getCarPos(i))){
                count++;
            }
        }
        return count == 2;
    }

    private boolean collisionWithWall(int carIndex, PositionVector position){
        List<PositionVector> pathList = calculatePath(getCarPosition(carIndex), position);
        boolean collision = false;
        for (int i = 0; i < pathList.size(); i++) {
            if (track.getSpaceType(pathList.get(i)) == (Config.SpaceType.WALL)) {
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
        return (collisionWithWall(carIndex, position) || collisionWithOtherCars(carIndex, position));
    }

    public String getGrid(){
        return track.toString();
    }


}
