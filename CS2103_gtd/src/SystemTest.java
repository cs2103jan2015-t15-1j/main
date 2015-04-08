import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

//@author A0135295B
public class SystemTest {

	@Test
	public void TranslatorTest_1() throws Exception {
		Translator t = new Translator(new StorageStub(), new History());
		String testInput = "add guest speech at Shaw Alumni Center start 4/5 18:00 end 20:00";
		Command c = t.createCommand(testInput);
		assertEquals(c.getClass(), AddCommand.class);
	}
	
	@Test
	public void TranslatorTest_2() throws Exception {
		Translator t = new Translator(new StorageStub(), new History());
		String testInput = "add guest speech at Shaw Alumni Center start 4/5 18:00 end 20:00";
		AddCommand c = (AddCommand) t.createCommand(testInput);
		Task taskOutput = c.tasksToAdd[0];
		assertEquals(taskOutput.getId(), -1);
	}
	
	@Test
	public void TranslatorTest_3() throws Exception {
		Translator t = new Translator(new StorageStub(), new History());
		String testInput = "add guest speech at Shaw Alumni Center start 4/5 18:00 end 20:00";
		AddCommand c = (AddCommand) t.createCommand(testInput);
		Task taskOutput = c.tasksToAdd[0];
		assertEquals(taskOutput.getDescription(), "guest speech at Shaw Alumni Center");
	}
	
	@Test
	public void TranslatorTest_4() throws Exception {
		Translator t = new Translator(new StorageStub(), new History());
		String testInput = "add guest speech at Shaw Alumni Center start 4/5 18:00 end 20:00";
		AddCommand c = (AddCommand) t.createCommand(testInput);
		Task taskOutput = c.tasksToAdd[0];
		assertEquals(taskOutput.getDone(), false);
	}
	
	@Test
	public void TranslatorTest_5() throws Exception {
		Translator t = new Translator(new StorageStub(), new History());
		String testInput = "add guest speech at Shaw Alumni Center start 18:00 end 20:00";
		AddCommand c = (AddCommand) t.createCommand(testInput);
		Task taskOutput = c.tasksToAdd[0];
		LocalDateTime today = LocalDateTime.now();
		assertEquals(taskOutput.getStartDateTime(), LocalDateTime.of(today.getYear(),
				today.getMonthValue(), today.getDayOfMonth(), 18, 0));
	}

	@Test
	public void TranslatorTest_6() throws Exception {
		Translator t = new Translator(new StorageStub(), new History());
		String testInput = "add guest speech at Shaw Alumni Center start 18:00 end 20:00";
		AddCommand c = (AddCommand) t.createCommand(testInput);
		Task taskOutput = c.tasksToAdd[0];
		LocalDateTime today = LocalDateTime.now();
		assertEquals(taskOutput.getEndDateTime(), LocalDateTime.of(today.getYear(),
				today.getMonthValue(), today.getDayOfMonth(), 20, 0));
	}

}
