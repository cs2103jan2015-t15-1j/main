//@author A0111337U-unused
public class StorageStub extends Storage {

	Task someTask;
	
	public StorageStub(StorageIO _storageIO) {
	    super(_storageIO);
	}

	public String add(Task newTask) {
		someTask = newTask;
		return String.format(Constants.MESSAGE_ADDED, newTask.getId(),
				newTask.getDescription());
	}

	public String update(int idToUpdate, Task changes) {
		Task taskToUpdate = someTask;
		if (changes.getDescription() != null) {
			taskToUpdate.setDescription(changes.getDescription());
		}
		return ("new description " + someTask.getDescription());
	}

	public Task getTask(int taskId) {
		return someTask;
	}
}
