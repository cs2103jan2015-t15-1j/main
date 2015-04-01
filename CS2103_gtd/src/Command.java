interface Command {
	
    public String execute();
    public Command makeUndo();
    public void updateHistory();
    
	
}
