import java.util.Stack;

//@author A0111337U
public class History {

	Stack<Command> undoStack = new Stack<Command>();
	Stack<Command> redoStack = new Stack<Command>();
	boolean isUndo = false;
	boolean isRedo = false;

	public void pushUndo(Command cmd) {
		// cmd should already be in its undo state
		if (isUndo) {
			isUndo = false;
			return;
		}
		undoStack.push(cmd);
		if (!isRedo) {
			redoStack.clear();
		} else {
			isRedo = false;
		}
	}

	public void pushRedo(Command cmd) {
		// cmd should already be in its undo state
		redoStack.push(cmd);
	}

	public Command getUndo() {
		Command lastCmd;
		try {
			lastCmd = undoStack.pop();
			isUndo = true;
			return lastCmd;
		} catch (Exception e) {
			throw new NullPointerException("No commands to undo");
		}
	}

	// @author A0111337U
	public Command getRedo() {
		Command lastCmd;
		try {
			lastCmd = redoStack.pop();
			isUndo = true;
			isRedo = true;
			return lastCmd;
		} catch (Exception e) {
			throw new NullPointerException("No commands to redo");
		}
	}

}
