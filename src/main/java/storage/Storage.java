package storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import task.Application;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles reading from and writing to the persistent storage file for JobPilot.
 * Ensures that the application list is saved between sessions.
 */
public class Storage {

    private static final Logger LOGGER = Logger.getLogger(Storage.class.getName());

    private static final String CURRENT_WORKING_DIRECTORY = System.getProperty("user.dir");
    private static final File FILE = Paths.get(CURRENT_WORKING_DIRECTORY, "data", "JobPilotData.json").toFile();

    private final Gson gson;
    private final File jobPilotDataFile;

    /**
     * Initializes the storage management.
     * Ensures the storage file exists before any operations are performed.
     */
    public Storage() {
        this.jobPilotDataFile = FILE;
        LOGGER.setLevel(Level.SEVERE);

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class,
                        (com.google.gson.JsonSerializer<LocalDate>) (src, type, context) ->
                                new com.google.gson.JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class,
                        (com.google.gson.JsonDeserializer<LocalDate>) (json, type, context) ->
                                LocalDate.parse(json.getAsString()))
                .create();

        ensureFileExists();
    }

    /**
     * Checks for the existence of the data directory and the storage file, JobPilotData.json.
     * If they do not exist, it creates them.
     */
    private void ensureFileExists() {
        try {
            File parentDir = jobPilotDataFile.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (!jobPilotDataFile.exists()) {
                jobPilotDataFile.createNewFile();
            }

        } catch (IOException e) {
            System.out.println("Failed to create storage file: " + e.getMessage());
        }
    }

    /**
     * Reads applications from the storage file, parses them, and returns them as a list.
     *
     * @return An ArrayList containing the saved Applications. Returns an empty list if the file is empty.
     */
    public ArrayList<Application> loadFromFile() {
        ensureFileExists();

        ArrayList<Application> applications = new ArrayList<>();

        try (FileReader reader = new FileReader(jobPilotDataFile)) {

            Type listType = new TypeToken<ArrayList<Application>>() {}.getType();

            ArrayList<Application> data = gson.fromJson(reader, listType);

            if (data != null) {
                applications = data;
            }

        } catch (Exception e) {
            System.out.println("Error reading data: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Load error", e);
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
        ensureFileExists();

        try (FileWriter writer = new FileWriter(jobPilotDataFile)) {

            gson.toJson(applications, writer);

        } catch (IOException e) {
            System.out.println("I could not save your data! " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Save error", e);
        }
    }
}
