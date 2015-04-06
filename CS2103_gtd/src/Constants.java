import java.time.format.DateTimeFormatter;

public final class Constants {
    
    // Feedback messages
	public static final String MESSAGE_ADDED = "The task %1$d. %2$s, has been added";
	public static final String MESSAGE_DELETED = "Task nr %1$d has been deleted";
	public static final String MESSAGE_UPDATED = "Task nr %1$d has been updated";
	public static final String MESSAGE_ALL_DELETED = "All tasks are now deleted";
	public static final String MESSAGE_NO_TASKS = "You have currently no tasks to show.";
	public static final String MESSAGE_SEARCH_UNSUCCESSFUL = "No tasks containing your keyword was found";
	public static final String MESSAGE_WELCOME = "Welcome to TaskWaltz!";
	public static final String MESSAGE_HELP = "Available commands:\nadd <description> <start time (if any)> <end time(if any)>\n  equivalent: a\n\ndisplay\n  equivalent: dis, show, ls\n\ndone <task identification number(s)>\n  equivalent: do\n\ndelete <task identification number(s)>\n  equivalent: remove, rm, del\n\nedit <task identification number> <new parameters>\n  equivalent: e\n\nundo\n  equivalent: u\n\nredo\n  equivalent: r\n\nsearch <Keyword>\n  equivalent: s\n\nhelp\nsetdir <file path>";
	public static final String MESSAGE_INITIALIZATION_ERROR = "There was a problem opening TaskWaltz";
	public static final String MESSAGE_COMMAND_EXECUTION_ERROR = "TaskWaltz was not able to execute the following command: ";
	public static final String MESSAGE_FILE_CHANGE = "The file %1$s is now used for storing tasks";
	public static final String MESSAGE_ERROR_SET_DICT = "The file name was not input correctly";
	public static final String MESSAGE_INCORRECT_ID = "Provided Task ID(s) was(were) either non-existent or in invalid format";
	public static final String MESSAGE_INVALID_INPUT = "Invalid Input";
	
	// Formatting
	public static final String FORMAT_DISPLAY_TASKINFO = "\nTaskID: %1$d\n%2$s\nStart: %3$s\nEnd: %4$s\n";
	public static final DateTimeFormatter FORMAT_STORAGE_DATETIME = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static final String DISPLAY_TABLE_HEADERS = String.format("%-4s%-6s%-19s%-19s%s", "ID", "Done", "Start Time", "End Time", "Description");
	public static final String DISPLAY_UNFINISHED = "[ ]";
	public static final String DISPLAY_DONE = "[x]";
	
	// Storage values
    public static final String STORAGE_ENTRY_DOES_NOT_EXIST = "EMTPY";
	
	// Numbers
	public static final int NO_ID_GIVEN = -1;
	public static final int INCLUDED_IN_SEARCH = 1;
	public static final int NOT_INCLUDED_IN_SEARCH = 0;
	
	//Default values
	public static final String DEFAULT_STORAGE_PATH = "storage_file.json";
}
