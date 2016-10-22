package seedu.address.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Entry;
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
    
    private class DateAfterQualifier implements Qualifier {
        private LocalDateTime startDate;

        DateAfterQualifier(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        // TODO: Change this when we introduce Events
        @Override
        public boolean run(Entry entry) {
            String entryClass = entry.getClass().getSimpleName();

            switch(entryClass) {
                case "Task":
                    Task task = (Task) entry;
                    if (task.getDeadline() == null) {
                        return false;
                    } else {
                        return task.getDeadline().compareTo(startDate) >= 0;
                    }

                default:
                    return false;
            }
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

        // TODO: Change this when we introduce Events
        @Override
        public boolean run(Entry entry) {
            String entryClass = entry.getClass().getSimpleName();

            switch(entryClass) {
                case "Task":
                    Task task = (Task) entry;
                    if (task.getDeadline() == null) {
                        return false;
                    } else {
                        return task.getDeadline().compareTo(endDate) <= 0;
                    }

                default:
                    return false;
            }
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

        // TODO: Change this when we introduce Events
        @Override
        public boolean run(Entry entry) {
            String entryClass = entry.getClass().getSimpleName();

            switch(entryClass) {
                case "Task":
                    Task task = (Task) entry;
                    if (task.getDeadline() == null) {
                        return false;
                    } else {
                        LocalDateTime beginningOfDay = onDate.minusDays(1).plusSeconds(1);

                        return (task.getDeadline().compareTo(onDate) <= 0)
                            && (task.getDeadline().compareTo(beginningOfDay) >= 0);
                    }

                default:
                    return false;
            }
        }

        @Override
        public String toString() {
            return "Due on: " + onDate.toString();
        }
    }
}
