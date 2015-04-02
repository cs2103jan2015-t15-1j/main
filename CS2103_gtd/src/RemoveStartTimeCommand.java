import java.time.LocalDateTime;

public class RemoveStartTimeCommand implements Command {

	Storage storage;
	History history;
	LocalDateTime originalStartTime;
	Task task;

	public RemoveStartTimeCommand(Storage _storage, History _history, int TaskId) {
		storage = _storage;
		history = _history;
		task = storage.getTask(TaskId);
		originalStartTime = task.getStartDateTime();
	}

	@Override
	public String execute() {
		task.setStartDateTime(null);
		updateHistory();
		return "Removed start time for task " + task.getId();

	}

	@Override
	public Command makeUndo() {
		Task taskWithOriginalStart = new Task();
		taskWithOriginalStart.setStartDateTime(originalStartTime);
		taskWithOriginalStart.setId(task.getId());
		return new EditCommand(storage, history, taskWithOriginalStart);
	}

	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());

	}

}
