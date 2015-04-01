public class DeleteCommand implements Command {
    
    Storage storage;
    History history;
    int[] taskIds;
    Task[] deletedTasks;
    
    public DeleteCommand(Storage _storage, History _history, int[] _taskIds) {
        storage = _storage;
        history = _history;
        taskIds = _taskIds;
        deletedTasks = new Task[taskIds.length];
    }
    
    @Override
    public String execute() {
        String userFeedback = "";
        for (int i=0; i<taskIds.length; i++) {
            deletedTasks[i] = storage.getTask(taskIds[i]);
            userFeedback += storage.delete(taskIds[i]) + "\n";
        }
        updateHistory();
        return userFeedback;
    }

    public Command makeUndo() {
        Command reversedCommand = new AddCommand(storage, history, deletedTasks);
        return reversedCommand;
    }

	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());
		
	}

}
