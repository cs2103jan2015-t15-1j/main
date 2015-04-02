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
//		logger.log(Level.CONFIG, newTask.getUserFormat());

	}

	@Override
	public String execute() {
		int taskId = newTask.getId();
		oldTask = makeShallowCopyOfOriginalTask(storage, taskId);
		String feedback = "";
		if (newTask.getDescription() != null) {
		    feedback = storage.updateDescription(taskId, newTask.getDescription());
        }
        if (newTask.getStartDateTime() != null) {
            feedback = storage.updateStartDate(taskId, newTask.getStartDateTime());
        }
        if (newTask.getEndDateTime() != null) {
            feedback = storage.updateEndDate(taskId, newTask.getEndDateTime());
        }
        if (newTask.getLocation() != null) {
            feedback = storage.updateLocation(taskId, newTask.getLocation());
        }
        Task realTask = storage.getTask(taskId);
        feedback += "\n" + realTask.getUserFormat();
//		logger.log(Level.FINE, storage.getTask(taskId).getUserFormat());
        updateHistory();
		return feedback;
	}

	@Override
	public Command makeUndo() {
		return new EditCommand(storage, history, oldTask);
	}

	private Task makeShallowCopyOfOriginalTask(Storage storage, int TaskId) {
		Task oldTask = new Task();
		Task old = storage.getTask(TaskId);
		oldTask.setDescription(old.getDescription());
		oldTask.setDone(old.getDone());
		oldTask.setId(old.getId());
		oldTask.setStartDateTime(old.getStartDateTime());
		oldTask.setEndDateTime(old.getEndDateTime());
		oldTask.setLocation(old.getLocation());
		return oldTask;
	}

	@Override
	public void updateHistory() {
		history.pushUndo(makeUndo());
		
	}



}
