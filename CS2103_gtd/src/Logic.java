public class Logic {

	private static final String MESSAGE_ENVIRONMENT_READY = null;
	private static final String MESSAGE_INITIALIZATION_ERROR = null;
	private static final int EXPECTED_DESCRIPTION_POSITION = 0;
	private static final int EXPECTED_START_DATE_POSITION = 1;
	private static final int EXPECTED_END_DATE_POSITION = 2;

	public static String initializeEnvironment() {
		if (Storage.prepareStorage()) {
			return MESSAGE_ENVIRONMENT_READY;
		} else {
			return MESSAGE_INITIALIZATION_ERROR;
		}
	}

	public static String execute(String userInput) {
		String returnMessage;
		COMMAND_TYPE commandType = Interpreter.getCommandType(userInput);

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

		case UNDO:
			returnMessage = undo();
			return returnMessage;
		case REDO:
			returnMessage = redo();
			return returnMessage;
		case HELP:

		case SETDIR:

		case EXIT:

		default:

		}
	}

	private static String add(String userInput) {
		Task taskToAdd = Interpreter.getParameters(userInput);
		String userFeedback = Storage.add(taskToAdd);
		return userFeedback;
	}

	private static String delete(String userInput) {
		int[] linesToDelete = Interpreter.getLineIndices(userInput);
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
		int[] linesDone = Interpreter.getLineIndices(userInput);
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

	
	
}
