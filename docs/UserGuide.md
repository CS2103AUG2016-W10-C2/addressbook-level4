User guide
===================

<!-- @@author A0116603R -->
## Introduction
PriorityQ is a task manager for the modern power user who enjoys working primarily without a mouse. You care about productivity and your workflow might be primarily keyboard-based.

In fact, you could subscribe to the ‘Inbox Zero’ philosophy. Consequently, it is important that you can quickly keep track of tasks that come in. Because of the variety of such tasks and events, you want a manager that is flexible. From creating tasks without a specific deadline to blocking out specific times on your calendar, PriorityQ does it all, in a clean and easy-to-use interface. Use PriorityQ to capture your ideas, goals, wish lists, trip plans and daily tasks.

This user guide aims to provide you with a brief overview of PriorityQ and how to maximize your productivity with it.

## Getting Started

You need Java 8(Version 1.8) JVM(Java Virtual Machine) in order to be able to run the program. To start, simply download the .jar file from our release page and run it by double clicking on the icon.

At the launch of PriorityQ, you should see the following screen:

<img src="images/pq_start.png" width="600"><br>

All tasks and events are sorted in the conventional calendar manner such that your most important tasks and events are displayed at the top. At the bottom of the screen, you will find a command line which enables you to interact with PriorityQ.

## Quick Start

To start, try adding a new task:

```
add Buy groceries
>>> [1] New entry added: Buy groceries
```
<img src="images/add.png" width="600">

The task can be edited with the following command:

```
edit 1 end/tomorrow 5pm
>>> Edited entry Buy groceries Due: 21 hours from now
```
Yep, natural language is parsed as well!

<img src="images/edit.png" width="600">

Undo is quite easily done:

```
$ undo
>>> Undo edits to entry: Buy groceries
```

Finally, try deleting the task:

```
delete 1
>>> Deleted Entry: Buy groceries
```

<img src="images/delete.png" width="600">

<!-- @@author A0126539Y -->
## Basic Features

#### Supported entries: Task, Floating Task and Event
PriorityQ supports three types of entries. They are task, floating task and event. A task is a unit of work that has to be completed. You can add additional information to tasks, such as deadlines, descriptions, and tags. Task without a deadline are called floating task. It is also possible to create events by specifying a start and end time for a particular task.

#### Creating Task or Event
New entries can be created using the add command. As mentioned, additional details can also be added. For instance, a description can be added using the desc flag. A deadline can be created by specifying the end time and omitting the start time.

To better organise your tasks, you can tag your tasks. Simply prefix the tag with a hashtag as seen in the example command in the diagram.

To add a task, specify an end date:
```
add CS2105 Assignment 1 end/2016-10-10 10:00`
```

To add a floating task, do not specify a start or end date:
```
add Buy groceries desc/don’t forget to buy banana #groceries
>>> [1] New entry added: Buy groceries (don’t forget to buy banana) #GROCERIES
```

Last but not least, to add an event, specify both the start and end date:
```
add Watch The Matric start/tonight 9pm end/tonight 11.16pm #movie night
>>> [1] New entry added: Watch The Matrix #movie night due: 11 hours from now
```

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

#### Deleting Entry
Eventually, you might find that you no longer need a particular task. Sometimes, tasks might also be added by mistake. You can delete such tasks via the delete command, along with the id of the task. The task will then be removed from PriorityQ and will no longer be displayed.

To delete the first task in the list displayed above, simply enter the following command:
```
delete 1
>>> Deleted Entry: Buy groceries at NTUC(don’t forget to buy banana) #GROCERIES Due: 7 hours from now
```

<!-- @@author A0127828W -->
#### Searching for Entry
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

By default, completed tasks will be hidden to let you concentrate more on what needs to be done next. You can still include completed entries in the search by using list-all command.
```
list
>>> [1] Buy blankets for doge
list-all
>>> [1] Train for marathon
>>> [2] Buy blankets for doge
>>> [3] buy apple #shopping
>>> [4] Buy bananas for scales
```

## Advanced Features
We also provide some additional advanced features to help you with navigating the app.

#### Undoing and redoing a command
Task that is deleted erroneously can be recovered using the undo and redo command. You can undo up to the last 20 commands.
```
list
>>> [1] Buy groceries
delete 1
>>> Buy groceries deleted.
undo
>>> Undo delete Buy groceries
list
>>> [1] Buy groceries
```

<!-- @@author A0121501E -->

#### Configuring the storage location
If there is a need, you can also change where PriorityQ stores your data.
```
save new_location/PriorityQ.xml
```

#### Loading from external storage location
After saving your data in another location, you can also load the data.
```
load new_location/PriorityQ.xml
```

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


#### Showing Help
```
help [<command>]
```

You can type `help` to display all the commands available. This displays a summary which details the options for each command.


To leave the help screen, simply hit the `ESC` key.

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
|list|`list [[keywords] [[after/<date>] [before/<date>] | [on/<date>]] [#<tag_name> ...]] [type/{event|task}]`|
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
