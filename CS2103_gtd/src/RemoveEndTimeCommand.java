import java.time.LocalDateTime;

public class RemoveEndTimeCommand implements Command {

	Storage storage;
	History history;
	LocalDateTime originalEndTime;
	Task task;

	public RemoveEndTimeCommand(Storage _storage, History _history, int TaskId) {
		storage = _storage;
		history = _history;
		task = storage.getTask(TaskId);
		originalEndTime = task.getEndDateTime();
	}

	@Override
	public String execute() {
		task.setEndDateTime(null);
		updateHistory();
		return "Removed end time for task " + task.getId();

	}

	@Override
	public Command makeUndo() {
		Task taskWithOriginalEnd = new Task();
		taskWithOriginalEnd.setEndDateTime(originalEndTime);
		taskWithOriginalEnd.setId(task.getId());
		return new EditCommand(storage, history, taskWithOriginalEnd);
	}

	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());

	}

}
