import java.time.format.DateTimeFormatter;

public final class Constants {

	public static final String MESSAGE_ADDED = "Your task %1$s has been added";
	public static final String MESSAGE_DELETED = "The task %1$s has been deleted";
	public static final String MESSAGE_UPDATED = "The task %1$s has been updated";
	public static final String MESSAGE_NO_TASKS = "You have currently no tasks.";
	public static final String MESSAGE_SEARCH_UNSUCCESSFUL = "No tasks containing your keyword was found";
	public static final String MESSAGE_WELCOME = "Welcome to TaskWaltz!\nThe file %1$s is ready for storing your tasks";
	public static final String MESSAGE_HELP = "Available commands:\nadd <description> <start time (if any)> <end time(if any)>\ndisplay\ndone <task identification number(s)>\ndelete <task identification number(s)>\nedit <task identification number> <new parameters>\nundo\nredo\nsearch <Keyword>\nhelp\nsetdir <file path>";
	public static final String MESSAGE_INITIALIZATION_ERROR = "There was a problem opening TaskWaltz";
	public static final String MESSAGE_COMMAND_EXECUTION_ERROR = "TaskWaltz was not able to execute the following command: ";
	public static final String MESSAGE_FILE_CHANGE = "The file %1$s is now used for storing tasks";
	
	public static final String STORAGE_ENTRY_DOES_NOT_EXIST = "EMTPY";
	public static final String FORMAT_DISPLAY_TASKINFO = "\nTaskID: %1$d\n%2$s\nStart: %3$s\nEnd: %4$s\n";
	public static final DateTimeFormatter FORMAT_STORAGE_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

}
