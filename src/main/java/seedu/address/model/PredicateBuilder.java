package seedu.address.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Entry;

public class PredicateBuilder {
    public Predicate<Entry> buildPredicate(Set<String> keywords, LocalDateTime startDate, LocalDateTime endDate) {
        // Initial predicate
        Predicate<Entry> pred = e -> true;
        if (keywords != null && !keywords.isEmpty()) {
            pred = pred.and(buildKeywordsPredicate(keywords));
        }
        if (startDate != null) {
            pred = pred.and(buildAfterPredicate(startDate));
        }
        if (endDate != null) {
            pred = pred.and(buildBeforePredicate(endDate));
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
            // Don't include FloatingTasks, which have no deadline
            if (entry.getClass().getSimpleName().equals("FloatingTask")) {
                return false;
            }
             
            // Deadline
            Deadline deadline = (Deadline) entry;
            return deadline.getDeadline().compareTo(startDate) >= 0;
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
            // Don't include FloatingTasks, which have no deadline
            if (entry.getClass().getSimpleName().equals("FloatingTask")) {
                return false;
            }
             
            // Deadline
            Deadline deadline = (Deadline) entry;
            return deadline.getDeadline().compareTo(endDate) <= 0;
        }

        @Override
        public String toString() {
            return "Due before: " + endDate.toString();
        }
    }
}
