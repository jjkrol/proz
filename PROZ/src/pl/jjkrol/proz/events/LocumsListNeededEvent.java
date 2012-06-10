package pl.jjkrol.proz.events;

import pl.jjkrol.proz.controller.LocumsDisplayer;

/**
 * @author   jjkrol
 */
public class LocumsListNeededEvent extends PROZEvent {
	/**
	 * @uml.property  name="caller"
	 * @uml.associationEnd  
	 */
	final public LocumsDisplayer caller;

	public LocumsListNeededEvent(LocumsDisplayer caller) {
		this.caller = caller;
	}
}
