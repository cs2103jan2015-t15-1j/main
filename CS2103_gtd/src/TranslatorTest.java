import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


public class TranslatorTest {
	

		//==========Constants for Translator class Section Beginning==========//
		// Keywords for ADD command
		private static final String KEYWORD_ADD_DEADLINE = "((by)|(BY)|(due)|(DUE))";
		private static final String KEYWORD_ADD_LOCATION = "((at)|(AT)|(@))";
		private static final String KEYWORD_ADD_EVENTSTART = "((from)|(FROM)|(start)|(START))";
		private static final String KEYWORD_ADD_EVENTEND = "((to)|(TO)|(end)|(END))";
		private static final String[] addParameterKeywords = 
			{KEYWORD_ADD_DEADLINE, KEYWORD_ADD_LOCATION, KEYWORD_ADD_EVENTSTART, KEYWORD_ADD_EVENTEND};
		
		// Keywords for SEARCH command
		private static final String KEYWORD_SEARCH_DUE = "((due)|(DUE))";
		private static final String KEYWORD_SEARCH_AFTER = "((after)|(AFTER))";
		private static final String KEYWORD_SEARCH_BEFORE = "((before)|(BEFORE))";
		private static final String KEYWORD_SEARCH_ON = "((on)|(ON))";
		private static final String[] searchParameterKeywords = 
			{KEYWORD_SEARCH_DUE, KEYWORD_SEARCH_AFTER, KEYWORD_SEARCH_BEFORE, KEYWORD_SEARCH_ON};
		
		// Keywords for EDIT command
		private static final String KEYWORD_EDIT_DEADLINE = "((deadline)|(DEADLINE))";
		private static final String KEYWORD_EDIT_LOCATION = "((location)|(LOCATION))";
		private static final String KEYWORD_EDIT_EVENTSTART = "((start)|(START))";
		private static final String KEYWORD_EDIT_EVENTEND = "((end)|(END))";
		private static final String KEYWORD_EDIT_DESCRIPTION = "((desc)|(DESC)|(description)|(DESCRIPTION))";
		private static final String[] editParameterKeywords = 
			{KEYWORD_EDIT_DEADLINE, KEYWORD_EDIT_LOCATION, KEYWORD_EDIT_EVENTSTART, KEYWORD_EDIT_EVENTEND,
			KEYWORD_EDIT_DESCRIPTION};
		
		
		// Format for Date-Time input.
		private static final String DELIMITTER_DATE = "(\\s|-|/)";
		private static final String DD_MM_YYYY = "\\d\\d" + DELIMITTER_DATE + "\\d\\d" +
				DELIMITTER_DATE + "\\d\\d\\d\\d";
		
		private static final String DELIMITTER_TIME = "(:)";
		private static final String HH_MM = "(0|1|2)\\d" + DELIMITTER_TIME + "([0-5])\\d";
		private static final String H_MM = "\\d" + DELIMITTER_TIME + "([0-5])\\d";
		private static final String HH = "(0|1|2)\\d";
		private static final String H = "\\d";
		
		
		// Default values for Date-Time variables.
		private static final String FILLER_DEFAULT_TIME = " 12:00 ";
		private static final int DATETIME_HOUR_MINIMUM = 0;
		private static final int DATETIME_MINUTE_MINIMUM = 0;
		private static final int DATETIME_HOUR_MAXIMUM = 23;
		private static final int DATETIME_MINUTE_MAXIMUM = 59;
		
		// Miscellaneous default values.
		private static final String EMPTY_STRING = "";
		private static final String SINGLE_SPACE = " ";
		private static final String WHITESPACE = "\\s+";
		private static final int ARRAY_POSITION_FIRST = 0;
		private static final int ARRAY_POSITION_SECOND = 1;
		private static final int NUM_TASKID_MINIMUM = 1;
		
		// Error values.
		private static final String INVALID_COMMAND = "%1$s is not a valid command!";
		private static final String PARAMETER_DOES_NOT_EXIST = null;
		private static final int INT_PARAM_INVALID = -1;
		private static final int INVALID_TASK_ID = -1;
		//==========Constants for Translator class Section End================//
		
