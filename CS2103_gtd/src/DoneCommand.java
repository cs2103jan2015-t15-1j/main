
public class DoneCommand implements Command {
    
    int[] taskIds;
    boolean setDone;
    Storage _storage;
    History _history;
    
    public DoneCommand(Storage storage, History history, int[] _ids, boolean _setDone){
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
        return new DoneCommand(_storage, _history, taskIds, !setDone);
    }

	@Override
	public void updateHistory() {
		_history.pushUndo(makeUndo());
		
	}




}
