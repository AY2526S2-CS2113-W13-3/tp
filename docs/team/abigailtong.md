# Abigail Tong's Project Portfolio Page

## Overview

### Project: JobPilot
**JobPilot** is a command-line application designed to help computing students efficiently manage their job applications.

## Summary of Contributions

### Code contributed:
*[Link to code on tP Code Dashboard.](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=&sort=defaultSortOrder&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=abigailtong&tabRepo=AY2526S2-CS2113-W13-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false])*

### New Features:
- **Deletion of Application (`Deleter.java`)**
  - Implemented the full deletion workflow for job applications, rather than just a manual removal from the ArrayList.
  - Added validation for invalid indices and empty lists to prevent crashes and provide safer execution.
  - Used assertions to strengthen the internal logic and ensure assumptions about application states remain valid during execution.
  - Designed the feature so that `Deleter` only handles deletion logic and returns the deleted `Application` object to `CommandRunner`, keeping the architecture modular and separating business logic from UI handling.
  - The implementation was moderately challenging because it required coordination between various components, ensuring validation of user input.
  - The feature is complete because it supports deletion across all valid indexes in the list and safely handles invalid or edge-case inputs.

- **Persistent Storage (`Storage.java`)**
  - Implemented persistent storage of applications for JobPilot by saving application data into `JobPilotData.txt` inside a `data/` directory.
  - Added logic to automatically create the storage directory and file if they do not already exist, making the application more robust for first-time users.
  - Implemented `loadFromFile()` to parse saved applications back into objects while actively skipping corrupted or incomplete lines instead of crashing the application.
  - Implemented `saveToFile()` to persist all application details, including tags and notes, ensuring that no user data is lost between sessions.
  - Added logging support to make it easier to debug file-related issues and verify that storage operations are functioning correctly.
  - This was a deeper enhancement because it required handling file I/O, parsing logic, error recovery, and data consistency across multiple fields.
  - The feature is complete because it supports both reading and writing of all application data and handles common failure cases such as missing files and corrupted entries.

### Enhancements Added:
- **UI Component (`Ui.java`)**
  - Designed the CLI interface for JobPilot to ensure all user interactions are displayed consistently.
  - Implemented dedicated methods for displaying user-facing and error messages for all commands.
  - Standardized the formatting of outputs across the application so that users receive clear and readable feedback for debugging.
  - Kept the UI logic separate from parsing and command execution, improving maintainability and making it easier to expand application scope in the future.
  - This enhancement was important because it acts as the bridge between all backend features and ensures the application feels polished.

- **Storage Tests (`StorageTest.java`)**
  - Achieved 83% Line Coverage of the Storage class.
  - Created tests to verify that saving and loading work correctly for both single and multiple applications.
  - Test scope included edge cases such as empty files, missing files, and corrupted lines to ensure the storage component behaves safely.
  - Verified that tags, notes, and other application fields are correctly preserved after saving and reloading.
  - These tests make the storage component more reliable because they confirm that user data will remain consistent across sessions.

- **Delete Feature Tests (`DeleterTest.java`)**
  - Achieved 71% Line Coverage of the Deleter class.
  - Test scope included the deleting applications at different positions, including the first, middle, and last entries in the list.
  - Verified that invalid indices and empty lists are handled gracefully without causing the application to crash.
  - Tested that `CommandRunner` correctly delegates delete commands to `Deleter`, ensuring that the feature works properly across multiple components.
  - These tests strengthen confidence in the feature because they cover both normal usage and edge cases.

### Developer Guide Contributions:
- **UI Component Documentation**
    - Illustrated architecture, methods, and responsibilities.
    - Included plantUML class diagram.

- **Storage Component Documentation**
    - Documented API, file format and handling of corrupted lines and persistent storage mechanism.
    - Included plantUML class diagram.

- **Delete Feature Documentation**
    - Documented flow of deleting an application
    - Included step-by-step usage scenario and sequence diagrams.
    - Highlighted design considerations for separation of concerns and unit testing.

