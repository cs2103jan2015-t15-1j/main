import java.nio.file.Path;

public class SetDirectoryCommand implements Command {
	
	private Path oldDir;
	private Path newDirToSet;
	
	public SetDirectoryCommand(Path path) {
		newDirToSet = path;
	}

	@Override
	public String execute(Storage storage) {
		// TODO Auto-generated method stub
		// Call methods in Storage to set to new dir.
		return null;
	}

	@Override
	public Command makeUndo() {
		// TODO Auto-generated method stub
		// set back to Old Dir?
		return null;
	}

	@Override
	public boolean isToBeAddedToHistory() {
		return true;
	}
}
