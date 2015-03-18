import java.util.ArrayList;

public class DeleteCommand implements Command {
    
    int[] taskIds;
    ArrayList<Task> deletedTasks = new ArrayList<Task>();
    
    public DeleteCommand(int[] _taskIds) {
        taskIds = _taskIds;
    }
    
    @Override
    public String execute(Storage storage) {
        String userFeedback = "";
        for (int id : taskIds) {
            deletedTasks.add(storage.getTask(id));
            userFeedback += storage.delete(id) + "\n";
        }
        return userFeedback;
    }

    @Override
    public String undo(Storage storage) {
        String userFeedback = "Recovered tasks";
        for (Task task : deletedTasks) {
            storage.add(task);
            userFeedback += "\n" + task.getUserFormat();
        }
        return userFeedback;
    }

}
