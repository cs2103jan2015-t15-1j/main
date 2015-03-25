interface Command {
	public String execute(Storage storage);

	public Command makeUndo();
	
	public boolean isUndo();
	
	public boolean isRedo();
	
	public boolean isToBeAddedToHistory();
}
