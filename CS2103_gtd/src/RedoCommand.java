
public class RedoCommand implements Command {
        
        public RedoCommand() {
            
        }
        
        @Override
        public String execute(Storage storage) {
            return "";
        }

        @Override
        public Command makeUndo() {
            return new RedoCommand();
        }

    }
}
