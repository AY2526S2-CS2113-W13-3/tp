# Aswin Rajesh Kumar - Project Portfolio Page

## Overview
JobPilot is a CLI application designed to help computing students efficiently manage their job applications. It allows users to track recruitment progress, maintain status notes, and search through entries using a simple command-line interface.

I led the implementation of the advanced status and note management system, developed the status-based filtering utility, and resolved critical CI/CD environment conflicts to ensure cross-platform stability.

### Summary of Contributions
*[Link to code on tP Code Dashboard](https://nus-cs2113-ay2526s2.github.io/tp-dashboard-viewer/lastActivity/index.html#group=W13-3&status=any&order=name&reverse=false&repoSort=true)*

### Enhancements Implemented

#### Advanced Status and Notes Management
Decoupled the original status field into two independent attributes: `status` (progress tracking) and `notes` (recruitment comments).

Key Features:
* **Independent Prefixes**: Uses `s/` and `note/` prefixes for non-destructive updates.
* **Advanced Parsing**: Implemented a state-based parsing loop ("The Inflator") in `StatusParser` to support flexible prefix ordering.
* **Data Persistence**: Integrated separate fields into the application model with appropriate assertions.

Why it's complete:
* Allows users to update their recruitment stage without overwriting existing detailed notes.
* Supports flexible prefix ordering; `status 1 s/OFFER note/Great` and `status 1 note/Great s/OFFER` are both valid.
* Handles empty status values defensively to prevent data corruption.

Implementation complexity:
* Required designing a specialized subparser that handles multiple optional flags in a single command string.
* Solved a critical bug where `java.util.logging` timestamps caused non-deterministic output in GitHub Actions.
* Implemented a static initialization block to suppress loggers, ensuring stable character-for-character matching in the test suite.

#### Filter by Status Utility
Specialized utility to retrieve a subset of applications matching a specific recruitment stage.

Key Features:
* **Case-Insensitive Logic**: Implemented matching logic in the `Filterer` class.
* **Partial Matching**: Support for partial status matches (e.g., "OFF" matches "OFFER").
* **Defensive Design**: Included null handling to ensure the application remains robust against incomplete data.

Why it's complete:
* Efficiently narrows down large lists to focus only on active leads or specific stages.
* Gracefully handles scenarios where no matches are found or the application list is empty.
* Integrates seamlessly with the `Ui` component to display filtered results in a standard list format.

Implementation complexity:
* Designed a `FilterParser` that validates required prefixes (`s/`) before execution.
* Balanced performance by utilizing linear search on the `ArrayList`, maintaining efficiency for the target load.
* Followed strict separation of concerns, decoupling the logical filtering from the user interface display.

### Team Contributions
* Identified and resolved Continuous Integration (CI) failures related to cross-platform logger timestamps.
* Coordinated the team's transition from legacy main-loop execution to the modular `CommandRunner` architecture.
* Mentored team members on implementing defensive coding patterns and unit testing strategies.

## Contributions to Developer Guide (Extracts)

### Filter by Status Feature
The **Filter by Status** mechanism allows users to retrieve a subset of applications matching a specific status via a dedicated `Filterer` class.

#### Implementation
The operation is handled via the following methods:
* `FilterParser#parse(String)`: Extracts the status query from raw input.
* `Filterer#filterByStatus(ArrayList<Application>, String)`: Performs the logical match.

**Step 1**: User executes `filter s/OFFER`.
**Step 2**: `Parser` routes execution to `FilterParser`.
**Step 3**: `FilterParser` validates prefix and returns `ParsedCommand`.
**Step 4**: `CommandRunner` calls `Filterer.filterByStatus`.

![Filter Sequence Diagram](diagrams/filter/sequence.png)
*Figure 1: Filter Feature Execution Flow*

### Separate Notes from Status Feature

#### Design Considerations
**Aspect: Data Integrity**
* **Current Implementation**: Separate fields for `status` and `notes`.
    * *Pros*: High flexibility; users can update one without the other.
    * *Cons*: Requires more complex parsing logic.

#### Sequence Diagram
![Status Sequence Diagram](diagrams/status/sequence.png)
*Figure 2: Status and Note Update Flow*

**Error Handling**
| Error Scenario | Condition | User Response |
|----------------|-----------|---------------|
| Missing Index | User enters `status` without a number | "Please provide an index..." |
| Invalid Index | Index exceeds list size | "Invalid application number!" |
| Missing Prefixes | No `s/` or `note/` flags provided | "No valid fields to update!" |
| Empty Status | User enters `status 1 s/` | "Status cannot be empty!" |

### User Stories
| Version | As a ... | I want to ... | So that I can ... |
|------|----------|---------------------------------------|------------------------------------|
| v1.0 | user | update application status | track my application progress |
| v2.0 | user | search applications by company name | locate specific companies |
| v2.0 | user | filter applications by status | focus on a specific stage |

## Contributions to User Guide (Extracts)

### Updating application status: `status`
Updates the recruitment progress and allows for independent note-taking.

**Format**: `status INDEX [s/STATUS] [note/NOTE]`
* `s/STATUS`: The current stage (e.g., Interview, Offer).
* `note/NOTE`: (Optional) Additional feedback or context.

**Example**:
`status 1 s/Interview note/Scheduled for next Tuesday`

**Example output**:
`Status updated: Google | SE manager | 2025-03-10 | INTERVIEW (Note: Scheduled for next Tuesday)`

### Filtering applications by status: `filter`
Retrieves all applications that match a specific status.

**Format**: `filter s/STATUS`
* `s/STATUS`: The status string to match.

**Example**:
`filter s/OFFER`

**Example output**:
`Found 2 application(s) with status matching 'OFFER':`
`1. Google | SE manager | 2025-03-10 | OFFER (Note: Salary negotiation)`

---
### Manual Testing Instructions
**Test Case: Search by Status**
1. Setup: Ensure at least one application has status "OFFER".
2. Action: `search s/OFFER`.
3. Expected: List shows only applications matching "OFFER".

**Test Case: Partial Status Match**
1. Action: `filter s/PEND`.
2. Expected: Applications with status "PENDING" are displayed.