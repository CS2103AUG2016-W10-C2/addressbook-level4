User guide
===================

<!-- @@author A0116603R -->
## Introduction
PriorityQ is a task manager for the modern power user who enjoys working primarily without a mouse. You care about productivity and your workflow is largely keyboard-based.

In fact, you could subscribe to the ‘Inbox Zero’ philosophy. Consequently, it is important that you can quickly keep track of tasks that come in. Because of the variety of such tasks and events, you want a manager that is flexible. From creating tasks without a specific deadline to blocking out specific times on your calendar, PriorityQ does it all, in a clean and easy-to-use interface. Use PriorityQ to capture your ideas, goals, wish lists, trip plans and daily tasks.

This user guide aims to provide you with a brief overview of PriorityQ and how to maximize your productivity with it.

## Getting Started

Java 8(Version 1.8) JVM(Java Virtual Machine) is required to run the program. To start, simply download the .jar file from our [release page](https://github.com/CS2103AUG2016-W10-C2/main/releases) and run it by double clicking on the icon.

At the launch of PriorityQ, you should see the following screen:

<img src="images/pq_start.png" width="600"><br>

All tasks and events are sorted in the conventional calendar manner such that your most important tasks and events are displayed at the top. At the bottom of the screen, you will find a command line which enables you to interact with PriorityQ.

## Quick Start

To start, try adding a new task:

```
add Buy groceries
```
<img src="images/add.png" width="600">

The task can be edited with the following command:

```
edit 1 end/tomorrow 5pm
```
Yep, natural language is parsed as well!

<img src="images/edit.png" width="600">

Undo is quite easily done:

```
$ undo

$ list
[ ] Get groceries
```

Finally, try deleting the task:

```
delete 1
```

<img src="images/delete.png" width="600">

<!-- @@author A0126539Y -->
## Features

#### Adding a new task or event
```
add <task_name> [start/<start> end/<end>] [#<tag_name> ...] [repeat/<recurrence>] [desc/<description>]
```
PriorityQ supports three types of entries. They are task, floating task and event.

To add a task, specify an end date which represents the deadline.

Examples:


- `add CS2105 Assignment 1 end/2016-10-10 10:00`

To add a floating task, add without specifying a the start or end date.

Examples:


- `add watch The Matrix`


Last but not least, to add an event, specify both the start and end time.

Examples:


- `add CS2103T Lecture start/2016-10-10 10:00 end/2016-10-10 12:00 repeat/weekly #rocks`


<!-- @@author A0127828W -->
#### Listing and searching for entries
```
list [[keywords] [[after/<date>] [before/<date>] | [on/<date>]] [#<tag_name> ...] [type/{entry, task}]
```

List all entries, or entries that satisfy the given search criteria. You may search for entries by title, date, tags or even entries For the best user experience, completed entries and past events are _automatically excluded_.

Examples:

- `list`

- `list title/buy`

- `list after/2016-10-10`

- `list on/today`

- `list #groceries #shopping`

If you want to include completed entries in your search, replace `list` with `list-all`

- `list-all buy banana`

<!-- @@author A0121501E -->
#### Tagging an entry
```
tag <entry_id> #<tag_name> [#<tag_name>...]
```

Add tag(s) to a particular entry. To add tags to an entry, specify the entry you want to tag by it's entry_id, which is displayed on the left of all entries.

Examples:

- `tag 12 #CS2103T #rocks`

Delete tag(s) from a particular entry with the specified id using `untag`

- `untag 12 #rocks`

Duplicated tags will only be added once

<!-- @@author A0126539Y -->
#### Editing an entry
```
edit <entry_id> [title/new title] [start/<start> end/<end>] [#<tags>...] [r/ <recurrence>] [desc/<description>]
```

 It is also possible to modify an existing entry by specifying it's id. Almost all aspects of an entry can be edited. You can choose to change the title, dates, description and tags. Note that tag(s) specified in the edit command would replace the existing tag(s) of the entry. If you only want to add a single tag to the existing list of tags, use the tag command instead.

Examples:

- `edit 3 title/Animatrix`

- `edit 3 #movie #must watch`

 You may also choose to chain the edits into a single command like this:

- `edit 3 title/Animatrix #movie #must watch`


<!-- @@author A0121501E -->
#### Deleting an entry
```
delete <entry_id>
```
Eventually, you might find that you no longer need a particular task. Sometimes, tasks might also be added by mistake. You can delete such tasks via the delete command, along with the id of the task.

The task will then be removed from PriorityQ and will no longer exist.

Example:

- `Delete 4`


#### Marking a task as complete
```
mark <entry_id>
```

Tasks can be marked to indicate they are completed and would be hidden from the `list` view. Note that events cannot be marked and they will be automatically hidden when they are over. 

Check (or uncheck, for `unmark`) a entry as completed.


Examples:

- `mark 42`

- `unmark 42`

<!-- @@author A0127828W -->
#### Undo

```
undo
```

Undo the latest change to the todo list. Handles every changes, including `clear`.

#### Redo

```
redo
```

Redo the latest command reverted with `undo`.

<!-- @@author A0116603R -->
#### Help
```
help [<command>]
```

You can type `help` to display all the commands available. This displays a summary which details the options for each command.

<!-- @@author A0126539Y -->
#### option
```
option [<type>/<value> ...]
```
If there is a need, you can also change where PriorityQ stores your data. 

Examples:

- `config save/data/MyNewLocation.xml`

<!-- @@author A0127828W -->
## Misc

You can use UP and DOWN keys to browse through your past commands in the session.

<!-- @@author A0116603R -->
To quickly jump to the command line, simply press the ENTER key and you can start typing your command.

When the app is maximised beyond a particular width, the text automatically becomes larger.

<!-- @@author A0127828W -->

## Command Summary
Here is format for the command summary:
- Parameters enclosed in square brackets are optional. e.g: [#\<tag_name>]
- Parameters enclosed in angle brackets are required. e.g: \<task_name>
- Parameters with trailing … means multiple value separated by commas can be inserted. e.g: #school #university #CS2101

| Command |Format |
| --- | --- |
|add|`add <task_name> [start/<start> end/<end>] [#<tag_name> ...]  [repeat/<recurrence>] [desc/<description>]`|
|list|`list [[keywords] [[after/<date>] [before/<date>] | [on/<date>]] [#<tag_name> ...]] [type/{entry, task}]`|
|tag|`tag <entry_id> #<tag_name> [#<tag_name> ...]`|
|untag|`untag <entry_id> #<tag_name> [#<tag_name> ...]`|
|edit|`edit <entry_id> [title/new title] [start/<start> end/<end>] [#<tags>...] [desc/<description>]`|
|delete|`delete <entry_id>`|
|mark|`mark <entry_id>`|
|unmark|`unmark <entry_id>`|
|undo|`undo`|
|redo|`redo`|
|help|`help`|
|option|`option [<type>/<value> ...]`|
