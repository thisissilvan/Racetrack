package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exception.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.DoNotMove;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

import java.io.FileNotFoundException;

/**
 * This class is responsible for the game setup and the initialisation of the game.
 * It receives a display-instance of the game-launcher which is responsible for the text-output and
 * could be replaced with a java-fx class.
 */
public class Runner implements Game.CarCrashListener {
    Display display;
    Game game;

    public Runner() {
        display = new Display();
    }

    public void run() {
        //Initialise Game with chosen track
        int currentCar = 0;
        display.welcomeMessage();
        try {
            game = new Game(new Track(display.readInputFile()), this);
        } catch (FileNotFoundException | InvalidTrackFormatException e) {
            e.printStackTrace();
        }
        display.printGrid(game.getGrid());

        while (game.getWinner() == -1) {
            for (int index = 0; index < game.getCarsList().size(); index++) {
                currentCar = game.getCurrentCarIndex();
                display.currentTurn(game.getCarId(currentCar), game.getCarVelocity(currentCar));
                game.getCarsList().get(currentCar).setMoveStrategy(display.retrieveMoveStrategy());
                game.doCarTurn(game.getCarsList().get(currentCar).getMoveStrategy().nextMove());

                display.printGrid(game.getGrid());
            }
            if (game.getWinner() == -1) {
                game.switchToNextActiveCar();
            } else {
                display.winnerMessage(game.getCarId(currentCar));
            }


        }
    }

    @Override
    public void onCarCrash() {

    }
}
