import java.io.IOException;

//@author A0111337U
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
        StorageIO storageIO = new StorageIO();
		storage = new Storage(storageIO);
		String initializationFeedback;
		try {
			initializationFeedback = storage.prepareStorage();
		} catch (IOException e) {
			return e.getMessage();
		}
		history = new History();
		translator = new Translator(storage, history);
		return initializationFeedback;
	}

	public String execute(String userInput) {
		Command command;
		try {
			command = translator.createCommand(userInput);
		} catch (Exception e) {
            return Constants.MESSAGE_COMMAND_EXECUTION_ERROR + userInput;
        }
		String feedback = command.execute();
		return feedback;
		

	}

}
