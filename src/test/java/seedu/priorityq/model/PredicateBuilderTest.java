package seedu.priorityq.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.tag.UniqueTagList;
import seedu.priorityq.model.task.Entry;
import seedu.priorityq.model.task.Task;
import seedu.priorityq.model.task.Title;
import seedu.priorityq.testutil.EntryBuilder;
import seedu.priorityq.testutil.TestEntry;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by joeleba on 22/10/16.
 */
public class PredicateBuilderTest {
    private TestEntry testEntryWithoutDeadline;
    private TestEntry testMarkedEntryWithoutDeadline;
    private Entry testEntryWithDeadline;

    @Before
    public void setup() {
        try {
            testEntryWithoutDeadline = new EntryBuilder().withTitle("banana").withTags("groceries").build();

            testMarkedEntryWithoutDeadline = new EntryBuilder().withTitle("banana").withTags("groceries").build();
            testMarkedEntryWithoutDeadline.mark();

            testEntryWithDeadline = new Task(new Title("apple"), LocalDateTime.parse("2016-10-10T10:00:00"), new UniqueTagList(), 
                                             true, "", LocalDateTime.parse("2016-10-10T10:00:00") );
        } catch (IllegalValueException ive) {
            System.err.println(ive);
        }
    }

    @Test
    public void buildPredicate() throws Exception {
        Set<String> keywords = new HashSet<>(Arrays.asList("banana", "apple"));
        Set<String> emptyKeywords = new HashSet<>(Arrays.asList(""));
        Set<String> tags = new HashSet<>(Arrays.asList("groceries"));
        LocalDateTime earlierDate = LocalDateTime.parse("2016-10-01T10:00:00");
        LocalDateTime laterDate = LocalDateTime.parse("2016-10-20T10:00:00");
        LocalDateTime sameDate = LocalDateTime.parse("2016-10-10T10:00:00");

        assertPredicate(testEntryWithoutDeadline, null, null, null, null, null, true, "", true);
        assertPredicate(testEntryWithoutDeadline, keywords, null, null, null, null, true, "", true);
        assertPredicate(testEntryWithoutDeadline, emptyKeywords, null, null, null, null, true, "", false);

        assertPredicate(testEntryWithoutDeadline, null, tags, null, null, null, true, "", true);
        assertPredicate(testEntryWithoutDeadline, keywords, tags, null, null, null, true, "", true);
        assertPredicate(testEntryWithoutDeadline, emptyKeywords, tags, null, null, null, true, "", false);

        assertPredicate(testEntryWithoutDeadline, keywords, tags, earlierDate, null, null, true, "", false);
        assertPredicate(testEntryWithoutDeadline, keywords, tags, null, earlierDate, null, true, "", false);
        assertPredicate(testEntryWithoutDeadline, keywords, tags, null, null, earlierDate, true, "", false);

        assertPredicate(testEntryWithDeadline, null, null, null, null, null, true, "", true);
        assertPredicate(testEntryWithDeadline, keywords, null, null, earlierDate, null, true, "", false);
        assertPredicate(testEntryWithDeadline, keywords, null, laterDate, null, null, true, "", false);
        assertPredicate(testEntryWithDeadline, keywords, null, null, null, sameDate, true, "", true);
        assertPredicate(testEntryWithDeadline, keywords, null, earlierDate, laterDate, null, true, "", true);

        assertPredicate(testMarkedEntryWithoutDeadline, null, null, null, null, null, true, "", true);
        assertPredicate(testMarkedEntryWithoutDeadline, keywords, null, null, null, null, true, "", true);
        assertPredicate(testMarkedEntryWithoutDeadline, keywords, null, null, null, null, false, "", false);
    }

    private void assertPredicate(Entry entry, Set<String> keywords, Set<String> tags, LocalDateTime startDate,
                                 LocalDateTime endDate, LocalDateTime onDate, boolean includeCompleted, String entryType, boolean expected) {
        PredicateBuilder predicateBuilder = PredicateBuilder.getInstance();
        try {
            Predicate<Entry> pred = predicateBuilder.buildPredicate(keywords, tags, startDate, endDate, onDate, includeCompleted, entryType);
            assertEquals(expected, pred.test(entry));
        }
        catch(Exception e) {
            fail("Failed creating predicate");
        }
    }

}