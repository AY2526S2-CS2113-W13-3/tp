package exception;

/**
* Throws exception and shows the user the error message caused by invalid input passed.
* */
public class JobPilotException extends Exception {
    public JobPilotException(String description) {
        super(description);
    }
}
