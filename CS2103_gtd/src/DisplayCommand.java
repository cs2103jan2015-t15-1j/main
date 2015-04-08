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

	// @author A0111337U
	@Override
	public Command makeUndo() {
		return null;
	}

	// @author A0111337U
	@Override
	public void updateHistory() {

	}

}
