
public class UndoCommand implements Command {
    
    History history;
    
    public UndoCommand(History _history) {
        history = _history;
    }
    
    @Override
    public String execute() {
        Command cmdToUndo = history.getUndo();
        String feedback = cmdToUndo.execute();
        return feedback;
    }

    @Override
    public Command makeUndo() {
        return new UndoCommand(history);
    }

	@Override
	public boolean isToBeAddedToHistory() {
		return false;
	}

}
