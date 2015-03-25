public class DeleteCommand implements Command {
    
    int[] taskIds;
    Task[] deletedTasks;
    
    public DeleteCommand(int[] _taskIds) {
        taskIds = _taskIds;
        deletedTasks = new Task[taskIds.length];
    }
    
    @Override
    public String execute(Storage storage) {
        String userFeedback = "";
        for (int i=0; i<taskIds.length; i++) {
            deletedTasks[i] = storage.getTask(taskIds[i]);
            userFeedback += storage.delete(taskIds[i]) + "\n";
        }
        return userFeedback;
    }

    @Override
    public Command makeUndo() {
        return new AddCommand(deletedTasks);
    }


	@Override
	public boolean isToBeAddedToHistory() {
		return true;
	}

}
