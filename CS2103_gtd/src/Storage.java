import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.json.*; //download: http://mvnrepository.com/artifact/org.json/json


public class Storage {
    
    private static Map<Integer, Task> tasks = new HashMap<Integer, Task>();
    private static Stack undoStack = new Stack();
    private static String filePath;
    private static int lastIdNumber = 0;
    
    // Public methods
    public static String prepareStorage(String fileName) {
        setFilePath(fileName);
        getDataFromFile();
        return String.format(Constants.MSG_TASK_FILE, fileName);
    }
    
    public static void setFilePath(String fileName) {
        //todo: check if file/filename is valid
        filePath = System.getProperty("user.dir") + "/" + fileName;
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String add(Task newTask) {
        tasks.put(getNextIdNr(), newTask);
        writeToFile();
        return String.format(Constants.MSG_ADDED, newTask.getUserFormat());
    }
    
    public static String delete(int id) {
        Task removedTask = tasks.get(id);
        tasks.remove(id);
        writeToFile();
        return String.format(Constants.MSG_DELETED, removedTask.getUserFormat());
    }
    
    public static String update(Task changes) {
        idToUpdate = changes.getId();
        Task taskToUpdate = tasks.get(idToUpdate);
        if (changes.desc != null) {
            taskToUpdate.setDescription(changes.desc);
        }
        if (changes.startDate != null) {
            taskToUpdate.setStartDate(changes.startDate);
        }
        if (changes.endDate != null) {
            taskToUpdate.setEndDate(changes.endDate);
        }
        tasks.put(idToUpdate, taskToUpdate);
        writeToFile();
        return String.format(Constants.MSG_UPDATED, taskToUpdate.getUserFormat());
    }
    
    public static String done(int id) {
        Task doneTask = tasks.get(id);
        doneTask.setDone(true);
        tasks.put(id, doneTask);
        writeToFile();
        return String.format(Constants.MSG_UPDATED, doneTask.getUserFormat());
    }
    
    public static String getTasks() {
        if (tasks.isEmpty()) {
            return Constants.MSG_NO_TASKS;
        }
        String allTasks = "";
        for (Task task : tasks.values()) {
            allTasks += task.getId() + ". " + task.getUserFormat() + "\n";
        }
        return allTasks;
    }
    
    public static String search(String keyword) {
        String searchResult = "";
        for (Task task : tasks.values()) {
            String taskDesc = task.getUserFormat();
            if (taskDesc.toLowerCase().contains(keyword.toLowerCase())) {
                searchResult += task.getId() + ". " + task.getUserFormat() + "\n";
            }
        }
        if (searchResult == "") {
            return Constants.MSG_SEARCH_UNSUCCESSFUL;
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
            currentObj = arr.getInt(i);
            String desc = jsonObj.getString("desc");
            Date startDate = jsonObj.getDate("startDate");
            Date startDate = jsonObj.getDate("endDate");
            boolean done = jsonObj.getBoolean("done");
            Task newTask = new Task(desc, startDate, endDate, done); 
            tasks.put(i, newTask);
        }
    }
    
    private static void writeToFile() {
        JSONObject jsonObj = new JSONObject();
 
        JSONArray jsonArray = new JSONArray();
        for (Task task : tasks.values()) {
            JSONObject taskObj = new JSONObject();
            String desc = task.getDescription();
            taskObj.put("desc: "+desc);
            Date startDate = task.getStartDate().toString();
            taskObj.put("startDate: "+startDate);
            Date endDate = task.getStartDate().toString();
            taskObj.put("endDate: "+endDate);
            String done = task.getDone().toString();
            taskObj.put("done: "+done);
            jsonArray.add(taskObj);
        }
        
        jsonObj.put("tasks", jsonArray);
 
        FileWriter file = new FileWriter(filePath);
        try {
            file.write(jsonObj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }
    
    private static void addUndo(COMMAND_TYPE ct, int taskId) {
         
    }
    
    private static void addRedo(COMMAND_TYPE ct, int taskId) {
        
    }


}
