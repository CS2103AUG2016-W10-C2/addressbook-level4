package seedu.address.logic;

import com.google.common.eventbus.Subscribe;
import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.DidMarkTaskEvent;
import seedu.address.commons.events.ui.MarkTaskEvent;
import seedu.address.logic.commands.*;
import seedu.address.logic.commands.UndoableCommand.CommandState;
import seedu.address.logic.parser.Parser;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.Entry;
import seedu.address.storage.Storage;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;
    UndoableCommandHistory undoableCommandHistory;
    private final UserPrefs userPrefs;
    private final Storage storage;
    private final CommandHistory commandHistoryManager;

    public LogicManager(Model model, Storage storage, UserPrefs userPrefs) {
        this.model = model;
        this.parser = new Parser();
        this.undoableCommandHistory = new UndoableCommandHistory();
        this.storage = storage;
        this.userPrefs = userPrefs;
        commandHistoryManager = new CommandHistory();
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);

        //@@author A0126539Y
        if (command instanceof OptionCommand) {
            OptionCommand optionCommand = (OptionCommand)command;
            optionCommand.setUserPrefs(userPrefs);
            try {
                CommandResult result = optionCommand.execute();
                storage.saveUserPrefs(userPrefs);
                if (userPrefs.getSaveLocation() != null && !userPrefs.getSaveLocation().isEmpty()){
                    storage.setTaskManagerFilepath(userPrefs.getSaveLocation());
                    storage.saveTaskManager(model.getTaskManager());
                }
                return result;
            } catch (IOException e) {
                return new CommandResult("Failed saving user preference");
            } finally {
                optionCommand.setUserPrefs(null); // to prevent userPrefs from changing again
            }
        }
        //@@author A0121501E
        else if (command instanceof UndoCommand) {
            ((UndoCommand) command).setData(model, undoableCommandHistory);
        }
        else if (command instanceof RedoCommand) {
            ((RedoCommand) command).setData(model, undoableCommandHistory);
        }
        else {
            command.setData(model);
        }

        CommandResult commandResult = command.execute();
        if (command instanceof UndoableCommand &&
            ((UndoableCommand) command).getCommandState() == CommandState.UNDOABLE) {
            undoableCommandHistory.pushToHistory((UndoableCommand) command);
        }

        commandHistoryManager.appendCommand(commandText);
        commandHistoryManager.resetPosition();

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

    @Override
    public CommandHistory getCommandHistoryManager() {
        return this.commandHistoryManager;
    }

}
