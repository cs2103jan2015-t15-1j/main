
public class DoneCommand implements Command {
    
    int[] taskIds;
    boolean setDone;
    Storage _storage;
    History _history;
    
    public DoneCommand(int[] _ids, boolean _setDone, History history, Storage storage){
        taskIds = _ids;
        setDone = _setDone;
        _history = history;
        _storage = storage;
    }
    
    @Override
    public String execute() {
        String userFeedback = "";
        for (int id : taskIds) {
        	if(userFeedback.equals("")){
        		userFeedback += _storage.done(id, setDone);
        	}
        	else{
        		userFeedback += "\n" + _storage.done(id, setDone);
        	}
        	
        }
        updateHistory();
        return userFeedback;
    }

    @Override
    public Command makeUndo() {
        return new DoneCommand(taskIds, !setDone, _history, _storage);
    }

	@Override
	public void updateHistory() {
		_history.pushUndo(makeUndo());
		
	}




}
