
public class AddCommand implements Command {

	Task taskToAdd;
	int taskID;
	
	public AddCommand(Task task){
		taskToAdd = task;
	}
	
	@Override
	public String execute(Storage storage) {
		String userFeedback = storage.add(taskToAdd);
		taskID = taskToAdd.getId();
		return userFeedback;
	}

	@Override
	public Command makeUndo(Storage storage) {
		//TO-DO change task id
		int[] taskId = {1 };
		return new DeleteCommand(taskId);
	}

}
