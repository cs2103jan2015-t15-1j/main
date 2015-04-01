import java.nio.file.Path;

public class SetDirectoryCommand implements Command {
	
	private Path oldDir;
	private Path newDirToSet;
	Storage _storage;
	History _history;
	
	public SetDirectoryCommand(Path path, Storage storage, History history) {
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
		return new SetDirectoryCommand(oldDir, _storage, _history);
	}

	@Override
	public void updateHistory() {
		_history.pushUndo(makeUndo());
		
	}

}
