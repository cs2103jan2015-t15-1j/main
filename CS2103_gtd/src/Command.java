interface Command {
	public String execute(Storage storage);

	public Command makeUndo();
	
	public boolean isToBeAddedToHistory();
}
