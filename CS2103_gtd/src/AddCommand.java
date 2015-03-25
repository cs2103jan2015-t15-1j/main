
public class AddCommand implements Command {

	Task[] tasksToAdd;
	int[] taskIds;
	
	public AddCommand(Task[] tasks){
		tasksToAdd = tasks;
		taskIds = new int[tasksToAdd.length];
	}
	
	@Override
	public String execute(Storage storage) {
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
	public boolean isUndo() {
		return false;
	}

	@Override
	public boolean isRedo() {
		return false;
	}

	@Override
	public boolean isToBeAddedToHistory() {
		return true;
	}

}
