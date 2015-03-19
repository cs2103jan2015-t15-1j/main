import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class EditCommandTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() {
		Command test = new EditCommand(new Task(1, "test", null, null, false));
		Storage storage = new StorageStub();
		storage.add(new Task(1, "original", null, null, false));
		assertEquals("new description "+"test", test.execute(storage));
	}

	@Test
	public void testUndo() {
		Command test = new EditCommand(new Task(1, "test", null, null, false));
		Storage storage = new StorageStub();
		storage.add(new Task(1, "original", null, null, false));
		assertEquals("new description "+"test", test.execute(storage));
		assertEquals("new description "+"original", test.undo(storage));
		assertEquals("new description "+"test", test.execute(storage));

	}

}
