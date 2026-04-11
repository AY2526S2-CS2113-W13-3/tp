package task;

import exception.JobPilotException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Represents a job application and provides methods to manage its data.
 */

public class Application implements Comparable<Application> {

    private String company;
    private String position;
    private String date;
    private String status;
    private String notes;
    private final Set<IndustryTag> industryTags = new HashSet<>();

    public Application(String company, String position, String date) throws DateTimeParseException {
        assert company != null: "Company cannot be null" ;
        assert position != null: "Position cannot be null";
        assert date != null: "Date cannot be null";

        assert !company.trim().isEmpty(): "Company cannot be empty string!";
        assert !position.trim().isEmpty(): "Position cannot be empty string!";
        assert !date.trim().isEmpty(): "Date cannot be empty string!";

        // Validate date
        LocalDate.parse(date);

        this.company = company;
        this.position = position;
        this.date = date;
        this.status = "PENDING";
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
        assert date != null : "Date should not be null";
        return date;
    }

    public void setStatus(String status) {
        assert status != null: "Status should not be null";
        assert !status.trim().isEmpty() : "Status cannot be empty";
        this.status = status.trim().toUpperCase();
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
    public void setDate(String date) throws JobPilotException {
        assert date != null && !date.trim().isEmpty()
                : "Date cannot be null or empty";

        // Validate date
        LocalDate.parse(date);

        this.date = date;
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

        return company + " | " + position + " | " + date + " | " + status + noteDisplay + tagDisplay;
    }

    public void addIndustryTag(IndustryTag tag) {
        assert tag != null : "Tag cannot be null";
        industryTags.add(tag);
    }

    public void removeIndustryTag(IndustryTag tag) {
        assert tag != null : "Tag cannot be null";
        industryTags.remove(tag);
    }

    public Set<IndustryTag> getIndustryTags() {
        return Collections.unmodifiableSet(industryTags);
    }
}
