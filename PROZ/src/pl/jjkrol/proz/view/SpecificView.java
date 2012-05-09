package pl.jjkrol.proz.view;

import javax.swing.JPanel;

public interface SpecificView {
	public JPanel getJPanel();
	public String getName();
	public void getReady();
}
