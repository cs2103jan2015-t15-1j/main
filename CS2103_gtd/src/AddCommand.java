
public class AddCommand implements Command {

	Task taskToAdd;
	int taskID;
	public AddCommand(String userInput){
		taskToAdd = Interpreter.interpretAddOREditParameter(userInput, CommandType.ADD);
	}
	
	@Override
	public String execute(Storage storage) {
		String userFeedback = storage.add(taskToAdd);
		taskID = taskToAdd.getId();
		return userFeedback;
	}

	@Override
	public String undo(Storage storage) {
		String userFeedback = storage.delete(taskID);
		return userFeedback;
	}

}
