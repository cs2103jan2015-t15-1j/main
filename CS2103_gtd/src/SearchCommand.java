
public class SearchCommand implements Command {
    
    Task searchObj;
    Storage _storage;
    
    public SearchCommand(Storage storage, Task _searchObj) {
        searchObj = _searchObj;
        _storage = storage;
    }
    
    @Override
    public String execute() {
        String searchFeedback = _storage.search(searchObj);
        return searchFeedback;
    }

    @Override
    public Command makeUndo() {
        return null;
    }

	@Override
	public void updateHistory() {
		
	}



}