
public class ClearCommand implements Command {
    
    Storage storage;
    History history;
    Task[] deletedTasks;
    
    public ClearCommand(Storage _storage, History _history, Task[] _tasks) {
        storage = _storage;
        history = _history;
    }
    
    @Override
    public String execute() {
        deletedTasks = storage.getAllTasks();
        String feedback = storage.deleteAll();
        makeUndo();
        return feedback;
    }
    
    private void makeUndo() {
        Command reversedCommand = new AddCommand(storage, history, deletedTasks);
        history.pushUndo(reversedCommand);
    }

}
