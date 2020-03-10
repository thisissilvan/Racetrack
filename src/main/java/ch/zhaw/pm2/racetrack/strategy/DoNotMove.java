package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector;

public class DoNotMove implements MoveStrategy {
    @Override
    public PositionVector.Direction nextMove() {
        return PositionVector.Direction.NONE;
    }
}
