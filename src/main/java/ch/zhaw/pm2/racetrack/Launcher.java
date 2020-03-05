package ch.zhaw.pm2.racetrack;
import static ch.zhaw.pm2.racetrack.PositionVector.*;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import java.awt.*;

public class Launcher {
    void runGame(TextIO textIO, TextTerminal<?> terminal, Game game) {
        boolean running = true;
        while (game.getWinner()== -1 && running) {

        }
        terminal.println("-------------------------------------------------------------");
        terminal.println("Congratulation " + game.getWinner());
        terminal.println("You have won!");
    }
}


