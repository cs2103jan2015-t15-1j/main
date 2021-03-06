import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@author A0135295B
public class Translator {
	// ==========Constants for Translator class Section Beginning==========//
	// === Keywords for ADD command
	private static final String KEYWORD_ADD_DEADLINE = "((by)|(BY)|(due)|(DUE))";
	private static final String KEYWORD_ADD_EVENTSTART = "((begin)|(BEGIN)|(start)|(START))";
	private static final String KEYWORD_ADD_EVENTEND = "((until)|(UNTIL)|(end)|(END))";
	private static final String[] addParameterKeywords = {
			KEYWORD_ADD_DEADLINE, KEYWORD_ADD_EVENTSTART, KEYWORD_ADD_EVENTEND };
	
	// === Parameters for DONE command
	private static final String PARAMETER_DISPLAY_ALL = "all";
	private static final String PARAMETER_DISPLAY_DONE = "done";
	private static final String PARAMETER_DISPLAY_THISWEEK = "this week";
	private static final String PARAMETER_DISPLAY_TODAY = "today";
	private static final String PARAMETER_DISPLAY_TOMORROW = "tomorrow";

	// === Keywords for SEARCH command
	private static final String KEYWORD_SEARCH_DUE = "((due)|(DUE))";
	private static final String KEYWORD_SEARCH_AFTER = "((after)|(AFTER))";
	private static final String KEYWORD_SEARCH_BEFORE = "((before)|(BEFORE))";
	private static final String KEYWORD_SEARCH_ON = "((on)|(ON))";
	private static final String[] searchParameterKeywords = {
			KEYWORD_SEARCH_DUE, KEYWORD_SEARCH_AFTER, KEYWORD_SEARCH_BEFORE,
			KEYWORD_SEARCH_ON };

	// === Keywords for EDIT command
	private static final String KEYWORD_EDIT_DEADLINE = "((deadline)|(DEADLINE))";
	private static final String KEYWORD_EDIT_EVENTSTART = "((start)|(START)|(beg)|(BEG))";
	private static final String KEYWORD_EDIT_EVENTEND = "((end)|(END))";
	private static final String KEYWORD_EDIT_DESCRIPTION = "((desc)|(DESC)|(description)|(DESCRIPTION))";
	private static final String KEYWORD_EDIT_REMOVE = "((rm)|(RM)|(remove)|(REMOVE))";
	private static final String PARAMETER_EDIT_REMOVE_START = "((start)|(START))";
	private static final String PARAMETER_EDIT_REMOVE_TIME = "((time)|(TIME))";
	private static final String[] editParameterKeywords = {
			KEYWORD_EDIT_DEADLINE, KEYWORD_EDIT_EVENTSTART,
			KEYWORD_EDIT_EVENTEND, KEYWORD_EDIT_DESCRIPTION,
			KEYWORD_EDIT_REMOVE };

	private static final String PARAMETER_EDIT_LAST_TASK = "((last)|(LAST))";
	private static final int SPECIAL_ID_LAST_TASK = -100;
	private static final String PARAMETER_DELETE_DONE_TASK = "((done)|(DONE))";
	private static final int SPECIAL_ID_DELETE_DONE = -200;

