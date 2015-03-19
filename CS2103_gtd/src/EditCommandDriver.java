public class EditCommandDriver {

	public static void main(String[] args) {
		Command test = new EditCommand(new Task(1, "test", null, null, false));
		Storage storage = new StorageStub();
		System.out.println(storage.add(new Task(1, "original", null, null, false)));
		System.out.println(test.execute(storage));
		System.out.println(test.undo(storage));
		System.out.println(test.execute(storage));

	}

}
