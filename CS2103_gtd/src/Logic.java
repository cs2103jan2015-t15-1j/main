public class Logic {

	public static String initializeEnvironment() {
	    // Todo: get current file path from a file
	    String filePath = "storage_file.txt";
		String initializationFeedback = Storage.prepareStorage(filePath);
		return initializationFeedback;
	}

	public static String execute(String userInput) {
		String returnMessage;
		COMMAND_TYPE commandType = Interpreter.interpretCommandType(userInput);

		switch (commandType) {
		case ADD:
			returnMessage = add(userInput);
			return returnMessage;
		case DISPLAY:
			returnMessage = display();
			return returnMessage;
		case DONE:
			returnMessage = done(userInput);
			return returnMessage;
		case DELETE:
			returnMessage = delete(userInput);
			return returnMessage;
		case EDIT:
			returnMessage = edit(userInput);
			return returnMessage;
		case UNDO:
			returnMessage = undo();
			return returnMessage;
		case REDO:
			returnMessage = redo();
			return returnMessage;
		case SEARCH:
			returnMessage = search(userInput);
			return returnMessage;
		case HELP:
			returnMessage = Constants.MESSAGE_HELP;
			return returnMessage;
		case SETDIR:
			returnMessage = setDirectory(userInput);
			return returnMessage;
		case EXIT:
			returnMessage = save();
			return returnMessage;
		default:
			return Constants.MESSAGE_COMMAND_EXECUTION_ERROR + userInput;
		}
	}
	
	private static String save() {
		String userFeedback = Storage.exit();
		return userFeedback;
	}

	private static String setDirectory(String userInput) {
		String returnMessage;
		String filePath = Interpreter.interpretFilePath(userInput);
		returnMessage = Storage.setFilePath(filePath);
		return returnMessage;
	}

	private static String add(String userInput) {
		Task taskToAdd = Interpreter.interpretAddOREditParameter(userInput, COMMAND_TYPE.ADD);
		String userFeedback = Storage.add(taskToAdd);
		return userFeedback;
	}

	private static String delete(String userInput) {
		int[] linesToDelete = Interpreter.interpretDeleteParameter(userInput);
		String userFeedback = "";
		for (int line : linesToDelete) {
			if (userFeedback.isEmpty()) {
				userFeedback = Storage.delete(line);
			} else {
				userFeedback += "\n" + Storage.delete(line);
			}
		}
		return userFeedback;
	}

	private static String done(String userInput) {
		int[] linesDone = Interpreter.interpretDoneParameter(userInput);
		String userFeedback = "";
		for (int line : linesDone) {
			if (userFeedback.isEmpty()) {
				userFeedback = Storage.done(line);
			} else {
				userFeedback += "\n" + Storage.done(line);
			}
		}
		return userFeedback;
	}

	private static String display() {
		return Storage.getTasks();
	}

	private static String undo() {
		String userFeedback = Storage.undo();
		return userFeedback;
	}

	private static String redo() {
		String userFeedback = Storage.redo();
		return userFeedback;
	}

	private static String search(String userInput) {
		Task searchParameter = Interpreter
				.interpretDisplayParameter(userInput);
		String userFeedback = Storage.search(searchParameter);
		return userFeedback;
	}

	private static String edit(String userInput) {
		int editTaskID = Interpreter.interpretEditParameter(userInput);
		//TODO: Pull info of the Task of this ID from storage and display to user.
		Task taskToEdit = Interpreter.interpretAddOREditParameter(userInput, COMMAND_TYPE.EDIT);
		String userFeedback = Storage.update(editTaskID,taskToEdit);
		return userFeedback;
	}
}
