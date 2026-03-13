package task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * Represents a job application and provides methods to manage its data.
 */
public class Add implements Comparable<Add>{
    private String company;
    private String position;
    private LocalDate date; 
    private String status;

    public Add(String company, String position, String dateStr) {
        this.company = company;
        this.position = position;
        this.date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        this.status = "Pending";
    }

    @Override
    public int compareTo(Add other) {
        return this.date.compareTo(other.date);
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return company + " | " + position + " | " + date + " | " + status;
    }
}

