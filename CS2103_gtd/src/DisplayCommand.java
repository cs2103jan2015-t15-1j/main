
public class DisplayCommand implements Command {
    
    public DisplayCommand() {
        
    }
    
    @Override
    public String execute(Storage storage) {
        String displayFeedback = storage.getTasksAsString();
        return displayFeedback;
    }

    @Override
    public Command makeUndo() {
        return null;
    }


	@Override
	public boolean isToBeAddedToHistory() {
		return false;
	}

}
