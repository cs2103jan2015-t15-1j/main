/**
 * 
 * @author Hanbin
 *
 */
public class Interpreter {
	
	private static final String EMPTY_STRING = "";
	private static final String WHITESPACE = "\\s+";
	private static final int POSITION_FIRST = 0;
	
	public static COMMAND_TYPE interpretCommandType(String usercommand) {
		
		return null;
	}
	
	private static String extractFirstWord(String usercommand) {
		if (usercommand.trim().equals(EMPTY_STRING)) {
			return EMPTY_STRING;
		}
		String command = usercommand.trim().split(WHITESPACE)[POSITION_FIRST];
		return command;
	}
	
	// accommodate different names/abbreviations for the same command
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		//ADD, DISPLAY, DONE, DELETE, EDIT, UNDO, REDO, HELP, SETDIR, EXIT
		if (commandTypeString.equalsIgnoreCase("ADD")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("DISPLAY") || 
				commandTypeString.equalsIgnoreCase("SEARCH")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("EDIT")) {
			return COMMAND_TYPE.EDIT;
		} else if (commandTypeString.equalsIgnoreCase("DELETE")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("DONE")) {
			return COMMAND_TYPE.DONE;
		} else if (commandTypeString.equalsIgnoreCase("UNDO")) {
			return COMMAND_TYPE.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("REDO")) {
			return COMMAND_TYPE.REDO;
		} else if (commandTypeString.equalsIgnoreCase("HELP")) {
			return COMMAND_TYPE.HELP;
		} else if (commandTypeString.equalsIgnoreCase("SETDIR") ||
				commandTypeString.equalsIgnoreCase("SETDIRECTORY")) {
			return COMMAND_TYPE.SETDIR;
		} else if (commandTypeString.equalsIgnoreCase("EXIT")) {
			return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	public static Task interpretAddParameter(String usercommand) {
		
	}
	
	public static Task interpretEditParameter(String usercommand) {
		
	}
	
	public static Task interpretDisplayParameter(String usercommand) {
		
	}
	
	public static int[] interpretDeleteParameter(String usercommand) {
		
	}
	
	public static int[] interpretDoneParameter(String usercommand) {
		
	}
}
