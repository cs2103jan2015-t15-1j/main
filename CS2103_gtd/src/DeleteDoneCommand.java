import java.util.ArrayList;

//@author A0135280M
public class DeleteDoneCommand implements Command {
    
    Storage storage;
    History history;
    Task[] deletedTasks;
    
    public DeleteDoneCommand(Storage _storage, History _history) {
        storage = _storage;
        history = _history;
    }
    
    @Override
    public String execute() {
        ArrayList<Task> doneTasksList = new ArrayList<Task>();
        String userFeedback = "";
        Task[] allTasks = storage.getAllTasks();
        for (Task task : allTasks) {
            if (task.getDone()) {
                doneTasksList.add(task);
                userFeedback += storage.delete(task.getId()) + "\n";
            }
        }
        if (doneTasksList.size() > 0) {
            deletedTasks = new Task[doneTasksList.size()];
            deletedTasks = doneTasksList.toArray(deletedTasks);
            updateHistory();
            return userFeedback;
        } else {
            return Constants.MESSAGE_NO_DONE_TAKS;
        }
    }

    public Command makeUndo() {
        Command reversedCommand = new AddCommand(storage, history, deletedTasks);
        return reversedCommand;
    }

    @Override
    public void updateHistory() {
        history.pushUndo(makeUndo());
    }

}
