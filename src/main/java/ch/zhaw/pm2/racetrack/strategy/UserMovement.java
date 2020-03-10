package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Display;
import ch.zhaw.pm2.racetrack.PositionVector;

/**
 * This class implements MoveStrategy and defines a strategy where the user enters the direction at each turn.
 */
public class UserMovement implements MoveStrategy{

    Display display = new Display();

    /*
     * The user enters the direction through display.
     * @return the users choice on direction
     */

    @Override
    public PositionVector.Direction nextMove() {
        return display.userMovement();
    }
}
