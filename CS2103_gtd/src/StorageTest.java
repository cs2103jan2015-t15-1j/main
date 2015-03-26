import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.containsString;
import java.time.LocalDateTime;
import org.junit.Test;


public class StorageTest {

    Storage storage = new Storage();
    String fileName = "file_path_for_test.txt";

    @Test
    // Need to try out to be able to use it in other tests
    public void setAndGetFilePath() {
        storage.setFilePath(fileName);
        String newFilePath = storage.getFilePath();
        assertEquals("Check that setFilePath() updates the filePath\n", 
                System.getProperty("user.dir") + "/" + fileName, newFilePath);
    }
    
    @Test
    // Only floating tasks working right now
    // - later do tests for event and deadline tasks as well
    public void addTask() {
        storage.setFilePath(fileName);
        Task testTask = new Task();
        String theDesc = "test task";
        testTask.setDescription(theDesc);
        storage.add(testTask);
        int taskId = testTask.getId();
        Task theStoredTask = storage.getTask(taskId);
        assertEquals("Check that adding a task worked\n", 
                theDesc, theStoredTask.getDescription());
    }
    
    @Test
    public void deleteAllTasks() {
        storage.setFilePath(fileName);
        Task testTask = new Task();
        testTask.setDescription("test task");
        storage.deleteAll();
        String tasksFeedback = storage.getTasksAsString();
        assertEquals("Check that deleteAll() deletes all tasks\n", 
                Constants.MESSAGE_NO_TASKS, tasksFeedback);
    }
    
    private void initialize() {
        storage.setFilePath(fileName);
        storage.deleteAll();
    }
    
    @Test
    //No separate partitions and therefore no need to try out different values
    public void deleteTask() {
        initialize();
        Task testTask = new Task();
        testTask.setDescription("test task");
        storage.add(testTask);
        int taskId = testTask.getId();
        storage.delete(taskId);
        String tasksFeedback = storage.getTasksAsString();
        assertEquals("Check that delete(id) deletes the task\n", 
                Constants.MESSAGE_NO_TASKS, tasksFeedback);
    }
    
    @Test
    // Only floating tasks working right now
    // - later do tests for event and deadline tasks as well
    public void updateTaskDescription() {
        storage.setFilePath(fileName);
        Task testTask = new Task();
        testTask.setDescription("test task");
        storage.add(testTask);
        int taskId = testTask.getId();
        String newDesc = "new description";
        storage.updateDescription(taskId, newDesc);
        String acctualNewDesc = storage.getTask(taskId).getDescription();
        assertEquals("Check that the description can be updated\n", 
                newDesc, acctualNewDesc);
    }
    
    @Test
    // Could try out to both set to done and undone, but the implementation 
    // is the same and therefore not efficient to try both
    public void setToDone() {
        initialize();
        Task testTask = new Task();
        String desc = "test task";
        testTask.setDescription(desc);
        storage.add(testTask);
        int taskId = testTask.getId();
        storage.done(taskId, true);
        String displayFeedback = storage.getTasksAsString();
        assertEquals("Check that done(id) removes the task from display\n", 
                Constants.MESSAGE_NO_TASKS, displayFeedback);
    }
    
    private void createTask(String taskDesc, String startDateString, 
            String endDateString) {
        Task testTask = new Task();
        testTask.setDescription(taskDesc);
        if (startDateString != null) {
            LocalDateTime startDate = LocalDateTime.parse(startDateString, Constants.FORMAT_STORAGE_DATETIME);
            testTask.setStartDateTime(startDate);
        } else {
            testTask.setStartDateTime(null);
        }
        if (endDateString != null) {
            LocalDateTime endDate = LocalDateTime.parse(endDateString, Constants.FORMAT_STORAGE_DATETIME);
            testTask.setEndDateTime(endDate);
        } else {
            testTask.setEndDateTime(null);
        }
        storage.add(testTask);
    }
    
    private Task createSearchObj(String taskDesc, LocalDateTime startDate, 
            LocalDateTime endDate) {
        Task searchObj = new Task();
        searchObj.setDescription(taskDesc);
        searchObj.setStartDateTime(startDate);
        searchObj.setEndDateTime(endDate);
        return searchObj;
    }
    
    @Test
    // Search for tasks on a specific date/time
    public void searchOn() {
        initialize();
        
        String taskDesc = "search task";
        String startDateString = "15-01-2015 10:00";
        String endDateString = "15-01-2015 12:00";
        createTask(taskDesc, startDateString, endDateString);
        
        LocalDateTime startDate = LocalDateTime.parse(startDateString, Constants.FORMAT_STORAGE_DATETIME);
        LocalDateTime endDate = LocalDateTime.parse(endDateString, Constants.FORMAT_STORAGE_DATETIME);
        Task searchObj = createSearchObj("", startDate, endDate);
        String feedback = storage.search(searchObj);
        
        assertThat("Check that search shows an event starting on that time\n", 
                feedback, containsString(taskDesc));
    }
    
    @Test
    // Search after a date/time
    public void searchAfter() {
        initialize();
        
        String taskDesc = "search task";
        String startDateString = "15-01-2015 10:00";
        String endDateString = "15-01-2015 12:00";
        createTask(taskDesc, startDateString, endDateString);
        
        String searchDateString = "15-01-2015 08:00";
        LocalDateTime searchStartDate = LocalDateTime.parse(searchDateString, Constants.FORMAT_STORAGE_DATETIME);
        
        Task searchObj = createSearchObj("", searchStartDate, LocalDateTime.MAX);
        String feedback = storage.search(searchObj);
        
        assertThat("Check that search shows an event starting after that time\n", 
                feedback, containsString(taskDesc));
    }
    
    @Test
    // Search before a date/time
    public void searchBefore() {
        initialize();
        
        String taskDesc = "search task";
        String startDateString = "15-01-2015 10:00";
        String endDateString = "15-01-2015 12:00";
        createTask(taskDesc, startDateString, endDateString);
        
        String searchDateString = "15-01-2015 13:00";
        LocalDateTime searchEndDate = LocalDateTime.parse(searchDateString, Constants.FORMAT_STORAGE_DATETIME);
        
        Task searchObj = createSearchObj("", LocalDateTime.MIN, searchEndDate);
        String feedback = storage.search(searchObj);
        
        assertThat("Check that search shows an event happening before that time\n", 
                feedback, containsString(taskDesc));
    }
    
    @Test
    // Search doesn't show events happening outside of the search span
    public void searchWithoutFinding() {
        initialize();
        
        String taskDesc = "search task";
        String endDateString = "15-01-2015 12:00";
        createTask(taskDesc, null, endDateString);
        
        String searchDateString = "15-01-2015 10:00";
        LocalDateTime searchEndDate = LocalDateTime.parse(searchDateString, Constants.FORMAT_STORAGE_DATETIME);
        
        Task searchObj = createSearchObj("", LocalDateTime.MIN, searchEndDate);
        String feedback = storage.search(searchObj);
        
        assertEquals("Check that search desn't show events happening after given time\n", 
                feedback, Constants.MESSAGE_SEARCH_UNSUCCESSFUL);
    }
    @Test
    // Search for string
    public void searchForString() {
        initialize();
        
        String taskDesc = "search task";
        createTask(taskDesc, null, null);
        
        Task searchObj = createSearchObj("", LocalDateTime.MIN, LocalDateTime.MAX);
        String feedback = storage.search(searchObj);
        
        assertThat("Check that search can find tasks on substrings of the description\n", 
                feedback, containsString("search"));
    }
    
}