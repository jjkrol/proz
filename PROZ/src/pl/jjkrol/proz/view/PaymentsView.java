package pl.jjkrol.proz.view;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.controller.LocumMockup;
import pl.jjkrol.proz.controller.ViewPayments;

import java.util.List;
import org.apache.log4j.Logger;

/**
 * A class responsible for handling all user interactions
 * connected with operating on payments 
 * @author jjkrol
 */
public class PaymentsView implements SpecificView {
	private String name = "P³atnoœci";
	private final JPanel panel = new JPanel();
	private final JTextArea textArea = new JTextArea(15,50);
	private final Controller core = Controller.getInstance();
	static Logger logger = Logger.getLogger(PROZJFrame.class);
	
		public void displayLocums(List<LocumMockup> locums){
			for(LocumMockup loc : locums){
				textArea.append(loc+"\n");
			}
		}
		PaymentsView(){
			panel.setLayout(new FlowLayout(FlowLayout.LEADING));
			panel.add(new JScrollPane(textArea));
			
		}
		
		@Override
		public JPanel getJPanel() {
			return panel;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		public void getReady(){
			logger.debug(name+" got ready");
			core.putEvent(new ViewPayments());
		}

}
