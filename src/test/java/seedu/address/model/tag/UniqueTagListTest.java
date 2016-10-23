package seedu.address.model.tag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Created by joeleba on 23/10/16.
 */
public class UniqueTagListTest {
    private UniqueTagList uniqueTagList;
    private UniqueTagList otherTagList;
    private Tag singleTag;
    private Tag otherTag;

    @Before
    public void setUp() throws Exception {
        singleTag = new Tag("singleTag");
        otherTag = new Tag("other");
        uniqueTagList = new UniqueTagList(singleTag);
        otherTagList = new UniqueTagList(otherTag);
    }

    @Test(expected = UniqueTagList.DuplicateTagException.class)
    public void constructors() throws Exception {
        Collection<Tag> tagCollectionWithDuplication = Stream.of(singleTag, singleTag).collect(Collectors.toList());
        Set<Tag> tagSet = Stream.of(singleTag, otherTag).collect(Collectors.toSet());

        assertTrue(new UniqueTagList(singleTag) instanceof UniqueTagList);
        assertTrue(new UniqueTagList(tagSet) instanceof UniqueTagList);
        assertTrue(new UniqueTagList(new UniqueTagList(singleTag)) instanceof UniqueTagList);
        new UniqueTagList(tagCollectionWithDuplication);
    }

    @Test
    public void toSet() throws Exception {
        HashSet<Tag> expected = new HashSet<>();
        expected.add(singleTag);
        assertEquals(expected, uniqueTagList.toSet());
    }

    @Test
    public void setTags() throws Exception {
        uniqueTagList.setTags(otherTagList);
        assertEquals(otherTagList, uniqueTagList);
        uniqueTagList = new UniqueTagList(singleTag);
    }

    @Test
    public void mergeFrom() throws Exception {
        uniqueTagList.mergeFrom(otherTagList);
        assertEquals(Stream.of(singleTag, otherTag).collect(Collectors.toSet()), uniqueTagList.toSet());
        uniqueTagList = new UniqueTagList(singleTag);
    }

    @Test
    public void removeFrom() throws Exception {
        uniqueTagList.removeFrom(otherTagList);
        assertEquals(new UniqueTagList(singleTag), uniqueTagList);

        uniqueTagList.removeFrom(new UniqueTagList(singleTag));
        assertEquals(new UniqueTagList(), uniqueTagList);
        uniqueTagList = new UniqueTagList(singleTag);
    }

    @Test
    public void contains() throws Exception {
        assertTrue(uniqueTagList.contains(singleTag));
        assertFalse(uniqueTagList.contains(otherTag));
    }

    @Test
    public void add() throws Exception {
        uniqueTagList.add(otherTag);
        assertEquals(new UniqueTagList(singleTag, otherTag), uniqueTagList);
        uniqueTagList = new UniqueTagList(singleTag);
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue(new UniqueTagList().isEmpty());
    }

    @Test
    public void getInternalList() throws Exception {
        ObservableList<Tag> expected = FXCollections.observableArrayList();
        expected.add(singleTag);

        assertEquals(uniqueTagList.getInternalList(), expected);
    }

}