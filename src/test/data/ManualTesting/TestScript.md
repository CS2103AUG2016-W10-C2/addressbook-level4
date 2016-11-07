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

### [4] Delete

### [5] List

### [6] Tag, Untag

### [7] Mark, Unmark

### [8] Undo, Redo
