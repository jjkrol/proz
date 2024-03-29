package pl.jjkrol.proz.controller.occupants;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchOccupant;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.OccupantsTab;
import pl.jjkrol.proz.view.View;
import pl.jjkrol.proz.events.occupants.OccupantChosenForViewingEvent;
import org.apache.log4j.Logger;

/**
 * displays data of a specific occupant.
 */
public class DisplayOccupantDataStrategy extends OccupantsStrategy {

	/** The logger. */
	static Logger logger = Logger.getLogger("strategy");

	/**
	 * Instantiates a new display occupant data strategy.
	 * 
	 * @param view
	 *            the view
	 * @param model
	 *            the model
	 */
	public DisplayOccupantDataStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(final PROZEvent event) {
		OccupantMockup moc;
		try {
			moc =
					model.getOccupantMockup(((OccupantChosenForViewingEvent) event).moc);
			OccupantsTab v;
			v = (OccupantsTab) view.getSpecificView(OccupantsTab.class);
			v.displayOccupantsData(moc);
		} catch (NoSuchTabException e) {
			logger.warn("No such tab");
		} catch (NoSuchOccupant e) {
			logger.warn("No such occupant");
		}
	}
}
