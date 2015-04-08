//@author A0135280M
public class HelpCommand implements Command {

	public HelpCommand() {

	}

	@Override
	public String execute() {
		return Constants.MESSAGE_HELP;
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