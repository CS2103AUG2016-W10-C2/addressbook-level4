package seedu.priorityq.testutil;

import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.TaskManager;
import seedu.priorityq.model.tag.Tag;
import seedu.priorityq.model.task.Task;
import seedu.priorityq.model.task.UniqueTaskList;

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
