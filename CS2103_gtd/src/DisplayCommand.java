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
		if (displayObj.isDone() == true) {
			displayFeedback = storage.getDoneTasksAsString();
		} else {
			displayFeedback = storage.display(displayObj);
		}
		return displayFeedback;
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
