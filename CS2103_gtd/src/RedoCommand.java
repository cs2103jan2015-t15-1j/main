//@author A0111337U
public class RedoCommand implements Command {
	Storage storage;
	History history;
	Command cmd;

	public RedoCommand(Storage _storage, History _history) {
		storage = _storage;
		history = _history;
	}

	@Override
	public String execute() {
		String feedback;
		try {
			cmd = history.getRedo();
			feedback = cmd.execute();
			updateHistory();

		} catch (NullPointerException e) {
			feedback = e.getMessage();
		}
		return feedback;
	}

	@Override
	public Command makeUndo() {
		return cmd.makeUndo();
	}

	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());

	}

}
