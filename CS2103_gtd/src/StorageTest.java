import static org.junit.Assert.*;

import org.junit.Test;


public class StorageTest {

    Storage storage = new Storage();
    String fileName = "file_path_for_test.txt";

    @Test
    public void setAndGetFilePath() {
        storage.setFilePath(fileName);
        String newFilePath = storage.getFilePath();
        assertEquals("Check that setFilePath() updates the filePath\n", 
                System.getProperty("user.dir") + "/" + fileName, newFilePath);
    }
    
    @Test
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
    public void updateTask() {
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
    
}