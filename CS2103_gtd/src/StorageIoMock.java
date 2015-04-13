import java.util.Map;

//@author A0135280M
public class StorageIoMock extends StorageIO {
    
    private String storageFilePath;
    private int lastIdNumber;
    
    public String getFilePath() {
        return storageFilePath;
    }
    
    public String initializeConfigFile() {
        storageFilePath = getDefaultStoragePath();
        return storageFilePath;
    }
    
    private String getDefaultStoragePath() {
        return System.getProperty("user.dir") + "/" + Constants.DEFAULT_STORAGE_PATH;
    }
    
    public String setFilePath(String path) {
        storageFilePath = path;
        return String.format(Constants.MESSAGE_FILE_CHANGE, storageFilePath);
    }
    
    public int getLastIdNumber() {
        return lastIdNumber;
    }
    
    public void writeToFile(Map<Integer, Task> tasks) {
        
    }
    
    public void getDataFromFile(Map<Integer, Task> tasks) {
        
    }
    
}
