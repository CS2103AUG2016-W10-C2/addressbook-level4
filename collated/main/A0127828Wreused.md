# A0127828Wreused
###### /java/seedu/address/logic/parser/ArgumentTokenizer.java
``` java
 */

/**
 * Tokenizes arguments string of the form: {@code preamble <prefix>value <prefix>value ...}<br>
 *     e.g. {@code some preamble text /t 11.00/dToday /t 12.00 /k /m July}  where prefixes are {@code /t /d /k /m}.<br>
 * 1. An argument's value can be an empty string e.g. the value of {@code /k} in the above example.<br>
 * 2. Leading and trailing whitespaces of an argument value will be discarded.<br>
 * 3. A prefix need not have leading and trailing spaces e.g. the {@code /d in 11.00/dToday} in the above example<br>
 * 4. An argument may be repeated and all its values will be accumulated e.g. the value of {@code /t}
 *    in the above example.<br>
 */
public class ArgumentTokenizer {
    /** Given prefixes **/
    private final List<Prefix> prefixes;

    /** Arguments found after tokenizing **/
    private final Map<Prefix, List<String>> tokenizedArguments = new HashMap<>();

    /**
     * Creates an ArgumentTokenizer that can tokenize arguments string as described by prefixes
     */
    public ArgumentTokenizer(Prefix... prefixes) {
        this.prefixes = Arrays.asList(prefixes);
    }

    /**
     * @param argsString arguments string of the form: preamble <prefix>value <prefix>value ...
     */
    public void tokenize(String argsString) {
        resetTokenizerState();
        List<PrefixPosition> positions = findAllPrefixPositions(argsString);
        extractArguments(argsString, positions);
    }

    /**
     * Returns last value of given prefix.
     */
    public Optional<String> getValue(Prefix prefix) {
        return getAllValues(prefix).flatMap((values) -> Optional.of(values.get(values.size() - 1)));
    }

    /**
     * Returns all values of given prefix.
     */
    public Optional<List<String>> getAllValues(Prefix prefix) {
        if (!this.tokenizedArguments.containsKey(prefix)) {
            return Optional.empty();
        }
        List<String> values = new ArrayList<>(this.tokenizedArguments.get(prefix));
        return Optional.of(values);
    }

    /**
     * Returns the preamble (text before the first valid prefix), if any. Leading/trailing spaces will be trimmed.
     *     If the string before the first prefix is empty, Optional.empty() will be returned.
     */
    public Optional<String> getPreamble() {

        Optional<String> storedPreamble = getValue(new Prefix(""));

        /* An empty preamble is considered 'no preamble present' */
        if (storedPreamble.isPresent() && !storedPreamble.get().isEmpty()) {
            return storedPreamble;
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns true if the prefix appeared more than once in the arguments
```
