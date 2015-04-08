//@author A0111337U
interface Command {

	public String execute();

	public Command makeUndo();

	public void updateHistory();

}
