# Developer Guide

## Acknowledgements

This project was guided by the [SE-EDU initiative's](https://se-education.org) AddressBook-Level3 (AB3). We took guidance from its core architecture., parser design, 
and command execution flow to better suit JobPilot.
  
## Design

### UI Component

The API of this component is specified in `Ui.java`.

![Ui Component](diagrams/component-ui/ui-class.png)

The UI consists of a centralized `Ui` class that serves as the boundary between the user and the system. Since it is a CLI application, it relies on standard input and output.

The `UI` component uses the standard Java `Scanner` to capture user input. The formatting for UI outputs (e.g., the welcome logo, help menus, and application lists) is defined directly within the `Ui` class using text blocks and formatted strings.

The `UI` component,

* reads raw user commands from the console.
* displays formatted messages, search results, and errors to the user based on command execution.
* operates passively; it relies on the `JobPilot` main loop and `CommandRunner` to invoke its specific display methods (e.g., `showApplicationAdded`, `showSearchResults`).
* depends on some classes in the `task` component, as it displays `Application` and `IndustryTag` objects.

### Parser Component

The **Parser** component is responsible for interpreting raw user input and converting it into structured `ParsedCommand` objects.

![Class Structure](diagrams/component-parser/parser-class-structure.png)
*Figure 1: Class Structure*

The EditorParser handles the parsing of edit commands. 

![Seqeuence Diagram](diagrams/component-parser/parser-sequence diagram.png)
*Figure 2: Sequence Diagram*

The sequence diagram shows the interaction flow for parsing an edit command.

![ParsedCommand](diagrams/component-parser/parser-parsed-command.png)
*Figure 3: ParsedCommand*

When parsing is successful, the EditorParser returns a ParsedCommand object.

![Parser Routing](diagrams/component-parser/parser-routing.png)
*Figure 4: Integration with Main Parser*

The main Parser routes the edit command to EditorParser based on the command keyword.

### Storage Component

The **API** of this component is specified in `Storage.java`.

![Storage Component](diagrams/component-storage/storage-class.png)

The `Storage` component,

* Can save job application data in **JSON format (`.json`)**, and read them back into corresponding `Application` objects.
* Handles missing directories or files automatically by creating the necessary `data/JobPilotData.json` file upon initialization if it does not exist.
* Implements **defensive parsing** to ensure the application never crashes upon startup:
  * If individual application entries are missing required fields (caught via `AssertionError` or `NullPointerException`), the component safely skips the corrupted entry and continues loading the rest of the intact data.
  * If the entire file is structurally malformed (e.g., `JsonParseException`), it catches the error, delegates a warning to the `Ui` component, and boots up with a fresh, empty list.
* Depends on classes in the `task` component, because the `Storage` component's primary job is to serialize and deserialize `Application` and `IndustryTag` objects.
* Utilizes the external **Gson** library for all JSON serialization and deserialization processes.

### CommandRunner Component

The **CommandRunner** component serves as the central router for all user commands. It receives a `ParsedCommand` object from the `Parser` and delegates execution to the appropriate handler.

![CommandRunner Class Diagram](diagrams/component-commandrunner/commandrunner-class.png)

The `CommandRunner` component,

* receives a `ParsedCommand` containing command type and relevant parameters (index, search term, status, notes, tag, etc.).
* maintains the central `ArrayList<Application>` that holds all job applications.
* validates command parameters (e.g., index bounds) before delegating to specialized handlers.
* coordinates between the `Ui` and domain logic classes (`Deleter`, `Editor`, `Filterer`, etc.).
* returns a boolean flag to indicate whether the application should continue running.

The following diagram illustrates how the `CommandRunner` processes different command types:

![CommandRunner Flow](diagrams/component-commandrunner/commandrunner-flow.png)

**Key Responsibilities:**

| Responsibility | Description |
|----------------|-------------|
| **Command Routing** | Uses a switch statement to route `ParsedCommand` to the appropriate handler based on `CommandType` |
| **Index Validation** | Validates that indexes are within bounds before passing to handlers |
| **State Management** | Maintains the single source of truth for the application list |
| **Error Handling** | Catches exceptions and delegates error display to `Ui` |

**Command Types Handled:**

| Command Type | Handler | Description |
|--------------|---------|-------------|
| `ADD` | `Application` constructor | Creates new application and adds to list |
| `DELETE` | `Deleter` | Removes application from list |
| `EDIT` | `Editor` | Updates fields of existing application |
| `LIST` | `Ui.showApplicationList()` | Displays all applications |
| `SORT` | `CommandRunner` + `Collections.sort()` | Sorts applications by date, company, or status (optional `reverse`) |
| `SEARCH` | `CommandRunner` + `SearcherParser` | Case-insensitive partial match on company, position, or status |
| `FILTER` | `Filterer` | Filters applications by status |
| `STATUS` | `Application.setStatus()` / `setNotes()` | Updates status and/or notes |
| `TAG` | `Application.addIndustryTag()` / `removeIndustryTag()` | Adds or removes industry tags |
| `HELP` | `Ui.showHelp()` | Displays available commands |
| `BYE` | None | Exits the application |

**Design Rationale:**

| Decision | Rationale |
|----------|-----------|
| Centralized command routing | All commands flow through a single component, making the execution flow easy to trace |
| Validation before delegation | Ensures invalid commands never reach domain logic, improving robustness |
| Return boolean flag | Simple mechanism to control main loop continuation without exceptions |
| Switch statement over mapping | Simple, readable, and sufficient for the number of command types |

## Implementation

### Editor Application Feature

#### Sequence Diagram

![Editor Sequence Diagram](diagrams/editor/sequence.png)
*Figure 3: Editor Feature Sequence Diagram*

**Error Handling**

| Error Scenario | Condition | User Response |
|----------------|-----------|---------------|
| Missing Index | User enters `edit` without a number | "Please provide an index. Example: edit 1 c/Google" |
| Invalid Index | Index is 0, negative, or exceeds list size | "Invalid application number! You have X application(s)." |
| No Fields | User provides index but no fields to update | "No valid fields to update! Use: c/, p/, d/, s/" |
| Invalid Date Format | Date not in `YYYY-MM-DD` format | "Invalid date! Use YYYY-MM-DD (e.g., 2024-09-12)" |

### Delete Application Feature

#### Implementation Details

The Delete application mechanism is facilitated by the `CommandRunner` component, which manages the application's core state through an `ArrayList<Application>` named `applications`.

The operations are exposed and handled internally via the following flow:

* `DeleterParser#parse(String)` — Parses the raw user input to extract the target index, converting it to a 0-based index and returning a structured `ParsedCommand` object.
* `CommandRunner#run(ParsedCommand)` — Acts as the central router, matching the `DELETE` command type and coordinating between the logic and UI components.
* `Deleter#deleteApplication(ArrayList<Application>, int)` — Validates the array bounds, removes the target `Application` object from the memory list, and returns the removed object.
* `Ui#showApplicationDeleted(Application, int)` — Handles the console output to inform the user of the successful deletion.

Given below is an example usage scenario demonstrating how the Delete mechanism behaves at each step.

**Step 1.** The user executes `delete 2`. The `Ui.readCommand()` method captures the raw input string.

**Step 2.** The input is passed to `Parser.parse()`, which identifies the `delete` keyword and delegates to `DeleterParser.parse()`.

**Step 3.** `DeleterParser` splits the string, extracts the index `"2"`, converts it to a 0-based integer (`1`), and packages it into a `ParsedCommand(type=DELETE, index=1)` object.

**Step 4.** The `CommandRunner#run()` method receives the `ParsedCommand`. The `switch` statement recognizes the `DELETE` type and invokes `Deleter.deleteApplication(applications, cmd.index)`.

![Steps 1 to 4](diagrams/delete/step14.png)

**Step 5.** `Deleter` validates that index `1` is within the valid bounds of the `applications` list. It removes the target `Application` object via `applications.remove(1)` and returns the deleted object back to the `CommandRunner`.

![Step 5](diagrams/delete/step5.png)

**Step 6.** `CommandRunner` receives the deleted `Application` and passes it to `Ui.showApplicationDeleted(removed, applications.size())`, which safely prints the confirmation message to the console.

*Note: If the user inputs a non-numeric index (e.g., `delete abc`), a `NumberFormatException` is caught internally by the `DeleterParser`, which then throws a custom `JobPilotException`. This exception is caught in `Parser`, which directly calls `Ui.showError()` to display the message to the user, and returns null to safely bypass the `CommandRunner` execution.

The following sequence diagram shows the flow of deleting an application:

![Sequence](diagrams/delete/sequence.png)

#### Design Considerations

**Aspect: Command delegation and Separation of Concerns:**

* **Current Implementation:** The `Deleter` class is strictly responsible for domain logic (removing the `Application` object from the `ArrayList`) and returns the deleted object back to the caller. It does not contain any `System.out.println` statements.
  * *Pros:* High cohesion and loose coupling. By returning the object rather than printing directly, the deletion logic becomes purely functional. This makes `Deleter` extremely easy to unit test and ensures that formatting changes only need to be made in the `Ui` class.
  * *Cons:* Requires a slightly longer call chain (Parser -> Runner -> Deleter -> Ui) compared to a monolithic approach.
* **Alternative:** Have `Deleter.deleteApplication` handle the `ArrayList` removal and print the success message directly to the console.
  * *Pros:* Less boilerplate code and fewer method hand-offs in the `CommandRunner`.
  * *Cons:* Violates the Single Responsibility Principle (SRP) by mixing domain logic with presentation logic, making automated testing difficult and UI migrations (e.g., moving to a GUI) nearly impossible.

### Multi-Type Search Feature

#### Implementation Details

The **Multi-Type Search** feature allows users to retrieve job applications by matching **company name, position, or status** using a **case-insensitive partial search**. Parsing is handled by `SearcherParser`; matching and result display are handled in `CommandRunner#handleSearch(String type, String query)` on the central `ArrayList<Application>`.

The search operation works by iterating through the list and checking whether each application's relevant field contains the user-provided search keyword:

* Type `"c"` → company name
* Type `"p"` → position
* Type `"s"` → status

---

Given below is an example usage scenario demonstrating how the search mechanism behaves at each step.

**Step 1.** The user executes one of the following commands:

```text
search c/google
search p/software engineer
search s/interviewed
```

Input is read in the `JobPilot` main loop and passed to `Parser.parse(String)`.

**Step 2.** `Parser` recognizes the `search` keyword and delegates to `SearcherParser.parse(String)`.

**Step 3.** `SearcherParser` extracts the search type (`c`, `p`, or `s`) and the keyword after `/`. If the keyword is empty or the prefix is invalid, a `JobPilotException` is thrown and shown via `Ui.showError()`. If the application list is empty, `CommandRunner` reports that the list is empty.

**Step 4.** `CommandRunner` builds a result list with a case-insensitive partial match on the chosen field (`contains` on the lowercased field and query).

**Step 5.** `Ui.showSearchResults` lists matches or reports that none were found.

---

#### Sequence Diagrams
##### Main Success Flow
The following diagram illustrates the normal execution flow when a user performs a valid search:
![search_main.png](diagrams/search/search_main.png)
##### Empty Search Term
The following diagram shows the system behavior when the user provides an empty search keyword:
![search_empty.png](diagrams/search/search_empty.png)
##### No Match Found
The following diagram illustrates the case where no applications match the search keyword:
![search_nomatch.png](diagrams/search/search_nomatch.png)

---
**Error Handling**

| Error Scenario       | Condition                                      | User Response                                                |
|---------------------|-----------------------------------------------|-------------------------------------------------------------|
| Empty Search Term     | User enters `search c/`, `p/`, or `s/` without keyword | "Search value cannot be empty!"                              |
| No Applications       | Application list is empty                     | "No applications to search!"                                 |
| No Match Found        | No application matches the keyword           | "No applications found for [type]: [keyword]"               |
| Invalid Format        | Input does not follow `search c/xxx`, `p/xxx`, or `s/xxx` | "Invalid search format! Use: search c/xxx or p/xxx or s/xxx"|
| Invalid Search Type   | Type is not `c`, `p`, or `s`                 | "Invalid search type! Use c/, p/, or s/"                     |

---

**Design Rationale**

| Decision                            | Rationale                                                                 |
|------------------------------------|---------------------------------------------------------------------------|
| Implement search in `CommandRunner` | Keeps parsing in `SearcherParser` and matching next to other list operations |
| Support multiple search types       | Improves usability by allowing field-specific searches                  |
| Case-insensitive matching           | Flexible input, user-friendly                                           |
| Partial matching using `contains()` | Allows users to search with incomplete input                             |
| Linear search on `ArrayList`        | Adequate for small datasets, simple to implement                         |
| Direct result printing               | Simplifies control flow without extra layers                             |

---

#### Design Considerations

**Aspect: Search logic placement**

* **Current Implementation:** The search logic is implemented in `CommandRunner` with parsing in `SearcherParser`.
    * *Pros:* Clear separation between parsing and execution; list logic stays in one place.
    * *Cons:* `CommandRunner` still coordinates UI calls for results.

---

**Aspect: Matching strategy**

* **Current Implementation:** Case-insensitive partial matching via `toLowerCase().contains()`.
    * *Pros:* Flexible, supports partial input (e.g., "goo" matches "Google").
    * *Cons:* Less efficient for large datasets, limited to substring matching.

* **Alternative:** Exact match using `equalsIgnoreCase()`.
    * *Pros:* More precise, slightly more efficient.
    * *Cons:* Too strict, reduces usability.

---

#### Future Improvements

- Support multi-field search (e.g., `search c/google p/developer`)
- Implement fuzzy search for typo tolerance
- Introduce indexing for faster lookups in large datasets
- Extract search logic into a separate class for better modularity

### Sort Application Feature

#### Implementation Details

The Sort feature sorts all job applications **in place** in the central `ArrayList<Application>`. Users may sort by **submission date** (default), **company name**, or **status**, in ascending order or **reverse** order.

**Command format** (parsed by `Parser`; optional argument passed as `ParsedCommand` search term):

- `sort` — by date, ascending
- `sort date` / `sort company` / `sort status` — by that field, ascending
- `sort <field> reverse` — descending for that field (e.g. `sort company reverse`)

The sorting logic lives in `CommandRunner#handleSort(String rawSortTerm)`, which selects a `Comparator` and applies `Collections.sort` or `Collections.reverseOrder(comparator)`. Unknown fields (after removing the word `reverse`) produce an error and leave the list unchanged.

Given below is an example usage scenario:

**Step 1.** The user executes e.g. `sort company`. Input is read in the main loop and passed to `Parser.parse()`.

**Step 2.** `Parser` builds a `ParsedCommand(CommandType.SORT, "company")` (or an empty sort command for default date sort).

**Step 3.** `CommandRunner` checks that the application list is non-empty.

**Step 4.** If the field token is invalid (not `date`, `company`, or `status`), an error is shown and no sort runs.

**Step 5.** Otherwise the list is sorted and `Ui.showSortedMessage` reports the field and whether reverse order was used, followed by `Ui.showApplicationList`.

---

#### Error Handling

| Error Scenario | Condition | User Response |
|----------------|----------|---------------|
| No Applications | Application list is empty | "There is no application yet." |
| Invalid sort field | e.g. `sort hi` | "Invalid sort field! Use: sort [date, company, or status] [reverse]" |

---

#### Design Rationale

| Decision | Rationale |
|----------|----------|
| Optional sort field | Matches User Guide; date remains the default when omitted |
| Reverse keyword | Single consistent modifier instead of separate commands |
| Sort in `CommandRunner` | Same list instance as other commands; no extra copies |
| Use `Collections.sort` | Reliable and easy to maintain |

---

#### Design Considerations

**Aspect: Sorting logic placement**

* **Current Implementation:** Sorting handled in `CommandRunner`
  * *Pros:* Stays next to `SEARCH`, `FILTER`, and list display
  * *Cons:* `CommandRunner` grows with feature set

---

### Tag Industry to Job Application Feature

#### Implementation Details

The Tag feature allows users to add or remove industry tags for job applications. Tags are automatically normalized to uppercase, trimmed of whitespace, and duplicates are automatically prevented. This feature is implemented using a dedicated `IndustryTag` class and integrated with the `Application` model.

**Command Format**:
- `tag INDEX add/TAG` — Add a tag to an application
- `tag INDEX remove/TAG` — Remove a tag from an application

**Example Usage**:
- `tag 1 add/TECH` — Adds tag "TECH" to application at index 1
- `tag 2 remove/FINANCE` — Removes tag "FINANCE" from application at index 2

The feature is implemented using the following components:
- `IndustryTag` — Immutable value object representing a normalized tag
- `Application` — Stores a `Set<IndustryTag>` and provides `addIndustryTag()` and `removeIndustryTag()` methods
- `CommandRunner` — Routes the command to the appropriate handler
- `TaggerParser` — Parses the raw input to extract index, action, and tag content

Given below is an example usage scenario demonstrating how the Tag mechanism behaves at each step.

**Step 1.** The user executes `tag 1 add/TECH`. The command is read by `Ui` and passed to `Parser`.

**Step 2.** `Parser` identifies the `tag` keyword and delegates to `TaggerParser.parse()`.

**Step 3.** `TaggerParser` extracts the index `1`, action `add`, and tag content `TECH`. It returns a `ParsedCommand` object with type `TAG`, index, action, and tag.

**Step 4.** `CommandRunner` receives the `ParsedCommand` and validates the index is within bounds.

**Step 5.** For **add**, `CommandRunner` calls `Application.addIndustryTag(...)`. If the set already contained that tag, it shows an error instead of a success message.

**Step 6.** The `IndustryTag` constructor normalizes the tag (trim → uppercase). New tags are stored in the `Set<IndustryTag>`.

**Step 7.** On successful add, `Ui.showTagAdded()` displays the updated application. For **remove**, `removeIndustryTag` returns whether the tag existed; failure shows `Tag not found on this application!`.

#### Sequence Diagram

The following sequence diagram illustrates the flow of adding a tag to an application:

![Tag Sequence Diagram](diagrams/tag/sequence.png)

#### Error Handling

| Error Scenario | Condition | User Response |
|----------------|-----------|---------------|
| Missing index | User enters `tag add/TECH` without index | "Please provide an index. Example: tag 1 add/TECH" |
| Invalid index | Index is 0, negative, or exceeds list size | "Invalid application number! You have X application(s)." |
| Invalid format | Missing `add/` or `remove/` prefix | "Invalid tag format! Use: tag INDEX add/TAG or tag INDEX remove/TAG" |
| Empty tag | User enters `tag 1 add/` | "Tag cannot be empty!" |
| Duplicate tag | User adds a tag already on the application | "This application already has that tag." |
| Remove non-existent tag | Tag not found on application | "Tag not found on this application!" |

#### Design Rationale

| Decision | Rationale |
|----------|----------|
| Dedicated `IndustryTag` class | Encapsulates tag normalization logic (uppercase, trim) and ensures immutability |
| Use `Set<IndustryTag>` | Automatically prevents duplicate tags |
| Tag normalization (uppercase) | Ensures consistency and prevents case-sensitive duplicates |
| `add/` and `remove/` syntax | Matches existing command patterns (`set/`, `note/`) |
| Separate `TaggerParser` | Maintains separation of concerns and simplifies unit testing |

### Filter by Status Feature

#### Implementation Details

The **Filter by Status** mechanism allows users to retrieve a subset of applications matching a specific status (e.g., "OFFER"). This is implemented via a dedicated `Filterer` utility class and a `FilterParser` sub-parser, following the **Separation of Concerns** principle used in the `Delete` and `Edit` features.

The operations are handled via the following methods:
* `FilterParser#parse(String)` — Extracts the status query from the raw input (e.g., extracts "OFFER" from `filter s/OFFER`).
* `Filterer#filterByStatus(ArrayList<Application>, String)` — Iterates through the list, performs the logical match, and delegates the display to the `Ui` class.

**Execution Scenario**

**Step 1.** The user executes `filter s/OFFER`. The `Scanner` in `JobPilot.main()` reads the input string.

**Step 2.** The `Parser` identifies the `filter` keyword and routes execution to `FilterParser.parse("filter s/OFFER")`.

**Step 3.** `FilterParser` validates the `s/` prefix, extracts the string `"OFFER"`, and returns a `ParsedCommand` object with `type = FILTER` and `searchTerm = "OFFER"`.

**Step 4.** `CommandRunner` handles `FILTER` and calls `Filterer.filterByStatus(applications, cmd.getSearchTerm())`.

**Step 5.** The `Filterer` iterates through the `ArrayList<Application>`. For each application, it performs a case-insensitive check:
`app.getStatus().toUpperCase().contains(query.toUpperCase())`.

**Step 6.** Matching applications are collected and `Ui.showFilterResults` displays them (or a no-match message when the list is empty).

#### Sequence Diagram

The following sequence diagram illustrates the flow of filtering applications by status:

![Filter Sequence Diagram](diagrams/filter/sequence.png)


#### Design Rationale

| Decision | Rationale |
|----------|-----------|
| **Separate `Filterer` Class** | Maintains the Single Responsibility Principle and simplifies unit testing. |
| **Case-Insensitive `contains()`** | Provides a "search-like" experience, allowing partial matches (e.g., "OFF" matches "OFFER"). |


#### Error Handling

| Error Scenario | Condition | User Response |
|----------------|-----------|---------------|
| Missing Arguments | User enters `filter` alone | "Filter command is missing arguments! Use: filter s/STATUS" |
| Missing Prefix | User enters `filter PENDING` | "Invalid filter format! Expected: filter s/STATUS" |
| Empty Value | User enters `filter s/` | "The filter value cannot be empty! Please provide a status after 's/'." |

### Separate Notes from Status Feature

#### Implementation Details

This feature separates the original `status` field into two independent fields: `status` (application progress) and `notes` (user comments). This allows users to update status and notes independently without overwriting the other.

**Command Format**: `status INDEX s/STATUS note/NOTES`

**Example Usage**:
- `status 1 s/OFFER note/Negotiate salary` — Update both status and notes
- `status 2 s/INTERVIEW` — Update status only (notes unchanged)
- `status 3 note/Follow up next week` — Update notes only (status unchanged)

The feature is implemented using the following components:
- `Application` — Stores `status` and `notes` as separate fields with getter/setter methods
- `StatusParser` — Parses the raw input to extract index, status value, and notes
- `CommandRunner` — Routes the command and updates the target application

Given below is an example usage scenario demonstrating how the Status mechanism behaves at each step.

**Step 1.** The user executes `status 1 s/OFFER note/Negotiate salary`. The command is read by `Ui` and passed to `Parser`.

**Step 2.** `Parser` identifies the `status` keyword and delegates to `StatusParser.parse()`.

**Step 3.** `StatusParser` extracts the index `1`, status value `OFFER` (after `s/`), and notes `Negotiate salary` (after `note/`). It returns a `ParsedCommand` object with type `STATUS`, index, status, and notes.

**Step 4.** `CommandRunner` validates the index is within bounds and retrieves the target `Application`.

**Step 5.** `CommandRunner` calls `app.setStatus(status)` if status is provided, and `app.setNotes(notes)` if notes are provided.

**Step 6.** `Ui.showStatusUpdated(app)` displays the updated application.

#### Sequence Diagram

The following sequence diagram illustrates the flow of updating status and notes:

![Status Sequence Diagram](diagrams/status/sequence.png)

#### Error Handling

| Error Scenario | Condition                                | User Response |
|----------------|------------------------------------------|---------------|
| Missing index | User enters `status s/OFFER` without index | "Please provide an index. Example: status 1 s/OFFER" |
| Invalid index | Index out of range                       | "Invalid application number! You have X application(s)." |
| Invalid format | Missing `s/` or incorrectly formatted    | "No status or note provided! Use s/ or note/." (or related format errors from `StatusParser`) |
| Empty status | User enters `status 1 s/`                | "Status cannot be empty!" |
| Both fields missing | User enters `status 1`                   | "No status or note provided! Use s/ or note/." |

#### Design Rationale

| Decision                            | Rationale |
|-------------------------------------|----------|
| Separate `status` and `notes` fields | Improves data clarity and allows independent updates |
| Optional `note/` field              | Supports updating status without overwriting existing notes |
| Backward compatibility              | Existing applications with combined format are migrated correctly |
| Dedicated `StatusParser`            | Maintains separation of concerns and simplifies testing |
| `s/` and `note/` prefixes           | Consistent with existing command patterns (`c/`, `p/`, `d/`) |

## Product Scope
### Target User Profile
Computing students applying for jobs and want to keep track of their applications.

### Value Proposition
In the current job market, applying to many roles has become the norm. As such, JobPilot acts a
tracker to allow users to get a bird's eye view of all their applications and manage them effectively.

## User Stories

| Version | As a ... | I want to ...                                                     | So that I can ...                      |
|------|----------|-------------------------------------------------------------------|----------------------------------------|
| v1.0 | user | add a job application with company, position, and submission date | keep track of where I have applied     |
| v1.0 | user | list all my applications                                          | see a summary of my applications       |
| v1.0 | user | delete applications                                               | manage my application list effectively |
| v1.0 | user | update application status                                         | track my application progress          |
| v1.0 | user | sort applications by submission date                              | prioritize older applications          |
| v2.0 | user | store my applications persistently | come back to it at different points in time                |
| v2.0 | user | edit an existing application | update details without deleting and re-adding applications |
| v2.0 | user | search applications by company name | locate applications for specific companies                 |
| v2.0 | user | add industry tags to applications | categorize applications by industry                        |
| v2.0 | user | filter applications by status | focus on applications at a specific stage                  |

## Non-Functional Requirements

### 1. Performance
- The application shall respond to any command (add, edit, delete, search, sort, tag, status) within **1 second** for up to **500 job applications**.
- Searching, sorting, and filtering operations shall execute in **O(n)** time complexity or better, where n is the number of applications.

### 2. Usability
- Command syntax shall remain consistent with clear prefixes (`c/`, `p/`, `d/`, `s/`, `add/`, `remove/`, `note/`) to minimize user errors.
- Error messages shall be **descriptive and actionable**, guiding users to correct input mistakes.

### 3. Accessibility
- Command-line outputs shall be **readable with standard font sizes**, use clear formatting (tables, line breaks), and avoid color dependence.
- Messages shall be concise, avoiding technical jargon when addressing end users.

### 4. OS Requirement
- Shall work on any mainstream OS as long as it has Java 17 or above installed.

## Glossary

* **CLI** - Command Line Interface.
* **Filter** - A function to narrow down the application list based on specific criteria.
* **Tag** - A label assigned to an application for categorization.
* **Mainstream OS** - Windows, Linux, Unix, MacOS.

## Instructions for manual testing

## Initial Launch
1. Download the `JobPilot.jar` file.
2. Place the jar in an empty folder. 
3. Open a terminal in that folder and run the app with the following command: `java -jar JobPilot.jar`
      **Expected:** JobPilot launches. The CLI prompt appears showing the JobPilot logo.


### Edit Feature Testing

| Test          | Command                              | Expected                    |
|---------------|--------------------------------------|-----------------------------|
| Edit company  | `edit 1 c/Microsoft`                 | Company updated             |
| Edit position | `edit 1 p/Senior Engineer`           | Position updated            |
| Edit date     | `edit 1 d/2024-12-01`                | Date updated                |
| Edit status   | `edit 1 s/Interview`                 | Status updated to Interview |
| Edit multiple | `edit 1 c/Google p/SWE d/2024-09-12` | All fields updated          |
| Invalid index | `edit 99 c/Google`                   | Error: invalid index        |
| No fields     | `edit 1`                             | Error: no fields to update  |
| Invalid date  | `edit 1 d/2024-13-01`                | Error: invalid date format  |

### Search by Company Feature Testing

| Test | Command | Expected |
|------|--------|----------|
| No match | `search Microsoft` | Prints "No applications found for company: Microsoft" |
| Exact match | `search Google` | Shows 1 result with Google application only |
| Partial match | `search Go` | Shows multiple results (e.g., Google, GoGoTravel) |
| Case insensitive | `search GOOGLE` | Matches "Google" successfully |
| Empty search term | `search ` | Error: "Please provide a company name to search" |
| Empty list | `search Google` (no applications) | "No applications to search!" |

### Filter by Status Feature Testing

| Test | Command | Expected |
|------|---------|----------|
| Exact match | `filter s/OFFER` | Shows only applications with status "OFFER" |
| Case insensitive | `filter s/offer` | Matches "OFFER" successfully |
| Partial match | `filter s/PEND` | Matches "PENDING" successfully |
| No match | `filter s/REJECTED` | Prints a no-match style message from the filter flow |
| Empty list | `filter s/OFFER` | Prints "There is no application yet." |

### Delete Feature Testing

| Test | Command | Expected |
|---|---|---|
| Valid delete | `delete 1` | First application removed from list. Deleted application details and remaining count shown. `JobPilotData.txt` updated. |
| Invalid index | `delete 0` | `JobPilotException` thrown indicating invalid index. No deletion occurs. Storage remains unchanged. |
| Missing index | `delete` | `JobPilotException` thrown indicating invalid index. No deletion occurs. Data file remains unchanged. |
| Non-numeric index | `delete abc` | `JobPilotException` thrown due to non-numeric input. No deletion occurs. Storage remains consistent. |
| Index out of range | `delete N+1` | `JobPilotException` thrown indicating index is out of bounds. No deletion occurs. Data file remains unchanged. |

### Storage Feature Testing

| Test | Action | Expected                                                                                                                                               |
|---|---|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| Save after modification | Perform `add`, `edit`, or `delete` command | `Storage.saveToFile()` is called. `JobPilotData.txt` is updated with the latest application list. On next launch, the list reflects all modifications. |