- **Delete Feature Testing**
    - Authored test cases for `delete` command covering valid indices, invalid inputs, and edge cases.
    - Specified expected output from `Ui.showApplicationDeleted()` and ensured `Storage.saveToFile()` updates correctly.

- **Storage Feature Testing**
    - Authored instructions to verify that modifications (add, edit, delete) are persisted.
    - Ensured data reflects correctly on subsequent launches.

### User Guide Contributions:
- Documented purpose, format, and example output for `help`, `delete`, and `bye` commands.
- Documented **command summary** for all features:
    - `add`, `edit`, `delete`, `status`, `list`, `sort`, `search`, `tag`, `help`, `bye`.

### Contributions to team-based tasks:
1. Kept track of internal timelines for the group.
2. Maintained the issue tracker, ensuring tasks and milestone deadlines were properly set.
3. Spearheaded separation of concerns by splitting the code into Ui, CommandRunner, and Parser classes.
4. Released v1.0 and v2.0, including all necessary items.
5. Documented product info for the Introduction of User Guide and explained directory setup and quick start instructions for first-time users. 
6. Authored the Frequently Asked Questions and their respective answers in the User Guide.
7. Documented the acknowledgements in the Developer Guide.
8. Defined target user profile (computing students applying for jobs) and value proposition in the Developer Guide.
9. Specified **performance, usability, and accessibility** non-functional requirements in the Developer Guide.
10. Authored instructions to verify JobPilot starts correctly using `JobPilot.jar` and checking CLI prompt for JobPilot logo in manual testing section of Developer Guide.

### Instances of Helping the Group:
- Fixed Continuous Integration issues when pushing changes to the group repository.
- Fixed JavaDocs and coding standard issues.

## Contributions to the Developer Guide:

### UI Component:
The API of this component is specified in `Ui.java`.

![Ui Component](../diagrams/component-ui/ui-class.png)

### Delete Feature:
The following sequence diagram shows the flow of deleting an application:

![Sequence](../diagrams/delete/sequence.png)

### Target User Profile
Computing students applying for jobs and want to keep track of their applications.

### Value Proposition
In the current job market, applying to many roles has become the norm. As such, JobPilot acts a
tracker to allow users to get a bird's eye view of all their applications and manage them effectively.

### Non-Functional Requirements
1.Performance
- The application shall respond to any command (add, edit, delete, search, sort, tag, status) within **1 second** for up to **500 job applications**.
- Searching, sorting, and filtering operations shall execute in **O(n)** time complexity or better, where n is the number of applications.

2. Usability
- Command syntax shall remain consistent with clear prefixes (`c/`, `p/`, `d/`, `s/`, `add/`, `remove/`, `note/`) to minimize user errors.
- Error messages shall be **descriptive and actionable**, guiding users to correct input mistakes.
- Commands shall support **partial input** where applicable (e.g., partial company names for search).
 
3. Accessibility
- Command-line outputs shall be **readable with standard font sizes**, use clear formatting (tables, line breaks), and avoid color dependence.
- Messages shall be concise, avoiding technical jargon when addressing end users.

## Contributions to the User Guide:

### Introduction:
JobPilot is an application designed to help computing students manage their job applications efficiently. It works through a Command Line Interface (CLI), but still provides the convenience of a simple graphical interface.
By using JobPilot, users can track application progress and important details without the hassle of manual lists or spreadsheets.

### Quick Start
1. **Install Java 17+:** Verify that your computer has Java `17` or a newer version installed. <br>
   *Mac users:* Please follow the specific JDK installation guide [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
2. **Download the App:** Grab the latest `.jar` release file from [here]().
3. **Set Up Your Directory:** Move the downloaded file into a dedicated new folder. (Note: Running the app for the first time will automatically generate a `data/JobPilotData.txt` file in this directory to save your tasks).
4. **Launch JobPilot** Open your terminal and run the app with the following command: `java -jar <release-name>.jar`

### FAQ
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

