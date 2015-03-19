import java.util.logging.Level;
import java.util.logging.Logger;

class EditCommand implements Command {
	Task _newTask;
	Task _oldTask = new Task();

	private static final Logger logger = Logger.getLogger(EditCommand.class
			.getName());

	public EditCommand(Task newTask) {
		_newTask = newTask;
		logger.log(Level.CONFIG, _newTask.getUserFormat());

	}

	@Override
	public String execute(Storage storage) {
		int TaskId = _newTask.getId();
		makeShallowCopyOfOriginalTask(storage, TaskId);
		String feedback = storage.update(TaskId, _newTask);
		logger.log(Level.FINE, storage.getTask(TaskId).getUserFormat());
		return feedback;
	}

	@Override
	public String undo(Storage storage) {
		int TaskId = _newTask.getId();
		String feedback = storage.update(TaskId, _oldTask);
		logger.log(Level.FINE, storage.getTask(TaskId).getUserFormat());
		return feedback;
	}

	private void makeShallowCopyOfOriginalTask(Storage storage, int TaskId) {
		Task old = storage.getTask(TaskId);
		_oldTask.setDescription(old.getDescription());
		_oldTask.setDone(old.getDone());
		_oldTask.setId(old.getId());
		_oldTask.setStartDateTime(old.getStartDateTime());
		_oldTask.setEndDateTime(old.getEndDateTime());
		_oldTask.setLocation(old.getLocation());
	}

}
