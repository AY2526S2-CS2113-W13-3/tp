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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage {

    private static final Logger LOGGER = Logger.getLogger(Storage.class.getName());

    private static final String CURRENT_WORKING_DIRECTORY = System.getProperty("user.dir");
    private static final File FILE = new File(CURRENT_WORKING_DIRECTORY + "/data/JobPilotData.json");

    private final Gson gson;
    private final File jobPilotDataFile;

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
