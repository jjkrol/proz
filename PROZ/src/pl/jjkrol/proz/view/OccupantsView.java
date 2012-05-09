package pl.jjkrol.proz.view;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import pl.jjkrol.proz.controller.Controller;

public class OccupantsView implements SpecificView {
	private String name = "Najemcy";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public JPanel getJPanel() {

		JPanel panel = new JPanel();
		Controller core = Controller.getInstance();
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Mieszkania");
		for (String locumName : core.getLocums()) {
			DefaultMutableTreeNode locumNode = new DefaultMutableTreeNode(
					locumName);
			for (String occName : core.getLocumOccupants(locumName)) {
				locumNode.add(new DefaultMutableTreeNode(occName));
			}

			top.add(locumNode);
		}

		JTree tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		// tree.addTreeSelectionListener(this);
		JScrollPane scrollPane = new JScrollPane(tree);
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));
		panel.add(scrollPane);
		return panel;
	}

	@Override
	public void getReady() {
		// TODO Auto-generated method stub

	}
}
