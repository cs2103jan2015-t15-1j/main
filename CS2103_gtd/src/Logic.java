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
		String filePath = "storage_file.json";//to be replaced with getFilePath
		String initializationFeedback = storage.prepareStorage(filePath);
		history = new History();
		translator = new Translator(storage,history);
		return initializationFeedback;
	}

	private String getFilePath() {
		// Todo: get current file path from a file
		String filePath  = "storage_file.json";
		String filename = "hiddenFilePath.txt";
		try {
			File datafile = new File(filename);
			
			Scanner filepathScanner = new Scanner(datafile);
			String newFilePath = filepathScanner.nextLine().trim();
			filepathScanner.close();
			if(!newFilePath.equals("")){
				filePath = newFilePath;
			}
		} catch (Exception e) {
		}		
		return filePath;
	}

	public String execute(String userInput) {
		Command c;
		try {
			c = translator.createCommand(userInput);
			String feedback = c.execute();
			return feedback;
		} catch (Exception e) {
			return Constants.MESSAGE_COMMAND_EXECUTION_ERROR + userInput;
		}


	}

}
