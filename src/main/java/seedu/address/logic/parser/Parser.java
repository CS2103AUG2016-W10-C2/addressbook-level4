package seedu.address.logic.parser;

import seedu.address.logic.commands.*;
import seedu.address.commons.util.StringUtil;
import seedu.address.commons.exceptions.IllegalValueException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern PERSON_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern FLOATING_TASK_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<title>[^/]+)"
            		+ "(?<tagArguments>(?: t/[^/]+)?)" // comma separated tags;
                    + "(?<desc>(?: desc/[^/]*)?)");

    private static final Pattern DEADLINE_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<title>[^/]+)"
            		+ "(?<deadlineArguments>(?: dl/\\d{4}-\\d{1,2}-\\d{1,2} \\d{2}:\\d{2}))" // Date time format: DD/MM/YYYY/HH:MM
            		+ "(?<tagArguments>(?: t/[^/]+)?)" // comma separated tags; 
            		+ "(?<desc>(?: desc/[^/]*)?)");
    
    private static final Pattern EDIT_TASK_ARGS_FORMAT = Pattern
            .compile("(?<targetIndex>\\d+)\\s*(?<title>[\\s\\w\\d]*)"
                    + "(?<tagArguments>(?: t/[^/]+)*)"
                    + "(?<desc>(?: desc/[^/]*)?)");

    private static final Pattern TAG_ARGS_FORMAT = Pattern
            .compile("(?<targetIndex>\\d+)\\s*(?<tagArguments>(?:[^/]*))");
    
    private static final Pattern UNTAG_ARGS_FORMAT = Pattern
            .compile("(?<targetIndex>\\d+)\\s*(?<tagArguments>(?:[^/]*))");

    public Parser() {
    }

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
     * Parses arguments in the context of the add task command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args) {
    	args = args.trim();
        final Matcher deadlineMatcher = DEADLINE_DATA_ARGS_FORMAT.matcher(args);
        final Matcher floatingTaskMatcher = FLOATING_TASK_DATA_ARGS_FORMAT.matcher(args);
        // Validate arg string format
        if (!floatingTaskMatcher.matches() && !deadlineMatcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        if (deadlineMatcher.matches()) {
        	try {
        		return new AddCommand(deadlineMatcher.group("title"), getDeadlineFromArgument(deadlineMatcher.group("deadlineArguments")), getTagsFromArgs(deadlineMatcher.group("tagArguments")), getDescriptionFromArgs(deadlineMatcher.group("desc")));
        	} catch (IllegalValueException ive) {
                return new IncorrectCommand(ive.getMessage());
            }
        	
        } else {
        	try {
                return new AddCommand(floatingTaskMatcher.group("title"), getTagsFromArgs(floatingTaskMatcher.group("tagArguments")), getDescriptionFromArgs(floatingTaskMatcher.group("desc")));
            } catch (IllegalValueException ive) {
                return new IncorrectCommand(ive.getMessage());
            }
        }
    }

    /**
     * Extracts the new task's tags from the add command's tag arguments
     * string. Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(",\\s?"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Extracts the description from the add command's description argument
     */
    private static String getDescriptionFromArgs(String descriptionArguments) throws IllegalValueException {
        if (descriptionArguments == null || descriptionArguments.isEmpty()) {
            return "";
        }
        String[] descriptionStrings = descriptionArguments.split("/");
        if (descriptionStrings.length != 2) {
            throw new IllegalValueException("Command should contain only 1 'desc/' flag");
        }
        return descriptionStrings[1];
    }

    /**
     * Parses arguments in the context of the edit task command.
     * 
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareEdit(String args) {
        final Matcher matcher = EDIT_TASK_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        } else {
            Optional<Integer> index = parseIndex(matcher.group("targetIndex"));
            if (!index.isPresent()) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
            }
            try {
                return new EditCommand(index.get(), matcher.group("title"),
                        getTagsFromArgs(matcher.group("tagArguments")), getDescriptionFromArgs(matcher.group("desc")));
            } catch (IllegalValueException ive) {
                return new IncorrectCommand(ive.getMessage());
            }
        }
    }
    
    /**
     * Extracts the new entry's deadline from the add command's tag arguments
     * string. Format: YYYY-MM-DD HH:MM
     */
    private static LocalDateTime getDeadlineFromArgument(String deadlineArguments) throws IllegalValueException {
        if (deadlineArguments.isEmpty()) {
            return LocalDateTime.now();
        }
        // remove the tag.
        final List<String> cleanedStrings = Arrays.asList(deadlineArguments.replaceFirst(" dl/", "").split(" "));
        return LocalDateTime.parse(cleanedStrings.get(0) + "T" + cleanedStrings.get(1) + ":00");
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
        final Matcher matcher = TAG_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }
        
        Optional<Integer> targetIndex = parseIndex(matcher.group("targetIndex"));
        if (!targetIndex.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }
        String tagArg = matcher.group("tagArguments");
        Collection<String> tagStrings = Collections.emptySet();
        if (!tagArg.isEmpty()) {
            tagStrings = Arrays.asList(tagArg.split(",\\s?"));
        }
        try {
            return new TagCommand(targetIndex.get() ,new HashSet<>(tagStrings));
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    
    /**
     * Parses arguments in the context of the untag task command.
     */
    private Command prepareUntag(String args) {
        final Matcher matcher = UNTAG_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }
        
        Optional<Integer> targetIndex = parseIndex(matcher.group("targetIndex"));
        if (!targetIndex.isPresent()) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagCommand.MESSAGE_USAGE));
        }
        String tagArg = matcher.group("tagArguments");
        Collection<String> tagStrings = Collections.emptySet();
        if (!tagArg.isEmpty()) {
            tagStrings = Arrays.asList(tagArg.split(",\\s?"));
        }
        try {
            return new UntagCommand(targetIndex.get() ,new HashSet<>(tagStrings));
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
            return new ListCommand(new HashSet<>());
        }
        
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new ListCommand(keywordSet);
    }

}