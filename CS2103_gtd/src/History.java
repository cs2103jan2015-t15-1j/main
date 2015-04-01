import java.util.Stack;


public class History {
    
    Stack<Command> undoStack = new Stack<Command>();
    Stack<Command> redoStack = new Stack<Command>();
    
    public History() {
        
    }
    
    public void pushUndo(Command cmd) {
    	//cmd should already be in its undo state
        undoStack.push(cmd);
        redoStack.clear();
    }
    
    public void pushRedo(Command cmd) {
    	//cmd should already be in its undo state
        redoStack.push(cmd);
    }
    
    public Command getUndo() {
        Command lastCmd;
		try {
			lastCmd = undoStack.pop();
			return lastCmd;
		} catch (Exception e) {
			throw new NullPointerException("No commmands to undo");
		}
    }
    
    public Command getRedo() {
        Command lastCmd;
		try {
			lastCmd = redoStack.pop();
			return lastCmd;
		} catch (Exception e) {
			throw new NullPointerException("No commands to redo");
		}
    }

}
