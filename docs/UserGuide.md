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
status INDEX set/STATUS note/NOTE                           Update application status and add a note
filter s/STATUS                                             Filter applications by status
tag INDEX add/TAG                                           Add a tag to an application
tag INDEX remove/TAG                                        Remove a tag from an application
list                                                        List all job applications
sort                                                        Sort applications by date
search [c/COMPANY] [p/POSITION] [s/STATUS]                  Search applications
help                                                        Show this message
bye                                                         Exit the application
___________________________________________________________________
```

### Adding an application: add
Adds a new job application to JobPilot.

Format: add c/COMPANY p/POSITION d/DATE
- c/COMPANY - Name of the company
- p/POSITION - Job title/position
- d/DATE - Application submission date in YYYY-MM-DD format

Example:
add c/Google p/SE manager d/2025-03-10

Example output:
Added: Google | SE manager | 2025-03-10 | Pending

### Editing an application: edit
Edits an existing application's fields. Only specified fields will be updated.

Format: edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]

- INDEX - Position of the application in the list (from list command)
- c/COMPANY - (Optional) New company name
- p/POSITION - (Optional) New position title
- d/DATE - (Optional) New submission date in YYYY-MM-DD format
- s/STATUS - (Optional) New status

Examples:
- edit 1 c/Apple - Change company only
- edit 2 p/Senior Engineer - Change position only
- edit 3 d/2027-01-09 - Change date only
- edit 1 c/Google p/SWE d/2024-09-12 s/Offer - Change multiple fields

Example output:
Updated application:
Apple | SE manager | 2025-03-10 | OFFER (Note: Negotiate salary) | Tags: [TECH]

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
___________________________________________________________________
```

### Updating application status: 'status'
Updates the recruitment progress of a specific job application and allows for independent note-taking. This allows you to track interview feedback or offer details separately from the company info.

Format: status INDEX [set/STATUS] [note/NOTE]

Updates the application at the specified INDEX.

set/STATUS: The current stage (e.g., Invited, Rejected, Offer).

note/NOTE: (Optional) Additional context, comments, or interview feedback.

Flexibility: You can update both fields at once, or just one. If a field is omitted, the existing value is preserved. The order of set/ and note/ does not matter.

Examples:

status 1 set/Interview note/Scheduled for next Tuesday

status 2 set/Rejected (Previous note remains unchanged)

status 3 note/Salary negotiation in progress (Status remains unchanged)

Example Output:

Plaintext
Status updated:
Google | SE manager | 2025-03-10 | INTERVIEW (Note: Scheduled for next Tuesday) | Tags: [TECH]

### Filtering applications by status: 'filter'
Retrieves all applications that match a specific status. This is useful for focusing only on active leads.

Format: filter status/STATUS

The search is case-insensitive and supports partial matching (e.g., filter status/off will find applications with the status "OFFER").

Examples:

filter status/OFFER

filter status/pending

Example Output:

Plaintext
Found 2 application(s) with status matching 'OFFER':
1. Google | SE manager | 2025-03-10 | OFFER (Note: Salary negotiation)
2. Shopee | Backend Intern | 2025-03-15 | OFFER (Note: Pending acceptance)

### Listing
Lists all the applications

Format: `list`

Example: `list`

Example Output:
```text
Here are your applications:
1. Google | SE manager | 2025-03-10 | Pending
2. Amazon | Data Analyst | 2025-03-08 | Pending
3. Microsoft | SDE Intern | 2025-03-12 | Pending
___________________________________________________________________
```

### Searching Applications
- Searches job applications by matching the company name
  or the position or the current status
- Uses a **case-insensitive partial search**.

Format:
- `search c/COMPANY_NAME`
- `search p/POSITION`
- `search s/STATUS`

Examples:
- `search c/Google`
```text
Found 1 application(s) matching 'c/google':
1. Google | SE manager | 2025-03-10 | OFFER (Note: Negotiate salary) | Tags: [TECH]
___________________________________________________________________

```

- `search c/micro`
```text
Found 1 application(s) matching 'c/micro':
1. Microsoft | SDE Intern | 2025-03-12 | PENDING (Note: Waiting for reply) | Tags: [TECH, INTERN]
___________________________________________________________________
```

- `search p/intern`
```text
Found 1 application(s) matching 'p/intern':
1. Microsoft | SDE Intern | 2025-03-12 | PENDING (Note: Waiting for reply) | Tags: [TECH, INTERN]
___________________________________________________________________
```

- `search s/offer`
```text
Found 1 application(s) matching 's/offer':
1. Google | SE manager | 2025-03-10 | OFFER (Note: Negotiate salary) | Tags: [TECH]
___________________________________________________________________
```

### Exiting the program: bye

Exits JobPilot and saves the application data to a readable text file.

Format: `bye`

Example output:

```text
Bye! You added 5 application(s).
___________________________________________________________________
```

## FAQ

**Q**: How do I transfer my data to another computer?

**A**: Copy the application's data file from your current computer to the same file location on the new computer. 
       Make sure the application is closed before copying the file to avoid losing any recent changes.

**Q**: What should I do if I accidentally delete an application entry?

**A**: Deleted entries cannot be restored automatically. To avoid losing important information, consider keeping a backup 
       copy of your data file before making major changes.

**Q**: Why is my command not working?

**A**: Check that your command follows the correct format as provided in the `help` command and includes all required fields.

**Q**: Where is my data stored?

**A**: Your data is stored locally on your computer in the application's data folder. This allows your saved applications 
       to remain available even after you close the app.

**Q**: Can I edit an existing application entry?

**A**: Yes. You can use the edit command to edit details without creating a new entry.

**Q**: Can I use the application on multiple computers at the same time?

**A**: You can use the application on multiple computers, but changes made on one computer will not automatically sync 
       to the other unless you manually transfer the updated data file.

## Command Summary

| Action | Format, Examples|
|--------|--------------------------------------------------------------------------------------------------|
| Add | `add c/COMPANY p/POSITION d/DATE` <br> e.g., `add c/Google p/Software Engineer d/2026-03-29`|
| Edit | `edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]` <br> e.g., `edit 2 c/Google p/Backend Engineer s/Interview`|
| Delete | `delete INDEX` <br> e.g., `delete 1`|
| Status | `status INDEX set/STATUS note/NOTE` <br> e.g., `status 1 set/Interview note/Phone screening completed`|
| Filter | `filter s/STATUS` <br> e.g., `filter s/Applied`|
| Tag Add | `tag INDEX add/TAG` <br> e.g., `tag 1 add/Tech`|
| Tag Remove | `tag INDEX remove/TAG` <br> e.g., `tag 1 remove/Tech`|
| List | `list`|
| Sort | `sort`|
| Search | `search [c/company] [p/position] [s/status]` <br> e.g., `search c/Google`|
| Help | `help`|
| Exit | `bye`|
