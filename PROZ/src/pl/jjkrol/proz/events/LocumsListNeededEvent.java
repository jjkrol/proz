package pl.jjkrol.proz.events;

import pl.jjkrol.proz.controller.LocumsDisplayer;

/**
 * The Class LocumsListNeededEvent.
 * 
 * @author jjkrol
 */
public class LocumsListNeededEvent extends PROZEvent {

	/** The caller. */
	final public LocumsDisplayer caller;

	/**
	 * Instantiates a new locums list needed event.
	 * 
	 * @param caller
	 *            the caller
	 */
	public LocumsListNeededEvent(final LocumsDisplayer caller) {
		this.caller = caller;
	}
}
