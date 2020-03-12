package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exception.InvalidTrackFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class represents the racetrack board.
 *
 * <p>The racetrack board consists of a rectangular grid of 'width' columns and 'height' rows.
 * The zero point of he grid is at the top left. The x-axis points to the right and the y-axis points downwards.</p>
 * <p>Positions on the track grid are specified using {@link PositionVector} objects. These are vectors containing an
 * x/y coordinate pair, pointing from the zero-point (top-left) to the addressed space in the grid.</p>
 *
 * <p>Each position in the grid represents a space which can hold an enum object of type {@link Config.SpaceType}.<br>
 * Possible Space types are:
 * <ul>
 *  <li>WALL : road boundary or off track space</li>
 *  <li>TRACK: road or open track space</li>
 *  <li>FINISH_LEFT, FINISH_RIGHT, FINISH_UP, FINISH_DOWN :  finish line spaces which have to be crossed
 *      in the indicated direction to winn the race.</li>
 * </ul>
 * <p>Beside the board the track contains the list of cars, with their current state (position, velocity, crashed,...)</p>
 *
 * <p>At initialization the track grid data is read from the given track file. The track data must be a
 * rectangular block of text. Empty lines at the start are ignored. Processing stops at the first empty line
 * following a non-empty line, or at the end of the file.</p>
 * <p>Characters in the line represent SpaceTypes. The mapping of the Characters is as follows:
 * <ul>
 *   <li>WALL : '#'</li>
 *   <li>TRACK: ' '</li>
 *   <li>FINISH_LEFT : '&lt;'</li>
 *   <li>FINISH_RIGHT: '&gt;'</li>
 *   <li>FINISH_UP   : '^;'</li>
 *   <li>FINISH_DOWN: 'v'</li>
 *   <li>Any other character indicates the starting position of a car.<br>
 *       The character acts as the id for the car and must be unique.<br>
 *       There are 1 to {@link Config#MAX_CARS} allowed. </li>
 * </ul>
 * </p>
 * <p>All lines must have the same length, used to initialize the grid width).
 * Beginning empty lines are skipped.
 * The the tracks ends with the first empty line or the file end.<br>
 * An {@link InvalidTrackFormatException} is thrown, if
 * <ul>
 *   <li>not all track lines have the same length</li>
 *   <li>the file contains no track lines (grid height is 0)</li>
 *   <li>the file contains more than {@link Config#MAX_CARS} cars</li>
 * </ul>
 *
 * <p>The Track can return a String representing the current state of the race (including car positons)</p>
 */
public class Track {

    private List<Config.SpaceType[]> grid;
    private int width = -1;
    private int height = -1;
    private List<Car> cars;
    private Map<Character, List<PositionVector>> paths;
    private Map<Character, char[][]> pathGrids;

    /**
     * Initialize a Track from the given track file.
     *
     * @param  trackFile Reference to a file containing the track data
     * @throws FileNotFoundException if the given track file could not be found
     * @throws InvalidTrackFormatException if the track file contains invalid data (no tracklines, no
     */
    public Track(File trackFile) throws FileNotFoundException, InvalidTrackFormatException
    {
        this.grid = new ArrayList<>();
        this.cars = new ArrayList<>();
        this.paths = new HashMap<>();
        this.pathGrids = new HashMap<>();

        try {
            readTrackFile(trackFile);
            checkPathFiles(trackFile);
        } catch (IOException e) {
            FileNotFoundException newException = new FileNotFoundException();
            newException.addSuppressed(e);
            throw newException;
        }
    }

    private void readTrackFile(File trackFile) throws IOException, InvalidTrackFormatException {
        boolean trackGridFound = false;
        BufferedReader reader = Files.newBufferedReader(Paths.get(trackFile.getPath()));
        String line;
        int lineIndex = 0;
        while ((line = reader.readLine()) != null && !(trackGridFound && line.length() == 0)) {
            if (line.length() > 0) {
                this.grid.add(this.processLine(line, lineIndex));
                trackGridFound = true;
                lineIndex++;
            }
        }
        if (lineIndex == 0) {
            throw new InvalidTrackFormatException("Track without grid is invalid.");
        }
        this.height = lineIndex;
    }

    private void checkPathFiles(File trackFile) throws IOException, InvalidTrackFormatException {
        for (File possiblePathFile : trackFile.getParentFile().listFiles()) {
            String fileName = possiblePathFile.getName();
            String[] fileNameParts = fileName.split("[.]");
            if (fileNameParts.length > 1 && fileName.startsWith(fileNameParts[0]) && fileName.contains("_path_")) {
                String[] nameAndPath = fileNameParts[0].split("_path_");
                if (nameAndPath.length == 2) {
                    readPath(nameAndPath[1], possiblePathFile);
                }
            }
        }
    }

    private void readPath(String pathName, File file) throws IOException, InvalidTrackFormatException {
        if (pathName.length() != 1) {
            throw new IllegalArgumentException("Pathname must be one character");
        }
        char pathChar = pathName.charAt(0);
        System.out.println("pathChar: " + pathChar);
        this.pathGrids.put(pathChar, new char[this.height][this.width]);
        BufferedReader reader = Files.newBufferedReader(Paths.get(file.getPath()));
        String line;
        int lineIndex = 0;
        while ((line = reader.readLine()) != null) {
            if (line.length() > 0) {
                this.processPathLine(pathChar, line, lineIndex);
                lineIndex++;
            }
        }
    }

    public Config.SpaceType getSpaceType(PositionVector position) {
        return this.grid.get(position.getY())[position.getX()];
    }

    public int getCarCount() {
        return this.cars.size();
    }

    public char getCarId(int carNr) {
        return this.cars.get(carNr).getId();
    }

    public PositionVector getCarPosition(int carNr) {
        return this.cars.get(carNr).getPosition();
    }

    public PositionVector getCarVelocity(int carNr) {
        return this.cars.get(carNr).getVelocity();
    }

    private Character getCarId(PositionVector positionVector) {
        for (Car car : this.cars) {
            if (car.getPosition().equals(positionVector)) {
                return car.getId();
            }
        }
        return null;
    }

    private Config.SpaceType[] processLine(String line, int lineIndex) throws InvalidTrackFormatException {
        if (this.width == -1) {
            this.width = line.length();
        } else if (this.width != line.length()) {
            throw new InvalidTrackFormatException("All lines must have the same length.");
        }

        Config.SpaceType[] lineOfSpaces = new Config.SpaceType[this.width];
        for (int charIndex = 0; charIndex < line.length(); charIndex++) {
            char c = line.charAt(charIndex);
            Config.SpaceType spaceType = Config.SpaceType.get(c);
            if (spaceType != null) {
                lineOfSpaces[charIndex] = spaceType;
            } else {
                lineOfSpaces[charIndex] = Config.SpaceType.TRACK;
                addCarToGrid(lineIndex, charIndex, c);
            }
        }
        return lineOfSpaces;
    }

    private void processPathLine(char pathName, String line, int lineIndex) {
        for (int charIndex = 0; charIndex < line.length() && lineIndex <= this.height; charIndex++) {
            char c = line.charAt(charIndex);
            this.pathGrids.get(pathName)[lineIndex][charIndex] = c;
        }
    }

    private void addCarToGrid(int yPosition, int xPosition, char c) throws InvalidTrackFormatException {
        if (this.cars.size() + 1 > Config.MAX_CARS) {
            throw new InvalidTrackFormatException("The cars contained in this track file, exceed the car-limit.");
        }
        this.cars.add(new Car(c, new PositionVector(xPosition, yPosition), new PositionVector(0, 0)));
    }

    public List<Config.SpaceType[]> getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<ch.zhaw.pm2.racetrack.Car> getCars() {
        return cars;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int y = 0;
        for (Config.SpaceType[] line : this.grid) {
            int x = 0;
            for (Config.SpaceType spaceType : line) {
                Character carIdOnThisSpace = this.getCarId(new PositionVector(x, y));
                if (carIdOnThisSpace != null) {
                    stringBuilder.append(carIdOnThisSpace);
                } else {
                    stringBuilder.append(spaceType.getValue());
                }
                x++;
            }
            stringBuilder.append("\n");
            y++;
        }
        return stringBuilder.toString();
    }

    public Map<Character, char[][]> getPathGrids() {
        return pathGrids;
    }

    public PositionVector getCarPos(int id){ return cars.get(id).getPosition(); }


    public void setCarIsCrashed(int id) {
        cars.get(id).crash();
    }

}
