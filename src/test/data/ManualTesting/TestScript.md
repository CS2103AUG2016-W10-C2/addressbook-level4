# Test Script

## Loading the Sample Data
1. Run PriorityQ from `main/`
2. Enter command: `load src/test/data/ManualTesting/SampleData.xml`

## Test Cases 
### [1] Help

#### 1.1 Display Help Screen
Command Sequence: `help`, Press <kbd>ESC</kbd>

Result: A help screen showing a list of commands available, and their options is displayed. The task list is redisplayed upon pressing of the <kbd>ESC</kbd> key.

### [2] Add

#### 2.1 Add a task
Command Sequence: `add Buy bananas`

Result: `Buy bananas` is added to the task list and displayed.

#### 2.2 Add a deadline
Command Sequence: `add Do Assignment 3 end/today 10pm`

Result: `Do Assignment 3` is added to the task list and displayed. The deadline is shown on the right.

#### 2.3 Add an event
Command Sequence: `add Dinner with the gang start/tomorrow 7pm end/tomorrow 8pm`

Result: `Dinner with the gang` is added to the task list and displayed. The event's start and end time is shown on the right.

#### 2.4 Add tasks, deadlines or events with tags
Format: `add <title> #tag1 #tag2...`

Command Sequence: `add Buy apples #groceries`

Result: `Buy apples` is added to the task list and displayed. The task has a turquoise-coloured tag to the right of the title.

#### 2.5 Add tasks, deadlines or events with descriptions
Format: `add <title> desc/<description>`

Command Sequence: `add Write up project report desc/Max 6 pages`

Result: `Write up project report` is added to the task list and displayed. The task has a gray-coloured description below the title.

### [3] Edit

#### 3.1 Edit the title of a task
Format: `edit x title/<new title>`

Command Sequence: `edit 1 title/`

#### 3.2 Edit the deadline of a task
Format:

Command Sequence: ``

Result:

#### 3.3 Edit the start and end time of an event
Format:

Command Sequence: ``

Result:

#### 3.4 Edit the tags of a task
Format:

Command Sequence: ``

Result:

#### 3.5 Edit the description of a task
Format:

Command Sequence: ``

Result:

### [4] Delete

#### 4.1 Delete a task
Command Sequence: `delete 1`

Result:

### [5] List

#### 5.1 Search for tasks using keywords
Format:

Command Sequence: ``

Result:

#### 5.2 Search for tasks by their dates
Format:

Command Sequence: ``

Result:

#### 5.3 Search for events by their dates
Format:

Command Sequence: ``

Result:

#### 5.4 Search for tasks by their tags
Format:

Command Sequence: ``

Result:

#### 5.5 Search for tasks by their description
Format:

Command Sequence: ``

Result:


### [6] Tag, Untag

#### 6.1 Tag a task
Format:

Command Sequence: ``

Result:

#### 6.2 Untag a task
Format:

Command Sequence: ``

Result:

### [7] Mark, Unmark

#### 7.1 Mark a task as completed
Format:

Command Sequence: ``

Result:

#### 7.2 Unmark a task
Format:

Command Sequence: ``

Result:

### [8] Undo, Redo

#### 8.1 Undo a command
Format:

Command Sequence: ``

Result:

#### 8.2 Redo a command
Format:

Command Sequence: ``

Result:
