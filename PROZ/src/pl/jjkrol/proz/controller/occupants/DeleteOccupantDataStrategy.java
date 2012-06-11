package pl.jjkrol.proz.controller.occupants;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.occupants.DeleteOccupantEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchOccupant;
import pl.jjkrol.proz.view.View;

/**
 * deletes occupant from register.
 */
public class DeleteOccupantDataStrategy extends OccupantsStrategy {

	/** The logger. */
	static Logger logger = Logger.getLogger(PROZStrategy.class);

	/**
	 * Instantiates a new delete occupant data strategy.
	 * 
	 * @param view
	 *            the view
	 * @param model
	 *            the model
	 */
	public DeleteOccupantDataStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent event) {
		OccupantMockup moc = ((DeleteOccupantEvent) event).mockup;
		try {
			model.deleteOccupantData(moc.getId());
		} catch (NoSuchOccupant e) {
			logger.warn(e.getMessage());
		}
		PROZStrategy s = new DisplayOccupantsStrategy(view, model);
		s.execute(event);
	}
}
