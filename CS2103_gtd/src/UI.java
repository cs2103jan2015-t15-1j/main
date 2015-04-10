import java.util.Scanner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class UI extends JPanel implements ActionListener {
    private static Logic mainLogic;
    protected JTextField textField;
    protected JTextArea textArea;
    private final String newline = "\n\n";
    
    public UI() {
    	super(new GridBagLayout());
    	this.textField = new JTextField(100);
    	textField.addActionListener(this);
		textArea = new JTextArea(20, 100);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		//Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(textField, c);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);
    }
    
    public void actionPerformed(ActionEvent evt) {
        String userCommand = textField.getText();
        String feedback = mainLogic.execute(userCommand);
        showToUser(feedback);
        textField.setText("");
 
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    
    public void showToUser(String message) {
    	textArea.append(message + newline);
    }
    
    public static void main(String[] args) {
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	mainLogic = Logic.getLogicObject();
            	initializeEnvironment();
                createAndShowGUI();
            }
        });
    }
    
    private static String initializeEnvironment() {
        String initializationMessage = Constants.MESSAGE_WELCOME;
        initializationMessage += "\n" + mainLogic.initializeEnvironment();
        initializationMessage += "\n" + mainLogic.execute("display");
        return initializationMessage;
    }
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        frame.add(new UI());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
