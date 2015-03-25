
public class DoneCommand implements Command {
    
    int[] taskIds;
    boolean setDone;
    
    public DoneCommand(int[] _ids, boolean _setDone){
        taskIds = _ids;
        setDone = _setDone;
    }
    
    @Override
    public String execute(Storage storage) {
        String userFeedback = "";
        for (int id : taskIds) {
            userFeedback += storage.done(id, setDone);
        }
        return userFeedback;
    }

    @Override
    public Command makeUndo() {
        return new DoneCommand(taskIds, !setDone);
    }


	@Override
	public boolean isToBeAddedToHistory() {
		return true;
	}

}
