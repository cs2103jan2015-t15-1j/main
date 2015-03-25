
public class ClearCommand implements Command {
    
    Task[] deletedTasks;
    
    public ClearCommand() {
        
    }
    
    @Override
    public String execute (Storage storage) {
        deletedTasks = storage.getAllTasks();
        String feedback = storage.deleteAll();
        return feedback;
    }
    
    @Override
    public Command makeUndo() {
        return new AddCommand(deletedTasks);
    }
    
    @Override
    public boolean isToBeAddedToHistory() {
        return true;
    }

}
