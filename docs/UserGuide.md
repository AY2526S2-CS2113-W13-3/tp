# User Guide

## Introduction

JobPilot is an application designed to help computing students manage their job applications efficiently. It works through a Command Line Interface (CLI), but still provides the convenience of a simple graphical interface. 
By using JobPilot, users can track application progress and important details without the hassle of manual lists or spreadsheets.

## Quick Start

1. **Install Java 17+:** Verify that your computer has Java `17` or a newer version installed. <br>
   *Mac users:* Please follow the specific JDK installation guide [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
2. **Download the App:** Grab the latest `.jar` release file from [here]().
3. **Set Up Your Directory:** Move the downloaded file into a dedicated new folder. (Note: Running the app for the first time will automatically generate a `data/JobPilotData.txt` file in this directory to save your tasks).
4. **Launch JobPilot** Open your terminal and run the app with the following command: `java -jar <release-name>.jar`

## Features 

{Give detailed description of each feature}

### Viewing help message: `help`
Shows a message explaining the available commands in JobPilot.

Format: `help`

Example output:

```text
Available Commands:
add c/COMPANY p/POSITION d/DATE                             Add a new job application
edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]     Edit existing application
delete INDEX                                                Delete an application
status INDEX set/STATUS note/NOTE                           Update  application status and add a note
filter status/STATUS                                        Filter applications by status
tag INDEX add/TAG                                           Add a tag to an application
tag INDEX remove/TAG                                        Remove a tag from an application
list                                                        List all job applications
sort                                                        Sort applications by date
search COMPANY_NAME                                         Search applications by company name
help                                                        Show this message
bye                                                         Exit the application
```

### Deleting an application: `delete`
Deletes the specified application from JobPilot.

Format: `delete INDEX`

- Deletes the application at the specified INDEX.
- The index refers to the index number shown in the displayed application list though the list command.
- The index must be a positive integer 1, 2, 3, ...

Examples:
- `delete 1`
- `list` followed by `delete 2`

Example output:
```text
Deleted application:
Google | SE manager | 2025-03-10 | INTERVIEW
You have 4 application(s) left.
```

### Exiting the program: bye

Exits JobPilot and saves the application data to a readable text file.

Format: `bye`

Examples:
- `bye`

Example output:

```text
Bye! You added 4 application(s).
```

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: {your answer here}

## Command Summary

| Action | Format, Examples |
|--------|----------------|
| Add | `add c/COMPANY p/POSITION d/DATE` <br> e.g., `add c/Google p/Software Engineer d/2026-03-29` |
| Edit | `edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]` <br> e.g., `edit 2 c/Google p/Backend Engineer s/Interview` |
| Delete | `delete INDEX` <br> e.g., `delete 1` |
| Status | `status INDEX set/STATUS note/NOTE` <br> e.g., `status 1 set/Interview note/Phone screening completed` |
| Filter | `filter status/STATUS` <br> e.g., `filter status/Applied` |
| Tag Add | `tag INDEX add/TAG` <br> e.g., `tag 1 add/Tech` |
| Tag Remove | `tag INDEX remove/TAG` <br> e.g., `tag 1 remove/Tech` |
| List | `list` |
| Sort | `sort` |
| Search | `search COMPANY_NAME` <br> e.g., `search Google` |
| Help | `help` |
| Exit | `bye` |