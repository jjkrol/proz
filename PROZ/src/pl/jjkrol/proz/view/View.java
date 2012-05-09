package pl.jjkrol.proz.view;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Main class of the application view
 * @author jjkrol
 */
public class View {
private	PROZJFrame frame;
public SpecificView paymentsView;

/**
 * creates and shows the main frame
 */
public void startGUI(){
	paymentsView = new PaymentsView();
	frame = new PROZJFrame();
	SwingUtilities.invokeLater(new Runnable(){
		public void run(){
			frame.setTitle("PROZ");
			frame.setDefaultCloseOperation(PROZJFrame.EXIT_ON_CLOSE);
			frame.setSize(800,600);
			frame.addView(paymentsView);
			frame.setVisible(true);
		}
	});
}

/**
 * displays a message box
 * @param message
 */
public void displayUserMessage(String message){
	JOptionPane.showMessageDialog(frame, message);
}
}
