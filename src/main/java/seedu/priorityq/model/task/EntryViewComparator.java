package seedu.priorityq.model.task;

import java.time.LocalDateTime;
import java.util.Comparator;

//@@author A0121501E
/**
 * Comparator for the list view.
 */
public class EntryViewComparator implements Comparator<Entry>{
    @Override
    public int compare(Entry o1, Entry o2) {
        LocalDateTime thisDateTime = o1.getComparableTime();
        LocalDateTime oDateTime = o2.getComparableTime();
        int compareValue = thisDateTime.compareTo(oDateTime);
        if (compareValue==0) {
            return o1.getTitle().toString().compareTo(o2.getTitle().toString());
        }
        return thisDateTime.compareTo(oDateTime);
    }

}
