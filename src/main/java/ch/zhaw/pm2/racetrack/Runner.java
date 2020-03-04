package ch.zhaw.pm2.racetrack;

/**
 * This class is responsible for the game setup and the initialisation of the game.
 * It receives a display-instance of the game-launcher which is responsible for the text-output and
 * could be replaced with a java-fx class.
 */
public class Runner {
    private static GameLauncher gameLauncher;

    public static void main(String[] args) {
        gameLauncher = new GameLauncher();
        //gameLauncher.run();
    }
}
