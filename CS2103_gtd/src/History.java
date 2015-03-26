import java.util.Stack;


public class History {
    
    Stack<Command> undoStack = new Stack<Command>();
    Stack<Command> redoStack = new Stack<Command>();
    
    public History() {
        
    }
    
    public void pushUndo(Command cmd) {
        undoStack.push(cmd);
        redoStack.clear();
    }
    
    public Command getUndo() {
        Command lastCmd;
		try {
			lastCmd = undoStack.pop();
		} catch (Exception e) {
			return null;
		}
        Command reversedCmd = lastCmd.makeUndo();
        redoStack.push(reversedCmd);
        return lastCmd;
    }
    
    public Command getRedo() {
        Command lastCmd;
		try {
			lastCmd = redoStack.pop();
		} catch (Exception e) {
			return null;
		}
        Command reversedCmd = lastCmd.makeUndo();
        undoStack.push(reversedCmd);
        return lastCmd;
    }

}
