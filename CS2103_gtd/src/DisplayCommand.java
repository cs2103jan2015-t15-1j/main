
public class DisplayCommand implements Command {
    
    public DisplayCommand() {
        
    }
    
    @Override
    public String execute(Storage storage) {
        String displayFeedback = storage.getTasks();
        return displayFeedback;
    }

    @Override
    public Command makeUndo() {
        return null;
    }

	@Override
	public boolean isUndo() {
		return false;
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
