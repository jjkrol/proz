package pl.jjkrol.proz.view;

import javax.swing.*;
import java.awt.*;

public class LocumsView implements SpecificView {
	private String name = "Lokale";

	@Override
	public String getName() {
		return name;
	}

	public JPanel getJPanel() {
		JPanel panel = new JPanel();
		DefaultListModel listModel = new DefaultListModel();

		listModel.addElement("M1");
		listModel.addElement("M2");
		listModel.addElement("M3");
		JList list = new JList(listModel);
		list.setSize(70, 200);
		panel.add(list);
		panel.setLayout(new FlowLayout());
		return panel;
	}

	@Override
	public void getReady() {
		// TODO Auto-generated method stub

	}
}
