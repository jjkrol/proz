package pl.jjkrol.proz.controller;

import java.util.List;

import pl.jjkrol.proz.events.LocumsListNeededEvent;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * displays locums on locums panel
 */
class DisplayLocumsStrategy extends PROZStrategy {
	public DisplayLocumsStrategy(View view, Model model) {
		super(view, model);
	}

	public void execute(final PROZEvent e) {
		final List<LocumMockup> locMocks = model.getLocumsMockups();
		final LocumsDisplayer d = ((LocumsListNeededEvent) e).caller;
		d.displayLocumsList(locMocks);
	}
}
