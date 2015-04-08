import java.util.ArrayList;

public class DeleteCommand implements Command {

	Storage storage;
	History history;
	int[] taskIds;
	Task[] deletedTasks;

	public DeleteCommand(Storage _storage, History _history, int[] _taskIds) {
		storage = _storage;
		history = _history;
		taskIds = _taskIds;
	}

	@Override
	public String execute() {
		String userFeedback = "";
		ArrayList<Task> tasksThatExist = new ArrayList<Task>();
		for (int i = 0; i < taskIds.length; i++) {
			Task fetchedTask = storage.getTask(taskIds[i]);
			if (fetchedTask != null) {
				tasksThatExist.add(fetchedTask);
				userFeedback += storage.delete(taskIds[i]) + "\n";
			}

		}
		if (0 < tasksThatExist.size()) {
			deletedTasks = new Task[tasksThatExist.size()];
			deletedTasks = tasksThatExist.toArray(deletedTasks);
			updateHistory();
			return userFeedback;
		} else {
			return Constants.MESSAGE_NO_VALID_IDs;
		}
	}

	// @author A0111337U
	public Command makeUndo() {
		Command reversedCommand = new AddCommand(storage, history, deletedTasks);
		return reversedCommand;
	}

	// @author A0111337U
	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());
	}

}
