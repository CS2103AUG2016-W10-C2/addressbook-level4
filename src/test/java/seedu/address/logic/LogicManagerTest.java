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
import seedu.address.commons.events.ui.ShowHelpRequestEvent;
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
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
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
        logic = new LogicManager(model, new StorageManager(tempAddressBookFile, tempPreferencesFile));
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
                "add []\\[;]", Title.MESSAGE_NAME_CONSTRAINTS);
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

        Update update = new Update(newTitle, null, null);
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
        Update update = new Update(newTitle, newTagList, "");
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
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    @Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    @Test
    public void execute_select_jumpsToCorrectPerson() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threePersons = helper.generateEntreList(3);

        TaskManager expectedAB = helper.generateAddressBook(threePersons);
        helper.addToModel(model, threePersons);

        assertCommandBehavior("select 2",
                String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS, 2),
                expectedAB,
                expectedAB.getTaskList());
        assertEquals(1, targetedJumpIndex);
        assertEquals(model.getFilteredPersonList().get(1), threePersons.get(1));
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
    public void execute_tag_withoutTagnameArgs() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Task t1Copy = helper.generateTask(1);
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, t1Copy);
        
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
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, t1Copy);
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
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, t1Copy);
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
    public void execute_untag_withoutTagnameArgs() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Task t1Copy = helper.generateTask(1);
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, t1Copy);
        
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
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, t1Copy);
        assertCommandBehavior(helper.generateUntagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_untag_allowRemoveNonExistentTags() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Tag tag1 = new Tag("tag1");
        Tag tag3 = new Tag("tag3");  // Does not exist
        UniqueTagList tags = new UniqueTagList(tag1, tag3);
        
        Task t1Copy = helper.generateTask(1);
        t1Copy.removeTags(tags);
        
        List<Task> expectedList = helper.generateEntryList(t1Copy);
        TaskManager expectedAB = helper.generateAddressBook(expectedList);
        helper.addToAddressBook(expectedAB, new UniqueTagList(tag1));
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, t1Copy);
        assertCommandBehavior(helper.generateUntagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
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
                    new Title("Person " + seed),
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
