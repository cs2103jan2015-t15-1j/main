
public class Logic {

	private static final String MESSAGE_ENVIRONMENT_READY = null;
	private static final String MESSAGE_INITIALIZATION_ERROR = null;
	private static final int EXPECTED_DESCRIPTION_POSITION = 0;
	private static final int EXPECTED_START_DATE_POSITION = 1;
	private static final int EXPECTED_END_DATE_POSITION = 2;
	
	

	public static String initializeEnvironment(){
		if(Storage.prepareStorage()){
			return MESSAGE_ENVIRONMENT_READY;
		}
		else{
			return MESSAGE_INITIALIZATION_ERROR;
		}
	}

	public static String execute(String userInput){
		String returnMessage;
		commandType = Interpreter.getCommandType(userInput);
		
		switch (commandType){
		case ADD:
			commandParameters = Interpreter.getAddParameters(userInput);
			description = commandParameters[EXPECTED_DESCRIPTION_POSITION];
			startDate = commandParameters[EXPECTED_START_DATE_POSITION];
			endDate = commandParameters[EXPECTED_END_DATE_POSITION];
			taskAdded = Storage.add(description,startDate,endDate);
			returnMessage[0] = "Added " + taskAdded.getUserFormat();
			return returnMessage;
		case DISPLAY:
		
		case DONE:
			
		case DELETE:
			linesToDelete = Interpreter.getDeleteParameters(userInput);
			
			
		case EDIT:
			
		case UNDO:
			
		case REDO:
			
		case HELP:
			
		case SETDIR:
			
		case EXIT:
			
		default:
			
		}
	}
	
	private String add(String userInput){
		Task taskToAdd = Interpreter.getParameters(userInput);
		
	}
}


