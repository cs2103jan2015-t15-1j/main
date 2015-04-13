import java.io.IOException;

//@author A0135280M
public class SetDirectoryCommand implements Command {

    Storage storage;
    History history;
    private String newDir;
	private String oldDir;
	
	public SetDirectoryCommand(Storage _storage, History _history, String path) {
		newDir = path;
		storage = _storage;
		history = _history;
	}

	@Override
	public String execute() {
	    oldDir = storage.getFilePath();
		String feedback;
		try {
			feedback = storage.setFilePath(newDir);
		} catch (IOException e) {
			return e.getMessage();
		}
		updateHistory();
		return feedback;
	}

	@Override
	public Command makeUndo() {
		return new SetDirectoryCommand(storage, history, oldDir);
	}

	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());
		
	}

}