	// === Recognized Formats for Date-Time input.
	// == Date format including year
	private static final String DELIMITTER_DATE = "(-|/)";
	private static final String DD_MM_YYYY = "\\d\\d" + DELIMITTER_DATE
			+ "\\d\\d" + DELIMITTER_DATE + "\\d\\d\\d\\d";
	private static final String DD_M_YYYY = "\\d\\d" + DELIMITTER_DATE + "\\d"
			+ DELIMITTER_DATE + "\\d\\d\\d\\d";
	private static final String D_MM_YYYY = "\\d" + DELIMITTER_DATE + "\\d\\d"
			+ DELIMITTER_DATE + "\\d\\d\\d\\d";
	private static final String D_M_YYYY = "\\d" + DELIMITTER_DATE + "\\d"
			+ DELIMITTER_DATE + "\\d\\d\\d\\d";
	private static final String DD_MM_YY = "\\d\\d" + DELIMITTER_DATE
			+ "\\d\\d" + DELIMITTER_DATE + "\\d\\d";
	private static final String DD_M_YY = "\\d\\d" + DELIMITTER_DATE + "\\d"
			+ DELIMITTER_DATE + "\\d\\d";
	private static final String D_MM_YY = "\\d" + DELIMITTER_DATE + "\\d\\d"
			+ DELIMITTER_DATE + "\\d\\d";
	private static final String D_M_YY = "\\d" + DELIMITTER_DATE + "\\d"
			+ DELIMITTER_DATE + "\\d\\d";
	// == Date format omitting year
	private static final String DD_MM = "\\d\\d" + DELIMITTER_DATE + "\\d\\d";
	private static final String DD_M = "\\d\\d" + DELIMITTER_DATE + "\\d";
	private static final String D_MM = "\\d" + DELIMITTER_DATE + "\\d\\d";
	private static final String D_M = "\\d" + DELIMITTER_DATE + "\\d";
	private static final String[] FORMATS_DAY_MONTH_YEAR = { DD_MM_YYYY,
			DD_M_YYYY, D_MM_YYYY, D_M_YYYY, DD_MM_YY, DD_M_YY, D_MM_YY, D_M_YY,
			DD_MM, DD_M, D_MM, D_M };
	// == Time format including minutes
	private static final String DELIMITTER_TIME = "(:)";
	private static final String HH_MM = "(0|1|2)\\d" + DELIMITTER_TIME
			+ "([0-5])\\d";
	private static final String H_MM = "\\d" + DELIMITTER_TIME + "([0-5])\\d";
	// == Time format omitting minutes
	private static final String HH = "(0|1|2)\\d";
	private static final String H = "\\d";
	private static final String[] FORMATS_HOUR_MINUTE = { HH_MM, H_MM, HH, H };

	// === Default values for Date-Time variables.
	private static final String FILLER_DEFAULT_TIME = " 12:00 ";
	private static final int DATETIME_HOUR_MINIMUM = 0;
	private static final int DATETIME_MINUTE_MINIMUM = 0;
	private static final int DATETIME_HOUR_MAXIMUM = 23;
	private static final int DATETIME_MINUTE_MAXIMUM = 59;
	private static final int EXTRA_TIME_DAY = 1;
	private static final int EXTRA_TIME_HOUR = 1;
	private static final int TIME_HALFDAY = 12;
	private static final int DAYS_IN_ONE_WEEK = 7;
	private static final int MAX_YY_VALUE = 99;
	private static final int EXTRA_DATE_YEAR = 1;
	private static final int CURRENT_MILLENIUM = 2000;

	// === Miscellaneous default values.
	private static final String EMPTY_STRING = "";
	private static final String SINGLE_SPACE = " ";
	private static final String WHITESPACE = "\\s+";
	private static final int ARRAY_POSITION_FIRST = 0;
	private static final int ARRAY_POSITION_SECOND = 1;
	private static final int NUM_TASKID_MINIMUM = 1;

	// === Error values.
	private static final String INVALID_COMMAND = "%1$s is not a valid command!";
	private static final String ILLEGAL_EDIT_EVENT_TIME = "Event end time is before event start time!";
	private static final String PARAMETER_DOES_NOT_EXIST = null;
	private static final int INT_PARAM_INVALID = -1;
	private static final int INVALID_TASK_ID = -1;
	// ==========Constants for Translator class Section End================//

	// === Debugging
	private Logger logger;
	
	private Storage taskStorage;
	private History commandHistory;

	
	public Translator(Storage storage, History history) {
		assert storage != null;
		assert history != null;
		
		taskStorage = storage;
		commandHistory = history;
	}

	public void setStorage(Storage storage) {
		assert storage != null;
		taskStorage = storage;
	}

	public void setHistory(History history) {
		assert history != null;
		commandHistory = history;
	}

	public Storage getStorage() {
		assert taskStorage != null;
		return taskStorage;
	}

	public History getHistory() {
		assert commandHistory != null;
		return commandHistory;
	}

