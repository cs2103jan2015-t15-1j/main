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

	/**
	 * Get the storage file path if any saved in the config file, otherwise 
	 * create a new one.
	 * @return the storage file path
	 */
	public String initializeConfigFile() throws IOException {
		configFilePath = System.getProperty("user.dir") + "/config.txt";
		File file = new File(configFilePath);
		String storageFilePath = "";
		try {
			if (!file.exists()) {
				file.createNewFile();
				storageFilePath = getDefaultStoragePath();
			} else {
				BufferedReader br = new BufferedReader(new FileReader(
						configFilePath));
				storageFilePath = br.readLine();
				br.close();
				if (storageFilePath == null) {
					storageFilePath = getDefaultStoragePath();
				}
			}
		} catch (IOException e) {
			throw new IOException(Constants.MESSAGE_ERROR_CONFIG_FILE);
		}
		return storageFilePath;
	}

	private String getDefaultStoragePath() {
		return System.getProperty("user.dir") + "/"
				+ Constants.DEFAULT_STORAGE_PATH;
	}

	/**
	 * Set a new storage file path and save it to the config file
	 * @param path     the new storage file path
	 */
	public String setFilePath(String path) throws IOException {
		if (!path.contains(".json")) {
			return Constants.MESSAGE_ERROR_NOT_JSON;
		}
		storageFilePath = path;
		File file = new File(storageFilePath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			File configFile = new File(configFilePath);
			configFile.createNewFile(); // to remove the old config file if any
			BufferedWriter output = new BufferedWriter(new FileWriter(
					configFile));
			output.write(path); // write the storage file path
			output.flush();
			output.close();
			return String
					.format(Constants.MESSAGE_FILE_CHANGE, storageFilePath);
		} catch (IOException e) {
			throw new IOException(
					"TaskWaltz was not able to set your directory to " + path);
		}

	}

	public int getLastIdNumber() {
		return lastIdNumber;
	}

	/**
	 * Turn the task objects into JSON and save to file
	 */
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
			String done = task.isDone() + "";
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

	/**
	 * Read data from the storage file and create Task objects
	 */
	public void getDataFromFile(Map<Integer, Task> tasks) {
		String jsonStr = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					storageFilePath));
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

	private void createTasksFromJson(String jsonStr, Map<Integer, Task> tasks) {
		JSONObject jsonObj = new JSONObject(jsonStr);
		JSONArray jsonArr = jsonObj.getJSONArray("tasks");
		lastIdNumber = 0;
		for (int i = 0; i < jsonArr.length(); i++) {
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
			Task newTask = new Task(lastIdNumber, desc, startDate, endDate,
					done);
			tasks.put(lastIdNumber, newTask);
		}
	}

	private LocalDateTime converteToDate(String strDate) {
		return LocalDateTime.parse(strDate, Constants.FORMAT_STORAGE_DATETIME);
	}

}
