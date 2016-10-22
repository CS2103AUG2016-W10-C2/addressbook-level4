package seedu.address.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.task.Entry;
import seedu.address.model.task.Event;
import seedu.address.model.task.Task;

/**
 * Supports chaining of predicates for the `list` command
 * @@author A0127828W
 *
 */
public class PredicateBuilder {
    /**
     * Return a chained Predicate from all the conditions indicated in params
     * @param keywords
     * @param startDate
     * @param endDate
     * @param onDate
     * @return pred the chained Predicate
     */
    public Predicate<Entry> buildPredicate(Set<String> keywords, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime onDate) {
        // Initial predicate
        Predicate<Entry> pred = e -> true;
        if (keywords != null && !keywords.isEmpty()) {
            pred = pred.and(buildKeywordsPredicate(keywords));
        }

        if (onDate != null) {
            pred = pred.and(buildOnPredicate(onDate));
        } else {
            if (startDate != null) {
                pred = pred.and(buildAfterPredicate(startDate));
            }
            if (endDate != null) {
                pred = pred.and(buildBeforePredicate(endDate));
            }
        }

        return pred;

    }

    private Predicate<Entry> buildKeywordsPredicate(Set<String> keywords) {
        return new PredicateExpression(new NameQualifier(keywords))::satisfies;
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
    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(Entry person);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(Entry person) {
            return qualifier.run(person);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(Entry person);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(Entry entry) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(entry.getTitle().fullTitle, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

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
    //@@author
}
