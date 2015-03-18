/**
 * Interpreter.java
 * @author Hanbin
 * Created: 03/02/2015
 * Last Updated: 03/03/2015
 */

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {
	
	// Keywords
	private static final String KEYWORD_ADD_DEADLINE = "((by)|(BY)|(due)|(DUE))";
	private static final String KEYWORD_ADD_LOCATION = "((at)|(AT)|(@))";
	private static final String KEYWORD_ADD_EVENTSTART = "((from)|(FROM)|(start)|(START))";
	private static final String KEYWORD_ADD_EVENTEND = "((to)|(TO)|(end)|(END))";
	private static final String[] addParameterKeywords = 
		{KEYWORD_ADD_DEADLINE, KEYWORD_ADD_LOCATION, KEYWORD_ADD_EVENTSTART, KEYWORD_ADD_EVENTEND};
	
	private static final String KEYWORD_DISPLAY_DUE = "((due)|(DUE))";
	private static final String KEYWORD_DISPLAY_AFTER = "((after)|(AFTER))";
	private static final String KEYWORD_DISPLAY_BEFORE = "((before)|(BEFORE))";
	private static final String KEYWORD_DISPLAY_ON = "((on)|(ON))";
	private static final String[] displayParameterKeywords = 
		{KEYWORD_DISPLAY_DUE, KEYWORD_DISPLAY_AFTER, KEYWORD_DISPLAY_BEFORE, KEYWORD_DISPLAY_ON};
	
	private static final String DELIMITTER_DATE = "(\\s|-|/)";
	private static final String DD_MM_YYYY = "\\d\\d" + DELIMITTER_DATE + "\\d\\d" +
			DELIMITTER_DATE + "\\d\\d\\d\\d";
	private static final String DELIMITTER_TIME = "(:)";
	private static final String HH_MM = "(0|1|2)\\d" + DELIMITTER_TIME + "([0-5])\\d";
	
	
	private static final String EMPTY_STRING = "";
	private static final String SINGLE_SPACE = " ";
	private static final String WHITESPACE = "\\s+";
	private static final int ARRAY_POSITION_FIRST = 0;
	private static final String PARAMETER_DOES_NOT_EXIST = null;
	private static final int INT_PARAM_INVALID = -1;
	
	
	public static CommandType interpretCommandType(String usercommand) {
		return determineCommandType(extractFirstWord(usercommand));
	}
	
	// accommodate different names/abbreviations for the same command
	static CommandType determineCommandType(String commandTypeString) {
		//ADD, DISPLAY, DONE, DELETE, EDIT, UNDO, REDO, HELP, SETDIR, EXIT
		if (commandTypeString.equalsIgnoreCase("ADD")) {
			return CommandType.ADD;
		} else if (commandTypeString.equalsIgnoreCase("DISPLAY") ||
				commandTypeString.equalsIgnoreCase("SEARCH")) {
			return CommandType.DISPLAY;
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
	
	// Difference between Add and Edit: Add will fill in unspecified fields of LocalDateTime by default, whereas
	// Edit will only fill in fields of LocalDateTime that need to be changed.
	public static Task interpretAddOREditParameter(String usercommand, CommandType commandType) {
		//it doesn't know that it should ignore the id parameter when edit is being executed
	    if (commandType.equals(CommandType.EDIT)) {
	        usercommand = removeFirstWord(usercommand);
	    }
		KeywordInfoList kList = new KeywordInfoList(usercommand, addParameterKeywords);
		
		String paramDescription = kList.getDescription();
		String paramDeadline = kList.getParameter(KEYWORD_ADD_DEADLINE);
		String paramLocation = kList.getParameter(KEYWORD_ADD_LOCATION);
		String paramEventStart = kList.getParameter(KEYWORD_ADD_EVENTSTART);
		String paramEventEnd = kList.getParameter(KEYWORD_ADD_EVENTEND);
		
		Task newTask = new Task();
		
		if (paramDescription != EMPTY_STRING) {
			newTask.setDescription(paramDescription);
			
			if (paramLocation != PARAMETER_DOES_NOT_EXIST) {
				newTask.setLocation(paramLocation);
			}
			
			if (paramDeadline != PARAMETER_DOES_NOT_EXIST) {
				// task type is a deadline
				LocalDateTime deadline = interpretDateTimeParam(paramDeadline, commandType);
				newTask.setEndDateTime(deadline);
				
			} else if (paramEventStart != PARAMETER_DOES_NOT_EXIST && paramEventEnd != PARAMETER_DOES_NOT_EXIST) {
				// task type is an event
				LocalDateTime eventStart = interpretDateTimeParam(paramEventStart, commandType);
				LocalDateTime eventEnd = interpretDateTimeParam(paramEventEnd, commandType);
				newTask.setStartDateTime(eventStart);
				newTask.setEndDateTime(eventEnd);
				
			} else {
				// task type is floating
				// no more action needed.
			}
		}
		
		return newTask;
	}
	
	public static int interpretEditParameter(String usercommand) {
		String usercommandWithoutFirstWord = removeFirstWord(usercommand);
		int[] parameter = interpretTaskIDs(usercommandWithoutFirstWord);
		return parameter[ARRAY_POSITION_FIRST];
	}
	
	public static Task interpretDisplayParameter(String usercommand) {
		KeywordInfoList kList = new KeywordInfoList(usercommand, displayParameterKeywords);
		String paramDue = kList.getParameter(KEYWORD_DISPLAY_DUE);
		String paramAfter = kList.getParameter(KEYWORD_DISPLAY_AFTER);
		String paramBefore = kList.getParameter(KEYWORD_DISPLAY_BEFORE);
		String paramOn = kList.getParameter(KEYWORD_DISPLAY_ON);
		
		Task newTask = new Task();
		// Storage can take advantage of LocalDateTime.isAfter, LocalDateTime.isBefore.
		// Convention must be set up on how to tell Storage that this Display wants before/after/due/on.
		return newTask;
	}
	
	public static int[] interpretDeleteParameter(String usercommand) {
		String parameter = removeFirstWord(usercommand);
		return interpretTaskIDs(parameter);
	}
	
	public static int[] interpretDoneParameter(String usercommand) {
		String parameter = removeFirstWord(usercommand);
		return interpretTaskIDs(parameter);
	}
	
	static LocalDateTime interpretDateTimeParam(String param, CommandType commandType) {
		// NEED TO BE IMPLEMENTED:
		// Difference between Add and Edit: Add will fill in unspecified fields of LocalDateTime by default, whereas
		// Edit will only fill in fields of LocalDateTime that need to be changed.
		
		// Date example: 02-03-2015
		LocalDate date = extractLocalDate(param);
		// Time example: 8PM | 8:00 | 20:00 | 8
		LocalTime time = extractLocalTime(param);
		
		return LocalDateTime.of(date, time);
	}
	
	static LocalDate extractLocalDate(String param) {		
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
	
	static LocalTime extractLocalTime(String param) {		
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
	
	static String extractFirstWord(String usercommand) {
		usercommand = usercommand.trim();
		if (usercommand.equals(EMPTY_STRING)) {
			return EMPTY_STRING;
		}
		return usercommand.split(WHITESPACE)[ARRAY_POSITION_FIRST];
	}
	
	static String removeFirstWord(String str) {
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
	static int[] interpretTaskIDs(String param) {
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
	
	public static String interpretFilePath(String userInput) {
		// TODO Auto-generated method stub
		return null;
	}

}
