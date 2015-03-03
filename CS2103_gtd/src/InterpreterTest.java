/**
 * InterpreterTest.java
 * @author Hanbin
 * Created: 03/03/2015
 * Last Updated: 03/03/2015
 */
import static org.junit.Assert.*;

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
		COMMAND_TYPE expected = COMMAND_TYPE.EDIT;
		COMMAND_TYPE actual = Interpreter.interpretCommandType(testInput);
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
		COMMAND_TYPE expected = COMMAND_TYPE.ADD;
		COMMAND_TYPE actual = Interpreter.interpretCommandType(testInput);
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
}
