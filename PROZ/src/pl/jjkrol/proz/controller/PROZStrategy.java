package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

public class PROZStrategy {
	protected View view;
	protected Model model;
	
	PROZStrategy(View view, Model model){
		this.view = view;
		this.model = model;
	}
	
	void execute(PROZEvent e) {
	}

}
