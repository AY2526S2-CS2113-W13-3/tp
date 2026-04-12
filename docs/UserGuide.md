# User Guide

## Introduction

JobPilot is an application designed to help computing students manage their job applications efficiently. It works through a Command Line Interface (CLI), but still provides the convenience of a simple graphical interface. 
By using JobPilot, users can track application progress and important details without the hassle of manual lists or spreadsheets.

## Quick Start

1. **Install Java 17+:** Verify that your computer has Java `17` or a newer version installed. <br>
   *Mac users:* Please follow the specific JDK installation guide [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
2. **Download the App:** Grab the latest `.jar` release file from [here](https://github.com/AY2526S2-CS2113-W13-3/tp/releases).
3. **Set Up Your Directory:** Move the downloaded file into a dedicated new folder. (Note: Running the app for the first time will automatically generate a `data/JobPilotData.json` file in this directory to save your applications).
4. **Launch JobPilot:** Open your terminal, navigate to the folder, and run the following command: `java -jar JobPilot.jar`

## Features

### Viewing help message: `help`
Shows a message explaining the available commands in JobPilot.

Format: `help`

Example output:

Note: only a portion of the message is shown for brevity.

```text
Available Commands:
add c/COMPANY p/POSITION d/DATE         Add a new job application
edit INDEX [...]                        Edit existing application
delete INDEX                            Delete an application
...
help                                    Show this message
bye                                     Exit the application
____________________________________________________
```

_Note:_ In the `help` output above, `search [c/COMPANY] [p/POSITION] [s/STATUS]` means you use **any one** of these forms per command (company **or** position **or** status), not several prefixes in the same line.

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

Format: status INDEX [s/STATUS] [note/NOTE]

Updates the application at the specified INDEX.

s/STATUS: The current stage (e.g., Invited, Rejected, Offer).

note/NOTE: (Optional) Additional context, comments, or interview feedback.

Flexibility: You can update both fields at once, or just one. If a field is omitted, the existing value is preserved. The order of s/ and note/ does not matter.

Examples:

status 1 s/Interview note/Scheduled for next Tuesday

status 2 s/Rejected (Previous note remains unchanged)

status 3 note/Salary negotiation in progress (Status remains unchanged)

Example Output:

Plaintext
Status updated:
Google | SE manager | 2025-03-10 | INTERVIEW (Note: Scheduled for next Tuesday) | Tags: [TECH]

### Filtering applications by status: 'filter'
Retrieves all applications that match a specific status. This is useful for focusing only on active leads.

Format: filter s/STATUS

The search is case-insensitive and supports partial matching (e.g., `filter s/off` will find applications with the status "OFFER").

Examples:

filter s/OFFER

filter s/pending

Example Output:

Plaintext
Found 2 application(s) with status 'OFFER':
1. Google | SE manager | 2025-03-10 | OFFER (Note: Salary negotiation)
2. Shopee | Backend Intern | 2025-03-15 | OFFER (Note: Pending acceptance)

### Managing industry tags: `tag`

You can attach **industry tags** (for example `TECH`, `FINANCE`) to each application so you can see the sector at a glance in `list` output. One application may have multiple tags.

Format:

- `tag INDEX add/TAG` — add a tag to the application at the given `INDEX` (the number shown by `list`)
- `tag INDEX remove/TAG` — remove that tag from the application

Notes:

- `TAG` must not be empty (there must be text after `add/` or `remove/`).
- Tag text is **normalized to uppercase** (e.g. `add/tech` is stored and shown as `TECH`).
- The same tag cannot be stored twice on one application; the set of tags on an entry stays unique.

Examples:

- `tag 1 add/TECH`
- `tag 1 add/FINANCE`
- `tag 1 remove/TECH`

Example output:

```text
Added tag: TECH -> Google | SE manager | 2025-03-10 | PENDING | Tags: [TECH]
___________________________________________________________________
```

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
- Search function only supports searching
  by one dimension

**Single field per command:** each `search` uses **exactly one** prefix (`c/`, `p/`, or `s/`) and one keyword. Combining multiple criteria in one command (for example `search c/google p/engineer`) is **not supported** in the current version—run separate searches if you need more than one filter.

**Empty or incomplete input:** the keyword after `/` must not be empty (you will see an error such as `Search value cannot be empty!`). Running `search` alone, or without a valid `c/…`, `p/…`, or `s/…` pattern, also shows an error (for example `Please provide a search query. Example: search c/google` or `Invalid format! Use: search c/xxx or p/xxx or s/xxx`).

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

### Sorting Applications: `sort`

Sorts applications by date, company name, or status, with support for ascending or reverse order.
No applications will be removed during sorting.

- **`sort` alone** sorts by **submission date** (ascending), same as `sort date`.
- After sorting, the app prints one line describing what was used, e.g. `Sorted by submission date.` or `Sorted by company name (reverse order).`
- Only the keywords **`date`**, **`company`**, and **`status`** (each optionally with **`reverse`**) are accepted; other words (e.g. `sort hi`) show an error and do **not** reorder the list.

Format: 
- `sort`
- `sort date`
- `sort company`
- `sort status`
- `sort date reverse`
- `sort company reverse`
- `sort status reverse`

Examples:
- `sort`
- `sort date reverse`
- `sort company`
- `sort status reverse`

Example output:

```text
Sorted by submission date.
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

| Action | Format, Examples |
|--------|----------------------------------------------------------------------------------------------------------------------|
| Add | `add c/COMPANY p/POSITION d/DATE` <br> e.g., `add c/Google p/Software Engineer d/2026-03-29` |
| Edit | `edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]` <br> e.g., `edit 2 c/Google p/Backend Engineer s/Interview` |
| Delete | `delete INDEX` <br> e.g., `delete 1` |
| Status | `status INDEX [s/STATUS] [note/NOTE]` <br> e.g., `status 1 s/Interview note/Phone screening completed` |
| Filter | `filter s/STATUS` <br> e.g., `filter s/Pending` |
| Tag Add | `tag INDEX add/TAG` <br> e.g., `tag 1 add/Tech` |
| Tag Remove | `tag INDEX remove/TAG` <br> e.g., `tag 1 remove/Tech`|
| List | `list` |
| Sort | `sort` <br>`sort date` <br>`sort company` <br>`sort status` <br>`sort date reverse` <br>`sort company reverse` <br>`sort status reverse` |
| Search | `search c/COMPANY` <br> `search p/POSITION` <br> `search s/STATUS` <br> e.g., `search c/Google` |
| Help | `help` |
| Exit | `bye` |
