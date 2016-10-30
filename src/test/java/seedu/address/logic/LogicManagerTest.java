package seedu.address.logic;

import com.google.common.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.core.EventsCenter;
import seedu.address.logic.commands.*;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.ShowHelpListEvent;
import seedu.address.commons.events.model.TaskManagerChangedEvent;
import seedu.address.model.TaskManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyTaskManager;
import seedu.address.model.task.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.storage.StorageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.*;
import static seedu.address.logic.commands.AddCommand.TAG_FLAG;
import static seedu.address.logic.commands.EditCommand.TITLE_FLAG;

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlyTaskManager latestSavedAddressBook;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(TaskManagerChangedEvent abce) {
        latestSavedAddressBook = new TaskManager(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpListEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setup() {
        model = new ModelManager();
        String tempAddressBookFile = saveFolder.getRoot().getPath() + "TempAddressBook.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempAddressBookFile, tempPreferencesFile), null);
        EventsCenter.getInstance().registerHandler(this);

        latestSavedAddressBook = new TaskManager(model.getTaskManager()); // last saved assumed to be up to date before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'address book' and the 'last shown list' are expected to be empty.
     * @see #assertCommandBehavior(String, String, ReadOnlyTaskManager, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new TaskManager(), Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal address book data are same as those in the {@code expectedAddressBook} <br>
     *      - the backing list shown by UI matches the {@code shownList} <br>
     *      - {@code expectedAddressBook} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
                                       ReadOnlyTaskManager expectedAddressBook,
                                       List<? extends Entry> expectedShownList) throws Exception {

        //Execute the command
        CommandResult result = logic.execute(inputCommand);
        //Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedShownList, model.getFilteredPersonList());
        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedAddressBook, model.getTaskManager());
        assertEquals(expectedAddressBook, latestSavedAddressBook);
    }


    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", HelpCommand.SHOWING_HELP_MESSAGE);
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generateTask(1));
        model.addTask(helper.generateTask(2));
        model.addTask(helper.generateTask(3));

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new TaskManager(), Collections.emptyList());
    }


    @Test
    public void execute_add_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandBehavior(
                "add " + TAG_FLAG + "NTUC Valid Task", expectedMessage);
    }

    @Test
    public void execute_add_invalidTaskData() throws Exception {
        assertCommandBehavior(
                "add []\\[;]", String.format(Title.MESSAGE_NAME_CONSTRAINTS, "[]\\[;]"));
        assertCommandBehavior(
                "add Valid Task " + TAG_FLAG + "invalid_-[.tag", Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.taskWithTags();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedAB,
                expectedAB.getTaskList());

    }

    @Test
    public void execute_edit_successful() throws Exception {
        TestDataHelper helper = new TestDataHelper();

        Task toEdit = helper.taskWithTags();
        Task toEditCopy = helper.taskWithTags();
        Title newTitle = new Title("New Title");

        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toEdit);
        model.addTask(toEditCopy);

        Update update = new Update(newTitle,null, null, null, null);
        update.setTask(toEdit);
        expectedAB.editTask(update);

        // execute command and verify result
        assertCommandBehavior(helper.generateEditCommand(toEdit, 1, "New Title"),
                String.format(EditCommand.MESSAGE_SUCCESS, toEdit),
                expectedAB, expectedAB.getTaskList());
    }

    @Test
    public void execute_edit_tags_successful() throws Exception {
        TestDataHelper helper = new TestDataHelper();

        Task toEdit = helper.taskWithTags();
        Task toEditCopy = helper.taskWithTags();
        UniqueTagList newTagList = new UniqueTagList();
        newTagList.add(new Tag("tag3"));

        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toEdit);
        model.addTask(toEditCopy);

        Title newTitle = null;
        Update update = new Update(newTitle, null, null, newTagList, "");
        update.setTask(toEdit);
        expectedAB.editTask(update);

        // execute command and verify result
        assertCommandBehavior(helper.generateEditCommand(toEdit, 1, ""),
                String.format(EditCommand.MESSAGE_SUCCESS, toEdit),
                expectedAB, expectedAB.getTaskList());
    }

    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.taskWithTags();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeAdded);

        // setup starting state
        model.addTask(toBeAdded); // task already in internal address book
        
        // execute command and verify result
        assertCommandBehavior(
                helper.generateAddCommand(toBeAdded),
                AddCommand.MESSAGE_DUPLICATE_ENTRY,
                expectedAB,
                expectedAB.getTaskList());

    }


    @Test
    public void execute_list_showsAllPersons() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        TaskManager expectedAB = helper.generateAddressBook(2);
        List<? extends Entry> expectedList = expectedAB.getTaskList();

        // prepare address book state
        helper.addToModel(model, 2);

        assertCommandBehavior("list",
                ListCommand.MESSAGE_SUCCESS,
                expectedAB,
                expectedList);
    }


    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single task in the last shown list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage) throws Exception {
        assertCommandBehavior(commandWord , expectedMessage); //index missing
        assertCommandBehavior(commandWord + " +1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " -1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage); //index cannot be 0
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single task in the last shown list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> entryList = helper.generateEntreList(2);

        // set AB state to 2 entries
        model.resetData(new TaskManager());
        for (Task p : entryList) {
            model.addTask(p);
        }

        assertCommandBehavior(commandWord + " 3", expectedMessage, model.getTaskManager(), entryList);
    }

    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void execute_delete_removesCorrectPerson() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threePersons = helper.generateEntreList(3);

        TaskManager expectedAB = helper.generateAddressBook(threePersons);
        expectedAB.removeEntry(threePersons.get(1));
        helper.addToModel(model, threePersons);

        assertCommandBehavior("delete 2",
                String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, threePersons.get(1)),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_list_showAllEntries() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE);
        
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generateEntryWithTitle("bla bla KEY bla");
        Task pTarget2 = helper.generateEntryWithTitle("bla KEY bla bceofeia");
        Task p1 = helper.generateEntryWithTitle("KE Y");
        Task p2 = helper.generateEntryWithTitle("KEYKEYKEY sduauo");
        
        List<Task> fourPersons = helper.generateEntryList(p1, pTarget1, p2, pTarget2);
        TaskManager expectedAB = helper.generateAddressBook(fourPersons);
        List<Task> expectedList = helper.generateEntryList(p1, pTarget1, p2, pTarget2);
        helper.addToModel(model, fourPersons);

        assertCommandBehavior("list",
                ListCommand.MESSAGE_SUCCESS,
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_list_onlyMatchesFullWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generateEntryWithTitle("bla bla KEY bla");
        Task pTarget2 = helper.generateEntryWithTitle("bla KEY bla bceofeia");
        Task p1 = helper.generateEntryWithTitle("KE Y");
        Task p2 = helper.generateEntryWithTitle("KEYKEYKEY sduauo");

        List<Task> fourPersons = helper.generateEntryList(p1, pTarget1, p2, pTarget2);
        TaskManager expectedAB = helper.generateAddressBook(fourPersons);
        List<Task> expectedList = helper.generateEntryList(pTarget1, pTarget2);
        helper.addToModel(model, fourPersons);

        assertCommandBehavior("list KEY",
                Command.getMessageForPersonListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_list_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task p1 = helper.generateEntryWithTitle("bla bla KEY bla");
        Task p2 = helper.generateEntryWithTitle("bla KEY bla bceofeia");
        Task p3 = helper.generateEntryWithTitle("key key");
        Task p4 = helper.generateEntryWithTitle("KEy sduauo");

        List<Task> fourPersons = helper.generateEntryList(p3, p1, p4, p2);
        TaskManager expectedAB = helper.generateAddressBook(fourPersons);
        List<Task> expectedList = fourPersons;
        helper.addToModel(model, fourPersons);

        assertCommandBehavior("list KEY",
                Command.getMessageForPersonListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_list_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generateEntryWithTitle("bla bla KEY bla");
        Task pTarget2 = helper.generateEntryWithTitle("bla rAnDoM bla bceofeia");
        Task pTarget3 = helper.generateEntryWithTitle("key key");
        Task p1 = helper.generateEntryWithTitle("sduauo");

        List<Task> fourPersons = helper.generateEntryList(pTarget1, p1, pTarget2, pTarget3);
        TaskManager expectedAB = helper.generateAddressBook(fourPersons);
        List<Task> expectedList = helper.generateEntryList(pTarget1, pTarget2, pTarget3);
        helper.addToModel(model, fourPersons);

        assertCommandBehavior("list key rAnDoM",
                Command.getMessageForPersonListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_tagInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("tag", expectedMessage);
    }

    @Test
    public void execute_tagInvalidTagFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(Tag.MESSAGE_TAG_CONSTRAINTS);
        
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));

        List<Task> expectedList = helper.generateEntryList(t1);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        
        assertCommandBehavior("tag 1 " + TAG_FLAG + "**", expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_tag_withoutTagnameArgs_errorMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Task t1Copy = helper.generateTask(1);
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        
        assertCommandBehavior("tag 1", expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_tag_allowAlphanumericSpaceUnderscore() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        
        Tag tag1 = new Tag("tag10");
        Tag tag2 = new Tag("tag_10");
        Tag tag3 = new Tag("tag 10");
        UniqueTagList tags = new UniqueTagList(tag1, tag2, tag3);
        
        Task t1Copy = helper.generateTask(1);
        t1Copy.addTags(tags);
        
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, tags, t1Copy);
        assertCommandBehavior(helper.generateTagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_tag_allowAddExistingTags() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        UniqueTagList tags = new UniqueTagList(tag1, tag2, tag3);
        
        Task t1Copy = helper.generateTask(1);
        t1Copy.addTags(tags);
        
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, new UniqueTagList(tag3), t1Copy);
        assertCommandBehavior(helper.generateTagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_tagOnlyExistingTags_errorMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        UniqueTagList tags = new UniqueTagList(tag1, tag2);
        
        Task t1Copy = helper.generateTask(1);
        t1Copy.addTags(tags);
        
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(TagCommand.MESSAGE_ALREADY_EXISTS, t1Copy);
        assertCommandBehavior(helper.generateTagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_untagInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("untag", expectedMessage);
    }

    @Test
    public void execute_untagInvalidTagFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(Tag.MESSAGE_TAG_CONSTRAINTS);
        
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));

        List<Task> expectedList = helper.generateEntryList(t1);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        
        assertCommandBehavior("untag 1 " + TAG_FLAG + "**", expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_untagWithoutTagnameArgs_errorMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Task t1Copy = helper.generateTask(1);
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE);
        
        assertCommandBehavior("untag 1", expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_untag_allowAlphanumericSpaceUnderscore() throws Exception {
        TestDataHelper helper = new TestDataHelper();

        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag_1");
        Tag tag3 = new Tag("tag 1");
        UniqueTagList tags = new UniqueTagList(tag1, tag2, tag3);
                
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Task t1Copy = helper.generateTask(1);
        t1Copy.removeTags(tags);
        
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        helper.addToAddressBook(expectedAB, new UniqueTagList(tag1));

        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, new UniqueTagList(tag1), t1Copy);
        assertCommandBehavior(helper.generateUntagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_untagNonExistentTags_NonExistentMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Tag tag3 = new Tag("tag3");  // Does not exist
        UniqueTagList tags = new UniqueTagList(tag3);
        
        Task t1Copy = helper.generateTask(1);
        
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(UntagCommand.MESSAGE_NON_EXISTENT, t1Copy);
        assertCommandBehavior(helper.generateUntagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_mark_successful() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task toBeMarked = helper.generateTask(1);
        Task toBeMarkedCopy = helper.generateTask(1);

        TaskManager expectedAB = new TaskManager();
        toBeMarkedCopy.mark();
        expectedAB.addTask(toBeMarkedCopy);
        
        model.addTask(toBeMarked);
        assertCommandBehavior("mark 1",
                String.format(MarkCommand.MESSAGE_SUCCESS, toBeMarked),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_mark_alreadyMarked() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task alreadyMarked = helper.generateTask(1);
        alreadyMarked.mark();
        Task toBeMarkedCopy = helper.generateTask(1);

        TaskManager expectedAB = new TaskManager();
        toBeMarkedCopy.mark();
        expectedAB.addTask(toBeMarkedCopy);
        
        model.addTask(alreadyMarked);
        assertCommandBehavior("mark 1",
                String.format(MarkCommand.MESSAGE_SUCCESS, alreadyMarked),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_unmark_successful() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task toBeUnmarked = helper.generateTask(1);
        Task toBeUnmarkedCopy = helper.generateTask(1);

        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeUnmarkedCopy);
        
        model.addTask(toBeUnmarked);
        assertCommandBehavior("unmark 1",
                String.format(UnmarkCommand.MESSAGE_SUCCESS, toBeUnmarked),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_unmark_alreadyUnmarked() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task alreadyUnmarked = helper.generateTask(1);
        Task alreadyUnmarkedCopy = helper.generateTask(1);

        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(alreadyUnmarkedCopy);
        
        model.addTask(alreadyUnmarked);
        assertCommandBehavior("unmark 1",
                String.format(UnmarkCommand.MESSAGE_SUCCESS, alreadyUnmarked),
                expectedAB,
                expectedAB.getTaskList());
    }
    //###################
    //# Undo test cases #
    //###################
    @Test
    public void execute_undoWithNoUndoableCommandsInHistory_errorMessageShown() throws Exception {
        // setup expectations
        TaskManager expectedAB = new TaskManager();
        
        // command before undo
        logic.execute("list");
        // execute command and verify result
        assertCommandBehavior("undo",
                UndoCommand.MESSAGE_FAILURE,
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_undoAdd_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.taskWithTags();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeAdded);
        expectedAB.removeEntry(toBeAdded);
        
        // commands before undo
        logic.execute(helper.generateAddCommand(toBeAdded));
        // execute command and verify result
        assertCommandBehavior("undo",
                String.format(AddCommand.MESSAGE_UNDO_SUCCESS, toBeAdded),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_undoDelete_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task task1 = helper.generateTask(1);
        Task task2 = helper.generateTask(2);
        Task task3 = helper.generateTask(3);
        
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(task1);
        expectedAB.addTask(task2);
        expectedAB.addTask(task3);
        expectedAB.removeEntry(task2);
        expectedAB.addTask(task2);

        model.addTask(task1);
        model.addTask(task2);
        model.addTask(task3);
        // command to undo
        logic.execute("delete 2");
        // execute command and verify result
        assertCommandBehavior("undo",
                String.format(DeleteCommand.MESSAGE_UNDO_DELETE_PERSON_SUCCESS, task2),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_undoEdit_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toEdit = helper.taskWithTags();
        Task toEditCopy = helper.taskWithTags();

        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toEditCopy);
        
        model.addTask(toEdit);
        // command to undo
        logic.execute(helper.generateEditCommand(toEdit, 1, "New Title"));

        // execute command and verify result
        assertCommandBehavior("undo",
                String.format(EditCommand.MESSAGE_UNDO_SUCCESS, toEditCopy),
                expectedAB, expectedAB.getTaskList());
    }

    @Test
    public void execute_undoMark_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeMarked = helper.taskWithTags();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeMarked);

        model.addTask(toBeMarked);
        // command to undo
        logic.execute("mark 1");
        // execute command and verify result
        assertCommandBehavior("undo",
                String.format(MarkCommand.MESSAGE_UNDO_SUCCESS, toBeMarked),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_undoUnmark_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeUnmarked = helper.taskWithTags();
        toBeUnmarked.mark();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeUnmarked);

        model.addTask(toBeUnmarked);
        // command to undo
        logic.execute("unmark 1");
        // execute command and verify result
        assertCommandBehavior("undo",
                String.format(UnmarkCommand.MESSAGE_UNDO_SUCCESS, toBeUnmarked),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_undoTag_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeTagged = helper.taskWithTags();
        Task toBeTaggedCopy = helper.taskWithTags();

        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("newTag");
        UniqueTagList tags = new UniqueTagList(tag1, tag2);

        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeTagged);
        expectedAB.addTag(tag2);

        model.addTask(toBeTagged);
        // command to undo
        logic.execute(helper.generateTagCommand(tags, 1));
        // execute command and verify result
        assertCommandBehavior("undo",
                String.format(TagCommand.MESSAGE_UNDO_SUCCESS, new UniqueTagList(tag2), toBeTaggedCopy),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_undoUntag_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeUntagged = helper.taskWithTags();
        Task toBeUntaggedCopy = helper.taskWithTags();

        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("newTag");
        UniqueTagList tags = new UniqueTagList(tag1, tag2);

        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeUntagged);

        model.addTask(toBeUntagged);
        // command to undo
        logic.execute(helper.generateUntagCommand(tags, 1));
        // execute command and verify result
        assertCommandBehavior("undo",
                String.format(UntagCommand.MESSAGE_UNDO_SUCCESS, new UniqueTagList(tag1), toBeUntaggedCopy),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_undoClear_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task task1 = helper.generateTask(1);
        Task task2 = helper.generateTask(2);
        Task task3 = helper.generateTask(3);
        
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(task1);
        expectedAB.addTask(task2);
        expectedAB.addTask(task3);

        model.addTask(task1);
        model.addTask(task2);
        model.addTask(task3);
        // command to undo
        logic.execute("clear");
        // execute command and verify result
        assertCommandBehavior("undo",
                ClearCommand.MESSAGE_UNDO_SUCCESS,
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_undoMultipleCommands() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task task1 = helper.generateTask(1);
        Task task2 = helper.generateTask(2);
        Task task2Copy = helper.generateTask(2);
        Task task3 = helper.generateTask(3);
        
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(task1);
        expectedAB.addTask(task2);
        expectedAB.addTask(task3);
        expectedAB.removeEntry(task2);
        expectedAB.addTask(task2);

        model.addTask(task1);
        task2.mark();
        model.addTask(task2);
        // command to undo
        logic.execute(helper.generateAddCommand(task3));
        logic.execute("mark 2");
        logic.execute("list");  //non-undoable command.
        logic.execute("delete 2");

        // Undo "delete 2"
        assertCommandBehavior("undo",
                String.format(DeleteCommand.MESSAGE_UNDO_DELETE_PERSON_SUCCESS, task2),
                expectedAB,
                expectedAB.getTaskList());
        
        // Undo "mark 2"
        assertCommandBehavior("undo",
                String.format(MarkCommand.MESSAGE_UNDO_SUCCESS, task2Copy),
                expectedAB,
                expectedAB.getTaskList());

        // Undo add task
        expectedAB.removeEntry(task3);
        assertCommandBehavior("undo",
                String.format(AddCommand.MESSAGE_UNDO_SUCCESS, task3),
                expectedAB,
                expectedAB.getTaskList());
    }
    

    //###################
    //# Redo test cases #
    //###################
    @Test
    public void execute_redoWithNoUndoableCommandsInHistory_errorMessageShown() throws Exception {
        // setup expectations
        TaskManager expectedAB = new TaskManager();
        
        // execute command and verify result
        assertCommandBehavior("redo",
                RedoCommand.MESSAGE_FAILURE,
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_redoAdd_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeAdded = helper.taskWithTags();
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeAdded);
        
        // commands before undo
        logic.execute(helper.generateAddCommand(toBeAdded));
        logic.execute("undo");
        // execute command and verify result
        assertCommandBehavior("redo",
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_redoDelete_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task task1 = helper.generateTask(1);
        Task task2 = helper.generateTask(2);
        Task task3 = helper.generateTask(3);
        
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(task1);
        expectedAB.addTask(task2);
        expectedAB.addTask(task3);
        expectedAB.removeEntry(task2);

        model.addTask(task1);
        model.addTask(task2);
        model.addTask(task3);
        // command to undo
        logic.execute("delete 2");
        logic.execute("undo");
        // execute command and verify result
        assertCommandBehavior("redo",
                String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, task2),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_redoEdit_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toEdit = helper.taskWithTags();
        Task toEditCopy = helper.taskWithTags();

        TaskManager expectedAB = new TaskManager();
        toEditCopy.setTitle(new Title("New Title"));
        expectedAB.addTask(toEditCopy);
        
        model.addTask(toEdit);
        // command to undo
        logic.execute(helper.generateEditCommand(toEdit, 1, "New Title"));
        logic.execute("undo");

        // execute command and verify result
        assertCommandBehavior("redo",
                String.format(EditCommand.MESSAGE_SUCCESS, toEditCopy),
                expectedAB, expectedAB.getTaskList());
    }

    @Test
    public void execute_redoMark_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeMarked = helper.taskWithTags();
        Task toBeMarkedCopy = helper.taskWithTags();
        TaskManager expectedAB = new TaskManager();
        toBeMarkedCopy.mark();
        expectedAB.addTask(toBeMarkedCopy);

        model.addTask(toBeMarked);
        // command to undo
        logic.execute("mark 1");
        logic.execute("undo");
        // execute command and verify result
        assertCommandBehavior("redo",
                String.format(MarkCommand.MESSAGE_SUCCESS, toBeMarked),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_redoUnmark_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeUnmarked = helper.taskWithTags();
        Task toBeUnmarkedCopy = helper.taskWithTags();
        TaskManager expectedAB = new TaskManager();
        toBeUnmarked.mark();
        expectedAB.addTask(toBeUnmarkedCopy);

        model.addTask(toBeUnmarked);
        // command to undo
        logic.execute("unmark 1");
        logic.execute("undo");
        // execute command and verify result
        assertCommandBehavior("redo",
                String.format(UnmarkCommand.MESSAGE_SUCCESS, toBeUnmarked),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_redoTag_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeTagged = helper.taskWithTags();
        Task toBeTaggedCopy = helper.taskWithTags();

        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("newTag");
        UniqueTagList tags = new UniqueTagList(tag1, tag2);
        toBeTaggedCopy.addTags(tags);
        
        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeTaggedCopy);

        model.addTask(toBeTagged);
        // command to undo
        logic.execute(helper.generateTagCommand(tags, 1));
        logic.execute("undo");
        // execute command and verify result
        assertCommandBehavior("redo",
                String.format(TagCommand.MESSAGE_SUCCESS, toBeTaggedCopy),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_redoUntag_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task toBeUntagged = helper.taskWithTags();
        Task toBeUntaggedCopy = helper.taskWithTags();
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("newTag");
        UniqueTagList tags = new UniqueTagList(tag1, tag2);

        TaskManager expectedAB = new TaskManager();
        expectedAB.addTask(toBeUntaggedCopy);
        toBeUntaggedCopy.removeTags(tags);

        model.addTask(toBeUntagged);
        // command to undo
        logic.execute(helper.generateUntagCommand(tags, 1));
        logic.execute("undo");
        // execute command and verify result
        assertCommandBehavior("redo",
                String.format(UntagCommand.MESSAGE_SUCCESS, toBeUntaggedCopy),
                expectedAB,
                expectedAB.getTaskList());
    }

    @Test
    public void execute_redoClear_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Task task1 = helper.generateTask(1);
        Task task2 = helper.generateTask(2);
        Task task3 = helper.generateTask(3);
        
        TaskManager expectedAB = new TaskManager();

        model.addTask(task1);
        model.addTask(task2);
        model.addTask(task3);
        // command to undo
        logic.execute("clear");
        logic.execute("undo");
        // execute command and verify result
        assertCommandBehavior("redo",
                ClearCommand.MESSAGE_SUCCESS,
                expectedAB,
                expectedAB.getTaskList());
    }
    
    /**
     * A utility class to generate test data.
     */
    class TestDataHelper{

        Task taskWithTags() throws Exception {
            Title name = new Title("Adam Brown");
            Tag tag1 = new Tag("tag1");
            Tag tag2 = new Tag("tag2");
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new Task(name, tags);
        }

        /**
         * Generates a valid task using the given seed.
         * Running this function with the same parameter values guarantees the returned task will have the same state.
         * Each unique seed will generate a unique Person object.
         *
         * @param seed used to generate the task data field values
         */
        Task generateTask(int seed) throws Exception {
            return new Task(
                    new Title("Entry " + seed),
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1)))
            );
        }

        /** Generates the correct add command based on the task given */
        String generateAddCommand(Task p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");

            cmd.append(p.getTitle().toString());

            UniqueTagList tags = p.getTags();

            if (!tags.isEmpty()){
                for (Tag t : tags) {
                    cmd.append(" " + TAG_FLAG).append(t.tagName);
                }
            }

            return cmd.toString();
        }

        String generateEditCommand(Task p, int index, String title) {
            StringBuffer cmd = new StringBuffer();
            
            cmd.append(String.format("edit %d ", index));
            cmd.append(TITLE_FLAG).append(title);

            UniqueTagList tags = p.getTags();
            if (!tags.isEmpty()){
                for (Tag t : tags) {
                    cmd.append(" " + TAG_FLAG).append(t.tagName);
                }
            }
            return cmd.toString();
        }
        
        /** Generates the tag command based on the given tag and index*/
        String generateTagCommand(UniqueTagList tags, int index) {
            StringBuffer cmd = new StringBuffer();
            
            cmd.append(String.format("tag %d ", index));

            for (Tag t : tags) {
                cmd.append(" " + TAG_FLAG).append(t.tagName);
            }
            
            return cmd.toString();
        }
        
        /** Generates the tag command based on the given tag and index*/
        String generateTagCommand(Task p, int index) {
            UniqueTagList tags = p.getTags();
            return generateTagCommand(tags, index);
        }
        
        /** Generates the untag command based on the given tag and index*/
        String generateUntagCommand(UniqueTagList tags, int index) {
            StringBuffer cmd = new StringBuffer();
            
            cmd.append(String.format("untag %d ", index));
            
            for (Tag t : tags) {
                cmd.append(" " + TAG_FLAG).append(t.tagName);
            }
            
            return cmd.toString();
        }

        /** Generates the untag command based on the given tag and index*/
        String generateUntagCommand(Task p, int index) {
            UniqueTagList tags = p.getTags();
            return generateUntagCommand(tags, index);
        }

        /**
         * Generates an TaskManager with auto-generated persons.
         */
        TaskManager generateAddressBook(int numGenerated) throws Exception{
            TaskManager taskManager = new TaskManager();
            addToAddressBook(taskManager, numGenerated);
            return taskManager;
        }

        /**
         * Generates an TaskManager based on the list of Persons given.
         */
        TaskManager generateAddressBook(List<Task> persons) throws Exception{
            TaskManager taskManager = new TaskManager();
            addToAddressBook(taskManager, persons);
            return taskManager;
        }

        /**
         * Adds auto-generated Person objects to the given TaskManager
         * @param taskManager The TaskManager to which the Persons will be added
         */
        void addToAddressBook(TaskManager taskManager, int numGenerated) throws Exception{
            addToAddressBook(taskManager, generateEntreList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given TaskManager
         */
        void addToAddressBook(TaskManager taskManager, List<Task> personsToAdd) throws Exception{
            for(Task p: personsToAdd){
                taskManager.addTask(p);
            }
        }

        /**
         * Adds the given list of Tags to the given TaskManager
         */
        void addToAddressBook(TaskManager taskManager, UniqueTagList tagsToAdd) throws Exception{
            for(Tag t: tagsToAdd){
                taskManager.addTag(t);
            }
        }

        /**
         * Adds auto-generated Person objects to the given model
         * @param model The model to which the Persons will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception{
            addToModel(model, generateEntreList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given model
         */
        void addToModel(Model model, List<Task> personsToAdd) throws Exception{
            for(Task p: personsToAdd){
                model.addTask(p);
            }
        }

        /**
         * Adds the given list of tags to the given model
         */
        void addToModel(Model model, UniqueTagList tagList) throws Exception{
            for(Tag t: tagList){
                model.addTag(t);
            }
        }

        /**
         * Generates a list of Persons based on the flags.
         */
        List<Task> generateEntreList(int numGenerated) throws Exception{
            List<Task> entries = new ArrayList<>();
            for(int i = 1; i <= numGenerated; i++){
                entries.add(generateTask(i));
            }
            return entries;
        }

        List<Task> generateEntryList(Task... entries) {
            return Arrays.asList(entries);
        }

        /**
         * Generates a Person object with given name. Other fields will have some dummy values.
         */
        Task generateEntryWithTitle(String title) throws Exception {
            return new Task(
                    new Title(title),
                    new UniqueTagList(new Tag("tag"))
            );
        }
    }
}
