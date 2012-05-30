package pl.jjkrol.proz.view;

import javax.swing.JPanel;

public interface SpecificTab {
	public JPanel getJPanel();
	public String getName();
	public void getReady();
}
