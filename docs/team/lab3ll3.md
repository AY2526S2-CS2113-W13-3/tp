# Labelle's Project Portfolio Page

## Overview
JobPilot is a CLI application for job seekers to track applications efficiently. Users can add, edit, delete, search, and sort applications with status tracking and industry tags.

I led the implementation of the editor feature, designed and built the modular parser system, and enhanced the application model to support mutable fields.

### Summary of Contributions
[Link to code on tP Code Dashboard.](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=lab3ll3&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=lab3ll3&tabRepo=AY2526S2-CS2113-W13-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### Enhancements Implemented

#### Application Model Enhancement
Enhanced the `Application` class with setter methods and industry tag support.

**Key Features:**
- Setters: `setCompany`, `setPosition`, `setDate`, `setStatus`, `setNotes`
- Integrated `IndustryTag` for application categorization
- Input validation and assertions for data integrity

#### Editor Application Feature
Implemented the `edit` command to modify existing applications – update company, position, date, or status in a single command.

**Format:** `edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]`

**Key Features:**
- Partial updates (only specified fields change)
- Handles multi-word inputs (e.g., "Senior Software Engineer")
- Validates `YYYY-MM-DD` date format
- Clear error messages for invalid indices or empty fields

**Why it's complete:**
- Supports any combination of fields (e.g., update only company, or all four at once)
- Handles multi-word values (e.g., "Senior Software Engineer") correctly
- Validates date format (`YYYY-MM-DD`) and rejects invalid dates
- Provides clear error messages for invalid indices, empty fields, and malformed input

**Implementation complexity:**
- Required designing a prefix-based parser that extracts fields without breaking on spaces
- Needed careful whitespace normalization to handle user input with extra spaces
- Involved adding setter methods to the `Application` class while preserving immutability where needed
- Integrated with existing command loop without breaking other features

#### Parser System Refactoring
Designed and built a modular parser system that routes commands to dedicated subparsers.

**Key Features:**
- `Parser` class with `CommandType` enum
- `ParsedCommand` data class for clean command data
- Prefix-based parsing (`c/`, `p/`, `d/`, `s/`) that preserves spaces in values
- Subparsers: `ApplicationParser`, `DeleterParser`, `EditorParser`, `FilterParser`, `SearcherParser`, `StatusParser`, `TaggerParser`

**Why it's complete:**
- Encapsulates all parsing logic in one place, separate from command execution
- Uses a `CommandType` enum for clean routing
- Returns a `ParsedCommand` object containing all parsed data
- Subparsers each handle one command, making the system easy to extend

**Implementation complexity:**
- Designed a prefix-based parsing algorithm that correctly captures multi-word values (e.g., `c/Amazon Web Services`)
- Implemented robust error handling so malformed commands produce user-friendly messages
- Created a flexible `ParsedCommand` data class that supports different constructors for different command types
- Ensured the parser works consistently across all commands, reducing duplicate code

---

### Team Contributions
- **Communicator** – Set internal deadlines and tracked team tasks
- **Merge Support** – Helped resolve merge conflicts in `JobPilot.java`
- **Code Review** – Reviewed teammates' pull requests and provided feedback

---

### Developer Guide Contributions

#### Parser Component Documentation
- Documented the modular parser architecture with `CommandType` enum and `ParsedCommand` data class
- Illustrated prefix-based parsing logic (`c/`, `p/`, `d/`, `s/`) that handles multi-word values
- Included PlantUML class diagram and sequence diagram for parser flow

#### Editor Feature Documentation
- Documented the `edit` command for partial updates of company, position, date, and status
- Covered error handling for invalid indices, empty fields, and malformed dates
- Included PlantUML sequence diagram and error handling flow diagram

#### User Stories
- Authored user stories for add, list, delete, edit, search, sort, filter, tag, and persistent storage features

---

### User Guide Contributions

#### Add Application Feature
- Documented the `add` command for creating new job applications with company, position, and submission date
- Specified the required format and field descriptions (`c/COMPANY`, `p/POSITION`, `d/DATE`)
- Included example usage and expected output

#### Edit Application Feature
- Documented the `edit` command for modifying existing applications with partial updates
- Specified the flexible format with optional fields (`c/COMPANY`, `p/POSITION`, `d/DATE`, `s/STATUS`)
- Provided multiple examples demonstrating single-field and multi-field edits
- Included example output showing the updated application with status and tags

