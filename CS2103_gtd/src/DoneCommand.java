//@author A0135280M
public class DoneCommand implements Command {

	int[] taskIds;
	boolean setDone;
	Storage storage;
	History history;

	public DoneCommand(Storage _storage, History _history, int[] _ids,
			boolean _setDone) {
		taskIds = _ids;
		setDone = _setDone;
		history = _history;
		storage = _storage;
	}

	@Override
	public String execute() {
		String userFeedback = "";
		for (int id : taskIds) {
			if (userFeedback.equals("")) {
				userFeedback += storage.done(id, setDone);
			} else {
				userFeedback += "\n" + storage.done(id, setDone);
			}

		}
		updateHistory();
		return Constants.MESSAGE_UPDATED +"\n"+ Constants.DISPLAY_TABLE_HEADERS +"\n"+userFeedback;
	}

	//@author A0111337U
	@Override
	public Command makeUndo() {
		return new DoneCommand(storage, history, taskIds, !setDone);
	}

	//@author A0111337U
	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());

	}

}
