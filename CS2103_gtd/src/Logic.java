import java.io.File;
import java.util.Scanner;

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
		storage = new Storage();
		String initializationFeedback = storage.prepareStorage();
		history = new History();
		translator = new Translator(storage,history);
		return initializationFeedback;
	}

	public String execute(String userInput) {
		Command command;
		try {
			command = translator.createCommand(userInput);
			String feedback = command.execute();
			return feedback;
		} catch (Exception e) {
			return Constants.MESSAGE_COMMAND_EXECUTION_ERROR + userInput;
		}


	}

}
