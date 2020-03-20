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

        display.welcomeMessage();
        try {
            game = new Game(new Track(display.readInputFile()), this);
        } catch (FileNotFoundException e) {
            display.fileNotFoundMesseage();
        } catch (InvalidTrackFormatException e) {
            display.invalidTrackMesseage();
        }
        display.printGrid(game.getGrid());
        //set MoveStrategy
        for (int index = 0; index < game.getCarsList().size(); index++) {
            int currentCar = game.getCurrentCarIndex();
            display.currentTurn(game.getCarId(currentCar), game.getCarVelocity(currentCar));
            game.getCarsList().get(currentCar).setMoveStrategy(display.retrieveMoveStrategy());
            game.switchToNextActiveCar();
        }
        //turn
        boolean running = true;
        while (game.getWinner() == -1) {
            int currentCar = game.getCurrentCarIndex();
            display.currentTurn(game.getCarId(currentCar), game.getCarVelocity(currentCar));
            game.doCarTurn(game.getCarsList().get(currentCar).getMoveStrategy().nextMove());
            display.printGrid(game.getGrid());
            if (game.getWinner() == -1) {
                game.switchToNextActiveCar();
            } else {
                display.winnerMessage(game.getCarId(game.getWinner()));
                if (display.playANewGame()) {
                    run();
                }
            }
        }
    }

    @Override
    public void onCarCrash() {

    }
}
