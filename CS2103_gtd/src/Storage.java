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
    
    public static String add(String description, Date startDate, Date endDate) {
        return "feedback";
    }
    
    public static String delete(int id) {
        return "feedback";
    }
    
    public static String update(int id) {
        return "feedback";
    }
    
    public static String done(int id) {
        return "feedback";
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
