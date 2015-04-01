
public class HelpCommand implements Command {
    
    public HelpCommand(){
        
    }
    
    @Override
    public String execute() {
        return Constants.MESSAGE_HELP;
    }

	@Override
	public Command makeUndo() {
		return null;
	}

	@Override
	public void updateHistory() {
		
	}



}