package pl.jjkrol.proz.view;

import javax.swing.JPanel;

public class ReportsView implements SpecificView {
	private String name = "Raporty";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public JPanel getJPanel() {
		JPanel panel = new JPanel();
		return panel;
	}
 @Override
public void getReady() {
	// TODO Auto-generated method stub
	
}
}
