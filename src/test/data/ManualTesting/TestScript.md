# Test Script

## Loading the Sample Data
1. Run PriorityQ from `main/`
2. Enter command: `load src/test/data/ManualTesting/SampleData.xml`

### Note: In the following test cases, `idx` refers to the index of the entry in the list.

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
Format: `edit idx title/<new title>`

Command Sequence: `edit 1 title/Do Assignment 4`

Result: The task at index 1 now has the title `Do Assignment 4`.

#### 3.2 Edit the deadline of a task
Format: `edit idx end/<date>`

Command Sequence: `edit 1 end/tomorrow 10pm`

Result: The task at index 1 now has the deadline of tomorrow at 10pm.

#### 3.3 Edit the start and end time of an event
Format: `edit idx start/<date> end/<date>`

Precondition: The entry at index 1 is an event, not a task.

Command Sequence: `edit 1 start/5pm end/7pm`

Result: The event at the specified index now has a start time of 5pm today, and an end time of 7pm today.

#### 3.4 Edit the tags of a task
Format: `edit idx #<tag>`

Command Sequence: `edit 1 #tag1`

Result: The task at index 1 now has the tag #tag1, any existing previous tags are removed.

#### 3.5 Edit the description of a task
Format: `edit idx desc/<new description>`

Command Sequence: `edit 1 desc/Hello world`

Result: The task at index 1 now has the description `Hello world`.

### [4] Delete

#### 4.1 Delete a task
Command Sequence: `delete 1`

Result: The task at index 1 is now deleted.

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

#### 5.6 List all tasks, regardless of completion state
Command Sequence: `list-all`

Result: All tasks, including those that were preivously marked completed are shown.

### [6] Tag, Untag

#### 6.1 Tag a task
Format: `tag idx #<tag>`

Command Sequence: `tag 1 #shopping`

Result: The task at index 1 now has a new tag, `#shopping`.

#### 6.2 Untag a task
Format: `untag idx #<tag>`

Command Sequence: `untag 1 #shopping`

Result: The tag `#shopping` is removed from the task at index 1.

### [7] Mark, Unmark

#### 7.1 Mark a task as completed
Format: `mark idx`

Precondition: The entry at index 1 is a task, not an event.

Command Sequence: `mark 1`

Result: The task at index 1 is marked done and removed from view. It can be shown again using the `list-all` command (see below).

#### 7.2 Unmark a task
Format: `unmark idx`

Command Sequence: `list-all`, `unmark 1`

Result: The task at index 1 is unmarked and is no longer in the completed state.

### [8] Undo, Redo

#### 8.1 Undo a command
Format: `undo`

Command Sequence: `delete 1`, `undo`

Result: The task at index 1 is added back into the task list.

#### 8.2 Redo a command
Format: `redo`

Command Sequence: `delete 1`, `undo`, `redo`

Result: The task at index 1 is deleted first and disappears. With the undo command, the task is added back into the list. Finally, the redo command causes the deletion of the task again.
