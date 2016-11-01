package seedu.address.logic.parser;

import seedu.address.logic.commands.*;
import seedu.address.commons.util.StringUtil;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ArgumentTokenizer.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.nio.file.InvalidPathException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.AddCommand.*;
import static seedu.address.logic.commands.ListCommand.*;
import static seedu.address.logic.commands.OptionCommand.SAVE_LOCATION_FLAG;
import static seedu.address.logic.commands.EditCommand.TITLE_FLAG;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern PERSON_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final Pattern MONTH_DATE = Pattern.compile("(?<month>\\d\\d)(?<separator>/|-)(?<day>\\d\\d)(?<theRest>.*)");

    // TODO: Use PrettyTime to parse dates
    private static final Prefix startDatePrefix = new Prefix(AFTER_FLAG);
    private static final Prefix endDatePrefix = new Prefix(BEFORE_FLAG);
    private static final Prefix onDatePrefix = new Prefix(ON_FLAG);
    private static final Prefix startPrefix = new Prefix(START_FLAG);
    private static final Prefix endPrefix = new Prefix(END_FLAG);
    private static final Prefix tagPrefix = new Prefix(TAG_FLAG);
    private static final Prefix descPrefix = new Prefix(DESC_FLAG);
    private static final Prefix titlePrefix = new Prefix(TITLE_FLAG);
    private static final Prefix completedPrefix = new Prefix(COMPLETED_FLAG);
    private static final PrettyTimeParser prettyTimeParser = new PrettyTimeParser();
    private static final DateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final Prefix saveLocationPrefix = new Prefix(SAVE_LOCATION_FLAG);

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

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();
            
        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
        case OptionCommand.COMMAND_WORD:
            return prepareOption(arguments);
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
     * Extracts the new entry's startTime from the add command's tag arguments
     * string.
     */
    private static LocalDateTime getStartTimeFromArgument(ArgumentTokenizer argsTokenizer) throws IllegalValueException {
        return getDateTimeFromArgument(argsTokenizer, startPrefix);
    }

    /**
     * Extracts the new entry's endTime from the add command's tag arguments
     * string.
     */
    private static LocalDateTime getEndTimeFromArgument(ArgumentTokenizer argsTokenizer) throws IllegalValueException {
        return getDateTimeFromArgument(argsTokenizer, endPrefix);
    }

    private static LocalDateTime getDateTimeFromArgument(ArgumentTokenizer argsTokenizer, Prefix prefix) throws IllegalValueException {
        String dateTime = unwrapOptionalStringOrEmpty(argsTokenizer.getValue(prefix));

        if (dateTime.isEmpty()) {
            return null;
        }

        // Since PrettyTime uses the US date format (MM/DD),
        // Swap month-date order if necessary
        final Matcher matcher = MONTH_DATE.matcher(dateTime);
        if (matcher.matches()) {
            dateTime = matcher.group("day") + matcher.group("separator") + matcher.group("month") + matcher.group("theRest");
        }

        List<Date> possibleDates = prettyTimeParser.parse(dateTime);

        if (possibleDates.size() != 1) {
            throw new IllegalValueException(String.format(WRONG_DATE_TIME_INPUT, dateTime));
        }

        Date parsed = possibleDates.get(0);

        String formatted = dateTimeFormat.format(parsed);
        return LocalDateTime.parse(formatted);
    }

    /**
     * Parse LocalDateTime from an input string
     * string.
     *
     * @@author A0127828W
     */
    private static LocalDateTime getLocalDateTimeFromArgument(String dateTimeString) throws IllegalValueException {
        if (dateTimeString.isEmpty()) {
            return null;
        }

        List<Date> possibleDates = prettyTimeParser.parse(dateTimeString);

        if (possibleDates.size() != 1) {
            throw new IllegalValueException(String.format(WRONG_DATE_TIME_INPUT, dateTimeString));
        }

        Date parsed = possibleDates.get(0);

        String formatted = dateFormat.format(parsed);
        formatted = formatted + "T00:00";
        return LocalDateTime.parse(formatted);
    }
    //@@author

    /**
     *
     * @param argsTokenizer
     * @return new save location for PriorityQ
     */
    //@@author A0126539Y
    private static String getSaveLocationFromArgs(ArgumentTokenizer argsTokenizer) {
        return argsTokenizer.getValue(saveLocationPrefix).orElse(null);
    }
    //@@author

    /**
    * Parses arguments in the context of the add task command.
    *
    * @param args
    *            full command args string
    * @return the prepared command
    */
   private Command prepareAdd(String args) {
       ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(startPrefix, endPrefix, tagPrefix, descPrefix);
       argsTokenizer.tokenize(args.trim());
       // Validate arg string format
       String title = unwrapOptionalStringOrEmpty(argsTokenizer.getPreamble());
       if (title.isEmpty()) {
           return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
       }

       try {
           return new AddCommand(title,
                   getStartTimeFromArgument(argsTokenizer),
                   getEndTimeFromArgument(argsTokenizer),
                   getTagsFromArgs(argsTokenizer),
                   getDescriptionFromArgs(argsTokenizer));
       } catch (IllegalValueException|IllegalArgumentException ive) {
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
       ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(titlePrefix, startPrefix, endPrefix, tagPrefix, descPrefix);
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
               getStartTimeFromArgument(argsTokenizer),
               getEndTimeFromArgument(argsTokenizer),
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
            if (tagStrings.isEmpty()) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE)); 
            }
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
            if (tagStrings.isEmpty()) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
            }
            return new UntagCommand(targetIndex.get(), tagStrings);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    
    /**
     *
     * @param args
     *          full command args string
     * @return the prepared command
     */
    private Command prepareOption(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(saveLocationPrefix);
        argsTokenizer.tokenize(args.trim());

        try {
            return new OptionCommand(getSaveLocationFromArgs(argsTokenizer));
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        } catch (InvalidPathException ipe) {
            return new IncorrectCommand(ipe.getMessage());
        }
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
     * @@author A0127828W
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareList(String args) {
        // Guard statement
        if (args.isEmpty()) {
            return new ListCommand(false);
        }

        final ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(startDatePrefix, endDatePrefix, onDatePrefix, tagPrefix, completedPrefix);
        argsTokenizer.tokenize(args);

        try {
            boolean includeCompleted = argsTokenizer.getValue(completedPrefix).isPresent();
            ListCommand listCommand = new ListCommand(includeCompleted);

            // keywords delimited by whitespace
            Optional<String> keywordsString = argsTokenizer.getPreamble();
            if (keywordsString.isPresent()) {
                String[] keywords = keywordsString.get().split("\\s+");

                Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
                keywordSet.removeIf(s -> "".equals(s));
                listCommand.setKeywords(keywordSet);
            }

            String onDateString = unwrapOptionalStringOrEmpty(argsTokenizer.getValue(onDatePrefix));
            String startDateString = unwrapOptionalStringOrEmpty(argsTokenizer.getValue(startDatePrefix));
            String endDateString = unwrapOptionalStringOrEmpty(argsTokenizer.getValue(endDatePrefix));
            Set<String> tags = getTagsFromArgs(argsTokenizer);

            if (!tags.isEmpty()) {
                listCommand.setTags(tags);
            }

            // Ranged search and specific-day search should be mutually exclusive
            if (!onDateString.isEmpty() && (!startDateString.isEmpty() || !endDateString.isEmpty())) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_MUTUALLY_EXCLUSIVE_OPTIONS));
            }

            if (onDateString.isEmpty()) {
                final LocalDateTime startDate = getLocalDateTimeFromArgument(startDateString);
                LocalDateTime endDate = getLocalDateTimeFromArgument(endDateString);
                if (endDate != null) {
                    endDate = endDate.plusDays(1).minusSeconds(1);
                }

                if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
                    return new IncorrectCommand(ListCommand.MESSAGE_INVALID_DATE);
                }

                listCommand.setStartDate(startDate);
                listCommand.setEndDate(endDate);
            } else {
                final LocalDateTime onDate = getLocalDateTimeFromArgument(onDateString);

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
