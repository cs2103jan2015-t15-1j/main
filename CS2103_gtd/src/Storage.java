import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//@author A0135280M
public class Storage {
    
    private Map<Integer, Task> tasks;
    private StorageIO storageIO;
    private StorageSearch storageSearch;
    private int lastIdNumber;
    
    public String prepareStorage() {
        storageSearch = new StorageSearch();
        storageIO = new StorageIO();
        String configFilePath = storageIO.initializeConfigFile();
        String setStorageFileFeedback = storageIO.setFilePath(configFilePath);
        initializeTaskList();
        return setStorageFileFeedback;
    }
    
    public String setFilePath(String path) {
        String feedback = storageIO.setFilePath(path);
        initializeTaskList();
        return feedback;
    }
    
    private void initializeTaskList() {
        tasks = new HashMap<Integer, Task>();
        storageIO.getDataFromFile(tasks);
        lastIdNumber = storageIO.getLastIdNumber();
    }
    
    public String getFilePath() {
        return storageIO.getFilePath();
    }

    private int getNextIdNo() {
        lastIdNumber++;
        return lastIdNumber;
    }
    
    public String add(Task newTask) {
        int taskId = newTask.getId();
        if (taskId == Constants.NO_ID_GIVEN) {
            taskId = getNextIdNo();
            newTask.setId(taskId);
        }
        tasks.put(taskId, newTask);
        writeToFile();
        return String.format(Constants.MESSAGE_ADDED, newTask.getId(), newTask.getDescription());
    }
    
    public String delete(int id) {
        Task removedTask = tasks.get(id);
        if (removedTask != null) {
            tasks.remove(id);
            writeToFile();
            return String.format(Constants.MESSAGE_DELETED, removedTask.getId());
        }
        return Constants.MESSAGE_INCORRECT_ID;
    }
    
    public String deleteAll() {
        tasks = new HashMap<Integer, Task>();
        writeToFile();
        return String.format(Constants.MESSAGE_ALL_DELETED);
    }
    
    public String updateDescription(int id, String newDesc) {
        Task taskToUpdate = tasks.get(id);
        if (taskToUpdate != null) {
            taskToUpdate.setDescription(newDesc);
            return updateTask(id, taskToUpdate);
        }
        return Constants.MESSAGE_INCORRECT_ID;
    }
    
    public String updateStartDate(int id, LocalDateTime startDate) {
        Task taskToUpdate = tasks.get(id);
        if (taskToUpdate != null) {
            taskToUpdate.setStartDateTime(startDate);
            return updateTask(id, taskToUpdate);
        }
        return Constants.MESSAGE_INCORRECT_ID;
    }
    
    public String updateEndDate(int id, LocalDateTime endDate) {
        Task taskToUpdate = tasks.get(id);
        if (taskToUpdate != null) {
            taskToUpdate.setEndDateTime(endDate);
            return updateTask(id, taskToUpdate);
        }
        return Constants.MESSAGE_INCORRECT_ID;
    }
    
    private String updateTask(int id, Task updatedTask) {
        if (tasks.get(id) != null) {
            tasks.put(id, updatedTask);
            writeToFile();
            return String.format(Constants.MESSAGE_UPDATED, id);
        }
        return Constants.MESSAGE_INCORRECT_ID;
    }
    
    public String done(int id, boolean setDone) {
        if (tasks.get(id) != null) {
            Task doneTask = tasks.get(id);
            doneTask.setDone(setDone);
            tasks.put(id, doneTask);
            writeToFile();
            return String.format(Constants.MESSAGE_UPDATED, doneTask.getId());
        }
        return Constants.MESSAGE_INCORRECT_ID;
    }
    
    public Task getTask(int id) {
        if (tasks.get(id) != null) {
            return tasks.get(id);
        }
        return null;
    }
    
    public Task getLastAddedTask() {
        return getTask(lastIdNumber);
    }
    
    public Task[] getAllTasks() {
        Task[] taskArray = new Task[tasks.size()];
        int i = 0;
        for (Task task : tasks.values()) {
            taskArray[i] = task;
            i++;
        }
        return taskArray;
    }
    
    public String getTasksAsString() {
        Task[] taskArray = getAllTasks();
        ArrayList<Task> unfinishedTasks = new ArrayList<Task>();
        String allTasks = "";
        for (Task task : taskArray) {
            if (task.getDone()) {
                allTasks += "\n" + task.getUserFormat();
            } else {
                unfinishedTasks.add(task);
            }
        }
        Collections.sort(unfinishedTasks);
        for (Task newTask : unfinishedTasks) {
            allTasks += "\n" + newTask.getUserFormat();
        }
        if (allTasks.equals("")) {
            return Constants.MESSAGE_NO_TASKS;
        }
        return "\n" + Constants.DISPLAY_TABLE_HEADERS + allTasks;
    }
    
    public String search(Task searchObj) {
        String feedback = storageSearch.search(tasks, searchObj, lastIdNumber);
        return feedback;
    }
    
    public void writeToFile() {
        storageIO.writeToFile(tasks);
    }

}
