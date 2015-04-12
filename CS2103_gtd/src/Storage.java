import java.time.LocalDateTime;
import java.util.ArrayList;
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
        return newTask.getUserFormat();
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
            return doneTask.getUserFormat();
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
    
    public String display(Task displayObj) {
        String displayTasks = "\n";
        
        if (displayObj.isEventTask()) {
            String startTime = displayObj.getStartDateTimeInString();
            String endTime = displayObj.getEndDateTimeInString();
            displayTasks += String.format(Constants.MESSAGE_TIME_PERIOD, startTime, endTime);
        } else if (displayObj.isDeadlineTask()) {
            String endTime = displayObj.getEndDateTimeInString();
            displayTasks += String.format(Constants.MESSAGE_TIME_PERIOD, "now", endTime);
        } else {
            displayTasks += Constants.MESSAGE_DISPLAY_ALL;
        }
        
        displayTasks += Constants.DISPLAY_TABLE_HEADERS;
        displayTasks += storageSearch.search(tasks, displayObj, lastIdNumber);
        displayTasks += getFloatingTasksAsString();
        return displayTasks;
    }
    
    public String search(Task searchObj) {
        String feedback;
        String result = storageSearch.search(tasks, searchObj, lastIdNumber);
        if (result.equals("")) {
            feedback = Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
        } else {
            feedback = Constants.DISPLAY_TABLE_HEADERS+"\n"+result;
        }
        return feedback;
    }
    
    public String getTasksAsString() {
        ArrayList<Task> taskList = new ArrayList<Task>();
        taskList.addAll(getDoneTasks());
        taskList.addAll(getUnfinishedTasks());
        
        String allTasks = "";
        for (Task newTask : taskList) {
            allTasks += "\n" + newTask.getUserFormat();
        }
        if (allTasks.equals("")) {
            return Constants.MESSAGE_NO_TASKS;
        }
        return "\n" + Constants.DISPLAY_TABLE_HEADERS + allTasks;
    }
    
    private ArrayList<Task> getUnfinishedTasks() {
        Task[] taskArray = getAllTasks();
        ArrayList<Task> unfinishedTasks = new ArrayList<Task>();
        for (Task task : taskArray) {
            if (!task.isDone()) {
                unfinishedTasks.add(task);
            }
        }
        Collections.sort(unfinishedTasks);
        return unfinishedTasks;
    }
    
    private ArrayList<Task> getDoneTasks() {
        Task[] taskArray = getAllTasks();
        ArrayList<Task> doneTasks = new ArrayList<Task>();
        for (Task task : taskArray) {
            if (task.isDone()) {
                doneTasks.add(task);
            }
        }
        return doneTasks;
    }

    public String getDoneTasksAsString() {
        String doneTasksString = "";
        ArrayList<Task> doneTasks = getDoneTasks();
        for (Task task : doneTasks) {
            doneTasksString += "\n" + task.getUserFormat();
        }
        return doneTasksString;
    }
    
    private String getFloatingTasksAsString() {
        String floatingTasksString = "";
        for (Task task : tasks.values()) {
            if (task.isFloatingTask()) {
                floatingTasksString += "\n" + task.getUserFormat();
            }
        }
        return floatingTasksString;
    }
    
    public void writeToFile() {
        storageIO.writeToFile(tasks);
    }

}
