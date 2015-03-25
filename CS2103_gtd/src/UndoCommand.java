
public class UndoCommand implements Command {
    
    public UndoCommand() {
        
    }
    
    @Override
    public String execute(Storage storage) {
        return "";
    }

    @Override
    public Command makeUndo() {
        return new UndoCommand();
    }

	@Override
	public boolean isUndo() {
		return true;
	}

	@Override
	public boolean isRedo() {
		return false;
	}

	@Override
	public boolean isToBeAddedToHistory() {
		return false;
	}

}
