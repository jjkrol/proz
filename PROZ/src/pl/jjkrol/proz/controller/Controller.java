package pl.jjkrol.proz.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.events.*;
import pl.jjkrol.proz.events.measurements.AddMeasurementEvent;
import pl.jjkrol.proz.events.measurements.DeleteMeasurementEvent;
import pl.jjkrol.proz.events.measurements.LocumChosenForViewingEvent;
import pl.jjkrol.proz.events.occupants.AddOccupantEvent;
import pl.jjkrol.proz.events.occupants.DeleteOccupantEvent;
import pl.jjkrol.proz.events.occupants.OccupantChosenForViewingEvent;
import pl.jjkrol.proz.events.occupants.OccupantsListNeededEvent;
import pl.jjkrol.proz.events.occupants.SaveOccupantEvent;
import pl.jjkrol.proz.events.payments.CalculatedResultsNeededEvent;
import pl.jjkrol.proz.events.payments.GenerateInvoiceEvent;
import pl.jjkrol.proz.events.payments.GenerateUsageTableEvent;
import pl.jjkrol.proz.events.payments.LocumMeasurementsAndQuotationsNeededEvent;
import pl.jjkrol.proz.model.DocumentBuilder;
import pl.jjkrol.proz.model.DocumentDirector;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchLocum;
import pl.jjkrol.proz.model.NoSuchQuotationSet;
import pl.jjkrol.proz.model.PaymentCalculator;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.mockups.ResultMockup;
import pl.jjkrol.proz.model.UsageTableBuilder;
import pl.jjkrol.proz.view.InvoicesTab;
import pl.jjkrol.proz.view.LocumsTab;
import pl.jjkrol.proz.view.MeasurementsTab;
import pl.jjkrol.proz.view.OccupantsTab;
import pl.jjkrol.proz.view.PROZJFrame;
import pl.jjkrol.proz.view.PaymentsTab;
import pl.jjkrol.proz.view.ReportsTab;
import pl.jjkrol.proz.view.SpecificTab;
import pl.jjkrol.proz.view.View;

// TODO: Auto-generated Javadoc

/**
 * A class responsible for the application control flow.
 * 
 * @author jjkrol
 */
public class Controller {

	/** The blocking queue. */
	private BlockingQueue<PROZEvent> blockingQueue =
			new LinkedBlockingQueue<PROZEvent>();

	/** The view. */
	private final View view;

	/** The model. */
	private final Model model = new Model();

	/** The logger. */
	static Logger logger = Logger.getLogger(PROZJFrame.class);

	/** The event dictionary. */
	private final HashMap<Class<? extends PROZEvent>, PROZStrategy> eventDictionary =
			new HashMap<Class<? extends PROZEvent>, PROZStrategy>();

	/** The instance. */
	private static volatile Controller instance = null;

	/**
	 * Gets the single instance of Controller.
	 * 
	 * @return single instance of Controller
	 */
	public static Controller getInstance() {
		if (instance == null) {
			synchronized (Controller.class) {
				if (instance == null)
					instance = new Controller();
			}
		}
		return instance;
	}

	/**
	 * Instantiates a new controller.
	 */
	protected Controller() {
		model.initializeData();
		view = new View(blockingQueue);
		initializeEventDictionary();
	}

	/**
	 * puts events - startegy pairs into an event dictionary.
	 */
	private void initializeEventDictionary() {
		initializeOccupantsDictionary();
		initializeLocumsDictionary();
		initializeMeasurementsDictionary();
		
		eventDictionary.put(LocumsListNeededEvent.class,
				new DisplayLocumsStrategy(view, model));

		eventDictionary.put(LocumMeasurementsAndQuotationsNeededEvent.class,
				new DisplayLocumMeasurementsAndQuotationsStrategy(view, model));
		eventDictionary.put(CalculatedResultsNeededEvent.class,
				new DisplayCalculatedResultsStrategy(view, model));
		eventDictionary.put(GenerateUsageTableEvent.class,
				new GenerateUsageTableStrategy(view, model));
		eventDictionary.put(GenerateInvoiceEvent.class,
				new GenerateInvoiceStrategy(view, model));
		eventDictionary.put(WindowClosingEvent.class,
				new WindowClosingStrategy(view, model));
	}

	private void initializeOccupantsDictionary() {
		eventDictionary.put(OccupantsListNeededEvent.class,
				new DisplayOccupantsStrategy(view, model));
		eventDictionary.put(OccupantChosenForViewingEvent.class,
				new DisplayOccupantDataStrategy(view, model));
		eventDictionary.put(AddOccupantEvent.class,
				new AddOccupantDataStrategy(view, model));
		eventDictionary.put(SaveOccupantEvent.class,
				new SaveOccupantDataStrategy(view, model));
		eventDictionary.put(DeleteOccupantEvent.class,
				new DeleteOccupantDataStrategy(view, model));
	}
	
	private void initializeMeasurementsDictionary() {
		eventDictionary.put(AddMeasurementEvent.class,
				new AddMeasurementDataStrategy(view, model));
		eventDictionary.put(SaveMeasurementEvent.class,
				new SaveMeasurementDataStrategy(view, model));
		eventDictionary.put(DeleteMeasurementEvent.class,
				new DeleteMeasurementDataStrategy(view, model));
	}
	
	private void initializeLocumsDictionary() {
		eventDictionary.put(LocumChosenForViewingEvent.class,
				new DisplayLocumMeasurementsStrategy(view, model));
/*		eventDictionary.put(AddLocumEvent.class,
				new AddOccupantDataStrategy(view, model));
		eventDictionary.put(SaveLocumEvent.class,
				new SaveOccupantDataStrategy(view, model));
		eventDictionary.put(DeleteLocumEvent.class,
				new DeleteOccupantDataStrategy(view, model));*/
	}

	/**
	 * main function, creates and runs gui.
	 * 
	 */
	public void run() {
		List<SpecificTab> views = new ArrayList<SpecificTab>();
		views.add(new MeasurementsTab(blockingQueue));
		views.add(new PaymentsTab(blockingQueue));
		views.add(new OccupantsTab(blockingQueue));
		views.add(new LocumsTab(blockingQueue));
		views.add(new InvoicesTab(blockingQueue));
		views.add(new ReportsTab(blockingQueue));
		view.startGUI(views);
		runLoop();
	}

	/**
	 * Run loop.
	 */
	private void runLoop() {
		while (true) {
			try {
				PROZEvent event = blockingQueue.take();
				if (!eventDictionary.containsKey(event.getClass())) {
					logger.warn("No such class in the dictionary");
				}
				PROZStrategy obj = eventDictionary.get(event.getClass());
				try {
					obj.execute(event);
				} catch (SecurityException e) {
					logger.warn(e.getMessage());
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					logger.warn(e.getMessage());
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				logger.warn("Interrupted exception on take event "
						+ e.getMessage());
			}
		}
	}

}
