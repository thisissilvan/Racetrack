package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Display;
import ch.zhaw.pm2.racetrack.PositionVector;

public class UserMovement implements MoveStrategy{

    Display display = new Display();

    @Override
    public PositionVector.Direction nextMove() {
        return display.userMovement();
    }
}
