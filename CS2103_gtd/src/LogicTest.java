import static org.junit.Assert.*;

import org.junit.Test;

public class LogicTest {

	Storage storage = new Storage();
	String fileName = "file_path_for_test.txt";
	Logic logic = Logic.getLogicObject();

	@Test
	public void testSingleton() {
		Logic duplicateLogic = Logic.getLogicObject();
		assertEquals("Check that only one logic object is created",
				logic,duplicateLogic);
	}

	@Test
	public void clearCommand() {//to-do
		logic.initializeEnvironment();
		String feedback = logic.execute("clear");
		assertEquals("Check that the clear command is working\n", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo clear command works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of clear command works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void deleteCommandEmptyFile(){//to-do
		logic.initializeEnvironment();
		String feedback = logic.execute("delete 1");
		assertEquals("Check deletion of empty file", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo delete empty file command works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of delete empty file command works",redoFeedback,expectedRedoFeedback);
	}
	
	public void deleteCommandNonEmptyFile(){//to-do
		logic.initializeEnvironment();
		logic.execute("add hello world");
		String feedback = logic.execute("delete 1");
		assertEquals("Check deletion of empty file", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo delete command works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of delete command works",redoFeedback,expectedRedoFeedback);
	}

	@Test
	public void addCommand() {//to-do
		logic.initializeEnvironment();
		logic.execute("clear");
		String desc = "hello";
		String acctualFeedback = logic.execute("add " + desc);
		String expFeedback = String.format(Constants.MESSAGE_ADDED, 1, desc);
		assertEquals("Check that the add command is working\n",
				acctualFeedback, expFeedback);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo add command works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of add command works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void 

}