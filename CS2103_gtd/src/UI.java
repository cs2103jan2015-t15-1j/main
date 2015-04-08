import java.util.Scanner;

//@author A0135280M
public class UI {
    
    private static Scanner scanner = new Scanner(System.in);
    private static Logic mainLogic;
    
    public static void main(String[] args) {
    	mainLogic = Logic.getLogicObject();
        String initializationMessage = initializeEnvironment();
        showToUser(initializationMessage);
        while (true) {
            System.out.print("\nEnter command: ");
            String userCommand = scanner.nextLine();
            String feedback = mainLogic.execute(userCommand);
            showToUser(feedback);
        }
    }
    
    private static String initializeEnvironment() {
        String initializationMessage = Constants.MESSAGE_WELCOME;
        initializationMessage += "\n" + mainLogic.initializeEnvironment();
        initializationMessage += "\n" + mainLogic.execute("display");
        return initializationMessage;
    }
    
    private static void showToUser(String message) {
        System.out.println(message);
    }
    
}
