package pl.jjkrol.proz.controller;

import java.util.List;

import pl.jjkrol.proz.events.LocumsListNeededEvent;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * displays locums.
 */
class DisplayLocumsStrategy extends PROZStrategy {
	
	/**
	 * Instantiates a new display locums strategy.
	 *
	 * @param view the view
	 * @param model the model
	 */
	public DisplayLocumsStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent event) {
		final List<LocumMockup> locMocks = model.getLocumsMockups();
		final LocumsDisplayer d = ((LocumsListNeededEvent) event).caller;
		d.displayLocumsList(locMocks);
	}
}
