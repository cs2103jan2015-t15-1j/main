import java.util.Stack;


public class History {
    
    Stack<Command> undoStack = new Stack<Command>();
    Stack<Command> redoStack = new Stack<Command>();
    
    public History() {
        
    }
    
    public void pushUndo(Command cmd) {
        undoStack.push(cmd);
        redoStack = new Stack<Command>();
    }
    
    public Command getUndo() {
        Command lastCmd = undoStack.pop();
        Command reversedCmd = lastCmd.makeUndo();
        redoStack.push(reversedCmd);
        return lastCmd;
    }
    
    public Command getRedo() {
        Command lastCmd = redoStack.pop();
        Command reversedCmd = lastCmd.makeUndo();
        undoStack.push(reversedCmd);
        return lastCmd;
    }

}
