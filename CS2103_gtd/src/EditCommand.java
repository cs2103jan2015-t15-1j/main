//@author A0111337U
import java.util.logging.Level;
import java.util.logging.Logger;

class EditCommand implements Command {
	Task newTask;
	Task oldTask;
	Storage storage;
	History history;

	private static final Logger logger = Logger.getLogger(EditCommand.class
			.getName());

	public EditCommand(Storage _storage, History _history, Task _newTask) {
		newTask = _newTask;
		storage = _storage;
		history = _history;
		// logger.log(Level.CONFIG, newTask.getUserFormat());

	}

	@Override
	public String execute() {
		int taskId = newTask.getId();
		oldTask = makeShallowCopyOfOriginalTask(taskId);
		storage.updateDescription(taskId, newTask.getDescription());
		storage.updateStartDate(taskId, newTask.getStartDateTime());
		storage.updateEndDate(taskId, newTask.getEndDateTime());

		Task realTask = storage.getTask(taskId);
		String feedbackOrg = String.format("%-15s%s", "Original Task:",
				oldTask.getUserFormat());
		String feedbackChange = String.format("%-15s%s", "Changed to:",
				realTask.getUserFormat());
		// logger.log(Level.FINE, storage.getTask(taskId).getUserFormat());
		updateHistory();
		return feedbackOrg + "\n" + feedbackChange;
	}

	@Override
	public Command makeUndo() {
		return new EditCommand(storage, history, oldTask);
	}

	private Task makeShallowCopyOfOriginalTask(int TaskId) {
		Task oldTask = new Task();
		Task old = storage.getTask(TaskId);
		oldTask.setDescription(old.getDescription());
		oldTask.setDone(old.getDone());
		oldTask.setId(old.getId());
		oldTask.setStartDateTime(old.getStartDateTime());
		oldTask.setEndDateTime(old.getEndDateTime());
		return oldTask;
	}

	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());

	}

}
