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
		if (displayObj.getDone() == true) {
			String displayFeedback = storage.searchDone();
		} else {
			String displayFeedback = storage.search(displayObj);
		}
		String displayFeedback = storage.getTasksAsString();
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
