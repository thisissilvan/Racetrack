package ch.zhaw.pm2.racetrack.exception;

public class CarIsCrashedException extends RuntimeException {
    private String key;

    /**
     * Store the details in error.
     * @param key The key with no match.
     */
    public CarIsCrashedException(String key) {
        this.key = key;
    }

    /**
     * @return The key in error.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return A diagnostic string containing the key in error.
     */
    public String toString() {
        return "Car is crashed.";
    }
}
