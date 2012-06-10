package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * @author   jjkrol
 */
public class PROZStrategy {
	/**
	 * @uml.property  name="view"
	 * @uml.associationEnd  
	 */
	protected View view;
	/**
	 * @uml.property  name="model"
	 * @uml.associationEnd  
	 */
	protected Model model;
	
	PROZStrategy(View view, Model model){
		this.view = view;
		this.model = model;
	}
	
	void execute(PROZEvent e) {
	}

}
