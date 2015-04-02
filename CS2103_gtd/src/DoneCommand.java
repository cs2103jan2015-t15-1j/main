
public class DoneCommand implements Command {
    
    int[] taskIds;
    boolean setDone;
    Storage storage;
    History history;
    
    public DoneCommand(Storage _storage, History _history, int[] _ids, boolean _setDone){
        taskIds = _ids;
        setDone = _setDone;
        history = _history;
        storage = _storage;
    }
    
    @Override
    public String execute() {
        String userFeedback = "";
        for (int id : taskIds) {
        	if(userFeedback.equals("")){
        		userFeedback += storage.done(id, setDone);
        	}
        	else{
        		userFeedback += "\n" + storage.done(id, setDone);
        	}
        	
        }
        updateHistory();
        return userFeedback;
    }

    @Override
    public Command makeUndo() {
        return new DoneCommand(storage, history, taskIds, !setDone);
    }

	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());
		
	}




}
