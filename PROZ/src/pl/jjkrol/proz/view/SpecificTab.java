package pl.jjkrol.proz.view;

import javax.swing.JPanel;

public interface SpecificTab {
	JPanel getJPanel();
	String getName();
	void getReady();
}
