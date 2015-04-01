
public class DisplayCommand implements Command {
    
	Storage _storage;
    public DisplayCommand(Storage storage) {
        _storage = storage;
    }
    
    @Override
    public String execute() {
        String displayFeedback = _storage.getTasksAsString();
        return displayFeedback;
    }

    @Override
    public Command makeUndo() {
        return null;
    }

	@Override
	public void updateHistory() {
		
	}



}
