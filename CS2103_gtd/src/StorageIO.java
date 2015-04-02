import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


public class StorageIO {
    
    private String filePath;
    private int lastIdNumber = 0;
    
    public String getFilePath() {
        return filePath;
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
            FileWriter file = new FileWriter(filePath);
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
