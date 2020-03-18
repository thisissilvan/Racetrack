package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector;

/**
 * This class implements MoveStrategy and defines a strategy with no movement.
 * A car with this strategy always remains in the same position as in the beginning.
 */

public class DoNotMove implements MoveStrategy {



    /**
     * The car never moves. It's Direction is NONE which equals (0,0)
     * @return always returns NONE
     */
    @Override
    public PositionVector.Direction nextMove() {
        return PositionVector.Direction.NONE;
    }
}
