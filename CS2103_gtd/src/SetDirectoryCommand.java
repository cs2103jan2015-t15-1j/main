import java.nio.file.Path;

public class SetDirectoryCommand implements Command {
	
	private Path oldDir;
	private Path newDirToSet;
	Storage _storage;
	History _history;
	
	public SetDirectoryCommand(Storage storage, History history, Path path) {
		newDirToSet = path;
		//oldDir = _storage.get
		_storage = storage;
		_history = history;
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
		return new SetDirectoryCommand(_storage, _history, oldDir);
	}

	@Override
	public void updateHistory() {
		_history.pushUndo(makeUndo());
		
	}

}
