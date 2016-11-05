package seedu.todolist.logic.commands;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Predicate;

import seedu.todolist.commons.exceptions.IllegalValueException;
import seedu.todolist.model.PredicateBuilder;
import seedu.todolist.model.task.Entry;

/**
 * Lists all persons in the todolist book to the user.
 * @@author A0127828W
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String LIST_ALL_COMMAND_WORD = "list-all";

    public static final String MESSAGE_SUCCESS = "Listed all entries";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all entries whose titles contain any of "
            + "the specified keywords (case-sensitive), or with deadlines before/after certain dates "
            + "and displays them as a list with index numbers.\n"
            + "Parameters: [KEYWORDS] [#TAGS] ([after/DATE] [before/DATE] | [on/DATE])\n"
            + "Example: " + COMMAND_WORD + " banana apple #groceries after/tomorrow";
    
    public static final String MESSAGE_INVALID_DATE = "Invalid dates given.";
    public static final String MESSAGE_MUTUALLY_EXCLUSIVE_OPTIONS = "You can't search for a range and a specific day at the same time";

    public static final String AFTER_FLAG = "after/";
    public static final String BEFORE_FLAG = "before/";
    public static final String ON_FLAG = "on/";
    public static final String TYPE_FLAG = "type/";

    private Set<String> keywords;
    private Set<String> tags;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime onDate;
    private final boolean includeCompleted;
    private final String entryType;
    
    private final PredicateBuilder predicateBuilder = new PredicateBuilder();
    
    public ListCommand(boolean includeCompleted, String entryType) {
        this.includeCompleted = includeCompleted;
        this.entryType = entryType;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public void setOnDate(LocalDateTime onDate) {
        this.onDate = onDate;
    }
    
    @Override
    public CommandResult execute() {
        if (isListAll()) {
            return showAll();
        } else {
            try {
                Predicate<Entry> predicate = predicateBuilder.buildPredicate(keywords, tags, startDate, endDate, onDate, includeCompleted, entryType);
                model.updateFilteredEntryListPredicate(predicate);
                return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
            } catch (IllegalValueException e) {
                indicateAttemptToExecuteIncorrectCommand();
                return new CommandResult(e.getMessage());
            }
            
        }
    }
    
    private CommandResult showAll() {
        model.updateFilteredListToShowAll();
        return new CommandResult(MESSAGE_SUCCESS);
    }
    
    /**
     * Return whether this list command is a "list all"
     * 
     */
    private boolean isListAll() {
        return (keywords == null || keywords.isEmpty())
                && (tags == null || tags.isEmpty())
                && startDate == null
                && endDate == null
                && onDate == null
                && includeCompleted;
    }
}
