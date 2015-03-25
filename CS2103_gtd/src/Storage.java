import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.json.*; //download: http://mvnrepository.com/artifact/org.json/json


public class Storage {
    
    private Map<Integer, Task> tasks = new HashMap<Integer, Task>();
    private String filePath;
    private int lastIdNumber = 0;
    
    public String prepareStorage(String fileName) {
        setFilePath(fileName);
        getDataFromFile();
        return String.format(Constants.MESSAGE_WELCOME, fileName);
    }
    
    public String setFilePath(String fileName) {
        //TODO: check if file/filename is valid
        //TODO: delete data from old file
        filePath = System.getProperty("user.dir") + "/" + fileName;
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.format(Constants.MESSAGE_FILE_CHANGE, fileName);
    }
    
    public String getFilePath() {
        return filePath;
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
    
    //REMOVE THIS ONE WHEN COMMAND PATTERN IMPLEMENTED. DONT USE FOR NEW IMPLEMENTATIONS!
    public String update(int idToUpdate, Task changes) {
        //int idToUpdate = changes.getId();
        Task taskToUpdate = tasks.get(idToUpdate);
        if (changes.getDescription() != null) {
            taskToUpdate.setDescription(changes.getDescription());
        }
        if (changes.getStartDateTime() != null) {
            taskToUpdate.setStartDateTime(changes.getStartDateTime());
        }
        if (changes.getEndDateTime() != null) {
            taskToUpdate.setEndDateTime(changes.getEndDateTime());
        }
        tasks.put(idToUpdate, taskToUpdate);
        writeToFile();
        return String.format(Constants.MESSAGE_UPDATED, taskToUpdate.getId());
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
    
    public String updateLocation(int id, String location) {
        Task taskToUpdate = tasks.get(id);
        if (taskToUpdate != null) {
            taskToUpdate.setLocation(location);
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
    
    public String getTasks() {
        if (tasks.isEmpty()) {
            return Constants.MESSAGE_NO_TASKS;
        }
        String allTasks = "\n" + Constants.DISPLAY_TABLE_HEADERS;
        for (Task task : tasks.values()) {
            if (!task.getDone()) { 
                allTasks += "\n" + task.getUserFormat();
            }
        }
        return allTasks;
    }
    
    public String search(Task searchObj) {
        String keyword = searchObj.getDescription();
        LocalDateTime startDate = searchObj.getStartDateTime();
        LocalDateTime endDate = searchObj.getEndDateTime();
        
        int[] foundTasks = new int[lastIdNumber];
        foundTasks = searchOnKeyword(keyword, foundTasks);
        foundTasks = searchOnDate(startDate, endDate, foundTasks);

        String searchResult = "";
        for (int i=0; i<foundTasks.length; i++) {
            if (foundTasks[i] == Constants.INCLUDED_IN_SEARCH) {
                Task task = tasks.get(i+1);
                searchResult += "\n" + task.getUserFormat();
            }
        }
        
        if (searchResult.equals("")) {
            return Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
        }
        return Constants.DISPLAY_TABLE_HEADERS+"\n"+searchResult;
    }
    
    private int[] searchOnKeyword(String keyword, int[] foundTasks) {
        for (Task task : tasks.values()) {
            if (!task.getDone()) { 
                String taskDesc = task.getDescription();
                if (taskDesc.toLowerCase().contains(keyword.toLowerCase())) {
                    int index = task.getId()-1;
                    foundTasks[index] = Constants.INCLUDED_IN_SEARCH;
                }
            }
        }
        return foundTasks;
    }
    
    private int[] searchOnDate(LocalDateTime startDate, LocalDateTime endDate, 
            int[] foundTasks) {
        if (isDateSearch(startDate, endDate)) {
            for (Task task : tasks.values()) {
                if (!task.getDone()) {
                    int index = task.getId()-1;
                    foundTasks[index] = isTaskInInterval(task, startDate, 
                            endDate, foundTasks[index]);
                }
            }
        }
        return foundTasks;
    }
    
    private int isTaskInInterval(Task task, LocalDateTime searchStartDate, 
            LocalDateTime searchEndDate, int originalValue) {
        LocalDateTime taskStartDate = task.getStartDateTime();
        LocalDateTime taskEndDate = task.getEndDateTime();
        boolean startIsAfter = false;
        boolean startIsOn = false;
        boolean endIsBefore = false;
        boolean endIsOn = false;
        
        if (isFloatingTask(taskEndDate)) {
            return Constants.NOT_INCLUDED_IN_SEARCH;
        } else if (isDeadlineTask(taskStartDate)) {
            startIsAfter = taskEndDate.isAfter(searchStartDate);
            startIsOn = taskEndDate.equals(searchStartDate);
        } else {
            startIsAfter = taskStartDate.isAfter(searchStartDate);
            startIsOn = taskStartDate.equals(searchStartDate);
        }
        endIsBefore = taskEndDate.isBefore(searchEndDate);
        endIsOn = taskEndDate.equals(searchEndDate);
        
        if (isNotInInterval(startIsAfter, startIsOn, endIsBefore, endIsOn)) {
            return Constants.NOT_INCLUDED_IN_SEARCH;
        }
        return originalValue;
    }
    
    private boolean isDateSearch(LocalDateTime startDate, LocalDateTime endDate) {
        return !(startDate.equals(LocalDateTime.MIN) && endDate.equals(LocalDateTime.MAX));
    }
    
    private boolean isFloatingTask(LocalDateTime taskEndDate) {
        return taskEndDate == null;
    }
    
    private boolean isDeadlineTask(LocalDateTime taskStartDate) {
        return taskStartDate == null;
    }
    
    private boolean isNotInInterval(boolean startIsAfter, boolean startIsOn, 
            boolean endIsBefore, boolean endIsOn) {
        return !((startIsAfter || startIsOn) && (endIsBefore || endIsOn));
    }
    
    public void writeToFile() {
        JSONObject jsonObj = new JSONObject();
 
        JSONArray jsonArray = new JSONArray();
        for (Task task : tasks.values()) {
            JSONObject taskObj = new JSONObject();
            String desc = task.getDescription();
            taskObj.put("desc", desc);
            String startDateTime = task.getStartDateTimeInString();
            taskObj.put("startDate", startDateTime);
            String endDateTime = task.getEndDateTimeInString();
            taskObj.put("endDate", endDateTime);
            String done = task.getDone() + "";
            taskObj.put("done", done);
            jsonArray.put(taskObj);
        }
        
        jsonObj.put("tasks", jsonArray);
 
        try {
            FileWriter file = new FileWriter(filePath);
            file.write(jsonObj.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNextIdNo() {
        lastIdNumber++;
        return lastIdNumber;
    }
    
    private void getDataFromFile() {
        String jsonStr = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            while (line != null) {
                jsonStr += " " + line;
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonStr.length() > 0) {
            createTasksFromJson(jsonStr);
        }
    }
    
    /* JSON example:
     * { tasks: [
     *      {
     *          desc: do something,
     *          startDate: 190215
     *          endDate: 200215
     *          done: false
     *      },
     *      { ... },
     *      { ... },
     *      ...]
     *  }
     */
    private void createTasksFromJson(String jsonStr) {
        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray jsonArr = jsonObj.getJSONArray("tasks");
        for (int i = 0; i < jsonArr.length(); i++)  {
            JSONObject currentObj = jsonArr.getJSONObject(i);
            String desc = currentObj.getString("desc");
            String startDateString = currentObj.getString("startDate");
            LocalDateTime startDate = null;
            if (!startDateString.equals("")) {
            	startDate = converteToDate(startDateString);
            }
            String endDateString = currentObj.getString("endDate");
            LocalDateTime endDate = null;
            if (!endDateString.equals("")) {
                endDate = converteToDate(endDateString);
            }
            boolean done = currentObj.getBoolean("done");
            int taskId = getNextIdNo();
            Task newTask = new Task(taskId, desc, startDate, endDate, done);
            tasks.put(taskId, newTask);
        }
    }
    
    private LocalDateTime converteToDate(String strDate) {
        return LocalDateTime.parse(strDate, Constants.FORMAT_STORAGE_DATETIME);
    }


}
