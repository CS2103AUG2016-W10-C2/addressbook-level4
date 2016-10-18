package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all entries";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": List all entries whose titles contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";
    
    private Set<String> keywords;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    public ListCommand() {}
    
    /**
     * Convenient constructor
     * @param keywords
     */
    public ListCommand(Set<String> keywords) {
        this.keywords = keywords;
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
            model.updateFilteredPersonList(keywords);
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
