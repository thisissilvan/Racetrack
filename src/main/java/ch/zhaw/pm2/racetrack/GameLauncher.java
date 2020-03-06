package ch.zhaw.pm2.racetrack;
import static ch.zhaw.pm2.racetrack.PositionVector.*;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

public class GameLauncher {
    void run(TextIO textIO, TextTerminal<?> terminal, Game game) {
        boolean running = true;
        while (game.getWinner()== -1 && running) {
            terminal.println("-------------------------------------------------------------");
            terminal.println("Current player " + game.getCarId(game.getCurrentCarIndex()));
            terminal.println("Current velocity " + game.getCarVelocity(game.getCurrentCarIndex()));
            terminal.println("Change velocity to one of the following: ");
            String format = "%-13s%s%n";
            terminal.printf(format,"DOWN_LEFT", "UP_LEFT", "LEFT");
            terminal.printf(format,"DOWN", "UP", "NONE");
            terminal.printf(format,"DOWN_RIGHT", "UP_RIGHT", "RIGHT");
            Direction acceleration = textIO.newEnumInputReader(Direction.class).read("What would you like to choose?");
            game.doCarTurn(acceleration);
            if(game.getWinner()== -1){
                game.switchToNextActiveCar();
            }
        }
        terminal.println("-------------------------------------------------------------");
        terminal.println("Congratulation " + game.getCarId(game.getWinner()));
        terminal.println("You have won!");
    }
}


