
public class HelpCommand implements Command {
    
    public HelpCommand(){
        
    }
    
    @Override
    public String execute(Storage storage) {
        return Constants.MESSAGE_HELP;
    }

    @Override
    public Command makeUndo() {
        return null;
    }


	@Override
	public boolean isToBeAddedToHistory() {
		return false;
	}

}