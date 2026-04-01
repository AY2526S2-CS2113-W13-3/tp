package parser;

import task.IndustryTag;

// @@author Aswin-RajeshKumar
/**
 * Represents a command that has been parsed from raw user input.
 * It carries the necessary data to the CommandRunner for execution.
 */
public class ParsedCommand {

    // Core Command Identity
    private final CommandType type;
    private final String errorMessage;

    // Command Data Fields
    private int index = -1;
    private String company;
    private String position;
    private String date;

    // Search & Filter Fields
    private String searchTerm;

    // Status & Notes Fields
    private String statusValue;
    private String note;

    // Tag Fields
    private IndustryTag tag;
    private boolean isAddTag;

    // Edit Fields (Mapped to new values)
    private String newCompany;
    private String newPosition;
    private String newDate;
    private String newStatus;

    // ========== CONSTRUCTORS ==========

    /**
     * Constructor for simple commands (LIST, SORT, HELP, BYE).
     */
    public ParsedCommand(CommandType type) {
        this.type = type;
        this.errorMessage = null;
    }

    /**
     * Constructor for ERROR, FILTER, or SEARCH commands.
     */
    public ParsedCommand(CommandType type, String stringValue) {
        this.type = type;
        if (type == CommandType.ERROR) {
            this.errorMessage = stringValue;
            this.searchTerm = null;
        } else {
            this.errorMessage = null;
            this.searchTerm = stringValue;
        }
    }

    /**
     * Constructor for ADD command.
     */
    public ParsedCommand(String company, String position, String date) {
        this.type = CommandType.ADD;
        this.company = company;
        this.position = position;
        this.date = date;
        this.errorMessage = null;
    }

    /**
     * Constructor for DELETE command.
     */
    public ParsedCommand(int index) {
        this.type = CommandType.DELETE;
        this.index = index;
        this.errorMessage = null;
    }

    /**
     * Constructor for EDIT command.
     */
    public ParsedCommand(int index, String company, String position, String date, String status) {
        this.type = CommandType.EDIT;
        this.index = index;
        this.newCompany = company;
        this.newPosition = position;
        this.newDate = date;
        this.newStatus = status;
        this.errorMessage = null;
    }

    /**
     * Constructor for STATUS command (Status and Note separation).
     */
    public ParsedCommand(int index, String status, String note) {
        this.type = CommandType.STATUS;
        this.index = index;
        this.statusValue = status;
        this.note = note;
        this.errorMessage = null;
    }

    /**
     * Constructor for TAG command.
     */
    public ParsedCommand(int index, IndustryTag tag, boolean isAdd) {
        this.type = CommandType.TAG;
        this.index = index;
        this.tag = tag;
        this.isAddTag = isAdd;
        this.errorMessage = null;
    }

    // ========== GETTERS ==========

    public CommandType getType() {
        return type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getIndex() {
        return index;
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getDate() {
        return date;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public String getNote() {
        return note;
    }

    public IndustryTag getTag() {
        return tag;
    }

    public boolean isAddTag() {
        return isAddTag;
    }

    public String getNewCompany() {
        return newCompany;
    }

    public String getNewPosition() {
        return newPosition;
    }

    public String getNewDate() {
        return newDate;
    }

    public String getNewStatus() {
        return newStatus;
    }
}
// @@author