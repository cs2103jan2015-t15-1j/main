
public class UndoCommand implements Command {
	Storage _storage;
	History _history;
	Command cmd;
        
    public UndoCommand(Storage storage, History history) {
        _storage = storage;
        _history = history;
    }
    
    @Override
    public String execute() {
    	String feedback;
        try {
			cmd = _history.getUndo();
			feedback = cmd.execute();
			updateHistory();
			
		} catch (NullPointerException e) {
			feedback = e.getMessage();
		}
        return feedback;
    }

    @Override
    public Command makeUndo() {
        return cmd.makeUndo();
    }

	@Override
	public void updateHistory() {
		_history.pushRedo(makeUndo());
		
	}

}
