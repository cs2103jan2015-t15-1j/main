import static org.junit.Assert.*;

import org.junit.Test;

//@author A0111337U
public class LogicTest {

    StorageIO storageIO = new StorageIoMock();
	Storage storage = new Storage(storageIO);
	String fileName = "file_path_for_test.txt";
	Logic logic = Logic.getLogicObject();

	@Test
	public void testLogicSingleton() {
		logic.initializeEnvironment();
		Logic duplicateLogic = Logic.getLogicObject();
		assertEquals("Check that only one logic object is created", logic,
				duplicateLogic);
	}

	@Test
	public void testInvalidInput() {
		logic.initializeEnvironment();
		String feedback = logic.execute("SOmething");
		assertEquals("Check that Logic cathes an Invalid Input",
				Constants.MESSAGE_COMMAND_EXECUTION_ERROR + "SOmething",
				feedback);
	}

	@Test
	public void clearCommand() {// to-do
		logic.initializeEnvironment();
		logic.execute("clear");
		logic.initializeEnvironment();
		logic.execute("add task buffer");
		logic.execute("add another task");
		String actual = logic.execute("display");
		String expected = "\n"
				+ Constants.DISPLAY_TABLE_HEADERS
				+ "\n"
				+ String.format("%-4d%-70s%-17s%-17s", 1, "task buffer", "", "")
				+ "\n"
				+ String.format("%-4d%-70s%-17s%-17s", 2, "another task", "",
						"");

		assertEquals("check tasks added", expected, actual);
		logic.execute("clear");
		actual = logic.execute("display");
		expected = Constants.MESSAGE_NO_TASKS;

		assertEquals("Check that the clear command is working\n", expected,
				actual);
		logic.execute("Undo");
		actual = logic.execute("display");
		expected = "\n"
				+ Constants.DISPLAY_TABLE_HEADERS
				+ "\n"
				+ String.format("%-4d%-70s%-17s%-17s", 1, "task buffer", "", "")
				+ "\n"
				+ String.format("%-4d%-70s%-17s%-17s", 2, "another task", "",
						"");

		assertEquals("Check that undo clear command works", expected, actual);
		logic.execute("Redo");
		actual = logic.execute("display");
		expected = Constants.MESSAGE_NO_TASKS;

		assertEquals("Check that redo undo of clear command works", expected,
				actual);
	}

	@Test
	public void deleteCommandEmptyFile() {// to-do
		logic.initializeEnvironment();
		logic.execute("clear");
		logic.initializeEnvironment();

		String feedback = logic.execute("delete 1");
		String expected = Constants.MESSAGE_INCORRECT_ID + "\n";
		assertEquals("Check feedback for deletion of empty file", expected,
				feedback);

		feedback = logic.execute("display");
		expected = Constants.MESSAGE_NO_TASKS;
		assertEquals("Check display after deletion of empty file", expected,
				feedback);

		feedback = logic.execute("Undo");
		expected = "No commands to undo";
		assertEquals("Check that undo delete empty file command works",
				expected, feedback);

		feedback = logic.execute("Redo");
		expected = "No commands to redo";
		assertEquals("Check that redo undo of delete empty file command works",
				expected, feedback);
	}

	
	@Test
	public void addEmpty() {
		String addEmpty = "add       ";
		logic.initializeEnvironment();
		String feedback = logic.execute(addEmpty);
		assertEquals(
				"Check that adding of task with empty description is not executed",
				Constants.MESSAGE_COMMAND_EXECUTION_ERROR + addEmpty, feedback);
	}

	public void executeEmpty() {// TO-DO
		String empty = "";
		logic.initializeEnvironment();
		String feedback = logic.execute(empty);
		assertEquals(
				"Check that adding of task with empty description is not executed",
				Constants.MESSAGE_COMMAND_EXECUTION_ERROR + empty, feedback);
	}

	public void executeNull() {// TO-DO
		logic.initializeEnvironment();
		String feedback = logic.execute(null);
		assertEquals(
				"Check that adding of task with empty description is not executed",
				Constants.MESSAGE_COMMAND_EXECUTION_ERROR, feedback);
	}

}