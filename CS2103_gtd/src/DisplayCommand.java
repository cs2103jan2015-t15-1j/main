import java.time.LocalDateTime;

//@author A0135280M
public class DisplayCommand implements Command {

	Storage storage;
	Task displayObj;

	public DisplayCommand(Storage _storage, Task _displayObj) {
		storage = _storage;
		displayObj = _displayObj;
	}

	@Override
	public String execute() {
		String displayFeedback = "";
		if (displayObj != null) {
			if (displayObj.isDone() == true) {
				displayFeedback = storage.getDoneTasksAsString();
			} else if (isDisplayAllCommand()) {
				displayFeedback = storage.getTasksAsString();
			} else {
				displayFeedback = storage.displayByDate(displayObj);
			}
		} else {
			displayFeedback = "Invalid display command";
		}
		return displayFeedback;
	}
	
	private boolean isDisplayAllCommand() {
	    LocalDateTime start = displayObj.getStartDateTime();
	    LocalDateTime end = displayObj.getEndDateTime();
	    return start.equals(LocalDateTime.MIN) && end.equals(LocalDateTime.MAX);
	}

	//@author generated
	@Override
	public Command makeUndo() {
		return null;
	}

	//@author generated
	@Override
	public void updateHistory() {

	}

}
