
public class SearchCommand implements Command {
    
    Task searchObj;
    Storage storage;
    
    public SearchCommand(Storage _storage, Task _searchObj) {
        searchObj = _searchObj;
        storage = _storage;
    }
    
    @Override
    public String execute() {
        String searchFeedback = storage.search(searchObj);
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