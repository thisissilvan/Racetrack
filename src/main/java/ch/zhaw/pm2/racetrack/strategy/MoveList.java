package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector;


public class MoveList implements MoveStrategy {

    private final PositionVector[] moveList;

    public MoveList(PositionVector[] moveList) {
        this.moveList = moveList;
    }

    // TODO get next positions
    @Override
    public PositionVector.Direction nextMove() {
        return null;
    }
}