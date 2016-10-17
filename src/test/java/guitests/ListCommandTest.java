package guitests;

import org.junit.Test;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestEntry;

import static org.junit.Assert.assertTrue;

public class ListCommandTest extends AddressBookGuiTest {

    @Test
    public void list_nonEmptyList() {
        assertListResult("list Mark"); //no results
        assertListResult("list Meier", td.benson, td.daniel); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertListResult("list Meier",td.daniel);
    }

    @Test
    public void list_emptyList(){
        commandBox.runCommand("clear");
        assertListResult("list Jean"); //no results
    }

    @Test
    public void list_invalidCommand_fail() {
        commandBox.runCommand("listgeorge");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertListResult(String command, TestEntry... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " entries listed!");
        assertTrue(personListPanel.isListMatching(expectedHits));
    }
}
