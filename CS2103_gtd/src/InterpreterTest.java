/**
 * InterpreterTest.java
 * @author Hanbin
 * Created: 03/03/2015
 * Last Updated: 03/03/2015
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.Test;


public class InterpreterTest {

	private static final String KEYWORD_DEADLINE = "((b|B)(y|Y)|(d|D)(u|U)(e|E))";
	private static final String KEYWORD_LOCATION = "((a|A)(t|T)|@)";
	private static final String KEYWORD_EVENTSTART = "((f|F)(r|R)(o|O)(m|M)|(s|S)(t|T)(a|A)(r|R)(t|T))";
	private static final String KEYWORD_EVENTEND = "((t|T)(o|O)|(e|E)(n|N)(d|D))";
	
	private static final String[] addParameterKeywords = {"by", "at", "from", "to"};
	
	@Test
	public void interpretCommandTypeTest() {
		String testInput = "Edit past, present, and future";
		CommandType expected = CommandType.EDIT;
		CommandType actual = Interpreter.interpretCommandType(testInput);
		assertEquals(expected, actual);
	}

	@Test
	public void extractFirstWordTest() {
		String testInput = "     firstWord  first word  FirstWorld Problem";
		String expected = "firstWord";
		String actual = Interpreter.extractFirstWord(testInput);
		assertEquals(expected, actual);
	}
	
	@Test
	public void determineCommandTypeTest() {
		String testInput = "aDD";
		CommandType expected = CommandType.ADD;
		CommandType actual = Interpreter.interpretCommandType(testInput);
		assertEquals(expected, actual);
	}
	
	KeywordInfoList kList;
	public void initKList() {
		String usercommand = "add format a commit by 5PM at atLanta";
		kList = new KeywordInfoList(usercommand, addParameterKeywords);
	}
	
	@Test
	public void paramDescriptionTest() {
		initKList();
		String expected = "format a commit";
		String actual = kList.getDescription();
		assertEquals(expected, actual);
	}
	
	@Test
	public void paramDeadlineTest() {
		initKList();
		String expected = "5PM";
		String actual = kList.getParameter(KEYWORD_DEADLINE);
		assertEquals(expected, actual);
	}
	
	@Test
	public void paramLocationTest() {
		initKList();
		String expected = "atLanta";
		String actual = kList.getParameter(KEYWORD_LOCATION);
		assertEquals(expected, actual);
	}
	
	@Test
	public void paramEventStart() {
		initKList();
		String expected = null;
		String actual = kList.getParameter(KEYWORD_EVENTSTART);
		assertEquals(expected, actual);
	}
	
	@Test
	public void paramEventEnd() {
		initKList();
		String expected = null;
		String actual = kList.getParameter(KEYWORD_EVENTEND);
		assertEquals(expected, actual);
	}
	
	@Test
	public void removeFirstWordTest() {
		String testInput = "  firstWord  firstWordRemoved  yes";
		String expected = "firstWordRemoved  yes";
		String actual = Interpreter.removeFirstWord(testInput);
		assertEquals(expected, actual);
	}
	
	@Test
	public void interpretDeleteParametersTest() {
		String testInput = " delete 120 33   135  34";
		int[] expected = {120, 33, 135, 34};
		int[] actual = Interpreter.interpretDeleteParameter(testInput);
		assertTrue(Arrays.equals(expected, actual));
	}
	
	@Test
	public void interpretDoneParametersTest() {
		String testInput = " done 120 33   135  34";
		int[] expected = {120, 33, 135, 34};
		int[] actual = Interpreter.interpretDeleteParameter(testInput);
		assertTrue(Arrays.equals(expected, actual));
	}
	
	@Test
	public void extractLocalDateTest() {
		String testInput = " add task waltz meting start 10:30 on 04/03/2015 at Computing";
		LocalDate expected = LocalDate.of(2015, 3, 4);
		LocalDate actual = Interpreter.extractLocalDate(testInput);
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void extractLocalTimeTest() {
		String testInput = " add task waltz meting start 10:30 on 04/03/2015 at Computing";
		LocalTime expected = LocalTime.of(10, 30);
		LocalTime actual = Interpreter.extractLocalTime(testInput);
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void interpretDateTimeParamTest() {
		String testInput = " on the 20:30 of 03-12-5888 Supernova exploded.";
		LocalDateTime expected = LocalDateTime.of(5888, 12, 3, 20, 30);
		LocalDateTime actual = Interpreter.interpretDateTimeParam(testInput, CommandType.ADD);
		assertTrue(expected.equals(actual));
	}
	
	public void interpretAddOREditParameterTest() {
		String testInput = " add finish lunch and call a loved one at Hawker Center from 12:30 04/03/2015 to 15:30 04/03/2015";
		Task expected = new Task();
		expected.setDescription("finish lunch and call a loved one");
		expected.setDone(false);
		expected.setLocation("Hawker Center");
		expected.setStartDateTime(LocalDateTime.of(2015, 3, 4, 12, 30));
		expected.setEndDateTime(LocalDateTime.of(2015, 3, 4, 15, 30));
		Task actual = Interpreter.interpretAddOREditParameter(testInput, Interpreter.interpretCommandType(testInput));
		assertEquals(expected, actual);
	}
}
