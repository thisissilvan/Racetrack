package ch.zhaw.pm2.racetrack;

public class GameLauncher {
    Display display = new Display();
    Game game = new Game();
    Runner runner = new Runner();

    public static void main(String[] args) {
        GameLauncher gameLauncher = new GameLauncher();
        gameLauncher.gameInit();
        gameLauncher.gameStart();
    }

    private void gameInit() {
        display.welcomeMesseage();
        //Read Track-File here
        //Load Track file
    }

    private void gameStart() {
        while (game.getWinner() == Game.NO_WINNER) {
            runner.run();
        }
    }

}


