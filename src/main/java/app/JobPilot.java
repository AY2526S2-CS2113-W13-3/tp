package app;

import parser.Parser;
import parser.ParsedCommand;
import task.*;
import ui.Ui;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point of the JobPilot application.
 * Handles the main execution loop and delegates command execution.
 */
public class JobPilot {
    private static final Logger LOGGER = Logger.getLogger(JobPilot.class.getName());

    public static void main(String[] args) {
        LOGGER.setLevel(Level.OFF);

        Ui.showWelcome();
        ArrayList<Application> applications = new ArrayList<>();
        CommandRunner runner = new CommandRunner(applications);

        while (true) {
            String input = Ui.readCommand();
            ParsedCommand cmd = Parser.parse(input);

            boolean shouldContinue = runner.run(cmd);

            if (!shouldContinue) {
                break;
            }
        }
    }
}