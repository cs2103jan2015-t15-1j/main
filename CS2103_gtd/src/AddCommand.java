//@author A0135280M
public class AddCommand implements Command {

    Storage storage;
    History history;
	Task[] tasksToAdd;
	int[] taskIds;
	
	public AddCommand(Storage _storage, History _history, Task[] _tasks) {
	    storage = _storage;
	    history = _history;
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
		updateHistory();
		return userFeedback;
	}
	
	//@author A0111337U
	@Override
	public Command makeUndo() {
		Command reversedCommand = new DeleteCommand(storage, history, taskIds);
		return reversedCommand;
	}

	//@author A0111337U
	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());
		
	}

}
