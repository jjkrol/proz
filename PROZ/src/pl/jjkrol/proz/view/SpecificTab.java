package pl.jjkrol.proz.view;

import javax.swing.JPanel;

public abstract class SpecificTab {
	protected String name;
	abstract JPanel getJPanel();
	abstract String getName();
	abstract void getReady();
}
