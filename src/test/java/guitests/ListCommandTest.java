package guitests;

import org.junit.Test;
import java.util.Arrays;

import seedu.address.commons.core.Messages;
import seedu.address.model.task.EntryViewComparator;
import seedu.address.testutil.TestEntry;

import static org.junit.Assert.assertTrue;

public class ListCommandTest extends AddressBookGuiTest {

    @Test
    public void list_nonEmptyList() {
        //TODO: Write better, less fragile tests
        assertListResult("list nodoge"); //no results
        assertListResult("list Buy", td.apple, td.banana, td.eggplant); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertListResult("list doge",td.doge);
    }

    @Test
    public void list_emptyList(){
        commandBox.runCommand("clear");
        assertListResult("list doge"); //no results
    }

    @Test
    public void list_invalidCommand_fail() {
        commandBox.runCommand("listdoge");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertListResult(String command, TestEntry... expectedHits ) {
        Arrays.sort(expectedHits, new EntryViewComparator());
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " entries listed!");
        assertTrue(taskList.isListMatching(expectedHits));
    }
}