	public Command createCommand(String usercommand) throws Exception {
		if (usercommand == null) {
			throw new NullPointerException();
		}

		Command newCommand = null;
		String inputCommandTypeString = extractFirstWord(usercommand);
		CommandType inputCommandType = determineCommandType(inputCommandTypeString);
//		logger.log(Level.INFO, "CommandType is " + inputCommandType.toString());
		
		switch (inputCommandType) {
		case ADD:
			newCommand = createAddCommand(usercommand);
			break;
		case DISPLAY:
			newCommand = createDisplayCommand(usercommand);
			break;
		case DONE:
			newCommand = createDoneCommand(usercommand);
			break;
		case UNDONE:
			newCommand = createUndoneCommand(usercommand);
			break;
		case DELETE:
			newCommand = createDeleteCommand(usercommand);
			break;
		case EDIT:
			newCommand = createEditCommand(usercommand);
			break;
		case UNDO:
			newCommand = createUndoCommand();
			break;
		case REDO:
			newCommand = createRedoCommand();
			break;
		case HELP:
			newCommand = createHelpCommand();
			break;
		case SETDIR:
			newCommand = createSetDirectoryCommand(usercommand);
			break;
		case GETDIR:
			newCommand = createGetDirectoryCommand(usercommand);
			break;
		case EXIT:
			newCommand = createExitCommand();
			break;
		case SEARCH:
			newCommand = createSearchCommand(usercommand);
			break;
		case CLEAR:
			newCommand = createClearCommand();
			break;
		default:
			throw new Exception(String.format(INVALID_COMMAND, inputCommandTypeString));
		}
		return newCommand;
	}

	private CommandType determineCommandType(String commandTypeString) {
		if (commandTypeString == null) {
			return CommandType.INVALID;
		} else if (commandTypeString.equalsIgnoreCase("ADD")
				|| commandTypeString.equalsIgnoreCase("a")) {
			return CommandType.ADD;
		} else if (commandTypeString.equalsIgnoreCase("DISPLAY")
				|| commandTypeString.equalsIgnoreCase("show")
				|| commandTypeString.equalsIgnoreCase("dis")
				|| commandTypeString.equalsIgnoreCase("ls")) {
			return CommandType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("SEARCH")
				|| commandTypeString.equalsIgnoreCase("s")) {
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("EDIT")
				|| commandTypeString.equalsIgnoreCase("e")) {
			return CommandType.EDIT;
		} else if (commandTypeString.equalsIgnoreCase("DELETE")
				|| commandTypeString.equalsIgnoreCase("remove")
				|| commandTypeString.equalsIgnoreCase("rm")
				|| commandTypeString.equalsIgnoreCase("del")) {
			return CommandType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("CLEAR")) {
			return CommandType.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("DONE")) {
			return CommandType.DONE;
		} else if (commandTypeString.equalsIgnoreCase("UNDONE")) {
			return CommandType.UNDONE;
		} else if (commandTypeString.equalsIgnoreCase("UNDO")
				|| commandTypeString.equalsIgnoreCase("u")) {
			return CommandType.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("REDO")
				|| commandTypeString.equalsIgnoreCase("r")) {
			return CommandType.REDO;
		} else if (commandTypeString.equalsIgnoreCase("HELP")) {
			return CommandType.HELP;
		} else if (commandTypeString.equalsIgnoreCase("SETDIR")
				|| commandTypeString.equalsIgnoreCase("SETDIRECTORY")) {
			return CommandType.SETDIR;
		} else if (commandTypeString.equalsIgnoreCase("GETDIR")
				|| commandTypeString.equalsIgnoreCase("GETDIRECTORY")) {
			return CommandType.GETDIR;
		} else if (commandTypeString.equalsIgnoreCase("EXIT")) {
			return CommandType.EXIT;
		} else {
			return CommandType.INVALID;
		}
	}

	private Command createAddCommand(String usercommand) {
		Task addInformation = interpretAddParameter(usercommand);
		Task[] addInfoForAddCommandInit = new Task[]{addInformation};
		return new AddCommand(this.getStorage(), this.getHistory(),
				addInfoForAddCommandInit);
	}

	private Command createDisplayCommand(String usercommand) {
		Task displayInformation = interpretDisplayParameter(usercommand);
		return new DisplayCommand(this.getStorage(), displayInformation);
	}

	private Command createSearchCommand(String usercommand) {
		Task searchInformation = interpretSearchParameter(usercommand);
		return new SearchCommand(this.getStorage(), searchInformation);
	}

	private Command createEditCommand(String usercommand) {
		Task editInformation = interpretEditParameter(usercommand);
		return new EditCommand(this.getStorage(), this.getHistory(),
				editInformation);
	}

