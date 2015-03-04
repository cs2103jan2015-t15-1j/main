import java.util.Scanner;


public class UI {
    
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        String initalizationMessage = Logic.initializeEnvironment();
        showToUser(initalizationMessage);
        while (true) {
            System.out.print("Enter command: ");
            String userCommand = scanner.nextLine();
            String feedback = Logic.execute(userCommand);
            showToUser(feedback);
        }
    }
    
    private static void showToUser(String message) {
        System.out.println(message);
    }
    
}
