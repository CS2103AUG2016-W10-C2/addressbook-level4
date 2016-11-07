package seedu.priorityq.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.function.Predicate;

import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.commons.util.StringUtil;
import seedu.priorityq.model.entry.Entry;
import seedu.priorityq.model.entry.Event;
import seedu.priorityq.model.entry.Task;
import seedu.priorityq.model.tag.Tag;

import static seedu.priorityq.model.tag.Tag.MESSAGE_TAG_CONSTRAINTS;

/**
 * Supports chaining of predicates for the `list` command
 * @@author A0127828W
 *
 */
public class PredicateBuilder {
    /**
     * Return a chained Predicate from all the conditions indicated in params
     * @param keywords
     * @param tags
     * @param startDate
     * @param endDate
     * @param onDate
     * @param entryType TODO
     * @return pred the chained Predicate
     * @throws IllegalValueException TODO
     */
    public Predicate<Entry> buildPredicate(Set<String> keywords, Set<String> tags, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime onDate, boolean includeCompleted, String entryType) throws IllegalValueException {
        // Initial predicate
        Predicate<Entry> pred = e -> true;
        pred = addCompletedPredicateIfExist(pred, includeCompleted);
        pred = addKeywordsPredicateIfExist(pred, keywords);
        pred = addTagPredicateIfExist(pred, tags);
        pred = addDatePredicateIfExist(pred, onDate, startDate, endDate);
        pred = addTypePredicateIfExist(pred, entryType);

        return pred;

    }
    
    private Predicate<Entry> addCompletedPredicateIfExist(Predicate<Entry> pred, boolean includeCompleted) {
        Predicate<Entry> result = pred;
        if (!includeCompleted) {
            result = result.and(buildCompletedPredicate(includeCompleted));
        }
        return result;
    }
    
    private Predicate<Entry> addKeywordsPredicateIfExist(Predicate<Entry> pred, Set<String> keywords) {
        Predicate<Entry> result = pred;
        if (keywords != null && !keywords.isEmpty()) {
            result = result.and(buildKeywordsPredicate(keywords));
        }
        return result;
    }
    
    private Predicate<Entry> addTagPredicateIfExist(Predicate<Entry> pred, Set<String> tags) {
        Predicate<Entry> result = pred;
        if (tags != null && !tags.isEmpty()) {
            result = result.and(buildTagsPredicate(tags));
        }
        return result;
    }
    
    private Predicate<Entry> addDatePredicateIfExist(Predicate<Entry> pred, LocalDateTime onDate, LocalDateTime startDate, LocalDateTime endDate) {
        Predicate<Entry> result = pred;
        if (onDate != null) {
            result = result.and(buildOnPredicate(onDate));
        } else {
            if (startDate != null) {
                result = result.and(buildAfterPredicate(startDate));
            }
            if (endDate != null) {
                result = result.and(buildBeforePredicate(endDate));
            }
        }
        return result;
    }
    
    //@@author A0126539Y
    private Predicate<Entry> addTypePredicateIfExist(Predicate<Entry> pred, String entryType) throws IllegalValueException {
        Predicate<Entry> result = pred;
        if (entryType != null && !entryType.isEmpty()) {
            if (!entryType.equalsIgnoreCase(TypeQualifier.EVENT_TYPE_STRING) && !entryType.equalsIgnoreCase(TypeQualifier.TASK_TYPE_STRING)) {
                throw new IllegalValueException(TypeQualifier.INVALID_TYPE_MESSAGE);
            }
            result = result.and(buildTypePredicate(entryType));
        }
        return result;
    }
    //@@author

    private Predicate<Entry> buildKeywordsPredicate(Set<String> keywords) {
        return new PredicateExpression(new TitleQualifier(keywords))::satisfies;
    }

    private Predicate<Entry> buildTagsPredicate(Set<String> tags) {
        return new PredicateExpression(new TagsQualifier(tags))::satisfies;
    }

    private Predicate<Entry> buildBeforePredicate(LocalDateTime endDate) {
        return new PredicateExpression(new DateBeforeQualifier(endDate))::satisfies;
    }

    private Predicate<Entry> buildAfterPredicate(LocalDateTime startDate) {
        return new PredicateExpression(new DateAfterQualifier(startDate))::satisfies;
    }

    private Predicate<Entry> buildOnPredicate(LocalDateTime onDate) {
        return new PredicateExpression(new DateOnQualifier(onDate))::satisfies;
    }
    
    //@@author A0126539Y-reused
    private Predicate<Entry> buildTypePredicate(String entryType) {
        return new PredicateExpression(new TypeQualifier(entryType))::satisfies;
    }
    //@@author

    private Predicate<Entry> buildCompletedPredicate(boolean includeCompleted) {
        return new PredicateExpression(new CompletedQualifier(includeCompleted))::satisfies;
    }