	private Command createDeleteCommand(String usercommand) {
		int[] deleteInformation = interpretDeleteParameter(usercommand);
		if (deleteInformation[ARRAY_POSITION_FIRST] == SPECIAL_ID_DELETE_DONE) {
			return new DeleteDoneCommand(this.getStorage(), this.getHistory());
		} else {
			return new DeleteCommand(this.getStorage(), this.getHistory(), deleteInformation);
		}
	}

	private Command createClearCommand() {
		return new ClearCommand(this.getStorage(), this.getHistory());
	}

	private Command createDoneCommand(String usercommand) {
		int[] doneInformation = interpretDoneParameter(usercommand);
		return new DoneCommand(this.getStorage(), this.getHistory(), doneInformation, true);
	}

	private Command createUndoneCommand(String usercommand) {
		int[] undoneInformation = interpretDoneParameter(usercommand);
		return new DoneCommand(this.getStorage(), this.getHistory(), undoneInformation, false);
	}

	private Command createUndoCommand() {
		return new UndoCommand(this.getStorage(), this.getHistory());
	}

	private Command createRedoCommand() {
		return new RedoCommand(this.getStorage(), this.getHistory());
	}

	private Command createHelpCommand() {
		return new HelpCommand();
	}

	private Command createSetDirectoryCommand(String usercommand) {
		String setDirInformation = extractSecondWord(usercommand);
		return new SetDirectoryCommand(this.getStorage(), this.getHistory(), setDirInformation);
	}

	private Command createGetDirectoryCommand(String usercommand) {
		return new GetDirectoryCommand(this.getStorage());
	}

	private Command createExitCommand() {
		return new ExitCommand();
	}

	private Task interpretAddParameter(String usercommand) {

		Task newTask = new Task();
		KeywordInfoList kList = new KeywordInfoList(usercommand, addParameterKeywords);

		String paramDescription = kList.getDescription();
		String paramDeadline = kList.getParameter(KEYWORD_ADD_DEADLINE);
		String paramEventStart = kList.getParameter(KEYWORD_ADD_EVENTSTART);
		String paramEventEnd = kList.getParameter(KEYWORD_ADD_EVENTEND);

		if (paramDescription != EMPTY_STRING) {
			newTask.setDescription(paramDescription);

			if (paramDeadline != PARAMETER_DOES_NOT_EXIST) {
				// The task type is "deadline"
				LocalDateTime deadline = interpretDateTimeParam(paramDeadline);
				newTask.setEndDateTime(deadline);

			} else if (paramEventStart != PARAMETER_DOES_NOT_EXIST) {
				// The task type is "event"
				LocalDateTime eventStart = interpretDateTimeParam(paramEventStart);
				newTask.setStartDateTime(eventStart);
				LocalDateTime eventEnd = null;
				if (paramEventEnd != PARAMETER_DOES_NOT_EXIST) {
					eventEnd = provideDefaultEndDateTime(paramEventEnd,	eventStart);
				} else {
					eventEnd = eventStart.plusHours(EXTRA_TIME_HOUR);
				}
				newTask.setEndDateTime(eventEnd);

			} else {
				// The task type is "floating." No further action necessary.
			}
		}

		return newTask;
	}

