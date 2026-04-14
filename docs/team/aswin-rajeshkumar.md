# Aswin Rajesh Kumar's Project Portfolio Page

## Overview
**JobPilot** is a command-line application designed to help computing students efficiently manage their job applications.
I implemented the status command, filter by status feature, and extended the status command with a notes sub-feature, along with comprehensive code testing.
---

## Summary of Contributions

### Code Contributed
*[link to code on tP Code Dashboard.](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=Aswin-RajeshKumar&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=Aswin-RajeshKumar&tabRepo=AY2526S2-CS2113-W13-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=functional-code~test-code~docs&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### Enhancements Implemented

#### 1. Application Status and Notes Feature (Core Feature)
- Implemented `status INDEX [s/STATUS] [note/NOTE]`
- Supports **partial updates**:
  - Status only
  - Notes only
  - Both simultaneously
- Preserves existing values when fields are omitted

**Key Implementation Details:**
- Developed `StatusParser`:
  - Implements strict **“Junk Zone” validation** to reject unexpected text between index and prefixes.
  - Supports flexible prefix ordering (`s/`, `note/`).
  - Detects duplicate prefixes to prevent ambiguity.
- Implemented prefix-based parsing logic to extract arguments safely.
- Integrated with `CommandRunner` using **conditional updates**:

**Significance:**
- Handles complex CLI parsing edge cases.
- Prevents unintended data overwrites.
- Designed with strong modularity (Parser vs Logic separation).

#### 2. Filter by Status Feature (Core Feature)
- Implemented `filter s/STATUS`
- Supports **case-insensitive partial matching** (e.g., `off` → `OFFER`, `p` → `PENDING`, `PROCESSING`).

**Key Implementation Details:**
- Built `FilterParser`:
  - Enforces strict format (`s/` prefix required).
  - Rejects malformed input and missing arguments.
- Developed `Filterer` class:
  - Encapsulates filtering logic (Single Responsibility Principle).
  - Handles null safety and whitespace normalization.

**Significance:**
- Balances flexibility (partial matching) with strict validation.
- Clean separation between parsing, logic, and UI.

#### 3. Testing (High Depth)
Implemented comprehensive test coverage across features:

- **StatusTest**:
  - Equivalence Partitioning (valid/invalid updates).
  - Boundary Value Analysis (index handling).
  - Robustness testing: Long strings (1000+ characters), special characters, and null handling.
- **FiltererTest**:
  - Integration-level testing of filtering logic.
  - Validates case-insensitivity, partial matching, and multiple matches.
  - Edge cases: Empty application list, null status values, whitespace-heavy input.
- **Parser Tests**:
  - `StatusParserTest`: Junk Zone validation, duplicate prefix detection.
  - `FilterParserTest`: Format enforcement, edge case handling.

**Significance:**
- Goes beyond basic unit testing (BVA + EP + robustness).
- Tests real system behavior, not just isolated components.
- Improves reliability and prevents regressions.

---

### User Guide Contributions
Wrote documentation for the `status` (with notes) and `filter` commands.

**Key Contributions:**
- Explained partial updates and smart matching behavior.
- Outlined format integrity rules.
- Provided structured command examples with realistic outputs.

---

### Developer Guide Contributions

#### Status and Notes Feature
- Documented `StatusParser` design and parsing strategy.
- Detailed the execution flow via `CommandRunner` and the conditional update mechanism.
- Included error handling tables and design rationale (modularity, validation strategy).
- Added and Enhanced UML sequence diagram illustrating the full execution flow.

#### Filter Feature
- Documented `FilterParser` and `Filterer` architecture.
- Detailed the execution flow and validation logic.
- Included design rationale (SRP, partial matching), error handling cases, and performance considerations.
- Added and Enhanced UML sequence diagram illustrating the full execution flow.
---

### Contributions to Team-Based Tasks
- Integrated features into existing architecture (`Parser`, `CommandRunner`).
- Ensured compatibility with the `Application` data model and UI output formatting.
- Maintained consistency with prefix-based CLI command design.
- Contributed to feature completeness and system stability with high-level testing and debugging.
- Helped resolve CI blockers caused by command dispatch and compile errors.
- Updated the UG and DG to ensure consistency, proper structure, clear formatting, and up-to-date content.
---

## Contributions to the Developer Guide (Extracts)

### Application Status and Notes Feature

#### Implementation Details
The Status feature allows users to update an application's recruitment stage and attach notes independently without overwriting existing data.

**Command format:**
`status INDEX [s/STATUS] [note/NOTE]`

**Execution Flow:**
1. **User inputs:** `status 1 s/OFFER note/Negotiate salary`
2. **Parser** routes input to `StatusParser`.
3. **StatusParser:**
  - Validates index.
  - Performs Junk Zone validation.
  - Extracts prefix values into `ParsedCommand`.
4. **CommandRunner:**
  - Validates index bounds.
  - Applies conditional updates.
5. **Ui** displays the updated application.

#### Error Handling

| Error Scenario        | Condition                                                          | User Response                                              |
|-----------------------|--------------------------------------------------------------------|------------------------------------------------------------|
| **Missing Index**     | User enters `status s/OFFER` without index                         | "Please provide an index. Example: status 1 s/OFFER"       |
| **Invalid Index**     | Index is 0, negative, or exceeds list size                         | "Invalid application number! You have X application(s)."   |
| **Junk Zone Text**    | Unexpected text before prefixes (e.g., `status 1 updated s/OFFER`) | "Invalid format! Unexpected text before prefixes: updated" |
| **Empty Status**      | User enters `status 1 s/`                                          | "Status value cannot be empty!"                            |
| **Missing Arguments** | User enters `status 1` without prefixes                            | "No status or note provided! Use s/ or note/."             |

#### Design Rationale

| Decision                          | Rationale                                                                                                                       |
|-----------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| **Primary Status Field**          | Serves as the key metric for sorting and filtering; normalized to uppercase for consistent searching.                           |
| **Independent Notes Sub-feature** | Decouples subjective user comments from objective recruitment stages, preventing data loss during status transitions.           |
| **Conditional Setter Execution**  | By only calling setters for non-null `ParsedCommand` fields, the system supports partial updates, improving CLI efficiency.     |
| **Strict Junk Zone Validation**   | Prevents user ambiguity by ensuring all text following the index is associated with a valid prefix.                             |
| **Dedicated Sub-Parser**          | Encapsulates complex prefix-searching logic (e.g., handling `note/` inside a status string) away from the main command routing. |


### Filter by Status Feature

#### Implementation Details
The Filter feature retrieves applications that match a given status keyword using flexible matching.

**Command format:**
`filter s/STATUS`

#### Design Rationale

| Decision                                           | Rationale                                                                                                                                                                                              |
|----------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Separate `Filterer` Class**                      | Maintains the Single Responsibility Principle by isolating filtering logic from parsing and UI responsibilities, improving modularity and testability.                                                 |
| **Case-Insensitive Partial Matching (`contains`)** | Supports flexible "smart matching" behaviour, allowing partial inputs such as `off` to match `OFFER`, and `p` to match both `PENDING` and `PROCESSING`. This aligns with the User Guide specification. |
| **Linear Scan over `ArrayList`**                   | Simple and efficient for the expected dataset size (≤ 500 applications), avoiding unnecessary complexity while maintaining acceptable performance.                                                     |

---

#### Error Handling

| Error Scenario    | Condition                     | User Response                                                           |
|-------------------|-------------------------------|-------------------------------------------------------------------------|
| Missing Arguments | User enters `filter` alone    | "Filter command is missing arguments! Use: filter s/STATUS"             |
| Missing Prefix    | User enters `filter PENDING`  | "Invalid filter format! Expected: filter s/STATUS"                      |
| Empty Value       | User enters `filter s/`       | "The filter value cannot be empty! Please provide a status after 's/'." |
| Format Violation  | Junk text appears before `s/` | "Invalid filter format! Unexpected input before prefix."                |

---

## Contributions to the User Guide (Extracts)

### Updating Application Status
Updates the recruitment status and optionally attaches notes.

**Format:**
`status INDEX [s/STATUS] [note/NOTE]`

**Examples**

- `status 1 s/Interview note/Technical round next Tuesday`

```text
Status updated:
Google | SE manager | 2025-03-10 | INTERVIEW (Note: Technical round next Tuesday)
___________________________________________________________________________
```

- `status 2 s/REJECTED`

```text
Status updated:
Amazon | Data Analyst | 2025-03-08 | REJECTED
___________________________________________________________________________
```

### Filtering Applications by Status
Filters applications using case-insensitive partial matching.

**Format:**
`filter s/STATUS`

**Examples:**

- `filter s/OFFER`

```text
Filtered by status: OFFER
Found 2 application(s) with status 'OFFER':
1. Google | SE manager | 2025-03-10 | OFFER (Note: Salary negotiation) | Tags: [TECH]
2. Shopee | Backend Intern | 2025-03-15 | OFFER (Note: Pending acceptance)
___________________________________________________________________________
```
- `filter s/Pend`

```text
Filtered by status: PEND
Found 1 application(s) with status 'PEND':
1. Meta | Frontend | 2026-06-01 | PENDING
___________________________________________________________________________
```