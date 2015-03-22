public class Logic {

	private static Logic logicObject;
	private Storage storage;
	private History history;
	private Translator translator;

	private Logic() {

	}

	public static Logic getLogicObject() {
		if (logicObject == null) {
			logicObject = new Logic();
		}
		return logicObject;
	}

	public String initializeEnvironment() {
		// Todo: get current file path from a file
		storage = new Storage();
		String filePath = "storage_file.json";
		history = new History();
		translator = new Translator();
		String initializationFeedback = storage.prepareStorage(filePath);
		return initializationFeedback;
	}

	public String execute(String userInput){
		Command c;
		try {
			c = translator.createCommand(userInput);
		} catch (Exception e) {
			return "Invalid Input";
		}
		
		boolean isUndo = c.getClass().equals(UndoCommand.class);
		boolean isRedo = c.getClass().equals(RedoCommand.class);
		boolean isToBeAddedToHistory = !isUndo && !isRedo && !c.getClass().equals(DisplayCommand.class) && !c.getClass().equals(HelpCommand.class) && !c.getClass().equals(SearchCommand.class);
		Command cmd;
		if(isUndo){
			cmd = history.getUndo();
		}
		else if(isRedo){
			cmd = history.getRedo();
		}
		else{
			cmd = c;
		}
		String feedback = cmd.execute(storage);
		
		if(isToBeAddedToHistory){
			history.pushUndo(cmd.makeUndo());
		}
		return feedback;
		
	}

	/*
	 * public String execute(String userInput) { String returnMessage;
	 * CommandType commandType = Interpreter.interpretCommandType(userInput);
	 * 
	 * switch (commandType) { case ADD: returnMessage = add(userInput); return
	 * returnMessage; case DISPLAY: returnMessage = display(); return
	 * returnMessage; case DONE: returnMessage = done(userInput); return
	 * returnMessage; case DELETE: returnMessage = delete(userInput); return
	 * returnMessage; case CLEAR: returnMessage = clear(userInput); return
	 * returnMessage; case EDIT: returnMessage = edit(userInput); return
	 * returnMessage; case UNDO: returnMessage = undo(); return returnMessage;
	 * case REDO: returnMessage = redo(); return returnMessage; case SEARCH:
	 * returnMessage = search(userInput); return returnMessage; case HELP:
	 * returnMessage = Constants.MESSAGE_HELP; return returnMessage; case
	 * SETDIR: returnMessage = setDirectory(userInput); return returnMessage;
	 * case EXIT: exit(); default: return
	 * Constants.MESSAGE_COMMAND_EXECUTION_ERROR + userInput; } }
	 */

	private void exit() {
		storage.writeToFile();
		System.exit(0);
	}

	private String setDirectory(String userInput) {
		String returnMessage;
		if (userInput.length() > 0) {
			String filePath = Interpreter.interpretFilePath(userInput);
			returnMessage = storage.setFilePath(filePath);
			return returnMessage;
		}
		return Constants.MESSAGE_ERROR_SET_DICT;
	}

	private String add(String userInput) {
		Task taskToAdd = Interpreter.interpretAddOREditParameter(userInput,
				CommandType.ADD);
		String userFeedback = storage.add(taskToAdd);
		return userFeedback;
	}

	private String delete(String userInput) {
		int[] linesToDelete = Interpreter.interpretDeleteParameter(userInput);
		String userFeedback = "";
		for (int line : linesToDelete) {
			if (userFeedback.isEmpty()) {
				userFeedback = storage.delete(line);
			} else if (line > 0) {
				userFeedback += "\n" + storage.delete(line);
			}
		}
		return userFeedback;
	}

	private String clear(String userInput) {
		String userFeedback = storage.deleteAll();
		return userFeedback;
	}

	private String done(String userInput) {
		int[] linesDone = Interpreter.interpretDoneParameter(userInput);
		String userFeedback = "";
		for (int line : linesDone) {
			if (userFeedback.isEmpty()) {
				userFeedback = storage.done(line,true);
			} else if (line > 0) {
				userFeedback += "\n" + storage.done(line,true);
			}
		}
		return userFeedback;
	}

	private String display() {
		return storage.getTasks();
	}

	private String undo() {
		// undo something
		return "feedback";
	}

	private String redo() {
		// redo
		return "feedback";
	}

	private String search(String userInput) {
		Task searchParameter = Interpreter.interpretDisplayParameter(userInput);
		String userFeedback = storage.search(searchParameter);
		return userFeedback;
	}

	private String edit(String userInput) {
		int editTaskID = Interpreter.interpretEditParameter(userInput);
		// TODO: Pull info of the Task of this ID from storage and display to
		// user.
		Task taskToEdit = Interpreter.interpretAddOREditParameter(userInput,
				CommandType.EDIT);
		String userFeedback = storage.update(editTaskID, taskToEdit);
		return userFeedback;
	}
}
