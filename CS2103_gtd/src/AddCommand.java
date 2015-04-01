
public class AddCommand implements Command {

    Storage storage;
	Task[] tasksToAdd;
	int[] taskIds;
	
	public AddCommand(Storage _storage, Task[] _tasks) {
	    storage = _storage;
		tasksToAdd = _tasks;
		taskIds = new int[tasksToAdd.length];
	}
	
	@Override
	public String execute() {
		String userFeedback = "";
		for (int i=0; i<tasksToAdd.length; i++) {
		    userFeedback += storage.add(tasksToAdd[i]);
		    taskIds[i] = tasksToAdd[i].getId();
		}
		return userFeedback;
	}

	@Override
	public Command makeUndo() {
		return new DeleteCommand(taskIds);
	}

	@Override
	public boolean isToBeAddedToHistory() {
		return true;
	}

}
