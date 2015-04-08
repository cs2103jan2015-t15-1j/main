//@author A0135280M
public class GetDirectoryCommand implements Command {
    
    Storage storage;
    
    public GetDirectoryCommand(Storage _storage) {
        storage = _storage;
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
