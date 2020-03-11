package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exception.InvalidTrackFormatException;

import java.io.FileNotFoundException;

/**
 * This class is responsible for the game setup and the initialisation of the game.
 * It receives a display-instance of the game-launcher which is responsible for the text-output and
 * could be replaced with a java-fx class.
 */
public class Runner implements Game.CarCrashListener {
    Display display;
    Game game;

    public Runner(){
        display = new Display();
    }

    public void run() {
        //Initialise Game with choosen track
        display.welcomeMesseage();
        try {
            game = new Game(new Track(display.readInputFile()), this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidTrackFormatException e) {
            e.printStackTrace();
        }

        //turn
        int currentCar = game.getCurrentCarIndex();
        boolean running = true;
        while (game.getWinner()== -1 && running) {
            // game.doCarTurn(display.currentTurn();
            if(game.getWinner()== -1){
                game.switchToNextActiveCar();
            }
        }
    }

    @Override
    public void onCarCrash() {
        //todo call display for notification
    }
}
