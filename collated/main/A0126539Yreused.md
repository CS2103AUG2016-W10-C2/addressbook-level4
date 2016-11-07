# A0126539Yreused
###### /java/seedu/priorityq/model/PredicateBuilder.java
``` java
    private Predicate<Entry> buildTypePredicate(String entryType) {
        return new PredicateExpression(new TypeQualifier(entryType))::satisfies;
    }
```
