import java.util.Stack;

//@author A0111337U
public class History {

	Stack<Command> undoStack = new Stack<Command>();
	Stack<Command> redoStack = new Stack<Command>();
	boolean isUndoOrRedo = false;
	boolean isRedo = false;

	public void pushUndo(Command cmd) {
		// command should already be in its undo state
		if (isUndoOrRedo) {
			isUndoOrRedo = false;
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
		// command should already be in its undo state
		redoStack.push(cmd);
	}

	public Command getUndo() {
		Command lastCmd;
		try {
			lastCmd = undoStack.pop();
			isUndoOrRedo = true;
			return lastCmd;
		} catch (Exception e) {
			throw new NullPointerException(Constants.MESSAGE_NO_UNDO);
		}
	}

	// @author A0111337U
	public Command getRedo() {
		Command lastCmd;
		try {
			lastCmd = redoStack.pop();
			isUndoOrRedo = true;
			isRedo = true;
			return lastCmd;
		} catch (Exception e) {
			throw new NullPointerException(Constants.MESSAGE_NO_REDO);
		}
	}

}
