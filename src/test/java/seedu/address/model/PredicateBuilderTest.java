package seedu.address.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.Entry;
import seedu.address.testutil.EntryBuilder;
import seedu.address.testutil.TestEntry;

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

    @Before
    public void setup() {
        try {
            testEntryWithoutDeadline = new EntryBuilder().withTitle("banana").build();
        } catch (IllegalValueException ive) {
            System.err.println(ive);
        }
    }

    @Test
    public void buildPredicate() throws Exception {
        Set<String> keywords = new HashSet<>(Arrays.asList("banana"));
        Set<String> emptyKeywords = new HashSet<>(Arrays.asList(""));
        LocalDateTime date = LocalDateTime.now();

        // TODO: Add Task builder
        assertPredicate(keywords, null, null, null, true);
        assertPredicate(emptyKeywords, null, null, null, false);
        assertPredicate(keywords, date, null, null, false);
        assertPredicate(keywords, null, date, null, false);
        assertPredicate(keywords, null, null, date, false);
    }

    private void assertPredicate(Set<String> keywords, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime onDate, boolean expected) {
        PredicateBuilder predicateBuilder = new PredicateBuilder();
        Predicate<Entry> pred = predicateBuilder.buildPredicate(keywords, startDate, endDate, onDate);
        assertEquals(pred.test(testEntryWithoutDeadline), expected);
    }

}