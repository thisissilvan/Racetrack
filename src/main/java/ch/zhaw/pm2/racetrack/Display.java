package ch.zhaw.pm2.racetrack;

import static ch.zhaw.pm2.racetrack.PositionVector.*;
import java.io.File;
import java.io.ObjectInputFilter;
import java.util.Arrays;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;


public class Display {

    TextIO textIO = TextIoFactory.getTextIO();
    TextTerminal<?> terminal = textIO.getTextTerminal();
    Config config = new Config();

    public File gameInit() {
        terminal.println("Welcome to Racetrack! \n Please choose one of the following tracks:");
        for (int index = 0; index < config.getTrackDirectory().length(); index++) {
            terminal.println(Arrays.asList(config.getTrackDirectory().list()));
        }
        String trackFileName = textIO.newStringInputReader().read();
        return new File(config.getTrackDirectory(),trackFileName);
    }

    public PositionVector.Direction currentTurn(char id, PositionVector velocity){
        terminal.println("Current player " + id);
        terminal.println("Current velocity " + velocity);
        terminal.println("Change velocity to one of the following: ");
        String format = "%-13s%s%n";
        terminal.printf(format,"DOWN_LEFT", "UP_LEFT", "LEFT");
        terminal.printf(format,"DOWN", "UP", "NONE");
        terminal.printf(format,"DOWN_RIGHT", "UP_RIGHT", "RIGHT");
        PositionVector.Direction acceleration = textIO.newEnumInputReader(PositionVector.Direction.class).read("What would you like to choose?");
        return acceleration;
    }




}

