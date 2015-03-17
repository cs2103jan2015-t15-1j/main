interface Command {
	public String execute(Storage storage);

	public String undo(Storage storage);
}
