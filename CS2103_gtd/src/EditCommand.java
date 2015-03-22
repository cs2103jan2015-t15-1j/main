import java.util.logging.Level;
import java.util.logging.Logger;

class EditCommand implements Command {
	Task _newTask;

	private static final Logger logger = Logger.getLogger(EditCommand.class
			.getName());

	public EditCommand(Task newTask) {
		_newTask = newTask;
		logger.log(Level.CONFIG, _newTask.getUserFormat());

	}

	@Override
	public String execute(Storage storage) {
		int TaskId = _newTask.getId();
		String feedback = storage.update(TaskId, _newTask);
		logger.log(Level.FINE, storage.getTask(TaskId).getUserFormat());
		return feedback;
	}

	@Override
	public Command makeUndo(Storage storage) {
		int TaskId = _newTask.getId();
		Task oldTask = makeShallowCopyOfOriginalTask(storage, TaskId);
		return new EditCommand(oldTask);
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

}
