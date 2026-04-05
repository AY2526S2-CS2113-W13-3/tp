package parser;

import task.IndustryTag;

/**
 * Represents a command that has been parsed from raw user input.
 * Carries necessary data to the CommandRunner for execution.
 * Internal structure optimized for encapsulation and readability.
 */
public class ParsedCommand {

    private final CommandType type;
    private final String errorMessage;

    private int index = -1;
    private String company;
    private String position;
    private String date;

    private String searchTerm;
    private final String searchType;

    private String statusValue;
    private String note;

    private IndustryTag tag;
    private boolean isAddTag;

    private String newCompany;
    private String newPosition;
    private String newDate;
    private String newStatus;


    /** Simple commands (LIST, SORT, HELP, BYE) */
    public ParsedCommand(CommandType type) {
        this.type = type;
        this.errorMessage = null;
        this.searchType = null;
    }

    /** FILTER command */
    public ParsedCommand(CommandType type, String searchTerm) {
        this.type = type;
        this.searchTerm = searchTerm;
        this.errorMessage = null;
        this.searchType = null;
    }

    /** ADD command */
    public ParsedCommand(String company, String position, String date) {
        this(CommandType.ADD);
        this.company = company;
        this.position = position;
        this.date = date;
    }

    /** DELETE command */
    public ParsedCommand(int index) {
        this(CommandType.DELETE);
        this.index = index;
    }

    /** EDIT command */
    public ParsedCommand(int index, String company, String position, String date, String status) {
        this(CommandType.EDIT);
        this.index = index;
        this.newCompany = company;
        this.newPosition = position;
        this.newDate = date;
        this.newStatus = status;
    }

    /** SEARCH command */
    public ParsedCommand(String searchType, String searchTerm) {
        this.type = CommandType.SEARCH;
        this.searchType = searchType;
        this.searchTerm = searchTerm;
        this.errorMessage = null;
    }

    /** STATUS command */
    public ParsedCommand(int index, String status, String note) {
        this(CommandType.STATUS);
        this.index = index;
        this.statusValue = status;
        this.note = note;
    }

    /** TAG command */
    public ParsedCommand(int index, IndustryTag tag, boolean isAdd) {
        this(CommandType.TAG);
        this.index = index;
        this.tag = tag;
        this.isAddTag = isAdd;
    }


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

    public String getSearchType() {
        return searchType;
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
