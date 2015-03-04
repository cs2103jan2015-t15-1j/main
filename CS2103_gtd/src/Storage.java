import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.json.*; //download: http://mvnrepository.com/artifact/org.json/json


public class Storage {
    
    private static Map<Integer, Task> tasks = new HashMap<Integer, Task>();
    private static String filePath;
    private static int lastIdNumber = 0;
    
    // Public methods
    public static String prepareStorage(String fileName) {
        setFilePath(fileName);
        getDataFromFile();
        return String.format(Constants.MESSAGE_WELCOME, fileName);
    }
    
    public static String setFilePath(String fileName) {
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
    
    public static String add(Task newTask) {
        tasks.put(getNextIdNr(), newTask);
        writeToFile();
        return String.format(Constants.MESSAGE_ADDED, newTask.getUserFormat());
    }
    
    public static String delete(int id) {
        Task removedTask = tasks.get(id);
        tasks.remove(id);
        writeToFile();
        return String.format(Constants.MESSAGE_DELETED, removedTask.getUserFormat());
    }
    
    public static String update(Task changes) {
        int idToUpdate = changes.getId();
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
        return String.format(Constants.MESSAGE_UPDATED, taskToUpdate.getUserFormat());
    }
    
    public static String done(int id) {
        Task doneTask = tasks.get(id);
        doneTask.setDone(true);
        tasks.put(id, doneTask);
        writeToFile();
        return String.format(Constants.MESSAGE_UPDATED, doneTask.getUserFormat());
    }
    
    public static String getTasks() {
        if (tasks.isEmpty()) {
            return Constants.MESSAGE_NO_TASKS;
        }
        String allTasks = "";
        for (Task task : tasks.values()) {
            allTasks += task.getId() + ". " + task.getUserFormat() + "\n";
        }
        return allTasks;
    }
    
    public static String search(Task searchObj) {
        // Use Task object to access not only keyword, but also constraints for startDate, endDate...etc.
        String keyword = searchObj.getDescription();
        String searchResult = "";
        for (Task task : tasks.values()) {
            String taskDesc = task.getUserFormat();
            if (taskDesc.toLowerCase().contains(keyword.toLowerCase())) {
                searchResult += task.getId() + ". " + task.getUserFormat() + "\n";
            }
        }
        if (searchResult == "") {
            return Constants.MESSAGE_SEARCH_UNSUCCESSFUL;
        }
        return searchResult;
    }
    
    public static String undo() {
        return "feedback";
    }
    
    public static String redo() {
        return "feedback";
    }
    
    public static String exit() {
        writeToFile();
        return "feedback";
    }

    // Private methods
    private static int getNextIdNr() {
        lastIdNumber++;
        return lastIdNumber;
    }
    
    private static void getDataFromFile() {
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
        createTasksFromJson(jsonStr);
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
    private static void createTasksFromJson(String jsonStr) {
        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray jsonArr = jsonObj.getJSONArray("tasks");
        for (int i = 0; i < jsonArr.length(); i++)  {
            JSONObject currentObj = jsonArr.getJSONObject(i);
            String desc = currentObj.getString("desc");
            LocalDateTime startDate = converteToDate(currentObj.getString("startDate"));
            LocalDateTime endDate = converteToDate(currentObj.getString("endDate"));
            boolean done = currentObj.getBoolean("done");
            int taskId = getNextIdNr();
            Task newTask = new Task(taskId, desc, startDate, endDate, done);
            tasks.put(taskId, newTask);
        }
    }
    
    private static LocalDateTime converteToDate(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(strDate, formatter);
    }
    
    
    private static void writeToFile() {
        JSONObject jsonObj = new JSONObject();
 
        JSONArray jsonArray = new JSONArray();
        for (Task task : tasks.values()) {
            JSONObject taskObj = new JSONObject();
            String desc = task.getDescription();
            taskObj.put("desc", desc);
            String startDate = task.getStartDateTime().toString();
            taskObj.put("startDate", startDate);
            String endDate = task.getStartDateTime().toString();
            taskObj.put("endDate", endDate);
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


}
