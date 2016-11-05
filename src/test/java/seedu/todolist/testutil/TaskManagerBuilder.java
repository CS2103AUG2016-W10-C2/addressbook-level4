package seedu.todolist.testutil;

import seedu.todolist.commons.exceptions.IllegalValueException;
import seedu.todolist.model.TaskManager;
import seedu.todolist.model.tag.Tag;
import seedu.todolist.model.task.Task;
import seedu.todolist.model.task.UniqueTaskList;

/**
 * A utility class to help with building TaskManager objects.
 * Example usage: <br>
 *     {@code TaskManager ab = new TaskManagerBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class TaskManagerBuilder {

    private TaskManager taskManager;

    public TaskManagerBuilder(TaskManager taskManager){
        this.taskManager = taskManager;
    }

    public TaskManagerBuilder withPerson(Task person) throws UniqueTaskList.DuplicateTaskException {
        taskManager.addTask(person);
        return this;
    }

    public TaskManagerBuilder withTag(String tagName) throws IllegalValueException {
        taskManager.addTag(new Tag(tagName));
        return this;
    }

    public TaskManager build(){
        return taskManager;
    }
}
