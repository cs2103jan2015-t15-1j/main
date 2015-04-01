import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
	//==========Constants for Translator class Section Beginning==========//
	// Keywords for ADD command
	private static final String KEYWORD_ADD_DEADLINE = "((by)|(BY)|(due)|(DUE))";
	private static final String KEYWORD_ADD_EVENT_ONEHOUR = "((at)|(AT)|(@))";
	private static final String KEYWORD_ADD_EVENTSTART = "((from)|(FROM)|(start)|(START))";
	private static final String KEYWORD_ADD_EVENTEND = "((until)|(UNTIL)|(end)|(END))";
	private static final String[] addParameterKeywords = 
		{KEYWORD_ADD_DEADLINE, KEYWORD_ADD_EVENT_ONEHOUR, KEYWORD_ADD_EVENTSTART, KEYWORD_ADD_EVENTEND};
	
	// Keywords for SEARCH command
	private static final String KEYWORD_SEARCH_DUE = "((due)|(DUE))";
	private static final String KEYWORD_SEARCH_AFTER = "((after)|(AFTER))";
	private static final String KEYWORD_SEARCH_BEFORE = "((before)|(BEFORE))";
	private static final String KEYWORD_SEARCH_ON = "((on)|(ON))";
	private static final String[] searchParameterKeywords = 
		{KEYWORD_SEARCH_DUE, KEYWORD_SEARCH_AFTER, KEYWORD_SEARCH_BEFORE, KEYWORD_SEARCH_ON};
	
	// Keywords for EDIT command
	private static final String KEYWORD_EDIT_DEADLINE = "((deadline)|(DEADLINE))";
	private static final String KEYWORD_EDIT_LOCATION = "((location)|(LOCATION))";
	private static final String KEYWORD_EDIT_EVENTSTART = "((start)|(START))";
	private static final String KEYWORD_EDIT_EVENTEND = "((end)|(END))";
	private static final String KEYWORD_EDIT_DESCRIPTION = "((desc)|(DESC)|(description)|(DESCRIPTION))";
	private static final String[] editParameterKeywords = 
		{KEYWORD_EDIT_DEADLINE, KEYWORD_EDIT_LOCATION, KEYWORD_EDIT_EVENTSTART, KEYWORD_EDIT_EVENTEND,
		KEYWORD_EDIT_DESCRIPTION};
	
	
	// Format for Date-Time input.
	private static final String DELIMITTER_DATE = "(\\s|-|/)";
	private static final String DD_MM_YYYY = "\\d\\d" + DELIMITTER_DATE + "\\d\\d" +
			DELIMITTER_DATE + "\\d\\d\\d\\d";
	private static final String DELIMITTER_TIME = "(:)";
	private static final String HH_MM = "(0|1|2)\\d" + DELIMITTER_TIME + "([0-5])\\d";
	
	
	// Default values for Date-Time variables.
	private static final String FILLER_DEFAULT_TIME = " 12:00 ";
	private static final int DATETIME_HOUR_MINIMUM = 0;
	private static final int DATETIME_MINUTE_MINIMUM = 0;
	private static final int DATETIME_HOUR_MAXIMUM = 23;
	private static final int DATETIME_MINUTE_MAXIMUM = 59;
	
	// Miscellaneous default values.
	private static final String EMPTY_STRING = "";
	private static final String SINGLE_SPACE = " ";
	private static final String WHITESPACE = "\\s+";
	private static final int ARRAY_POSITION_FIRST = 0;
	private static final int ARRAY_POSITION_SECOND = 1;
	private static final int NUM_TASKID_MINIMUM = 1;
	
	// Error values.
	private static final String INVALID_COMMAND = "%1$s is not a valid command!";
	private static final String PARAMETER_DOES_NOT_EXIST = null;
	private static final int INT_PARAM_INVALID = -1;
	private static final int INVALID_TASK_ID = -1;
	//==========Constants for Translator class Section End================//
	
	private Storage taskStorage;
	private History commandHistory;
	
	public Translator(Storage storage, History history) {
		taskStorage = storage;
		commandHistory = history;
	}
	
	public void setStorage(Storage storage) {
		// any guard?
		taskStorage = storage;
	}
	
	public void setHistory(History history) {
		// any guard?
		commandHistory = history;
	}
	
	public Command createCommand(String usercommand) throws Exception {
		if (usercommand == null) {
			throw new NullPointerException();
		}
		
		Command newCommand = null;
		String inputCommandTypeString = extractFirstWord(usercommand);
		CommandType inputCommandType = determineCommandType(inputCommandTypeString);
		
		switch (inputCommandType) {
		case ADD :
			newCommand = createAddCommand(usercommand);
			break;
		case DISPLAY :
			newCommand = createDisplayCommand();
			break;
		case DONE :
			newCommand = createDoneCommand(usercommand);
			break;
		case DELETE :
			newCommand = createDeleteCommand(usercommand);
			break;
		case EDIT :
			newCommand = createEditCommand(usercommand);
			break;
		case UNDO :
			newCommand = createUndoCommand();
			break;
		case REDO :
			newCommand = createRedoCommand();
			break;
		case HELP :
			newCommand = createHelpCommand();
			break;
		case SETDIR :
			newCommand = createSetDirectoryCommand(usercommand);
			break;
		case EXIT :
			newCommand = createExitCommand();
			break;
		case SEARCH :
			newCommand = createSearchCommand(usercommand);
			break;
		case CLEAR :
			newCommand = createClearCommand();
			break;
		default :
			throw new Exception (String.format(INVALID_COMMAND, inputCommandTypeString));
		}
		
		return newCommand;
	}
	
	private CommandType determineCommandType(String commandTypeString) {
		if (commandTypeString == null) {
			return CommandType.INVALID;
		} else if (commandTypeString.equalsIgnoreCase("ADD") ||
				commandTypeString.equalsIgnoreCase("a")) {
			return CommandType.ADD;
		} else if (commandTypeString.equalsIgnoreCase("DISPLAY") ||
				commandTypeString.equalsIgnoreCase("show") ||
				commandTypeString.equalsIgnoreCase("dis") || 
				commandTypeString.equalsIgnoreCase("ls")) {
			return CommandType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("SEARCH") ||
				commandTypeString.equalsIgnoreCase("s")) {
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("EDIT") ||
				commandTypeString.equalsIgnoreCase("e")) {
			return CommandType.EDIT;
		} else if (commandTypeString.equalsIgnoreCase("DELETE") ||
				commandTypeString.equalsIgnoreCase("remove") ||
				commandTypeString.equalsIgnoreCase("rm") ||
				commandTypeString.equalsIgnoreCase("del")) {
			return CommandType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("CLEAR")) {
            return CommandType.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("DONE") ||
				commandTypeString.equalsIgnoreCase("do")) {
			return CommandType.DONE;
		} else if (commandTypeString.equalsIgnoreCase("UNDO") ||
				commandTypeString.equalsIgnoreCase("u")) {
			return CommandType.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("REDO") ||
				commandTypeString.equalsIgnoreCase("r")) {
			return CommandType.REDO;
		} else if (commandTypeString.equalsIgnoreCase("HELP")) {
			return CommandType.HELP;
		} else if (commandTypeString.equalsIgnoreCase("SETDIR") ||
				commandTypeString.equalsIgnoreCase("SETDIRECTORY")) {
			return CommandType.SETDIR;
		} else if (commandTypeString.equalsIgnoreCase("EXIT")) {
			return CommandType.EXIT;
		} else {
			return CommandType.INVALID;
		}
	}
	
	private Command createAddCommand(String usercommand) {
		Task[] addInformation = {interpretAddParameter(usercommand)};
		return new AddCommand(addInformation);
	}

	private Command createDisplayCommand() {
		return new DisplayCommand();
	}
	
	private Command createSearchCommand(String usercommand) {
		Task searchInformation = interpretSearchParameter(usercommand);
		return new SearchCommand(searchInformation);
	}
	
	private Command createEditCommand(String usercommand) {
		Task editInformation = interpretEditParameter(usercommand);
		return new EditCommand(editInformation);
	}
	
	private Command createDeleteCommand(String usercommand) {
		int[] deleteInformation = interpretDeleteParameter(usercommand);
		return new DeleteCommand(deleteInformation);
	}
	
	private Command createClearCommand() {
		return new ClearCommand();
	}
	
	private Command createDoneCommand(String usercommand) {
		int[] doneInformation = interpretDoneParameter(usercommand);
		return new DoneCommand(doneInformation, true);
	}
	
	private Command createUndoCommand() {
		return new UndoCommand();
	}
	
	private Command createRedoCommand() {
		return new RedoCommand();
	}

	private Command createHelpCommand() {
		return new HelpCommand();
	}
	
	private Command createSetDirectoryCommand(String usercommand) {
		Path setDirInformation = interpretFilePath(usercommand);
		return new SetDirectoryCommand(setDirInformation);
	}
	
	private Command createExitCommand() {
		return new ExitCommand();
	}
	
	private Task interpretAddParameter(String usercommand) {
		
		Task newTask = new Task();
		KeywordInfoList kList = new KeywordInfoList(usercommand, addParameterKeywords);

		String paramDescription = kList.getDescription();
		String paramEventOneHour = kList.getParameter(KEYWORD_ADD_EVENT_ONEHOUR);
		String paramDeadline = kList.getParameter(KEYWORD_ADD_DEADLINE);
		String paramEventStart = kList.getParameter(KEYWORD_ADD_EVENTSTART);
		String paramEventEnd = kList.getParameter(KEYWORD_ADD_EVENTEND);

		if (paramDescription != EMPTY_STRING) {
			newTask.setDescription(paramDescription);

			/*
			if (paramEventOneHour != PARAMETER_DOES_NOT_EXIST) {
				LocalDateTime eventOneHourStart = interpretDateTimeParam(paramEventOneHour);
				if (eventOneHourStart.)
				newTask.setLocation(paramEventOneHour);
			}
			*/

			if (paramDeadline != PARAMETER_DOES_NOT_EXIST) {
				// The task type is "deadline"
				LocalDateTime deadline = interpretDateTimeParam(paramDeadline);
				newTask.setEndDateTime(deadline);

			} else if (paramEventStart != PARAMETER_DOES_NOT_EXIST && paramEventEnd != PARAMETER_DOES_NOT_EXIST) {
				// The task type is "event"
				LocalDateTime eventStart = interpretDateTimeParam(paramEventStart);
				LocalDateTime eventEnd = interpretDateTimeParam(paramEventEnd);
				newTask.setStartDateTime(eventStart);
				newTask.setEndDateTime(eventEnd);

			} else {
				// The task type is "floating." No further action necessary.
			}
		}

		return newTask;
	}
	
	private Task interpretEditParameter(String usercommand) {
		
		Task newTask = new Task();
		KeywordInfoList kList = new KeywordInfoList(usercommand, editParameterKeywords);

		int taskID = extractEditTaskID(usercommand);
		if (taskID != INVALID_TASK_ID) {

			newTask.setId(taskID);
			boolean doesEditParameterExist = false;

			String paramDescription = kList.getParameter(KEYWORD_EDIT_DESCRIPTION);
			String paramLocation = kList.getParameter(KEYWORD_EDIT_LOCATION);
			String paramDeadline = kList.getParameter(KEYWORD_EDIT_DEADLINE);
			String paramEventStart = kList.getParameter(KEYWORD_EDIT_EVENTSTART);
			String paramEventEnd = kList.getParameter(KEYWORD_EDIT_EVENTEND);

			if (paramDescription != PARAMETER_DOES_NOT_EXIST) {
				newTask.setDescription(paramDescription);
				doesEditParameterExist = true;
			}
			
			if (paramLocation != PARAMETER_DOES_NOT_EXIST) {
				newTask.setLocation(paramLocation);
				doesEditParameterExist = true;
			}
			
			if (paramDeadline != PARAMETER_DOES_NOT_EXIST) {
				LocalDateTime deadline = interpretDateTimeParam(paramDeadline);
				newTask.setEndDateTime(deadline);
				doesEditParameterExist = true;
				
			} else {
				if (paramEventStart != PARAMETER_DOES_NOT_EXIST) {
					LocalDateTime eventStart = interpretDateTimeParam(paramEventStart);
					newTask.setStartDateTime(eventStart);
					doesEditParameterExist = true;
				}
				
				if (paramEventEnd != PARAMETER_DOES_NOT_EXIST) {
					LocalDateTime eventEnd = interpretDateTimeParam(paramEventEnd);
					newTask.setEndDateTime(eventEnd);
					doesEditParameterExist = true;
				}
			}
			
			if (doesEditParameterExist == true) {
				return newTask;
			}
		}
		
		return null;
	}
	
	private int extractEditTaskID(String usercommand) {
		String taskIDString = extractSecondWord(usercommand);
		int taskID;
		try {
			taskID = Integer.parseInt(taskIDString);
			if (taskID < NUM_TASKID_MINIMUM) {
				taskID = INVALID_TASK_ID;
			}
		} catch (NumberFormatException e) {
			taskID = INVALID_TASK_ID;
		}
		return taskID;
	}
	
	private Task interpretSearchParameter(String usercommand) {
		
		Task newTask = new Task();
		KeywordInfoList kList = new KeywordInfoList(usercommand, searchParameterKeywords);
		
		String paramDescription = kList.getDescription();
		String paramAfter = kList.getParameter(KEYWORD_SEARCH_AFTER);
		String paramBefore = kList.getParameter(KEYWORD_SEARCH_BEFORE);
		String paramOn = kList.getParameter(KEYWORD_SEARCH_ON);
		
		if (paramDescription == PARAMETER_DOES_NOT_EXIST) {
			newTask.setDescription(EMPTY_STRING);
			
		} else {
			newTask.setDescription(paramDescription);
		}
		
		if (paramOn == PARAMETER_DOES_NOT_EXIST) {
			if (paramAfter == PARAMETER_DOES_NOT_EXIST) {
				newTask.setStartDateTime(LocalDateTime.MIN);
				
			} else {
				LocalDateTime lowerBoundaryTime = interpretDateTimeParam(paramAfter);
				newTask.setStartDateTime(lowerBoundaryTime);
			}
			
			if (paramBefore == PARAMETER_DOES_NOT_EXIST) {
				newTask.setEndDateTime(LocalDateTime.MAX);
				
			} else {
				LocalDateTime upperBoundaryTime = interpretDateTimeParam(paramBefore);
				newTask.setEndDateTime(upperBoundaryTime);
			}
			
		} else {
			paramOn += FILLER_DEFAULT_TIME;
			LocalDateTime dayToSearch = interpretDateTimeParam(paramOn);
			newTask.setStartDateTime(getBeginningOfDay(dayToSearch));
			newTask.setEndDateTime(getEndOfDay(dayToSearch));
		}
		
		return newTask;
	}

	
	private LocalDateTime getBeginningOfDay(LocalDateTime dateTime) {
		LocalDateTime minuteSetToMinimum = dateTime.withMinute(DATETIME_MINUTE_MINIMUM);
		LocalDateTime hourAndMinuteSetToMinimum = minuteSetToMinimum.withHour(DATETIME_HOUR_MINIMUM);
		return hourAndMinuteSetToMinimum;
	}
	
	private LocalDateTime getEndOfDay(LocalDateTime dateTime) {
		LocalDateTime minuteSetToMaximum = dateTime.withMinute(DATETIME_MINUTE_MAXIMUM);
		LocalDateTime hourAndMinuteSetToMaximum = minuteSetToMaximum.withHour(DATETIME_HOUR_MAXIMUM);
		return hourAndMinuteSetToMaximum;
	}
	
	private int[] interpretDeleteParameter(String usercommand) {
		String parameter = removeFirstWord(usercommand);
		return interpretTaskIDs(parameter);
	}
	
	private int[] interpretDoneParameter(String usercommand) {
		String parameter = removeFirstWord(usercommand);
		return interpretTaskIDs(parameter);
	}
	
	private LocalDateTime interpretDateTimeParam(String param) {
		// Date format: dd-MM-yyyy
		LocalDate date = extractLocalDate(param);
		// Time format: HH:mm
		LocalTime time = extractLocalTime(param);
		return LocalDateTime.of(date, time);
	}
	
	private LocalDate extractLocalDate(String param) {
		if (param == null) {
			return null;
		} else {
			Pattern datePattern = Pattern.compile(DD_MM_YYYY);
			Matcher datePatternMatcher = datePattern.matcher(param);
			if (datePatternMatcher.find()) {
				String dateString = datePatternMatcher.group();
				String[] dateSegments = dateString.split(DELIMITTER_DATE);
				try {
					int day = Integer.parseInt(dateSegments[0]);
					int month = Integer.parseInt(dateSegments[1]);
					int year = Integer.parseInt(dateSegments[2]);
					return LocalDate.of(year, month, day);
				} catch (NumberFormatException e) {
					// Program should never reach here.
					// Suggest using assert to check that dateSegments are all parse-able?
					return null;
				}
			} else {
				return null;
			}
		}
	}
	
	private LocalTime extractLocalTime(String param) {
		if (param == null) {
			return null;
		} else {
			Pattern timePattern = Pattern.compile(HH_MM);
			Matcher timePatternMatcher = timePattern.matcher(param);
			if (timePatternMatcher.find()) {
				String timeString = timePatternMatcher.group();
				String[] timeSegments = timeString.split(DELIMITTER_TIME);
				try {
					int hour = Integer.parseInt(timeSegments[0]);
					int minute = Integer.parseInt(timeSegments[1]);
					return LocalTime.of(hour, minute);
				} catch (NumberFormatException e) {
					// Program should never reach here.
					// Suggest using assert to check that timeSegments are all parse-able?
					return null;
				}
			} else {
				return null;
			}
		}
	}
	
	private String extractFirstWord(String str) {
		if (str == null) {
			return null;
		} else {
			str = str.trim();
			if (str.equals(EMPTY_STRING)) {
				return EMPTY_STRING;
			} else {
				return str.split(WHITESPACE)[ARRAY_POSITION_FIRST];
			}
		}
	}
	
	private String extractSecondWord(String str) {
		if (str == null) {
			return null;
		} else {
			str = str.trim();
			if (str.equals(EMPTY_STRING)) {
				return EMPTY_STRING;
			} else {
				String[] words = str.split(WHITESPACE);
				if (words.length < 2) {
					return EMPTY_STRING;
				} else {
					return words[ARRAY_POSITION_SECOND];
				}
			}
		}
	}
	
	private String removeFirstWord(String str) {
		str = str.trim();
		int firstSpacePos = str.indexOf(SINGLE_SPACE);
		if (0 < firstSpacePos) {
			str = str.substring(firstSpacePos);
			return str.trim();
		} else {
			return EMPTY_STRING;
		}
	 }
	
	private int[] interpretTaskIDs(String param) {
		String[] paramsBeforeParse = param.split(WHITESPACE);
		int[] paramsAfterParse = new int[paramsBeforeParse.length];
		for (int i = 0; i < paramsBeforeParse.length; i++) {
			try {
				paramsAfterParse[i] = Integer.parseInt(paramsBeforeParse[i]);
			} catch (NumberFormatException paramNotInt) {
				return new int[]{INT_PARAM_INVALID};
			}
		}
		return paramsAfterParse;
	}
	
	private Path interpretFilePath(String userInput) {
		String pathString = extractSecondWord(userInput);
		Path pathCandidate = Paths.get(pathString);
		Path path;
		try {
			path = pathCandidate.toRealPath();
			return path;
		} catch (IOException e) {
			return null;
		}
	}
	
}
