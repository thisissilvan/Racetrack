package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.io.File;
import java.util.Arrays;


public class Display {

    TextIO textIO = TextIoFactory.getTextIO();
    TextTerminal<?> terminal = textIO.getTextTerminal();
    Config config = new Config();

    public File welcomeMesseage() {
        terminal.println("Welcome to Racetrack! \nPlease choose one of the following tracks:");
        terminal.println(Arrays.asList(config.getTrackDirectory().list()));
        String trackFileName = textIO.newStringInputReader().read();
        return new File(config.getTrackDirectory(), trackFileName);
    }

    public void currentTurn(char id, PositionVector velocity){
        terminal.println("Current player " + id);
        terminal.println("Current velocity " + velocity);
    }

    public PositionVector.Direction userMovement(){
        terminal.println("Change velocity to one of the following: ");
        String format = "%-13s%s%n";
        terminal.printf(format,"DOWN_LEFT", "UP_LEFT", "LEFT");
        terminal.printf(format,"DOWN", "UP", "NONE");
        terminal.printf(format,"DOWN_RIGHT", "UP_RIGHT", "RIGHT");
        PositionVector.Direction acceleration = textIO.newEnumInputReader(PositionVector.Direction.class).read("What would you like to choose?");
        return acceleration;
    }

    public MoveStrategy moveStrategyMessage(){
        terminal.println("Please choose a strategy.");
        return null;
    }
    public void winnerMessage(char winner){
        terminal.println("Congratulations, the winner is " + winner + " .");
    }
}