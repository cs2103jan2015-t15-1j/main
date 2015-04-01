
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
        updateHistory();
        return feedback;
    }
    

	@Override
	public Command makeUndo() {
		Command reversedCommand = new AddCommand(storage, history, deletedTasks);
		return reversedCommand;
	}

	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());
		
	}
    

}
