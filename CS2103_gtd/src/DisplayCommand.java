//@author A0135280M
public class DisplayCommand implements Command {

	Storage _storage;

	public DisplayCommand(Storage storage) {
		_storage = storage;
	}

	@Override
	public String execute() {
		String displayFeedback = _storage.getTasksAsString();
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
