package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.PredicateBuilder;
import seedu.address.model.task.Entry;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all entries";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all entries whose titles contain any of "
            + "the specified keywords (case-sensitive), or with deadlines before/after certain dates"
            + "and displays them as a list with index numbers.\n"
            + "Parameters: [after/YYYY-MM-DD] [before/YYYY-MM-DD] [KEYWORDS]\n"
            + "Example: " + COMMAND_WORD + " after/2016-10-10 alice bob charlie";

    public static final String AFTER_FLAG = "after/";
    public static final String BEFORE_FLAG = "before/";
    
    private Set<String> keywords;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    private PredicateBuilder predicateBuilder;
    
    public ListCommand() {}
    
    /**
     * Convenient constructor
     * @param keywords
     */
    public ListCommand(Set<String> keywords) {
        this.keywords = keywords;
        this.predicateBuilder = new PredicateBuilder();
    }
    
    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    @Override
    public CommandResult execute() {
        if (isListAll()) {
            return showAll();
        } else {
            Predicate<Entry> predicate = predicateBuilder.buildPredicate(keywords, startDate, endDate);
            model.updateFilteredEntryListPredicate(predicate);
            
            return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
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
                && startDate == null
                && endDate == null;
    }
}
