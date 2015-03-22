import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.containsString;

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
                theStoredTask.getDescription(), theDesc);
    }
    
    @Test
    public void deleteAllTasks() {
        storage.setFilePath(fileName);
        Task testTask = new Task();
        testTask.setDescription("test task");
        storage.deleteAll();
        String getTasksFeedback = storage.getTasks();
        assertEquals("Check that deleteAll() deletes all tasks\n", 
                getTasksFeedback, Constants.MESSAGE_NO_TASKS);
    }
    
    @Test
    //No separate partitions and therefore no need to try out different values
    public void deleteTask() {
        storage.setFilePath(fileName);
        storage.deleteAll();
        Task testTask = new Task();
        testTask.setDescription("test task");
        storage.add(testTask);
        int taskId = testTask.getId();
        storage.delete(taskId);
        String getTasksFeedback = storage.getTasks();
        assertEquals("Check that delete(id) deletes the task\n", 
                getTasksFeedback, Constants.MESSAGE_NO_TASKS);
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
        storage.setFilePath(fileName);
        storage.deleteAll();
        Task testTask = new Task();
        String desc = "test task";
        testTask.setDescription(desc);
        storage.add(testTask);
        int taskId = testTask.getId();
        storage.done(taskId, true);
        String displayFeedback = storage.getTasks();
        assertEquals("Check that done(id) removes the task from display\n", 
                displayFeedback, Constants.MESSAGE_NO_TASKS);
    }
    
}