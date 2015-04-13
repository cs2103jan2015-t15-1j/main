import java.time.format.DateTimeFormatter;

//@author A0135280M
public final class Constants {
    
    // Feedback messages
	public static final String MESSAGE_ADDED = "Added new Task:";
	public static final String MESSAGE_DELETED = "Task nr %1$d has been deleted";
	public static final String MESSAGE_UPDATED = "Tasks have been updated:";
	public static final String MESSAGE_ALL_DELETED = "All tasks are now deleted";
	public static final String MESSAGE_NO_TASKS = "You have currently no tasks to show.";
	public static final String MESSAGE_WELCOME = "Welcome to TaskWaltz!";
    public static final String MESSAGE_FILE_CHANGE = "The file %1$s is now used for storing tasks";
    public static final String MESSAGE_EDIT_ORIGINAL = "Original Task:";
    public static final String MESSAGE_EDIT_CHANGE = "Changed to:";
    public static final String MESSAGE_TIME_PERIOD = "DISPLAY TASKS FROM %1$s TO %2$s\n";
    public static final String MESSAGE_DISPLAY_ALL = "DISPLAY ALL TASKS\n";
	
	// Error Messages
    public static final String MESSAGE_GENERAL_ERROR = "Something went wrong";
    public static final String MESSAGE_INITIALIZATION_ERROR = "There was a problem opening TaskWaltz";
    public static final String MESSAGE_COMMAND_EXECUTION_ERROR = "TaskWaltz was not able to execute the following command: ";
	public static final String MESSAGE_NO_UNDO = "No commands to undo";
	public static final String MESSAGE_NO_REDO = "No commands to redo";
	public static final String MESSAGE_INVALID_INPUT = "Invalid Input";
	public static final String MESSAGE_INCORRECT_ID = "Provided Task ID(s) was(were) either non-existent or in invalid format";
    public static final String MESSAGE_NO_DONE_TAKS = "You have no done tasks to delete";
    public static final String MESSAGE_NO_VALID_IDs = "No task(s) corresponding to the requested ID(s) could be found.";
    public static final String MESSAGE_ERROR_SET_DICT = "The file name was not input correctly";
    public static final String MESSAGE_SEARCH_UNSUCCESSFUL = "No tasks matched your search criterias";
    public static final String MESSAGE_UNSUCCESSFUL_FILE_CHANGE = "The storage path could not be changed to %1$s";
    
	// Formatting
    public static final String DISPLAY_TASK_FORMAT = "%-4d%-8s%-5s%-23s%-23s%s";
	public static final String FORMAT_DISPLAY_TASKINFO = "\nTaskID: %1$d\n%2$s\nStart: %3$s\nEnd: %4$s\n";
	public static final DateTimeFormatter FORMAT_STORAGE_DATETIME = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static final String DISPLAY_TABLE_HEADERS = "\n" + String.format("%-4s%-8s%-5s%-23s%-23s%s", "ID", "Status","Done", "Start (DD-MM-YYYY)", "End (DD-MM-YYYY)", "Description");
	public static final String DISPLAY_DONE = "[x]";
	public static final String DISPLAY_UNDONE = "[ ]";
	public static final String DISPLAY_OVERDUE = "overdue";
	public static final String DISPLAY_TIME_NOW = "NOW";
	public static final String EDIT_FORMAT = "%-15s%s";
	
	
	// Numbers
	public static final int NO_ID_GIVEN = -1;
	public static final int INCLUDED_IN_SEARCH = 1;
	public static final int NOT_INCLUDED_IN_SEARCH = 0;
	
	//Default values
	public static final String DEFAULT_STORAGE_PATH = "storage_file.json";
    
    // Help
    public static final String MESSAGE_HELP = "Available commands:\n"
            + "add <description> <start time (if any)> <end time(if any)>\n  equivalent: a\n\n"
            + "delete <task identification number(s)>\n  equivalent: remove, rm, del\n\n"
            + "display\n  equivalent: dis, show, ls\n\n"
            + "done <task identification number(s)>\n  equivalent: do\n\n"
            + "edit <task identification number> <new parameters>\n  equivalent: e\n\n"
            + "exit\n\n"
            + "getdir\n\n"
            + "help\n\n"
            + "redo\n  equivalent: r\n\n"
            + "search <Keyword>\n  equivalent: s\n\n"
            + "setdir <file path>\n\n"
            + "undo\n  equivalent: u\n\n"
            + "undone";
}
