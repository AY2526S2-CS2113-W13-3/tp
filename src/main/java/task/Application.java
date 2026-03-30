package task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Represents a job application and provides methods to manage its data.
 */

public class Application implements Comparable<Application> {
    private static final Logger logger = Logger.getLogger(Application.class.getName());
    private String company;
    private String position;
    private LocalDate date;
    private String status;
    private String notes;
    private Set<IndustryTag> industryTags = new HashSet<>();

    public Application(String company, String position, String date) throws DateTimeParseException {
        assert company != null: "Company cannot be null" ;
        assert position != null: "Position cannot be null";
        assert date != null: "Date cannot be null";

        assert !company.trim().isEmpty(): "Company cannot be empty string!";
        assert !position.trim().isEmpty(): "Position cannot be empty string!";
        assert !date.trim().isEmpty(): "Date cannot be empty string!";

        this.company = company;
        this.position = position;
        this.date = LocalDate.parse(date);
        this.status = "Pending";
        this.notes = "";
    }

    @Override
    public int compareTo(Application other) {
        assert other != null: "Cannot compare with null object";
        assert other.date != null: "Other applications has null date";

        return this.date.compareTo(other.date);
    }

    public String getCompany() {
        assert company != null: "Company should not be null";
        return company;
    }

    public String getPosition() {
        assert position != null: "Position should not be null";
        return position;
    }

    public String getDate() {
        assert date != null: "Date should not be null";
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setStatus(String status) {
        assert status != null: "Status should not be null";
        assert !status.trim().isEmpty() : "Status cannot be empty";
        this.status = status;
    }

    public String getStatus() {
        assert status != null: "Status should not be null";
        return status;
    }

    public void setNotes(String notes) {
        assert notes != null: "Notes should not be null (empty allowed)";
        this.notes = notes;
    }

    public String getNotes() {
        assert notes != null: "Notes should not be null";
        return notes;
    }

    /**
     * Updates the company name of this application.
     *
     * @param company The new company name
     */
    public void setCompany(String company) {
        assert company != null && !company.trim().isEmpty()
                : "Company name cannot be null or empty";
        this.company = company;
    }

    /**
     * Updates the position title of this application.
     *
     * @param position The new position title
     */
    public void setPosition(String position) {
        assert position != null && !position.trim().isEmpty()
                : "Position cannot be null or empty";
        this.position = position;
    }

    /**
     * Updates the submission date of this application.
     *
     * @param date The new date in YYYY-MM-DD format
     */
    public void setDate(String date) {
        assert date != null && !date.trim().isEmpty()
                : "Date cannot be null or empty";
        this.date = LocalDate.parse(date);
    }

    @Override
    public String toString() {
        assert company != null: "Company cannot be null" ;
        assert position != null: "Position cannot be null";
        assert date != null: "Date cannot be null";
        assert status != null: "Status cannot be null";
        assert notes != null: "Notes cannot be null";

        String tagDisplay = industryTags.isEmpty() ? "" : " | Tags: " + industryTags;
        String noteDisplay = notes.isEmpty() ? "" : " (Note: " + notes + ")";

        return company + " | " + position + " | " + getDate() + " | " + status + noteDisplay + tagDisplay;
    }

    public void addIndustryTag(IndustryTag tag) {
        assert tag != null : "Tag cannot be null";
        industryTags.add(tag);
        logger.info("Added tag " + tag + " to application: " + company + " - " + position);
    }

    public void removeIndustryTag(IndustryTag tag) {
        assert tag != null : "Tag cannot be null";
        industryTags.remove(tag);
        logger.info("Removed tag " + tag + " from application: " + company + " - " + position);
    }

    public Set<IndustryTag> getIndustryTags() {
        return Collections.unmodifiableSet(industryTags);
    }
}