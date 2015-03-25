
public class SearchCommand implements Command {
    
    Task searchObj;
    
    public SearchCommand(Task _searchObj) {
        searchObj = _searchObj;
    }
    
    @Override
    public String execute(Storage storage) {
        String searchFeedback = storage.search(searchObj);
        return searchFeedback;
    }

    @Override
    public Command makeUndo() {
        return null;
    }


	@Override
	public boolean isToBeAddedToHistory() {
		return false;
	}

}