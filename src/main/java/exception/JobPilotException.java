package exception;

/**
* Throws exception and shows the user the error message caused by
 * incorrect input passed when parsing.
* */
public class JobPilotException extends Exception{
    public JobPilotException(String description) {
        super(description);
    }
}