    //@author A0127828W-reused
    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(Entry entry);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(Entry entry) {
            return qualifier.run(entry);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(Entry entry);
        String toString();
    }

    //@author A0127828W
    private class TitleQualifier implements Qualifier {
        private Set<String> titleKeyWords;

        TitleQualifier(Set<String> nameKeyWords) {
            this.titleKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(Entry entry) {
            return titleKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(entry.getTitle().fullTitle, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", titleKeyWords);
        }
    }

    private class TagsQualifier implements Qualifier {
        private Set<String> tags;

        TagsQualifier(Set<String> tags) {
            this.tags = tags;
        }

        @Override
        public boolean run(Entry entry) {
            Set<Tag> tags = new HashSet<>();
            try {
                for (String t: this.tags) {
                    tags.add(new Tag(t));
                }
            } catch (IllegalValueException ive) {
                System.err.println(MESSAGE_TAG_CONSTRAINTS);
                return false;
            }

            return tags.stream()
                .filter(tag -> entry.getTags().contains(tag))
                .findAny()
                .isPresent();
        }

        @Override
        public String toString() {
            return "tags=" + String.join(", ", tags);
        }
    }
    //@@author

    //@@author A0126539Y
    private class DateAfterQualifier implements Qualifier {
        private LocalDateTime startDate;

        DateAfterQualifier(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        @Override
        public boolean run(Entry entry) {
            if (entry instanceof Task) {
                Task task = (Task)entry;
                if (task.getDeadline() == null) {
                    return false;
                }
                return task.getDeadline().compareTo(startDate) >= 0;
            }
            if (entry instanceof Event) {
                Event event = (Event)entry;
                return event.getStartTime().compareTo(startDate) >= 0;
            }

            return false;
        }

        @Override
        public String toString() {
            return "Due after: " + startDate.toString();
        }
    }

    private class DateBeforeQualifier implements Qualifier {
        private LocalDateTime endDate;

        DateBeforeQualifier(LocalDateTime endDate) {
            this.endDate = endDate;
        }

        @Override
        public boolean run(Entry entry) {
            if (entry instanceof Task) {
                Task task = (Task)entry;
                if (task.getDeadline() == null) {
                    return false;
                }
                return task.getDeadline().compareTo(endDate) <= 0;
            }
            if (entry instanceof Event) {
                Event event = (Event)entry;
                return event.getStartTime().compareTo(endDate) <= 0;
            }

            return false;
        }

        @Override
        public String toString() {
            return "Due before: " + endDate.toString();
        }
    }

    //@@author A0127828W
    private class DateOnQualifier implements Qualifier {
        private LocalDateTime onDate;

        DateOnQualifier(LocalDateTime onDate) {
            this.onDate = onDate;
        }

        @Override
        public boolean run(Entry entry) {
            LocalDateTime beginningOfDay = onDate.truncatedTo(ChronoUnit.DAYS);
            LocalDateTime endOfDay = onDate.truncatedTo(ChronoUnit.DAYS).plusDays(1);

            if (entry instanceof Task) {
                Task task = (Task)entry;
                if (task.getDeadline() == null) {
                    return false;
                }
                return task.getDeadline().compareTo(beginningOfDay) >= 0 && task.getDeadline().compareTo(endOfDay) <= 0;
            }

            if (entry instanceof Event) {
                Event event = (Event)entry;
                return event.getStartTime().compareTo(beginningOfDay) >= 0 && event.getEndTime().compareTo(endOfDay) <= 0;
            }

            return false;
        }

        @Override
        public String toString() {
            return "Due on: " + onDate.toString();
        }
    }
    
    //@@author A0126539Y
    private class TypeQualifier implements Qualifier {
        private String type;
        private static final String TASK_TYPE_STRING = "task";
        private static final String EVENT_TYPE_STRING = "event";
        private static final String INVALID_TYPE_MESSAGE = "Invalid type filtering. Please use either 'task' or 'event' as value.";

        TypeQualifier(String type) {
            this.type = type;
        }

        @Override
        public boolean run(Entry entry) {
            switch (type) {
            case TASK_TYPE_STRING:
                return entry instanceof Task;
            case EVENT_TYPE_STRING:
                return entry instanceof Event;
            default:
                return false;
            }
        }

        @Override
        public String toString() {
            return "Type of: " + type;
        }
    }

    //@@author A0127828W
    private class CompletedQualifier implements Qualifier {
        private boolean includeCompleted;

        CompletedQualifier(boolean includeCompleted) {
            this.includeCompleted = includeCompleted;
        }

        @Override
        public boolean run(Entry entry) {
            if (includeCompleted) {
                return true;
            } else {
                return !entry.isMarked();
            }
        }

        @Override
        public String toString() {
            return "Include completed: " + includeCompleted;
        }
    }
}