		private Translator trans = new Translator(new StorageStub(), new History());
		private Class translatorClass = trans.getClass();
		
		/*
		private Command createAddCommand(String usercommand)
		private Command createDisplayCommand()
		private Command createSearchCommand(String usercommand)
		private Command createEditCommand(String usercommand) 
		private Command createDeleteCommand(String usercommand)
		private Command createClearCommand()
		private Command createDoneCommand(String usercommand)
		private Command createUndoCommand()
		private Command createRedoCommand()
		private Command createHelpCommand()
		private Command createSetDirectoryCommand(String usercommand)
		private Command createExitCommand()
		*/
		/*
		private Task interpretAddParameter(String usercommand)
		private Task interpretEditParameter(String usercommand)
		private int extractEditTaskID(String usercommand)
		private Task interpretSearchParameter(String usercommand)
		private LocalDateTime getBeginningOfDay(LocalDateTime dateTime)
		private LocalDateTime getEndOfDay(LocalDateTime dateTime)
		private int[] interpretDeleteParameter(String usercommand) 
		private int[] interpretDoneParameter(String usercommand)
		private LocalDateTime interpretDateTimeParam(String param)
		private LocalDate extractLocalDate(String param)
		private LocalTime extractLocalTime(String param)
		
		private String extractFirstWord(String str)
		
		private String extractSecondWord(String str);
		private String removeFirstWord(String str);
		private int[] interpretTaskIDs(String param);
		private Path interpretFilePath(String userInput);
		*/
		/*
		@Test
		public void interpretFilePathTest() {
			try {
				Method method = translatorClass.getDeclaredMethod("interpretFilePath", String.class);
				method.setAccessible(true);
				method.invoke(trans, ".");
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				System.out.println(e.toString());
			}
		}
		*/
	
		
		/* Boundary case for null input */
		@Test
		public void determineCommandTypeTest_1() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = null;
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.INVALID;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Boundary case for empty string input */
		@Test
		public void determineCommandTypeTest_2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.INVALID;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for none-existent command input */
		@Test
		public void determineCommandTypeTest_3() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "NONEEXISTENT ";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.INVALID;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for misplaced command input */
		@Test
		public void determineCommandTypeTest_4() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "CommandShouldComeFirst ADD ";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.INVALID;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for capital command */
		@Test
		public void determineCommandTypeTest_5() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "ADD";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.ADD;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for lower case command*/
		@Test
		public void determineCommandTypeTest_6() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "add";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.ADD;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_7() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "display";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.DISPLAY;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_8() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "done";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.DONE;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_9() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "delete";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.DELETE;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_10() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "edit";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.EDIT;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_11() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "undo";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.UNDO;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_12() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "redo";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.REDO;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_13() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "help";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.HELP;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_14() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "setdir";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.SETDIR;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_15() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "setdirectory";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.SETDIR;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_16() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "exit";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.EXIT;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_17() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "invalid";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.INVALID;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_18() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "search";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.SEARCH;
			assertEquals(actualOutput, expectedOutput);
		}
		
		/* Partition case for a command */
		@Test
		public void determineCommandTypeTest_19() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Method methodELD = translatorClass.getDeclaredMethod("determineCommandType", String.class);
			methodELD.setAccessible(true);
			String testInput = "clear";
			CommandType actualOutput = (CommandType) methodELD.invoke(trans, testInput);
			CommandType expectedOutput = CommandType.CLEAR;
			assertEquals(actualOutput, expectedOutput);
		}

	@Test
	public void extractLocalDateTest_1() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = null;
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = null;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = null;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_3() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "12:00";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = null;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_4() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "22#11#3333";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = null;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_5() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "22-11-3333";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(3333, 11, 22);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_6() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "22/11/3333";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(3333, 11, 22);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_7() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "22 11 3333";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(3333, 11, 22);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_8() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "22/11/3333 01/02/3000";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(3333, 11, 22);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_9() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "12:00 once upon a time 22/11/3333 some other time 01/02/3000";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(3333, 11, 22);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_10() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "12/1/1999";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(1999, 1, 12);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_11() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "1/12/1999";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(1999, 12, 1);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_12() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "1/2/1999";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(1999, 2, 1);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_13() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "11/12/16";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(2016, 12, 11);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_14() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "11/2/16";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(2016, 2, 11);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_15() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "1/12/16";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(2016, 12, 1);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_16() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "1/2/16";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(2016, 2, 1);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_17() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "11/12";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(2015, 12, 11);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_18() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "11/2";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(2015, 2, 11);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_19() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "1/12";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(2015, 12, 1);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalDateTest_20() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELD = translatorClass.getDeclaredMethod("extractLocalDate", String.class);
		methodELD.setAccessible(true);
		String testInput = "1/2";
		LocalDate actualOutput = (LocalDate) methodELD.invoke(trans, testInput);
		LocalDate expectedOutput = LocalDate.of(2015, 2, 1);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractFirstWordTest_1() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodEFW = translatorClass.getDeclaredMethod("extractFirstWord", String.class);
		methodEFW.setAccessible(true);
		String testInput = null;
		String actualOutput = (String) methodEFW.invoke(trans, testInput);
		String expectedOutput = null;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractFirstWordTest_2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodEFW = translatorClass.getDeclaredMethod("extractFirstWord", String.class);
		methodEFW.setAccessible(true);
		String testInput = "";
		String actualOutput = (String) methodEFW.invoke(trans, testInput);
		String expectedOutput = "";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractFirstWordTest_3() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodEFW = translatorClass.getDeclaredMethod("extractFirstWord", String.class);
		methodEFW.setAccessible(true);
		String testInput = "a";
		String actualOutput = (String) methodEFW.invoke(trans, testInput);
		String expectedOutput = "a";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractFirstWordTest_4() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodEFW = translatorClass.getDeclaredMethod("extractFirstWord", String.class);
		methodEFW.setAccessible(true);
		String testInput = " a";
		String actualOutput = (String) methodEFW.invoke(trans, testInput);
		String expectedOutput = "a";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractFirstWordTest_5() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodEFW = translatorClass.getDeclaredMethod("extractFirstWord", String.class);
		methodEFW.setAccessible(true);
		String testInput = " 1 a";
		String actualOutput = (String) methodEFW.invoke(trans, testInput);
		String expectedOutput = "1";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractFirstWordTest_7() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodEFW = translatorClass.getDeclaredMethod("extractFirstWord", String.class);
		methodEFW.setAccessible(true);
		String testInput = "abcdefg    ";
		String actualOutput = (String) methodEFW.invoke(trans, testInput);
		String expectedOutput = "abcdefg";
		assertEquals(actualOutput, expectedOutput);
	}
	
	
	@Test
	public void extractSecondWordTest_1() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodESW = translatorClass.getDeclaredMethod("extractSecondWord", String.class);
		methodESW.setAccessible(true);
		String testInput = null;
		String actualOutput = (String) methodESW.invoke(trans, testInput);
		String expectedOutput = null;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractSecondWordTest_2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodESW = translatorClass.getDeclaredMethod("extractSecondWord", String.class);
		methodESW.setAccessible(true);
		String testInput = "";
		String actualOutput = (String) methodESW.invoke(trans, testInput);
		String expectedOutput = "";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractSecondWordTest_3() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodESW = translatorClass.getDeclaredMethod("extractSecondWord", String.class);
		methodESW.setAccessible(true);
		String testInput = " a";
		String actualOutput = (String) methodESW.invoke(trans, testInput);
		String expectedOutput = "";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractSecondWordTest_4() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodESW = translatorClass.getDeclaredMethod("extractSecondWord", String.class);
		methodESW.setAccessible(true);
		String testInput = " a 1";
		String actualOutput = (String) methodESW.invoke(trans, testInput);
		String expectedOutput = "1";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractSecondWordTest_5() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodESW = translatorClass.getDeclaredMethod("extractSecondWord", String.class);
		methodESW.setAccessible(true);
		String testInput = " a b c";
		String actualOutput = (String) methodESW.invoke(trans, testInput);
		String expectedOutput = "b";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractSecondWordTest_6() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodESW = translatorClass.getDeclaredMethod("extractSecondWord", String.class);
		methodESW.setAccessible(true);
		String testInput = "abcdefg    ";
		String actualOutput = (String) methodESW.invoke(trans, testInput);
		String expectedOutput = "";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractSecondWordTest_7() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodESW = translatorClass.getDeclaredMethod("extractSecondWord", String.class);
		methodESW.setAccessible(true);
		String testInput = "11111111111 2";
		String actualOutput = (String) methodESW.invoke(trans, testInput);
		String expectedOutput = "2";
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalTimeTest_1() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELT = translatorClass.getDeclaredMethod("extractLocalTime", String.class);
		methodELT.setAccessible(true);
		String testInput = "10:15";
		LocalTime actualOutput = (LocalTime) methodELT.invoke(trans, testInput);
		LocalTime expectedOutput = LocalTime.of(10, 15);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalTimeTest_2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELT = translatorClass.getDeclaredMethod("extractLocalTime", String.class);
		methodELT.setAccessible(true);
		String testInput = "3:45";
		LocalTime actualOutput = (LocalTime) methodELT.invoke(trans, testInput);
		LocalTime expectedOutput = LocalTime.of(3, 45);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalTimeTest_3() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELT = translatorClass.getDeclaredMethod("extractLocalTime", String.class);
		methodELT.setAccessible(true);
		String testInput = "11";
		LocalTime actualOutput = (LocalTime) methodELT.invoke(trans, testInput);
		LocalTime expectedOutput = LocalTime.of(11, 0);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalTimeTest_4() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELT = translatorClass.getDeclaredMethod("extractLocalTime", String.class);
		methodELT.setAccessible(true);
		String testInput = "7";
		LocalTime actualOutput = (LocalTime) methodELT.invoke(trans, testInput);
		LocalTime expectedOutput = LocalTime.of(7, 0);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalTimeTest_5() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELT = translatorClass.getDeclaredMethod("extractLocalTime", String.class);
		methodELT.setAccessible(true);
		String testInput = "10:15";
		LocalTime actualOutput = (LocalTime) methodELT.invoke(trans, testInput);
		LocalTime expectedOutput = LocalTime.of(10, 15);
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalTimeTest_6() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELT = translatorClass.getDeclaredMethod("extractLocalTime", String.class);
		methodELT.setAccessible(true);
		String testInput = null;
		LocalTime actualOutput = (LocalTime) methodELT.invoke(trans, testInput);
		LocalTime expectedOutput = null;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void extractLocalTimeTest_7() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodELT = translatorClass.getDeclaredMethod("extractLocalTime", String.class);
		methodELT.setAccessible(true);
		String testInput = "";
		LocalTime actualOutput = (LocalTime) methodELT.invoke(trans, testInput);
		LocalTime expectedOutput = null;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void provideDefaultDateTest_1() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodPDD = translatorClass.getDeclaredMethod("provideDefaultDate", LocalTime.class);
		methodPDD.setAccessible(true);
		LocalTime testInput = null;
		LocalDate actualOutput = (LocalDate) methodPDD.invoke(trans, testInput);
		LocalDate expectedOutput = null;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void provideDefaultDateTest_2() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodPDD = translatorClass.getDeclaredMethod("provideDefaultDate", LocalTime.class);
		methodPDD.setAccessible(true);
		LocalTime testInput = LocalTime.of(23, 59);
		LocalDate actualOutput = (LocalDate) methodPDD.invoke(trans, testInput);
		LocalDate today = LocalDate.now();
		LocalDate expectedOutput = today;
		assertEquals(actualOutput, expectedOutput);
	}
	
	@Test
	public void provideDefaultDateTest_3() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method methodPDD = translatorClass.getDeclaredMethod("provideDefaultDate", LocalTime.class);
		methodPDD.setAccessible(true);
		LocalTime testInput = LocalTime.of(0, 0);
		LocalDate actualOutput = (LocalDate) methodPDD.invoke(trans, testInput);
		LocalDate today = LocalDate.now();
		LocalDate expectedOutput = today.plusDays(1);
		assertEquals(actualOutput, expectedOutput);
	}
	
	/*
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
	*/
	
}
