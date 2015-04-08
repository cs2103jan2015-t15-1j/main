import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

//@author A0135280M
public class StorageIO {
    
    private String storageFilePath;
    private String configFilePath;
    private int lastIdNumber;
    
    public String getFilePath() {
        return storageFilePath;
    }
    
    public String initializeConfigFile() {
        configFilePath = System.getProperty("user.dir") + "/config.txt";
        File file = new File(configFilePath);
        String storageFilePath = "";
        try {
            if (!file.exists()) {
                file.createNewFile();
                storageFilePath = getDefaultStoragePath();
            } else {
                BufferedReader br = new BufferedReader(new FileReader(configFilePath));
                storageFilePath = br.readLine();
                br.close();
                if (storageFilePath == null) {
                    storageFilePath = getDefaultStoragePath();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return storageFilePath;
    }
    
    private String getDefaultStoragePath() {
        return System.getProperty("user.dir") + "/" + Constants.DEFAULT_STORAGE_PATH;
    }
    
    public String setFilePath(String path) {
        storageFilePath = path;
        File file = new File(storageFilePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            File configFile = new File(configFilePath);
            BufferedWriter output = new BufferedWriter(new FileWriter(configFile));
            output.write(path);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.format(Constants.MESSAGE_FILE_CHANGE, storageFilePath);
    }
    
    public int getLastIdNumber() {
        return lastIdNumber;
    }
    
    public void writeToFile(Map<Integer, Task> tasks) {
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
            FileWriter file = new FileWriter(storageFilePath);
            file.write(jsonObj.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void getDataFromFile(Map<Integer, Task> tasks) {
        String jsonStr = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(storageFilePath));
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
            createTasksFromJson(jsonStr, tasks);
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
    private void createTasksFromJson(String jsonStr, Map<Integer, Task> tasks) {
        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray jsonArr = jsonObj.getJSONArray("tasks");
        lastIdNumber = 0;
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
            lastIdNumber++;
            Task newTask = new Task(lastIdNumber, desc, startDate, endDate, done);
            tasks.put(lastIdNumber, newTask);
        }
    }
    
    private LocalDateTime converteToDate(String strDate) {
        return LocalDateTime.parse(strDate, Constants.FORMAT_STORAGE_DATETIME);
    }

}
