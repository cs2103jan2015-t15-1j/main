import static org.junit.Assert.*;

import org.junit.Test;


public class LogicTest {

    Storage storage = new Storage();
    String fileName = "file_path_for_test.txt";
    Logic logic = Logic.getLogicObject();
    
    @Test
    public void clearCommand() {
        logic.initializeEnvironment();
        String feedback = logic.execute("clear");
        assertEquals("Check that the clear command is working\n", 
                feedback, Constants.MESSAGE_ALL_DELETED);
    }

    @Test
    public void addCommand() {
        logic.initializeEnvironment();
        logic.execute("clear");
        String desc = "hello";
        String acctualFeedback = logic.execute("add "+desc);
        String expFeedback = String.format(Constants.MESSAGE_ADDED, 1, desc);
        assertEquals("Check that the add command is working\n", 
                acctualFeedback, expFeedback);
    }
    
}