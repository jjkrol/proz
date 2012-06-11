package pl.jjkrol.proz.controller.occupants;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.occupants.AddOccupantEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.View;

/**
 * adds data of a new occupant.
 */
public class AddOccupantDataStrategy extends OccupantsStrategy {

	/**
	 * Instantiates a new adds the occupant data strategy.
	 * 
	 * @param view
	 *            the view
	 * @param model
	 *            the model
	 */
	public AddOccupantDataStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final PROZEvent e) {
		OccupantMockup moc = ((AddOccupantEvent) e).mockup;
		model.addOccupantData(moc);
		PROZStrategy s = new DisplayOccupantsStrategy(view, model);
		s.execute(e);
	}
}
