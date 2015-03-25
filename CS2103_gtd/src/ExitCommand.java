
public class ExitCommand implements Command {

	public ExitCommand() {
		
	}
	
	@Override
	public String execute(Storage storage) {
		System.exit(0);
		return null;
	}

	@Override
	public Command makeUndo() {
		return null;
	}

	@Override
	public boolean isToBeAddedToHistory() {
		// TODO Auto-generated method stub
		return false;
	}

}
