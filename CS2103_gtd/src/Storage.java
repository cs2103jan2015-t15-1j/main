import java.sql.Date;
import java.util.HashMap;
import java.util.Stack;


public class Storage {
    
    private static HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    private static Stack undoStack = new Stack();
    
    // Public methods
    public static String prepareStorage() {
        return "feedback";
    }
    
    public static String add(String desc, Date startDate, Date endDate) {
        Task newTask = new Task(desc, startDate, endDate);
        tasks.put(Task.getId(), Task);
        return String.format(Constants.MSG_ADDED, Task.getUserFormat());
    }
    
    public static String delete(int id) {
        Task removedTask = tasks.get(id);
        tasks.remove(id);
        return String.format(Constants.MSG_DELETED, removedTask.getUserFormat());
    }
    
    public static String update(int id, String desc, Date startDate, Date endDate) {
        Task updatedTask = tasks.get(id);
        if (desc != null) {
            updatedTask.setDescription(desc);
        }
        if (startDate != null) {
            updatedTask.setStartDate(startDate);
        }
        if (endDate != null) {
            updatedTask.setEndDate(endDate);
        }
        tasks.put(id, updatedTask);
        return String.format(Constants.MSG_UPDATED, updatedTask.getUserFormat());
    }
    
    public static String done(int id) {
        Task doneTask = tasks.get(id);
        doneTask.setDone(true);
        tasks.put(id, doneTask);
        return String.format(Constants.MSG_UPDATED, doneTask.getUserFormat());
    }
    
    public static String getTasks() {
        return "feedback";
    }
    
    public static String search(String keyword) {
        return "task string or error message";
    }
    
    public static String undo() {
        return "feedback";
    }
    
    public static String redo() {
        return "feedback";
    }

    // Private methods
    private static void initializeDataStructure() {
        
    }
    
    private static void getDataFromFile() {
        
    }
    
    private static void addUndo(COMMAND_TYPE ct, int taskId) {
         
    }
    
    private static void addRedo(COMMAND_TYPE ct, int taskId) {
        
    }
    
    private static void writeToFile() {
        
    }


}
