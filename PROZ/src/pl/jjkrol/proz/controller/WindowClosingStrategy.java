package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

public class WindowClosingStrategy extends PROZStrategy {

	WindowClosingStrategy(View view, Model model) {
		super(view, model);
	}
	
	@Override
	void execute(PROZEvent e) {
		model.closeDatabase();
		System.exit(0);
	}

}
