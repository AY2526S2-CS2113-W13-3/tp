package storage;

import task.Application;
import task.IndustryTag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles reading from and writing to the persistent storage file for JobPilot.
 * Ensures that the application list is saved between sessions.
 */
public class Storage {
    private static final Logger LOGGER = Logger.getLogger(Storage.class.getName());
    public static final String CURRENT_WORKING_DIRECTORY = System.getProperty("user.dir");
    private static final Path FILE_PATH = Paths.get(CURRENT_WORKING_DIRECTORY, "data", "JobPilotData.txt");
    private File jobPilotDataFile;

    /**
     * Initializes the storage management.
     * Checks for the existence of the data directory and the storage text file, JobPilotData.txt.
     * If they do not exist, it creates them.
     */
    public Storage() {
        jobPilotDataFile = FILE_PATH.toFile();
        LOGGER.setLevel(Level.SEVERE);

        try {
            File parentDir = jobPilotDataFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (!jobPilotDataFile.exists()) {
                jobPilotDataFile.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong when creating the file: " + e.getMessage());
        }
    }

    /**
     * Reads applications from the storage file, parses them, and returns them as a list.
     * Invalid or corrupted lines in the text file are skipped with a warning printed to the console.
     *
     * @return An ArrayList containing the saved Applications. Returns an empty list if the file is empty.
     */
    public ArrayList<Application> loadFromFile() {
        ArrayList<Application> applications = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(jobPilotDataFile);

            LOGGER.log(Level.INFO, "Loading data from " + FILE_PATH);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s*\\|\\s*", -1);

                if (parts.length < 6) {
                    System.out.println("JobPilot skipped a corrupted line (too short): " + line);
                    continue;
                }

                String company = parts[0];
                String position = parts[1];
                String date = parts[2];
                String status = parts[3];
                String notes = parts[4];
                String tagsString = parts[5];

                try {
                    Application app = new Application(company, position, date);
                    app.setStatus(status);
                    app.setNotes(notes);

                    if (!tagsString.isEmpty()) {
                        String[] tags = tagsString.split(",");
                        for (String tag : tags) {
                            app.addIndustryTag(new IndustryTag(tag.trim()));
                        }
                    }
                    applications.add(app);
                } catch (Exception e) {
                    System.out.println("JobPilot skipped a corrupted application entry: " + line);
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "JobPilot Data file not found at " + FILE_PATH, e);
            System.out.println("No existing data file found. Starting fresh!");
        }

        return applications;
    }

    /**
     * Writes the current list of applications to the storage file.
     * Overwrites the existing file content with the updated Applications.
     *
     * @param applications The ArrayList containing the Application objects to be saved.
     */
    public void saveToFile(ArrayList<Application> applications) {
        try {
            FileWriter writer = new FileWriter(jobPilotDataFile, false);
            LOGGER.log(Level.INFO, "Saving data to " + FILE_PATH);

            for (Application app : applications) {
                StringBuilder sb = new StringBuilder();
                sb.append(app.getCompany()).append(" | ")
                        .append(app.getPosition()).append(" | ")
                        .append(app.getDate()).append(" | ")
                        .append(app.getStatus()).append(" | ")
                        .append(app.getNotes() != null ? app.getNotes() : "").append(" | ");

                ArrayList<String> tagNames = new ArrayList<>();
                for (IndustryTag tag : app.getIndustryTags()) {
                    tagNames.add(tag.getTagName());
                }
                sb.append(String.join(",", tagNames));

                writer.write(sb.toString() + System.lineSeparator());
            }
            writer.close();
            LOGGER.log(Level.INFO, "Exported empty warehouse state.");
        } catch (IOException e) {
            System.out.println("I could not save your data! " + e.getMessage());
        }
    }
}
