package storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Application;
import task.IndustryTag;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    private Storage storage;
    private File file;

    @BeforeEach
    void setUp() {
        storage = new Storage();

        file = new File(System.getProperty("user.dir") + "/data/JobPilotData.json");

        if (file.exists()) {
            file.delete();
        }

        file.getParentFile().mkdirs();
    }

    @AfterEach
    void tearDown() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void saveAndLoadSingleApplication_shouldPreserveData() {
        ArrayList<Application> appsToSave = new ArrayList<>();

        Application app = new Application("Google", "SE Manager", "2025-03-10");
        app.setStatus("INTERVIEW");
        app.setNotes("Bring portfolio");
        app.addIndustryTag(new IndustryTag("Tech"));

        appsToSave.add(app);

        storage.saveToFile(appsToSave);

        ArrayList<Application> loadedApps = storage.loadFromFile();

        assertEquals(1, loadedApps.size());

        Application loadedApp = loadedApps.get(0);

        assertEquals("Google", loadedApp.getCompany());
        assertEquals("SE Manager", loadedApp.getPosition());
        assertEquals("2025-03-10", loadedApp.getDate());
        assertEquals("INTERVIEW", loadedApp.getStatus());
        assertEquals("Bring portfolio", loadedApp.getNotes());

        assertTrue(
                loadedApp.getIndustryTags().stream()
                        .anyMatch(tag -> tag.getTagName().equals("TECH"))
        );
    }

    @Test
    void saveAndLoadMultipleApplications_shouldPreserveAllData() {
        ArrayList<Application> appsToSave = new ArrayList<>();

        Application app1 = new Application("Google", "SE Manager", "2025-03-10");
        app1.setStatus("INTERVIEW");
        app1.addIndustryTag(new IndustryTag("Tech"));

        Application app2 = new Application("Amazon", "Data Scientist", "2025-04-01");
        app2.setStatus("APPLIED");
        app2.setNotes("Follow up in 2 weeks");
        app2.addIndustryTag(new IndustryTag("E-COMMERCE"));

        appsToSave.add(app1);
        appsToSave.add(app2);

        storage.saveToFile(appsToSave);

        ArrayList<Application> loadedApps = storage.loadFromFile();

        assertEquals(2, loadedApps.size());

        // first app
        assertEquals("Google", loadedApps.get(0).getCompany());
        assertEquals("SE Manager", loadedApps.get(0).getPosition());
        assertEquals("2025-03-10", loadedApps.get(0).getDate());
        assertEquals("INTERVIEW", loadedApps.get(0).getStatus());

        assertTrue(
                loadedApps.get(0).getIndustryTags().stream()
                        .anyMatch(tag -> tag.getTagName().equals("TECH"))
        );

        // second app
        assertEquals("Amazon", loadedApps.get(1).getCompany());
        assertEquals("Data Scientist", loadedApps.get(1).getPosition());
        assertEquals("2025-04-01", loadedApps.get(1).getDate());
        assertEquals("APPLIED", loadedApps.get(1).getStatus());
        assertEquals("Follow up in 2 weeks", loadedApps.get(1).getNotes());

        assertTrue(
                loadedApps.get(1).getIndustryTags().stream()
                        .anyMatch(tag -> tag.getTagName().equals("E-COMMERCE"))
        );
    }

    @Test
    void saveToFile_shouldOverwriteExistingData() {
        ArrayList<Application> first = new ArrayList<>();
        first.add(new Application("A", "Dev", "2026-01-01"));

        storage.saveToFile(first);

        ArrayList<Application> second = new ArrayList<>();
        second.add(new Application("B", "QA", "2026-02-01"));

        storage.saveToFile(second);

        ArrayList<Application> loaded = storage.loadFromFile();

        assertEquals(1, loaded.size());
        assertEquals("B", loaded.get(0).getCompany());
    }

    @Test
    void saveToFile_emptyList_shouldReturnEmptyListOnLoad() {
        ArrayList<Application> emptyList = new ArrayList<>();

        storage.saveToFile(emptyList);

        ArrayList<Application> loadedApps = storage.loadFromFile();

        assertTrue(loadedApps.isEmpty());
    }

    @Test
    void saveToFile_shouldCreateFileIfNotExists() {
        if (file.exists()) {
            file.delete();
        }

        ArrayList<Application> apps = new ArrayList<>();
        apps.add(new Application("Google", "SE", "2026-01-01"));

        storage.saveToFile(apps);

        assertTrue(file.exists());
    }

    @Test
    void load_whenFileDoesNotExist_shouldReturnEmptyList() {
        if (file.exists()) {
            file.delete();
        }

        ArrayList<Application> loaded = storage.loadFromFile();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }

    @Test
    void loadFromFile_withCorruptedJson_shouldReturnEmptyList() throws Exception {
        FileWriter writer = new FileWriter(file, false);
        writer.write("{ this is not valid json }");
        writer.close();

        ArrayList<Application> loaded = storage.loadFromFile();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }
}
