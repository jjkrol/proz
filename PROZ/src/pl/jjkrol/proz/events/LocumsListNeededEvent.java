package pl.jjkrol.proz.events;

import pl.jjkrol.proz.controller.LocumsDisplayer;

public class LocumsListNeededEvent extends PROZEvent {
	final public LocumsDisplayer caller;

	public LocumsListNeededEvent(LocumsDisplayer caller) {
		this.caller = caller;
	}
}
