package seedu.address.logic.commands;

import java.util.Set;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all entries";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all entries whose titles contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";
    
    private final Set<String> keywords;
    
    public ListCommand(Set<String> keywords) {
        this.keywords = keywords;
    }
    
    @Override
    public CommandResult execute() {
        if (keywords.isEmpty()) {
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
}
