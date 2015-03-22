
public class DoneCommand implements Command {
    
    int taskId;
    boolean setDone;
    
    public DoneCommand(int _id, boolean _setDone){
        taskId = _id;
        setDone = _setDone;
    }
    
    @Override
    public String execute(Storage storage) {
        String userFeedback = storage.done(taskId);
        return userFeedback;
    }

    @Override
    public Command makeUndo() {
        return new DoneCommand(taskId, !setDone);
    }

}
