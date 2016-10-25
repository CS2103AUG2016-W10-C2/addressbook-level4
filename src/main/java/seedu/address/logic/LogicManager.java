package seedu.address.logic;

import com.google.common.eventbus.Subscribe;
import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.DidMarkTaskEvent;
import seedu.address.commons.events.ui.MarkTaskEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.logic.commands.UndoableCommandHistory;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.model.Model;
import seedu.address.model.task.Entry;
import seedu.address.storage.Storage;

import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;
    UndoableCommandHistory undoableCommandHistory;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
        this.undoableCommandHistory = new UndoableCommandHistory();
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);

        //@@author A0121501E
        if (command instanceof UndoCommand) {
            ((UndoCommand) command).setData(model, undoableCommandHistory);
        } else {
            command.setData(model);
        }
        
        CommandResult commandResult = command.execute();
        if (command instanceof UndoableCommand &&
            ((UndoableCommand) command).getExecutionIsSuccessful()) {
            undoableCommandHistory.push((UndoableCommand) command);
        }
        //@@author
        return commandResult;
    }

    @Override
    public ObservableList<Entry> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Subscribe
    private void handleMarkTaskEvent(MarkTaskEvent event) {
        EventsCenter.getInstance().post(new DidMarkTaskEvent(execute(event.getCommandString())));
    }
}
