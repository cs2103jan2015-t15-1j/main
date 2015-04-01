
public class ExitCommand implements Command {

	public ExitCommand() {
		
	}
	
	@Override
	public String execute() {
		System.exit(0);
		return null;
	}

	@Override
	public Command makeUndo() {
		return null;
	}

	@Override
	public void updateHistory() {
		
	}



}
