package pl.jjkrol.proz.controller;

public class LocumsListNeededEvent extends PROZEvent {
	final public LocumsDisplayer caller;

	public LocumsListNeededEvent(LocumsDisplayer caller) {
		this.caller = caller;
	}
}
