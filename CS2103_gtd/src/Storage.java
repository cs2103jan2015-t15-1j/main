import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.json.*; //download: http://mvnrepository.com/artifact/org.json/json


public class Storage {
    
    private Map<Integer, Task> tasks = new HashMap<Integer, Task>();
    private String filePath;
    private int lastIdNumber = 0;
    
    
    // Public methods
    public String prepareStorage(String fileName) {
        assert fileName.length() == 0;
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
    
    public String add(Task newTask) {
    	int taskID = getNextIdNr();
    	newTask.setId(taskID);
        tasks.put(taskID, newTask);
        writeToFile();
        return String.format(Constants.MESSAGE_ADDED, newTask.getId(), newTask.getDescription());
    }
    
    public String delete(int id) {
        Task removedTask = tasks.get(id);
        tasks.remove(id);
        writeToFile();
        return String.format(Constants.MESSAGE_DELETED, removedTask.getId());
    }
    
    public String deleteAll() {
        tasks = new HashMap<Integer, Task>();
        writeToFile();
        return String.format(Constants.MESSAGE_ALL_DELETED);
    }
    
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
    
    public String done(int id) {
        Task doneTask = tasks.get(id);
        doneTask.setDone(true);
        tasks.put(id, doneTask);
        writeToFile();
        return String.format(Constants.MESSAGE_UPDATED, doneTask.getId());
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
        // Use Task object to access not only keyword, but also constraints for startDate, endDate...etc.
        String keyword = searchObj.getDescription();
        String searchResult = "";
        for (Task task : tasks.values()) {
            if (!task.getDone()) { 
                String taskDesc = task.getDescription();
                if (taskDesc.toLowerCase().contains(keyword.toLowerCase())) {
                    searchResult += "\n" + task.getUserFormat();
                }
            }
        }
        if (searchResult.equals("")) {
            return Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
        }
        return searchResult;
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

    // Private methods
    private int getNextIdNr() {
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
            int taskId = getNextIdNr();
            Task newTask = new Task(taskId, desc, startDate, endDate, done);
            tasks.put(taskId, newTask);
        }
    }
    
    private LocalDateTime converteToDate(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(strDate, formatter);
    }


}
