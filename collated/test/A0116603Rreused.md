# A0116603Rreused
###### /java/guitests/AddCommandTest.java
``` java
    @Test
    public void add() {
        TestEntry[] currentList = td.getTypicalSortedPersons(); // sample entries already present
        TestEntry testEntry;

        //add new entries
        for (TestEntry entry : td.getNonSampleEntries()) {
            testEntry = entry;
            assertAddSuccess(testEntry, currentList);
            currentList = TestUtil.addPersonsToList(currentList, testEntry);
        }

        assertTrue(currentList.length > 0);

        //add duplicate task
        commandBox.runCommand(currentList[0].getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_ENTRY);
        assertTrue(taskList.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertAddSuccess(currentList[0]);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

    }

```
###### /java/guitests/ListCommandTest.java
``` java
    @Test
    public void list_nonEmptyList() {
        // search miss
        assertListResult(ListCommand.COMMAND_WORD + " 404 doge not found");

        // search hits for BuyTasks
        TestTasks generator = new TypicalTestTasks.BuyTasks();
        assertListResult(ListCommand.COMMAND_WORD + " " + TypicalTestTasks.BuyTasks.VERB, generator.getSampleEntries());

        // search after deleting one result
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " 1");
        generator = new TypicalTestTasks.WatchTasks();
        assertListResult(ListCommand.COMMAND_WORD + " " + TypicalTestTasks.WatchTasks.VERB, generator.getSampleEntries());
    }

    @Test
    public void list_emptyList(){
        // clear the list and assert search miss
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertListResult(ListCommand.COMMAND_WORD + " " + TypicalTestTasks.BuyTasks.VERB);
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

    private void assertListResult(String command, List<TestEntry> expectedHits) {
        TestEntry[] array = new TestEntry[expectedHits.size()];
        expectedHits.toArray(array);
        assertListResult(command, array);
    }
}
```
