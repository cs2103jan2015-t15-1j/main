import static org.junit.Assert.*;

import org.junit.Test;

public class LogicTest {

	Storage storage = new Storage();
	String fileName = "file_path_for_test.txt";
	Logic logic = Logic.getLogicObject();
	logic.initializeEnvironment();

	@Test
	public void testSingleton() {
		Logic duplicateLogic = Logic.getLogicObject();
		assertEquals("Check that only one logic object is created",
				logic,duplicateLogic);
	}

	@Test
	public void clearCommand() {//to-do
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
		logic.execute("clear");
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
	
	@Test
	public void deleteCommandNonEmptyFile(){//to-do
		logic.execute("clear");
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
	public void deleteMultipleValid(){//to-do
		logic.execute("clear");
		logic.execute("add hello world");
		logic.execute("add hello TaskWaltz");
		logic.execute("add some line");
		String feedback = logic.execute("delete 1,2,3");
		assertEquals("Check deletion of multiple tasks(all valid)", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo deletion of multiple tasks(all valid) works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of deletion of multiple tasks(all valid) works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void deleteSomeInvalid(){//to-do
		logic.execute("clear");
		logic.execute("add hello world");
		logic.execute("add hello TaskWaltz");
		logic.execute("add some line");
		logic.execute("delete 2");
		String feedback = logic.execute("delete 1,2,3");
		assertEquals("Check deletion of multiple tasks(some invalid)", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo deletion of multiple tasks(some invalid) works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of deletion of multiple tasks(some invalid) works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void deleteInvalidTaskId(){//to-do
		logic.execute("clear");
		String feedback = logic.execute("delete 1,2,3");
		assertEquals("Check deletion of multiple tasks(task id invalid)", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo deletion of multiple tasks(task id invalid) works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of deletion of multiple tasks(task id invalid) works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void deleteNotInteger(){//to-do
		logic.execute("clear");
		logic.execute("add hello world");
		logic.execute("add hello TaskWaltz");
		logic.execute("add some line");
		String feedback = logic.execute("delete 1.5,2,3");
		assertEquals("Check deletion of multiple tasks(some not integer)", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo deletion of multiple tasks(some not integer) works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of deletion of multiple tasks(some not integer) works",redoFeedback,expectedRedoFeedback);
	}

	@Test
	public void addToDo() {//to-do
		logic.execute("clear");
		String desc = "hello";
		String acctualFeedback = logic.execute("add " + desc);
		String expFeedback = String.format(Constants.MESSAGE_ADDED, 1, desc);
		assertEquals("Check that the add to do is working\n",
				acctualFeedback, expFeedback);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo add to do works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of add to do works",redoFeedback,expectedRedoFeedback);
	}
	
	
	@Test
	public void addEvent() {//to-do
		logic.execute("clear");
		String desc = "hello";
		LocalDateTime start = ;
		LocalDateTime end = ;
		String acctualFeedback = logic.execute("add " + desc);
		String expFeedback = String.format(Constants.MESSAGE_ADDED, 1, desc);
		assertEquals("Check that the add to do is working\n",
				acctualFeedback, expFeedback);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo add event works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of add event works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void addDeadline() {//to-do
		logic.execute("clear");
		String desc = "hello";
		LocalDateTime end = ;
		String acctualFeedback = logic.execute("add " + desc);
		String expFeedback = String.format(Constants.MESSAGE_ADDED, 1, desc);
		assertEquals("Check that the add to do is working\n",
				acctualFeedback, expFeedback);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo add deadline works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of add deadline works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void doneCommandEmptyFile(){//to-do
		logic.execute("clear");
		String feedback = logic.execute("done 1");
		assertEquals("Check done of empty file", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo done empty file command works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of done empty file command works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void doneCommandNonEmptyFile(){//to-do
		logic.execute("clear");
		logic.execute("add hello world");
		String feedback = logic.execute("done 1");
		assertEquals("Check deletion of empty file", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo done command works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of done command works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void doneMultipleValid(){//to-do
		logic.execute("clear");
		logic.execute("add hello world");
		logic.execute("add hello TaskWaltz");
		logic.execute("add some line");
		String feedback = logic.execute("done 1,2,3");
		assertEquals("Check done of multiple tasks(all valid)", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo done of multiple tasks(all valid) works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of done of multiple tasks(all valid) works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void doneSomeInvalid(){//to-do
		logic.execute("clear");
		logic.execute("add hello world");
		logic.execute("add hello TaskWaltz");
		logic.execute("add some line");
		logic.execute("done 2");
		String feedback = logic.execute("done 1,2,3");
		assertEquals("Check done of multiple tasks(some invalid)", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo done of multiple tasks(some invalid) works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of done of multiple tasks(some invalid) works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void doneInvalidTaskId(){//to-do
		logic.execute("clear");
		String feedback = logic.execute("done 1,2,3");
		assertEquals("Check done of multiple tasks(task id invalid)", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo done of multiple tasks(task id invalid) works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of done of multiple tasks(task id invalid) works",redoFeedback,expectedRedoFeedback);
	}
	
	@Test
	public void doneNotInteger(){//to-do
		logic.execute("clear");
		logic.execute("add hello world");
		logic.execute("add hello TaskWaltz");
		logic.execute("add some line");
		String feedback = logic.execute("done 1.5,2,3");
		assertEquals("Check done of multiple tasks(some not integer)", feedback,
				Constants.MESSAGE_ALL_DELETED);
		String undoFeedback = logic.execute("Undo");
		String expectedUndoFeedback = ;
		assertEquals("Check that undo done of multiple tasks(some not integer) works",undoFeedback,expectedUndoFeedback);
		String redoFeedback = logic.execute("Redo");
		String expectedRedoFeedback = ;
		assertEquals("Check that redo undo of done of multiple tasks(some not integer) works",redoFeedback,expectedRedoFeedback);
	}

}