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

    




}

