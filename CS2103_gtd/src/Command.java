interface Command {
	public String execute(Storage storage);

	public Command makeUndo(Storage storage);
}
