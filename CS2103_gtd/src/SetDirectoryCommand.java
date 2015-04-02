import java.nio.file.Path;

public class SetDirectoryCommand implements Command {
	
	private Path oldDir;
	private Path newDirToSet;
	Storage storage;
	History history;
	
	public SetDirectoryCommand(Storage _storage, History _history, Path path) {
		newDirToSet = path;
		storage = _storage;
		history = _history;
	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		// Call methods in Storage to set to new dir.
		updateHistory();
		return null;
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
