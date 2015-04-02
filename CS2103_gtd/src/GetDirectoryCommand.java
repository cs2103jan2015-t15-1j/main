import java.nio.file.Path;


public class GetDirectoryCommand implements Command {
    
    Storage storage;
    History history;
    
    public GetDirectoryCommand(Storage _storage, History _history, Path path) {
        storage = _storage;
        history = _history;
    }
    
    @Override
    public String execute() {
        String filePath = storage.getFilePath();
        return filePath;
    }
    
    @Override
    public Command makeUndo() {
        return null;
    }

    @Override
    public void updateHistory() {
        
    }

}
