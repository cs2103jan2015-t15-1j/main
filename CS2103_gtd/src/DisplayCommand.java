
public class DisplayCommand implements Command {
    
    public DisplayCommand() {
        
    }
    
    @Override
    public String execute(Storage storage) {
        String displayFeedback = storage.getTasks();
        return displayFeedback;
    }

    @Override
    public Command makeUndo(Storage storage) {
        return null;
    }

}
