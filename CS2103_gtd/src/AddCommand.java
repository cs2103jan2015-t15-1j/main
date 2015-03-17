
public class AddCommand implements Command {

	Task taskToAdd;
	public AddCommand(String userInput){
		taskToAdd = Interpreter.interpretAddOREditParameter(userInput, COMMAND_TYPE.ADD);
	}
	
	@Override
	public String execute(Storage storage) {
		String userFeedback = storage.add(taskToAdd);
		return userFeedback;
	}

	@Override
	public String undo(Storage storage) {
		// TODO Auto-generated method stub
		return null;
	}

}
