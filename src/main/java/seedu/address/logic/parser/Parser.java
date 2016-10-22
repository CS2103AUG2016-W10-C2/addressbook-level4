package seedu.address.logic.parser;

import seedu.address.logic.commands.*;
import seedu.address.commons.util.StringUtil;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ArgumentTokenizer.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.AddCommand.*;
import static seedu.address.logic.commands.EditCommand.TITLE_FLAG;
import static seedu.address.logic.commands.ListCommand.AFTER_FLAG;
import static seedu.address.logic.commands.ListCommand.BEFORE_FLAG;
import static seedu.address.logic.commands.ListCommand.ON_FLAG;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern PERSON_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    // TODO: Use PrettyTime to parse dates
    private static final Prefix startDatePrefix = new Prefix(AFTER_FLAG);
    private static final Prefix endDatePrefix = new Prefix(BEFORE_FLAG);
    private static final Prefix onDatePrefix = new Prefix(ON_FLAG);
    private static final Prefix deadlinePrefix = new Prefix(DEADLINE_FLAG);
    private static final Prefix tagPrefix = new Prefix(TAG_FLAG);
    private static final Prefix descPrefix = new Prefix(DESC_FLAG);
    private static final Prefix titlePrefix = new Prefix(TITLE_FLAG);

    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput
     *            full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case EditCommand.COMMAND_WORD:
            return prepareEdit(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case MarkCommand.COMMAND_WORD:
            return prepareMark(arguments);

        case UnmarkCommand.COMMAND_WORD:
            return prepareUnmark(arguments);

        case TagCommand.COMMAND_WORD:
            return prepareTag(arguments);

        case UntagCommand.COMMAND_WORD:
            return prepareUntag(arguments);

        case ListCommand.COMMAND_WORD:
            return prepareList(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Extracts the new task's tags from the add command's tag arguments
     * string. Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(ArgumentTokenizer argsTokenizer) throws IllegalValueException {
        return unwrapOptionalStringCollectionOrEmpty(argsTokenizer.getAllValues(tagPrefix));
    }

    /**
     * Extracts the description from the add command's description argument
     */
    private static String getDescriptionFromArgs(ArgumentTokenizer argsTokenizer) throws IllegalValueException {
        if (argsTokenizer.hasMultiple(descPrefix)) {
            throw new IllegalValueException("Command should contain only 1 " + DESC_FLAG + " flag");
        }

        return unwrapOptionalStringOrEmpty(argsTokenizer.getValue(descPrefix));
    }

    /**
     * Extracts the title from the command's title argument
     */
    private static String getTitleFromArgs(ArgumentTokenizer argsTokenizer) throws IllegalValueException {
        if (argsTokenizer.hasMultiple(titlePrefix)) {
            throw new IllegalValueException("Command should contain only 1 " + TITLE_FLAG + "flag");
        }

        return unwrapOptionalStringOrEmpty(argsTokenizer.getValue(titlePrefix));
    }

    /**
     * Extracts the new entry's deadline from the add command's tag arguments
     * string. Format: YYYY-MM-DD HH:MM
     */
    private static LocalDateTime getDeadlineFromArgument(ArgumentTokenizer argsTokenizer) throws IllegalValueException {
        String deadline = unwrapOptionalStringOrEmpty(argsTokenizer.getValue(deadlinePrefix));
        
        if (deadline.isEmpty()) {
        	return null;
        }
        
        Matcher matcher = DATE_TIME_FORMAT.matcher(deadline);
        if (!matcher.matches()) {
            throw new IllegalValueException(WRONG_DATE_TIME_INPUT);
        }

        // remove the tag.
        final List<String> cleanedStrings = Arrays.asList(deadline.split(" "));
        return LocalDateTime.parse(cleanedStrings.get(0) + "T" + cleanedStrings.get(1) + ":00");
    }

    /**
     * Parse LocalDateTime from an input string
     * string. Format: YYYY-MM-DD
     */
    private static LocalDateTime getLocalDateTimeFromArgument(String dateTimeString, String time) throws IllegalValueException {
        if (dateTimeString.isEmpty()) {
            return null;
        }

        // remove the tag.
        final String cleanedString = dateTimeString + "T" + time;
        return LocalDateTime.parse(cleanedString);
    }

    /**
    * Parses arguments in the context of the add task command.
    *
    * @param args
    *            full command args string
    * @return the prepared command
    */
   private Command prepareAdd(String args) {
       ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(deadlinePrefix, tagPrefix, descPrefix);
       argsTokenizer.tokenize(args.trim());

       // Validate arg string format
       String title = unwrapOptionalStringOrEmpty(argsTokenizer.getPreamble());
       if (title.isEmpty()) {
           return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
       }

       try {
           return new AddCommand(title,
                   getDeadlineFromArgument(argsTokenizer),
                   getTagsFromArgs(argsTokenizer),
                   getDescriptionFromArgs(argsTokenizer));
       } catch (IllegalValueException ive) {
           return new IncorrectCommand(ive.getMessage());
       }
   }

   /**
    * Parses arguments in the context of the edit task command.
    *
    * @param args
    *            full command args string
    * @return the prepared command
    */
   private Command prepareEdit(String args) {
       ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(titlePrefix, deadlinePrefix, tagPrefix, descPrefix);
       argsTokenizer.tokenize(args.trim());

       // Validate arg string format
       String idString = unwrapOptionalStringOrEmpty(argsTokenizer.getPreamble());
       Optional<Integer> index = parseIndex(idString);
       if (!index.isPresent()) {
           return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
       }

       try {
           return new EditCommand(index.get(),
               getTitleFromArgs(argsTokenizer),
               getTagsFromArgs(argsTokenizer),
               getDescriptionFromArgs(argsTokenizer));
       } catch (IllegalValueException ive) {
           return new IncorrectCommand(ive.getMessage());
       }
   }

    /**
     * Parses arguments in the context of the delete task command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }

    /**
     * Parses arguments in the context of the mark task command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareMark(String args) {
        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        return new MarkCommand(index.get());
    }

    /**
     * Parses arguments in the context of the unmark task command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareUnmark(String args) {
        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        return new UnmarkCommand(index.get());
    }

    /**
     * Parses arguments in the context of the tag task command.
     */
    private Command prepareTag(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(tagPrefix);
        argsTokenizer.tokenize(args.trim());

        // Validate arg string format
        String idString = unwrapOptionalStringOrEmpty(argsTokenizer.getPreamble());
        Optional<Integer> targetIndex = parseIndex(idString);
        if (!targetIndex.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        try {
            Set<String> tagStrings = getTagsFromArgs(argsTokenizer);
            return new TagCommand(targetIndex.get(), tagStrings);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Parses arguments in the context of the untag task command.
     */
    private Command prepareUntag(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(tagPrefix);
        argsTokenizer.tokenize(args.trim());

        // Validate arg string format
        String idString = unwrapOptionalStringOrEmpty(argsTokenizer.getPreamble());
        Optional<Integer> targetIndex = parseIndex(idString);
        if (!targetIndex.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }

        try {
            Set<String> tagStrings = getTagsFromArgs(argsTokenizer);
            return new UntagCommand(targetIndex.get(), tagStrings);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Parses arguments in the context of the select task command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned
     * integer is given as the index. Returns an {@code Optional.empty()}
     * otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = PERSON_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if (!StringUtil.isUnsignedInteger(index)) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    /**
     * Parses arguments in the context of the list task command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareList(String args) {
        // Guard statement
        if (args.isEmpty()) {
            return new ListCommand();
        }

        final ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(startDatePrefix, endDatePrefix, onDatePrefix);
        argsTokenizer.tokenize(args);

        try {
            ListCommand listCommand = new ListCommand();

            // keywords delimited by whitespace
            Optional<String> keywordsString = argsTokenizer.getPreamble();
            if (keywordsString.isPresent()) {
                String[] keywords = keywordsString.get().split("\\s+");

                Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
                keywordSet.removeIf(s -> s.equals(""));
                listCommand.setKeywords(keywordSet);
            }

            String onDateString = unwrapOptionalStringOrEmpty(argsTokenizer.getValue(onDatePrefix));
            String startDateString = unwrapOptionalStringOrEmpty(argsTokenizer.getValue(startDatePrefix));
            String endDateString = unwrapOptionalStringOrEmpty(argsTokenizer.getValue(endDatePrefix));

            // Ranged search and specific-day search should be mutually exclusive
            if (!onDateString.isEmpty() && (!startDateString.isEmpty() || !endDateString.isEmpty())) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_MUTUALLY_EXCLUSIVE_OPTIONS));
            }

            if (onDateString.isEmpty()) {
                final LocalDateTime startDate = getLocalDateTimeFromArgument(startDateString, "00:00:00");
                final LocalDateTime endDate = getLocalDateTimeFromArgument(endDateString, "23:59:59");

                if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                    return new IncorrectCommand(ListCommand.MESSAGE_INVALID_DATE);
                }

                listCommand.setStartDate(startDate);
                listCommand.setEndDate(endDate);
            } else {
                final LocalDateTime onDate = getLocalDateTimeFromArgument(onDateString, "23:59:59");

                listCommand.setOnDate(onDate);
            }

            return listCommand;
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    private static String unwrapOptionalStringOrEmpty(Optional<String> optional) {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return "";
        }
    }

    private static Set<String> unwrapOptionalStringCollectionOrEmpty(Optional<List<String>> optional) {
        if (optional.isPresent()) {
            return new HashSet<String>(optional.get());
        } else {
            return Collections.emptySet();
        }
    }

}
