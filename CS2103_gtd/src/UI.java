import java.util.Scanner;


public class UI {
    
    private static Scanner scanner = new Scanner(System.in);
    private static Logic mainLogic;
    
    public static void main(String[] args) {
    	mainLogic = Logic.getLogicObject();
        String initalizationMessage = mainLogic.initializeEnvironment();
        showToUser(initalizationMessage);
        while (true) {
            System.out.print("\nEnter command: ");
            String userCommand = scanner.nextLine();
            String feedback = mainLogic.execute(userCommand);
            showToUser(feedback);
        }
    }
    
    private static void showToUser(String message) {
        System.out.println(message);
    }
    
}
