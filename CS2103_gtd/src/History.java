import java.util.Stack;


public class History {
    
    Stack<Command> undoStack = new Stack<Command>();
    Stack<Command> redoStack = new Stack<Command>();
    
    public History() {
        
    }
    
    public void pushUndo(Command cmd) {
        undoStack.push(cmd);
    }
    
    public Command getLastCommand() {
        Command lastCmd = undoStack.pop();
        Command reversedCmd = lastCmd.makeUndo();
        redoStack.push(reversedCmd);
        return lastCmd;
    }

}