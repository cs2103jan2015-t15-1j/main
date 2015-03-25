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

	public String execute(String userInput) {
		Command c;
		try {
			c = translator.createCommand(userInput);
		} catch (Exception e) {
			return "Invalid Input";
		}

		boolean isUndo = c instanceof UndoCommand;
		boolean isRedo = c instanceof RedoCommand;
		boolean isToBeAddedToHistory = c.isToBeAddedToHistory();
		Command cmd;
		if (isUndo) {
			cmd = history.getUndo();
		} else if (isRedo) {
			cmd = history.getRedo();
		} else {
			cmd = c;
		}
		String feedback = cmd.execute(storage);

		if (isToBeAddedToHistory) {
			history.pushUndo(cmd.makeUndo());
		}
		return feedback;

	}

}
