package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.DoNotMove;
import ch.zhaw.pm2.racetrack.strategy.MoveList;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.UserMovement;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;


public class Display {

    TextIO textIO;
    TextTerminal<?> terminal;
    Config config;

    public Display() {
        textIO = TextIoFactory.getTextIO();
        terminal = textIO.getTextTerminal();
        config = new Config();
    }

    public void welcomeMessage() {
        terminal.println("Welcome to Racetrack! \n\nPlease choose one of the following tracks:");
        terminal.println(Arrays.asList(Objects.requireNonNull(config.getTrackDirectory().list())));
    }

    public File readInputFile(){
        File newFile=null;
        boolean valid=false;
        while(!valid) {
            try {
                String trackFileName = textIO.newStringInputReader().read();
                for (int index = 0; index < config.getTrackDirectory().list().length; index++) {
                    if (Arrays.asList(config.getTrackDirectory().list()).get(index).equals(trackFileName)) {
                        newFile = new File(config.getTrackDirectory(), trackFileName);
                        valid=true;
                    }
                }
                if(!valid){
                    throw new  IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                terminal.println("Please enter the name of a valid track");
            }
        }
        return newFile;
    }

    public void currentTurn(char id, PositionVector velocity) {
        terminal.println("-------------------------------------------------------------");
        terminal.println("Current player " + id);
        terminal.println("Current velocity " + velocity);
    }

    public PositionVector.Direction userMovement(){
        terminal.println("\nChange velocity to one of the following: ");
        PositionVector.Direction acceleration = textIO.newEnumInputReader(PositionVector.Direction.class).read();
        return acceleration;
    }

    public MoveStrategy retrieveMoveStrategy(){
        MoveStrategy strategy=null;
        terminal.println("Please choose a strategy.");
        String format = "%-13s%s%n";
        terminal.printf(format,"1", "DoNotMove");
        terminal.printf(format,"2", "UserMovement");
        terminal.printf(format,"3", "MoveList");
        int number = textIO.newIntInputReader().read();
        boolean valid=false;
        while(!valid) {
            try {
                if (number == 1) {
                    strategy = new DoNotMove();
                    valid = true;
                } else if (number == 2) {
                    strategy = new UserMovement();
                    valid = true;
                } else if (number == 3) {
                    //strategy = new MoveList();
                    valid = true;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                terminal.println("Please enter a value between 1 - 3");
            }
        }
        return strategy;
    }

    public void winnerMessage(char winner){
        terminal.println("Congratulations, the winner is " + winner + " .");
    }

    public void carCrashedMessage(){
        terminal.println("Your car crashed!");
    }
}