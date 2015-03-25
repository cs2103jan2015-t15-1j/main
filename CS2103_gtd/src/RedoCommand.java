
public class RedoCommand implements Command {
        
    public RedoCommand() {
        
    }
    
    @Override
    public String execute(Storage storage) {
        return "";
    }

    @Override
    public Command makeUndo() {
        return new RedoCommand();
    }

	@Override
	public boolean isUndo() {
		return false;
	}

	@Override
	public boolean isRedo() {
		return true;
	}

	@Override
	public boolean isToBeAddedToHistory() {
		return false;
	}

}
