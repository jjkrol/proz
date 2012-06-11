package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * The Class WindowClosingStrategy.
 */
public class WindowClosingStrategy extends PROZStrategy {

	/**
	 * Instantiates a new window closing strategy.
	 *
	 * @param view the view
	 * @param model the model
	 */
	WindowClosingStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final PROZEvent e) {
		model.closeDatabase();
		System.exit(0);
	}

}
