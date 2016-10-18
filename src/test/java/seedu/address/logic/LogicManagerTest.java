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
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.task.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.storage.StorageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.*;

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlyAddressBook latestSavedAddressBook;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(AddressBookChangedEvent abce) {
        latestSavedAddressBook = new AddressBook(abce.data);
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

        latestSavedAddressBook = new AddressBook(model.getAddressBook()); // last saved assumed to be up to date before.
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
     * @see #assertCommandBehavior(String, String, ReadOnlyAddressBook, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new AddressBook(), Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal address book data are same as those in the {@code expectedAddressBook} <br>
     *      - the backing list shown by UI matches the {@code shownList} <br>
     *      - {@code expectedAddressBook} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
                                       ReadOnlyAddressBook expectedAddressBook,
                                       List<? extends Entry> expectedShownList) throws Exception {

        //Execute the command
        CommandResult result = logic.execute(inputCommand);
        //Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedShownList, model.getFilteredPersonList());
        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedAddressBook, model.getAddressBook());
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

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new AddressBook(), Collections.emptyList());
    }


    @Test
    public void execute_add_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandBehavior(
                "add t/NTUC Valid Task", expectedMessage);
    }

    @Test
    public void execute_add_invalidTaskData() throws Exception {
        assertCommandBehavior(
                "add []\\[;]", Title.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Task t/invalid_-[.tag", Tag.MESSAGE_TAG_CONSTRAINTS);
    }

    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        FloatingTask toBeAdded = helper.taskWithTags();
        AddressBook expectedAB = new AddressBook();
        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedAB,
                expectedAB.getPersonList());

    }

    @Test
    public void execute_edit_successful() throws Exception {
        TestDataHelper helper = new TestDataHelper();

        FloatingTask toEdit = helper.taskWithTags();
        FloatingTask toEditCopy = helper.taskWithTags();
        Title newTitle = new Title("New Title");

        AddressBook expectedAB = new AddressBook();
        expectedAB.addTask(toEdit);
        model.addTask(toEditCopy);

        Update update = new Update(newTitle, null, null);
        update.setTask(toEdit);
        expectedAB.editTask(update);

        // execute command and verify result
        assertCommandBehavior(helper.generateEditCommand(toEdit, 1, "New Title"),
                String.format(EditCommand.MESSAGE_SUCCESS, toEdit),
                expectedAB, expectedAB.getPersonList());
    }

    @Test
    public void execute_edit_tags_successful() throws Exception {
        TestDataHelper helper = new TestDataHelper();

        FloatingTask toEdit = helper.taskWithTags();
        FloatingTask toEditCopy = helper.taskWithTags();
        UniqueTagList newTagList = new UniqueTagList();
        newTagList.add(new Tag("tag3"));

        AddressBook expectedAB = new AddressBook();
        expectedAB.addTask(toEdit);
        model.addTask(toEditCopy);

        Title newTitle = null;
        Update update = new Update(newTitle, newTagList, "");
        update.setTask(toEdit);
        expectedAB.editTask(update);

        // execute command and verify result
        assertCommandBehavior(helper.generateEditCommand(toEdit, 1, ""),
                String.format(EditCommand.MESSAGE_SUCCESS, toEdit),
                expectedAB, expectedAB.getPersonList());
    }

    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        FloatingTask toBeAdded = helper.taskWithTags();
        AddressBook expectedAB = new AddressBook();
        expectedAB.addTask(toBeAdded);

        // setup starting state
        model.addTask(toBeAdded); // task already in internal address book
        
        // execute command and verify result
        assertCommandBehavior(
                helper.generateAddCommand(toBeAdded),
                AddCommand.MESSAGE_DUPLICATE_ENTRY,
                expectedAB,
                expectedAB.getPersonList());

    }


    @Test
    public void execute_list_showsAllPersons() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        AddressBook expectedAB = helper.generateAddressBook(2);
        List<? extends Entry> expectedList = expectedAB.getPersonList();

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
        List<FloatingTask> entryList = helper.generateEntreList(2);

        // set AB state to 2 entries
        model.resetData(new AddressBook());
        for (FloatingTask p : entryList) {
            model.addTask(p);
        }

        assertCommandBehavior(commandWord + " 3", expectedMessage, model.getAddressBook(), entryList);
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
        List<FloatingTask> threePersons = helper.generateEntreList(3);

        AddressBook expectedAB = helper.generateAddressBook(threePersons);
        helper.addToModel(model, threePersons);

        assertCommandBehavior("select 2",
                String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS, 2),
                expectedAB,
                expectedAB.getPersonList());
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
        List<FloatingTask> threePersons = helper.generateEntreList(3);

        AddressBook expectedAB = helper.generateAddressBook(threePersons);
        expectedAB.removePerson(threePersons.get(1));
        helper.addToModel(model, threePersons);

        assertCommandBehavior("delete 2",
                String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, threePersons.get(1)),
                expectedAB,
                expectedAB.getPersonList());
    }

    @Test
    public void execute_list_showAllEntries() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE);
        
        TestDataHelper helper = new TestDataHelper();
        FloatingTask pTarget1 = helper.generateEntryWithTitle("bla bla KEY bla");
        FloatingTask pTarget2 = helper.generateEntryWithTitle("bla KEY bla bceofeia");
        FloatingTask p1 = helper.generateEntryWithTitle("KE Y");
        FloatingTask p2 = helper.generateEntryWithTitle("KEYKEYKEY sduauo");
        
        List<FloatingTask> fourPersons = helper.generateEntryList(p1, pTarget1, p2, pTarget2);
        AddressBook expectedAB = helper.generateAddressBook(fourPersons);
        List<FloatingTask> expectedList = helper.generateEntryList(p1, pTarget1, p2, pTarget2);
        helper.addToModel(model, fourPersons);

        assertCommandBehavior("list",
                ListCommand.MESSAGE_SUCCESS,
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_list_onlyMatchesFullWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        FloatingTask pTarget1 = helper.generateEntryWithTitle("bla bla KEY bla");
        FloatingTask pTarget2 = helper.generateEntryWithTitle("bla KEY bla bceofeia");
        FloatingTask p1 = helper.generateEntryWithTitle("KE Y");
        FloatingTask p2 = helper.generateEntryWithTitle("KEYKEYKEY sduauo");

        List<FloatingTask> fourPersons = helper.generateEntryList(p1, pTarget1, p2, pTarget2);
        AddressBook expectedAB = helper.generateAddressBook(fourPersons);
        List<FloatingTask> expectedList = helper.generateEntryList(pTarget1, pTarget2);
        helper.addToModel(model, fourPersons);

        assertCommandBehavior("list KEY",
                Command.getMessageForPersonListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_list_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        FloatingTask p1 = helper.generateEntryWithTitle("bla bla KEY bla");
        FloatingTask p2 = helper.generateEntryWithTitle("bla KEY bla bceofeia");
        FloatingTask p3 = helper.generateEntryWithTitle("key key");
        FloatingTask p4 = helper.generateEntryWithTitle("KEy sduauo");

        List<FloatingTask> fourPersons = helper.generateEntryList(p3, p1, p4, p2);
        AddressBook expectedAB = helper.generateAddressBook(fourPersons);
        List<FloatingTask> expectedList = fourPersons;
        helper.addToModel(model, fourPersons);

        assertCommandBehavior("list KEY",
                Command.getMessageForPersonListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_list_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        FloatingTask pTarget1 = helper.generateEntryWithTitle("bla bla KEY bla");
        FloatingTask pTarget2 = helper.generateEntryWithTitle("bla rAnDoM bla bceofeia");
        FloatingTask pTarget3 = helper.generateEntryWithTitle("key key");
        FloatingTask p1 = helper.generateEntryWithTitle("sduauo");

        List<FloatingTask> fourPersons = helper.generateEntryList(pTarget1, p1, pTarget2, pTarget3);
        AddressBook expectedAB = helper.generateAddressBook(fourPersons);
        List<FloatingTask> expectedList = helper.generateEntryList(pTarget1, pTarget2, pTarget3);
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
        FloatingTask t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));

        List<FloatingTask> expectedList = helper.generateEntryList(t1);
        AddressBook expectedAB = helper.generateAddressBook(expectedList);
        
        assertCommandBehavior("tag 1 **", expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_tag_withoutTagnameArgs() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        FloatingTask t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        FloatingTask t1Copy = helper.generateTask(1);
        List<FloatingTask> expectedList = helper.generateEntryList(t1Copy);
        AddressBook expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, t1Copy);
        
        assertCommandBehavior("tag 1", expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_tag_allowAlphanumericSpaceUnderscore() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        FloatingTask t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        
        Tag tag1 = new Tag("tag10");
        Tag tag2 = new Tag("tag_10");
        Tag tag3 = new Tag("tag 10");
        UniqueTagList tags = new UniqueTagList(tag1, tag2, tag3);
        
        FloatingTask t1Copy = helper.generateTask(1);
        t1Copy.addTags(tags);
        
        List<FloatingTask> expectedList = helper.generateEntryList(t1Copy);
        AddressBook expectedAB = helper.generateAddressBook(expectedList);
        String expectedMessage = String.format(TagCommand.MESSAGE_SUCCESS, t1Copy);
        assertCommandBehavior(helper.generateTagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_tag_allowAddExistingTags() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        FloatingTask t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        UniqueTagList tags = new UniqueTagList(tag1, tag2, tag3);
        
        FloatingTask t1Copy = helper.generateTask(1);
        t1Copy.addTags(tags);
        
        List<FloatingTask> expectedList = helper.generateEntryList(t1Copy);
        AddressBook expectedAB = helper.generateAddressBook(expectedList);
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
        FloatingTask t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));

        List<FloatingTask> expectedList = helper.generateEntryList(t1);
        AddressBook expectedAB = helper.generateAddressBook(expectedList);
        
        assertCommandBehavior("untag 1 **", expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_untag_withoutTagnameArgs() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        FloatingTask t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        FloatingTask t1Copy = helper.generateTask(1);
        List<FloatingTask> expectedList = helper.generateEntryList(t1Copy);
        AddressBook expectedAB = helper.generateAddressBook(expectedList);
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
                
        FloatingTask t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        FloatingTask t1Copy = helper.generateTask(1);
        t1Copy.removeTags(tags);
        
        List<FloatingTask> expectedList = helper.generateEntryList(t1Copy);
        AddressBook expectedAB = helper.generateAddressBook(expectedList);
        helper.addToAddressBook(expectedAB, new UniqueTagList(tag1));
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, t1Copy);
        assertCommandBehavior(helper.generateUntagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
    }

    @Test
    public void execute_untag_allowRemoveNonExistentTags() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        FloatingTask t1 = helper.generateTask(1);
        helper.addToModel(model, helper.generateEntryList(t1));
        
        Tag tag1 = new Tag("tag1");
        Tag tag3 = new Tag("tag3");  // Does not exist
        UniqueTagList tags = new UniqueTagList(tag1, tag3);
        
        FloatingTask t1Copy = helper.generateTask(1);
        t1Copy.removeTags(tags);
        
        List<FloatingTask> expectedList = helper.generateEntryList(t1Copy);
        AddressBook expectedAB = helper.generateAddressBook(expectedList);
        helper.addToAddressBook(expectedAB, new UniqueTagList(tag1));
        String expectedMessage = String.format(UntagCommand.MESSAGE_SUCCESS, t1Copy);
        assertCommandBehavior(helper.generateUntagCommand(tags, 1),
                expectedMessage, expectedAB, expectedList);
    }

    /**
     * A utility class to generate test data.
     */
    class TestDataHelper{

        FloatingTask taskWithTags() throws Exception {
            Title name = new Title("Adam Brown");
            Tag tag1 = new Tag("tag1");
            Tag tag2 = new Tag("tag2");
            UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new FloatingTask(name, tags);
        }

        /**
         * Generates a valid task using the given seed.
         * Running this function with the same parameter values guarantees the returned task will have the same state.
         * Each unique seed will generate a unique Person object.
         *
         * @param seed used to generate the task data field values
         */
        FloatingTask generateTask(int seed) throws Exception {
            return new FloatingTask(
                    new Title("Person " + seed),
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1)))
            );
        }

        /** Generates the correct add command based on the task given */
        String generateAddCommand(FloatingTask p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");

            cmd.append(p.getTitle().toString());

            UniqueTagList tags = p.getTags();

            if (!tags.isEmpty()){
                StringJoiner joiner = new StringJoiner(",");
                for (Tag t : tags) {
                    joiner.add(t.tagName);
                }
                cmd.append(" t/").append(joiner.toString());
            }

            return cmd.toString();
        }

        String generateEditCommand(FloatingTask p, int index, String title) {
            StringBuffer cmd = new StringBuffer();
            
            cmd.append(String.format("edit %d ", index));
            cmd.append(title);

            UniqueTagList tags = p.getTags();
            if (!tags.isEmpty()){
                StringJoiner joiner = new StringJoiner(",");
                for (Tag t : tags) {
                    joiner.add(t.tagName);
                }
                cmd.append(" t/").append(joiner.toString());
            }
            return cmd.toString();
        }
        
        /** Generates the tag command based on the given tag and index*/
        String generateTagCommand(UniqueTagList tags, int index) {
            StringBuffer cmd = new StringBuffer();
            
            cmd.append(String.format("tag %d ", index));
            
            StringJoiner joiner = new StringJoiner(",");
            for (Tag t : tags) {
                joiner.add(t.tagName);
            }
            
            cmd.append(joiner.toString());
            
            return cmd.toString().replaceAll(",$", "");
        }
        
        /** Generates the tag command based on the given tag and index*/
        String generateTagCommand(FloatingTask p, int index) {
            UniqueTagList tags = p.getTags();
            return generateTagCommand(tags, index);
        }
        
        /** Generates the untag command based on the given tag and index*/
        String generateUntagCommand(UniqueTagList tags, int index) {
            StringBuffer cmd = new StringBuffer();
            
            cmd.append(String.format("untag %d ", index));
            
            StringJoiner joiner = new StringJoiner(",");
            for (Tag t : tags) {
                joiner.add(t.tagName);
            }
            
            cmd.append(joiner.toString());
            
            return cmd.toString().replaceAll(",$", "");
        }

        /** Generates the untag command based on the given tag and index*/
        String generateUntagCommand(FloatingTask p, int index) {
            UniqueTagList tags = p.getTags();
            return generateUntagCommand(tags, index);
        }

        /**
         * Generates an AddressBook with auto-generated persons.
         */
        AddressBook generateAddressBook(int numGenerated) throws Exception{
            AddressBook addressBook = new AddressBook();
            addToAddressBook(addressBook, numGenerated);
            return addressBook;
        }

        /**
         * Generates an AddressBook based on the list of Persons given.
         */
        AddressBook generateAddressBook(List<FloatingTask> persons) throws Exception{
            AddressBook addressBook = new AddressBook();
            addToAddressBook(addressBook, persons);
            return addressBook;
        }

        /**
         * Adds auto-generated Person objects to the given AddressBook
         * @param addressBook The AddressBook to which the Persons will be added
         */
        void addToAddressBook(AddressBook addressBook, int numGenerated) throws Exception{
            addToAddressBook(addressBook, generateEntreList(numGenerated));
        }

        /**
         * Adds the given list of Persons to the given AddressBook
         */
        void addToAddressBook(AddressBook addressBook, List<FloatingTask> personsToAdd) throws Exception{
            for(FloatingTask p: personsToAdd){
                addressBook.addTask(p);
            }
        }

        /**
         * Adds the given list of Tags to the given AddressBook
         */
        void addToAddressBook(AddressBook addressBook, UniqueTagList tagsToAdd) throws Exception{
            for(Tag t: tagsToAdd){
                addressBook.addTag(t);
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
        void addToModel(Model model, List<FloatingTask> personsToAdd) throws Exception{
            for(FloatingTask p: personsToAdd){
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
        List<FloatingTask> generateEntreList(int numGenerated) throws Exception{
            List<FloatingTask> entries = new ArrayList<>();
            for(int i = 1; i <= numGenerated; i++){
                entries.add(generateTask(i));
            }
            return entries;
        }

        List<FloatingTask> generateEntryList(FloatingTask... entries) {
            return Arrays.asList(entries);
        }

        /**
         * Generates a Person object with given name. Other fields will have some dummy values.
         */
        FloatingTask generateEntryWithTitle(String title) throws Exception {
            return new FloatingTask(
                    new Title(title),
                    new UniqueTagList(new Tag("tag"))
            );
        }
    }
}
