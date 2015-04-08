public class ExitCommand implements Command {

	public ExitCommand() {

	}

	@Override
	public String execute() {
		System.exit(0);
		return null;
	}

	// @author generated
	@Override
	public Command makeUndo() {
		return null;
	}

	// @author generated
	@Override
	public void updateHistory() {

	}

}
