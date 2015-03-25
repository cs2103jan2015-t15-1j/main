import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
	
	private static final String INVALID_COMMAND = "%1$s is not a valid command!";
	// Keywords
	private static final String KEYWORD_ADD_DEADLINE = "((by)|(BY)|(due)|(DUE))";
	private static final String KEYWORD_ADD_LOCATION = "((at)|(AT)|(@))";
	private static final String KEYWORD_ADD_EVENTSTART = "((from)|(FROM)|(start)|(START))";
	private static final String KEYWORD_ADD_EVENTEND = "((to)|(TO)|(end)|(END))";
	private static final String[] addParameterKeywords = 
		{KEYWORD_ADD_DEADLINE, KEYWORD_ADD_LOCATION, KEYWORD_ADD_EVENTSTART, KEYWORD_ADD_EVENTEND};
	
	private static final String KEYWORD_SEARCH_DUE = "((due)|(DUE))";
	private static final String KEYWORD_SEARCH_AFTER = "((after)|(AFTER))";
	private static final String KEYWORD_SEARCH_BEFORE = "((before)|(BEFORE))";
	private static final String KEYWORD_SEARCH_ON = "((on)|(ON))";
	private static final String[] searchParameterKeywords = 
		{KEYWORD_SEARCH_DUE, KEYWORD_SEARCH_AFTER, KEYWORD_SEARCH_BEFORE, KEYWORD_SEARCH_ON};
	
	private static final String KEYWORD_EDIT_DEADLINE = "((deadline)|(DEADLINE))";
	private static final String KEYWORD_EDIT_LOCATION = "((location)|(LOCATION))";
	private static final String KEYWORD_EDIT_EVENTSTART = "((start)|(START))";
	private static final String KEYWORD_EDIT_EVENTEND = "((end)|(END))";
	private static final String KEYWORD_EDIT_DESCRIPTION = "((desc)|(DESC)|(description)|(DESCRIPTION))";
	private static final String[] editParameterKeywords = 
		{KEYWORD_EDIT_DEADLINE, KEYWORD_EDIT_LOCATION, KEYWORD_EDIT_EVENTSTART, KEYWORD_EDIT_EVENTEND,
		KEYWORD_EDIT_DESCRIPTION};
	
	private static final String DELIMITTER_DATE = "(\\s|-|/)";
	private static final String DD_MM_YYYY = "\\d\\d" + DELIMITTER_DATE + "\\d\\d" +
			DELIMITTER_DATE + "\\d\\d\\d\\d";
	private static final String DELIMITTER_TIME = "(:)";
	private static final String HH_MM = "(0|1|2)\\d" + DELIMITTER_TIME + "([0-5])\\d";
	
	private static final String TIME_SEARCH_DEFAULT = " 12:00 ";
	private static final int DATETIME_HOUR_MINIMUM = 0;
	private static final int DATETIME_MINUTE_MINIMUM = 0;
	private static final int DATETIME_HOUR_MAXIMUM = 23;
	private static final int DATETIME_MINUTE_MAXIMUM = 59;
	
	private static final String EMPTY_STRING = "";
	private static final String SINGLE_SPACE = " ";
	private static final String WHITESPACE = "\\s+";
	private static final int ARRAY_POSITION_FIRST = 0;
	private static final int ARRAY_POSITION_SECOND = 1;
	private static final String PARAMETER_DOES_NOT_EXIST = null;
	private static final int INT_PARAM_INVALID = -1;
	private static final int INVALID_TASK_ID = -1;
	
	public Translator() {
		
	}
	
	public Command createCommand(String usercommand) throws Exception {
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
			break;
		case EXIT :
			newCommand = createExitCommand();
			break;
		case SEARCH :
			newCommand = createSearchCommand(usercommand);
			break;
		case CLEAR :
			break;
		default :
			throw new Exception (String.format(INVALID_COMMAND, inputCommandTypeString));
		}
		return newCommand;
	}
	
	private CommandType determineCommandType(String commandTypeString) {
		//ADD, DISPLAY, DONE, DELETE, EDIT, UNDO, REDO, HELP, SETDIR, EXIT
		if (commandTypeString.equalsIgnoreCase("ADD")) {
			return CommandType.ADD;
		} else if (commandTypeString.equalsIgnoreCase("DISPLAY")) {
			return CommandType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("SEARCH")) {
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("EDIT")) {
			return CommandType.EDIT;
		} else if (commandTypeString.equalsIgnoreCase("DELETE")) {
			return CommandType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("CLEAR")) {
            return CommandType.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("DONE")) {
			return CommandType.DONE;
		} else if (commandTypeString.equalsIgnoreCase("UNDO")) {
			return CommandType.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("REDO")) {
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
	
	private Command createEditCommand(String usercommand) {
		Task editInformation = interpretEditParameter(usercommand);
		return new EditCommand(editInformation);
	}
	
	private Command createDeleteCommand(String usercommand) {
		int[] deleteInformation = interpretDeleteParameter(usercommand);
		return new DeleteCommand(deleteInformation);
	}
	
	private Command createDoneCommand(String usercommand) {
		int[] doneInformation = interpretDoneParameter(usercommand);
		return new DoneCommand(doneInformation, true);
	}
	
	private Command createSearchCommand(String usercommand) {
		Task searchInformation = interpretSearchParameter(usercommand);
		return new SearchCommand(searchInformation);
	}
	
	private Command createDisplayCommand() {
		return new DisplayCommand();
	}
	
	private Command createHelpCommand() {
		return new HelpCommand();
	}
	
	private Command createUndoCommand() {
		return new UndoCommand();
	}
	
	private Command createRedoCommand() {
		return new RedoCommand();
	}
	
	private Command createExitCommand() {
		return new ExitCommand();
	}
	
	private Task interpretAddParameter(String usercommand) {
		
		KeywordInfoList kList = null;
		Task newTask = new Task();
		
			kList = new KeywordInfoList(usercommand, addParameterKeywords);
		
			String paramDescription = kList.getDescription();
			String paramDeadline = kList.getParameter(KEYWORD_ADD_DEADLINE);
			String paramLocation = kList.getParameter(KEYWORD_ADD_LOCATION);
			String paramEventStart = kList.getParameter(KEYWORD_ADD_EVENTSTART);
			String paramEventEnd = kList.getParameter(KEYWORD_ADD_EVENTEND);
			
			if (paramDescription != EMPTY_STRING) {
				newTask.setDescription(paramDescription);
				
				if (paramLocation != PARAMETER_DOES_NOT_EXIST) {
					newTask.setLocation(paramLocation);
				}
				
				if (paramDeadline != PARAMETER_DOES_NOT_EXIST) {
					// task type is a deadline
					LocalDateTime deadline = interpretDateTimeParam(paramDeadline);
					newTask.setEndDateTime(deadline);
					
				} else if (paramEventStart != PARAMETER_DOES_NOT_EXIST && paramEventEnd != PARAMETER_DOES_NOT_EXIST) {
					// task type is an event
					LocalDateTime eventStart = interpretDateTimeParam(paramEventStart);
					LocalDateTime eventEnd = interpretDateTimeParam(paramEventEnd);
					newTask.setStartDateTime(eventStart);
					newTask.setEndDateTime(eventEnd);
					
				} else {
					// task type is floating
					// no more action needed.
				}
			}
		
		return newTask;
	}
	
	private Task interpretEditParameter(String usercommand) {
		KeywordInfoList kList = null;
		Task newTask = new Task();
		kList = new KeywordInfoList(usercommand, editParameterKeywords);
		
		int taskID = extractEditTaskID(usercommand);
		if (taskID != INVALID_TASK_ID) {
			
			newTask.setId(taskID);
			boolean doesParameterExists = false;
			
			String paramDescription = kList.getParameter(KEYWORD_EDIT_DESCRIPTION);
			String paramDeadline = kList.getParameter(KEYWORD_EDIT_DEADLINE);
			String paramLocation = kList.getParameter(KEYWORD_EDIT_LOCATION);
			String paramEventStart = kList.getParameter(KEYWORD_EDIT_EVENTSTART);
			String paramEventEnd = kList.getParameter(KEYWORD_EDIT_EVENTEND);
			
			if (paramDescription != null) {
				newTask.setDescription(paramDescription);
				doesParameterExists = true;
			}
			if (paramDeadline != null) {
				LocalDateTime deadline = interpretDateTimeParam(paramDeadline);
				newTask.setEndDateTime(deadline);
				doesParameterExists = true;
			} else {
				if (paramEventStart != null) {
					LocalDateTime eventStart = interpretDateTimeParam(paramEventStart);
					newTask.setStartDateTime(eventStart);
					doesParameterExists = true;
				}
				if (paramEventEnd != null) {
					LocalDateTime eventEnd = interpretDateTimeParam(paramEventEnd);
					newTask.setEndDateTime(eventEnd);
					doesParameterExists = true;
				}
			}
			if (paramLocation != null) {
				newTask.setLocation(paramLocation);
				doesParameterExists = true;
			}
			if (doesParameterExists == true) {
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
		} catch (NumberFormatException e) {
			taskID = INVALID_TASK_ID;
		}
		return taskID;
	}
	
	private Task interpretSearchParameter(String usercommand) {
		
		Task newTask = new Task();
		KeywordInfoList kList = new KeywordInfoList(usercommand, searchParameterKeywords);
		//String paramDue = kList.getParameter(KEYWORD_SEARCH_DUE);
		String paramAfter = kList.getParameter(KEYWORD_SEARCH_AFTER);
		String paramBefore = kList.getParameter(KEYWORD_SEARCH_BEFORE);
		//String paramOn = kList.getParameter(KEYWORD_DISPLAY_ON);
		String paramDescription = kList.getDescription();
		String paramOn = kList.getParameter(KEYWORD_SEARCH_ON);
		
		// Storage can take advantage of LocalDateTime.isAfter, LocalDateTime.isBefore.
		// Convention must be set up on how to tell Storage that this Display wants before/after/due/on.
		
		if (paramDescription == null) {
			newTask.setDescription(EMPTY_STRING);
		} else {
			newTask.setDescription(paramDescription);
		}
		if (paramOn == null) {
			if (paramAfter == null) {
				newTask.setStartDateTime(LocalDateTime.MIN);
			} else {
				LocalDateTime lowerBoundaryTime = interpretDateTimeParam(paramAfter);
				newTask.setStartDateTime(lowerBoundaryTime);
			}
			if (paramBefore == null) {
				newTask.setEndDateTime(LocalDateTime.MAX);
			} else {
				LocalDateTime upperBoundaryTime = interpretDateTimeParam(paramBefore);
				newTask.setEndDateTime(upperBoundaryTime);
			}
		} else {
			// Assuming the user only inputs DD/MM/YYYY but no HH:mm
			paramOn += TIME_SEARCH_DEFAULT;
			LocalDateTime dayToSearch = interpretDateTimeParam(paramOn);
			newTask.setStartDateTime(getBeginningOfDay(dayToSearch));
			newTask.setEndDateTime(getEndOfDay(dayToSearch));
		}
		
		return newTask;
	}

	
	private LocalDateTime getBeginningOfDay(LocalDateTime dateTime) {
		LocalDateTime alteredHour = dateTime.withHour(DATETIME_HOUR_MINIMUM);
		LocalDateTime alteredHourAndMinute = alteredHour.withMinute(DATETIME_MINUTE_MINIMUM);
		return alteredHourAndMinute;
	}
	
	private LocalDateTime getEndOfDay(LocalDateTime dateTime) {
		LocalDateTime alteredHour = dateTime.withHour(DATETIME_HOUR_MAXIMUM);
		LocalDateTime alteredHourAndMinute = alteredHour.withMinute(DATETIME_MINUTE_MAXIMUM);
		return alteredHourAndMinute;
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
		// NEED TO BE IMPLEMENTED:
		// Difference between Add and Edit: Add will fill in unspecified fields of LocalDateTime by default, whereas
		// Edit will only fill in fields of LocalDateTime that need to be changed.
		
		// Date example: 02-03-2015
		LocalDate date = extractLocalDate(param);
		// Time example: 8PM | 8:00 | 20:00 | 8
		LocalTime time = extractLocalTime(param);
		
		return LocalDateTime.of(date, time);
	}
	
	private LocalDate extractLocalDate(String param) {		
		Pattern p = Pattern.compile(DD_MM_YYYY);
		Matcher m = p.matcher(param);
		if (m.find()) {
			String dateString = m.group();
			String[] dateSegments = dateString.split(DELIMITTER_DATE);
			// Following Singaporean date convention: day, month, year
			return LocalDate.of(Integer.parseInt(dateSegments[2]), 
					Integer.parseInt(dateSegments[1]), Integer.parseInt(dateSegments[0]));
		} else {
			return null;
		}
	}
	
	private LocalTime extractLocalTime(String param) {		
		Pattern p = Pattern.compile(HH_MM);
		Matcher m = p.matcher(param);
		if (m.find()) {
			String timeString = m.group();
			String[] timeSegments = timeString.split(DELIMITTER_TIME);
			// Following Singaporean time convention: hour:minute
			return LocalTime.of(Integer.parseInt(timeSegments[0]), Integer.parseInt(timeSegments[1]));
		} else {
			return null;
		}
	}
	
	private String extractFirstWord(String str) {
		str = str.trim();
		if (str.equals(EMPTY_STRING)) {
			return EMPTY_STRING;
		}
		return str.split(WHITESPACE)[ARRAY_POSITION_FIRST];
	}
	
	private String extractSecondWord(String str) {
		str = str.trim();
		if (str.equals(EMPTY_STRING)) {
			return EMPTY_STRING;
		}
		return str.split(WHITESPACE)[ARRAY_POSITION_SECOND];
	}
	
	private String removeFirstWord(String str) {
		str = str.trim();
		int firstSpacePos = str.indexOf(SINGLE_SPACE);
		if (0 < firstSpacePos) {
			str = str.substring(firstSpacePos);
			str = str.trim();
		} else {
			str = EMPTY_STRING;
		}
		return str;
	 }
	
	// return int array of -1's if the input is not valid (all args must be integer)
	private int[] interpretTaskIDs(String param) {
		String[] paramsBeforeParse = param.split(WHITESPACE);
		int[] paramsAfterParse = new int[paramsBeforeParse.length];
		for (int i = 0; i < paramsBeforeParse.length; i++) {
			try {
				paramsAfterParse[i] = Integer.parseInt(paramsBeforeParse[i]);
			} catch (NumberFormatException paramNotInt) {
				//INVALID!
				//Luqman->Hanbin: Why are you making it all invalid?
				//Is it possible that some of the uer inputs are valid?
				/*for (int j = 0; j < paramsAfterParse.length; j++) {
					paramsAfterParse[j] = INT_PARAM_INVALID;
				}
				break;*/
				paramsAfterParse[i] = INT_PARAM_INVALID;
			}
		}
		return paramsAfterParse;
	}
	
	private String interpretFilePath(String userInput) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
