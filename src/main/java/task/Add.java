package task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a job application and provides methods to manage its data.
 */
public class Add implements Comparable<Add>{
    private String company;
    private String position;
    private LocalDate date; 
    private String status;

    public Add(String company, String position, String date) throws DateTimeParseException {
        assert company != null: "Company cannot be null" ;
        assert position != null: "Position cannot be null";
        assert date != null: "Date cannot be null";

        assert !company.trim().isEmpty(): "Company cannot be empty string!";
        assert !position.trim().isEmpty(): "Position cannot be empty string!";
        assert !date.trim().isEmpty(): "Date cannot be empty string!";

        assert date.matches("\\d{4}-\\d{2}-\\d{2}")
                : "Date should be in YYYY-MM-DD format, but got: " + date;

        this.company = company;
        this.position = position;
        this.date = LocalDate.parse(date);
        this.status = "Pending";
    }

    @Override
    public int compareTo(Add other) {
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

    @Override
    public String toString() {
        assert company != null: "Company cannot be null" ;
        assert position != null: "Position cannot be null";
        assert date != null: "Date cannot be null";
        assert status != null: "Status cannot be null";

        return company + " | " + position + " | " + date + " | " + status;
    }
}