	private Task interpretEditParameter(String usercommand) {

		KeywordInfoList kList = new KeywordInfoList(usercommand, editParameterKeywords);

		int taskID = extractEditTaskID(usercommand);
		Task newTask;

		if (taskID != INVALID_TASK_ID) {

			if (taskID == SPECIAL_ID_LAST_TASK) {
				Task lastAddedOriginalTask = taskStorage.getLastAddedTask();
				newTask = makeShallowCopyOfOriginalTask(lastAddedOriginalTask.getId());
			} else {
				newTask = makeShallowCopyOfOriginalTask(taskID);
			}

			boolean doesEditParameterExist = false;

			String paramDescription = kList.getParameter(KEYWORD_EDIT_DESCRIPTION);
			String paramDeadline = kList.getParameter(KEYWORD_EDIT_DEADLINE);
			String paramEventStart = kList.getParameter(KEYWORD_EDIT_EVENTSTART);
			String paramEventEnd = kList.getParameter(KEYWORD_EDIT_EVENTEND);

			String paramRemove = kList.getParameter(KEYWORD_EDIT_REMOVE);

			if (paramDescription != PARAMETER_DOES_NOT_EXIST) {
				newTask.setDescription(paramDescription);
				doesEditParameterExist = true;
			}

			if (paramDeadline != PARAMETER_DOES_NOT_EXIST) {
				LocalDateTime deadline = interpretDateTimeParam(paramDeadline);
				newTask.setEndDateTime(deadline);
				doesEditParameterExist = true;

			} else {
				LocalDateTime eventStart = null;
				if (paramEventStart != PARAMETER_DOES_NOT_EXIST) {
					eventStart = interpretDateTimeParam(paramEventStart);
					if (paramEventEnd == PARAMETER_DOES_NOT_EXIST) {
						if (newTask.getEndDateTime() == null) {
							newTask.setStartDateTime(eventStart);
							newTask.setEndDateTime(eventStart.plusHours(EXTRA_TIME_HOUR));
							doesEditParameterExist = true;
						} else if (eventStart.isBefore(newTask.getEndDateTime())) {
							newTask.setStartDateTime(eventStart);
							doesEditParameterExist = true;
						} else {
							System.out.println(ILLEGAL_EDIT_EVENT_TIME);
							return null;
						}
					}
				}

				if (paramEventEnd != PARAMETER_DOES_NOT_EXIST) {
					if (eventStart != null) {
						LocalDateTime eventEnd = provideDefaultEndDateTime(paramEventEnd, eventStart);
						if (eventEnd.isAfter(eventStart)) {
							newTask.setStartDateTime(eventStart);
							newTask.setEndDateTime(eventEnd);
							doesEditParameterExist = true;
						} else {
							System.out.println(ILLEGAL_EDIT_EVENT_TIME);
							return null;
						}
					} else {
						LocalDateTime eventEnd = interpretDateTimeParam(paramEventEnd);
						if (newTask.getStartDateTime() == null
								|| eventEnd.isAfter(newTask.getStartDateTime())) {
							newTask.setEndDateTime(eventEnd);
							doesEditParameterExist = true;
						} else {
							System.out.println(ILLEGAL_EDIT_EVENT_TIME);
							return null;
						}
					}
				}
			}

			if (paramRemove != PARAMETER_DOES_NOT_EXIST) {
				Pattern patternRemoveTime = Pattern.compile(PARAMETER_EDIT_REMOVE_TIME);
				Matcher matcherRemoveTime = patternRemoveTime.matcher(paramRemove);
				if (matcherRemoveTime.find()) {
					newTask.setStartDateTime(null);
					newTask.setEndDateTime(null);
					doesEditParameterExist = true;
				} else {
					Pattern patternRemoveStart = Pattern.compile(PARAMETER_EDIT_REMOVE_START);
					Matcher matcherRemoveStart = patternRemoveStart.matcher(paramRemove);
					if (matcherRemoveStart.find()) {
						newTask.setStartDateTime(null);
						doesEditParameterExist = true;
					}
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
		Pattern patternLastTaskRequest = Pattern.compile(PARAMETER_EDIT_LAST_TASK);
		Matcher matcherLastTaskRequest = patternLastTaskRequest.matcher(usercommand);
		if (matcherLastTaskRequest.find()) {
			taskID = SPECIAL_ID_LAST_TASK;
		} else {
			try {
				taskID = Integer.parseInt(taskIDString);
				if (taskID < NUM_TASKID_MINIMUM) {
					taskID = INVALID_TASK_ID;
				}
				if (taskStorage.getTask(taskID) == null) {
					taskID = INVALID_TASK_ID;
				}
			} catch (NumberFormatException e) {
				taskID = INVALID_TASK_ID;
			}
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

	private int[] interpretDeleteParameter(String usercommand) {
		String parameter = removeFirstWord(usercommand);
		return interpretDeleteTaskIDs(parameter);
	}
	
	private int[] interpretDeleteTaskIDs(String param) {
		String[] paramsBeforeParse = param.split(WHITESPACE);
		Pattern patternDeleteDoneRequested = Pattern.compile(PARAMETER_DELETE_DONE_TASK);
		Matcher matcherDeleteDoneRequested = patternDeleteDoneRequested.matcher(param);
		if (matcherDeleteDoneRequested.find()) {
			return new int[] { SPECIAL_ID_DELETE_DONE };
		}
		int[] paramsAfterParse = new int[paramsBeforeParse.length];
		for (int i = 0; i < paramsBeforeParse.length; i++) {
			try {
				paramsAfterParse[i] = Integer.parseInt(paramsBeforeParse[i]);
			} catch (NumberFormatException paramNotInt) {
				return new int[] { INT_PARAM_INVALID };
			}
		}
		return paramsAfterParse;
	}

	private int[] interpretDoneParameter(String usercommand) {
		String parameter = removeFirstWord(usercommand);
		return interpretTaskIDs(parameter);
	}
	
	private int[] interpretTaskIDs(String param) {
		String[] paramsBeforeParse = param.split(WHITESPACE);
		int[] paramsAfterParse = new int[paramsBeforeParse.length];
		for (int i = 0; i < paramsBeforeParse.length; i++) {
			try {
				paramsAfterParse[i] = Integer.parseInt(paramsBeforeParse[i]);
			} catch (NumberFormatException paramNotInt) {
				return new int[] { INT_PARAM_INVALID };
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
	
	private Task interpretDisplayParameter(String usercommand) {
		String displayParam = removeFirstWord(usercommand);
		if (displayParam.equals(EMPTY_STRING)) {
			return createDisplayOneWeekInfoPackage();
		} else if (displayParam.equalsIgnoreCase(PARAMETER_DISPLAY_ALL)) {
			return createDisplayAllInfoPackage();
		} else if (displayParam.equalsIgnoreCase(PARAMETER_DISPLAY_DONE)) {
			return createDisplayDoneInfoPackage();
		} else if (displayParam.equalsIgnoreCase(PARAMETER_DISPLAY_TODAY)) {
			return createDisplayTodayInfoPackage();
		} else if (displayParam.equalsIgnoreCase(PARAMETER_DISPLAY_THISWEEK)) {
			return createDisplayThisWeekInfoPackage();
		} else if (displayParam.equalsIgnoreCase(PARAMETER_DISPLAY_TOMORROW)) {
			return createDisplayTomorrowInfoPackage();
		} else {
			return null;
		}
	}
	
	private Task createDisplayOneWeekInfoPackage() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oneWeekLaterEnd = getEndOfDay(now.plusDays(DAYS_IN_ONE_WEEK));
		assert now.isBefore(oneWeekLaterEnd);
		
		Task displayOneWeekInfoPackage = new Task();
		displayOneWeekInfoPackage.setStartDateTime(LocalDateTime.MIN);
		displayOneWeekInfoPackage.setEndDateTime(oneWeekLaterEnd);
		displayOneWeekInfoPackage.setDescription(EMPTY_STRING);
		return displayOneWeekInfoPackage;
	}
	
	private Task createDisplayAllInfoPackage() {
		LocalDateTime beginningOfTime = LocalDateTime.MIN;
		LocalDateTime endOfTime = LocalDateTime.MAX;
		assert beginningOfTime.isBefore(endOfTime);
		
		Task displayAllInfoPackage = new Task();
		displayAllInfoPackage.setStartDateTime(beginningOfTime);
		displayAllInfoPackage.setEndDateTime(endOfTime);
		displayAllInfoPackage.setDescription(EMPTY_STRING);
		return displayAllInfoPackage;
	}
	
	private Task createDisplayDoneInfoPackage() {
		Task displayDoneInfoPackage = new Task();
		displayDoneInfoPackage.setDone(true);
		displayDoneInfoPackage.setDescription(EMPTY_STRING);
		return displayDoneInfoPackage;
	}
	
	private Task createDisplayTodayInfoPackage() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime todayBeginning = getBeginningOfDay(now);
		LocalDateTime todayEnd = getEndOfDay(now);
		assert todayBeginning.isBefore(todayEnd);
		
		Task displayTodayInfoPackage = new Task();
		displayTodayInfoPackage.setStartDateTime(LocalDateTime.MIN);
		displayTodayInfoPackage.setEndDateTime(todayEnd);
		displayTodayInfoPackage.setDescription(EMPTY_STRING);
		return displayTodayInfoPackage;
	}
	
	private Task createDisplayThisWeekInfoPackage() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime thisWeekBeginning = getBeginningOfDay(now).with(DayOfWeek.MONDAY);
		LocalDateTime thisWeekEnd = getEndOfDay(now).with(DayOfWeek.SUNDAY);
		assert thisWeekBeginning.isBefore(thisWeekEnd);
		
		Task displayThisWeekInfoPackage = new Task();
		displayThisWeekInfoPackage.setStartDateTime(LocalDateTime.MIN);
		displayThisWeekInfoPackage.setEndDateTime(thisWeekEnd);
		displayThisWeekInfoPackage.setDescription(EMPTY_STRING);
		return displayThisWeekInfoPackage;
	}
	
	private Task createDisplayTomorrowInfoPackage() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime tomorrowBeginning = getBeginningOfDay(now).plusDays(EXTRA_TIME_DAY);
		LocalDateTime tomorrowEnd = getEndOfDay(now).plusDays(EXTRA_TIME_DAY);
		assert tomorrowBeginning.isBefore(tomorrowEnd);
		
		Task displayTomorrowInfoPackage = new Task();
		displayTomorrowInfoPackage.setStartDateTime(tomorrowBeginning);
		displayTomorrowInfoPackage.setEndDateTime(tomorrowEnd);
		displayTomorrowInfoPackage.setDescription(EMPTY_STRING);
		return displayTomorrowInfoPackage;
	}
	
	private LocalDateTime interpretDateTimeParam(String param) {
		StringBuilder dateTimeStr = new StringBuilder(param);
		LocalDate date = extractLocalDate(dateTimeStr);
		LocalTime time = extractLocalTime(dateTimeStr);
		if (date == null && time != null) {
			time = provideDefaultTime(time);
			date = provideDefaultDate(time);
		} else if (date != null && time == null) {
			time = getStartOfNextHour(LocalTime.now());
		}
		return LocalDateTime.of(date, time);
	}
	
	private LocalDateTime getBeginningOfDay(LocalDateTime dateTime) {
		return dateTime.withMinute(DATETIME_MINUTE_MINIMUM)
				.withHour(DATETIME_HOUR_MINIMUM);
	}

	private LocalDateTime getEndOfDay(LocalDateTime dateTime) {
		return dateTime.withMinute(DATETIME_MINUTE_MAXIMUM)
				.withHour(DATETIME_HOUR_MAXIMUM);
	}
	
	private LocalTime getStartOfNextHour(LocalTime ref) {
		return ref.withMinute(DATETIME_MINUTE_MINIMUM).plusHours(EXTRA_TIME_HOUR);
	}

	private LocalTime provideDefaultTime(LocalTime time) {
		if (time != null) {
			LocalTime currentTime = LocalTime.now();
			if (time.getHour() < TIME_HALFDAY
					&& currentTime.getHour() >= TIME_HALFDAY) {
				LocalTime afternoonTime = time.plusHours(TIME_HALFDAY);
				if (afternoonTime.isAfter(currentTime)) {
					return afternoonTime;
				} else {
					return time;
				}
			} else {
				return time;
			}
		} else {
			return null;
		}
	}

	private LocalTime provideDefaultEndTime(LocalTime endTime,
			LocalDateTime referenceStartDateTime) {
		if (endTime != null && referenceStartDateTime != null) {
			if (endTime.getHour() < TIME_HALFDAY
					&& referenceStartDateTime.getHour() >= TIME_HALFDAY) {
				LocalTime afternoonTime = endTime.plusHours(TIME_HALFDAY);
				if (afternoonTime.isAfter(referenceStartDateTime.toLocalTime())) {
					return afternoonTime;
				} else {
					return endTime;
				}
			} else {
				return endTime;
			}
		} else {
			return null;
		}
	}

	private LocalDate provideDefaultDate(LocalTime time) {
		if (time != null) {
			LocalDate defaultDate = LocalDate.now();
			LocalTime currentTime = LocalTime.now();
			if (time.isBefore(currentTime)) {
				defaultDate = defaultDate.plusDays(EXTRA_TIME_DAY);
			}
			return defaultDate;
		} else {
			return null;
		}
	}
	
	private LocalDateTime provideDefaultEndDateTime(String endTimeParam,
			LocalDateTime startDateTime) {
		StringBuilder dateTimeStr = new StringBuilder(endTimeParam);
		LocalDate date = extractLocalDate(dateTimeStr);
		LocalTime time = extractLocalTime(dateTimeStr);
		if (date == null && time != null) {
			time = provideDefaultEndTime(time, startDateTime);
			date = startDateTime.toLocalDate();
			LocalDateTime endDateTime = LocalDateTime.of(date, time);
			if (endDateTime.isBefore(startDateTime)) {
				endDateTime = endDateTime.plusDays(EXTRA_TIME_DAY);
			}
			assert startDateTime.isBefore(endDateTime);
			return endDateTime;
		} else if (date != null && time != null) {
			LocalDateTime endDateTime = LocalDateTime.of(date, time);
			if (endDateTime.isBefore(startDateTime)) {
				System.out.println(ILLEGAL_EDIT_EVENT_TIME);
				return null;
			} else {
				return endDateTime;
			}
		} else if (date != null && time == null) {
			time = getStartOfNextHour(LocalTime.now());
			return LocalDateTime.of(date, time);
		} else {
			return null;
		}
	}
	
	private LocalTime extractLocalTime(StringBuilder param) {
		if (param != null) {
			for (int i = 0; i < FORMATS_HOUR_MINUTE.length; i++) {
				Pattern timePattern = Pattern.compile(FORMATS_HOUR_MINUTE[i]);
				Matcher timePatternMatcher = timePattern.matcher(param);
				if (timePatternMatcher.find()) {
					String timeString = timePatternMatcher.group();
					int hour;
					int minute;
					Pattern delimitterPattern = Pattern.compile(DELIMITTER_TIME);
					Matcher delimitterMatcher = delimitterPattern.matcher(timeString);
					if (delimitterMatcher.find()) {
						String[] timeSegments = timeString.split(DELIMITTER_TIME);
						hour = Integer.parseInt(timeSegments[0]);
						minute = Integer.parseInt(timeSegments[1]);
					} else {
						hour = Integer.parseInt(timeString);
						minute = DATETIME_MINUTE_MINIMUM;
					}
					return LocalTime.of(hour, minute);
				}
			}
		}
		return null;
	}

	private LocalDate extractLocalDate(StringBuilder param) {
		if (param != null) {
			for (int i = 0; i < FORMATS_DAY_MONTH_YEAR.length; i++) {
				Pattern datePattern = Pattern.compile(FORMATS_DAY_MONTH_YEAR[i]);
				Matcher datePatternMatcher = datePattern.matcher(param);
				if (datePatternMatcher.find()) {
					String dateString = datePatternMatcher.group();
					LocalDate today = LocalDate.now();
					int year = today.getYear();
					int month = today.getMonthValue();
					int day = today.getDayOfMonth();
					String[] dateSegments = dateString.split(DELIMITTER_DATE);
					day = Integer.parseInt(dateSegments[0]);
					month = Integer.parseInt(dateSegments[1]);
					if (dateSegments.length == 3) {
						year = Integer.parseInt(dateSegments[2]);
						if (year <= MAX_YY_VALUE) {
							year += CURRENT_MILLENIUM;
						}
					} else {
						LocalDate extractedDate = LocalDate.of(year, month, day);
						if (extractedDate.isBefore(LocalDate.now())) {
							year += EXTRA_DATE_YEAR;
						}
					}
					param.replace(datePatternMatcher.start(), datePatternMatcher.end() + 1, EMPTY_STRING);
					return LocalDate.of(year, month, day);
				}
			}
		}
		return null;
	}

	private String extractFirstWord(String str) {
		if (str == null) {
			return null;
		} else {
			str = str.trim();
			if (str.equals(EMPTY_STRING)) {
				return EMPTY_STRING;
			} else {
				String[] words = str.split(WHITESPACE);
				return words[ARRAY_POSITION_FIRST];
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



	// @author A0111337U
	private Task makeShallowCopyOfOriginalTask(int TaskId) {
		Task oldTask = new Task();
		Task old = taskStorage.getTask(TaskId);
		oldTask.setDescription(old.getDescription());
		oldTask.setDone(old.isDone());
		oldTask.setId(old.getId());
		oldTask.setStartDateTime(old.getStartDateTime());
		oldTask.setEndDateTime(old.getEndDateTime());
		return oldTask;
	}
}
