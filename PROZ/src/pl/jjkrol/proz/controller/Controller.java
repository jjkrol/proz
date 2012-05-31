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
import pl.jjkrol.proz.events.occupants.AddOccupantEvent;
import pl.jjkrol.proz.events.occupants.DeleteOccupantEvent;
import pl.jjkrol.proz.events.occupants.OccupantChosenForViewingEvent;
import pl.jjkrol.proz.events.occupants.OccupantsListNeededEvent;
import pl.jjkrol.proz.events.occupants.SaveOccupantEvent;
import pl.jjkrol.proz.events.payments.CalculatedResultsNeededEvent;
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
/**
 * A class responsible for the application control flow
 * 
 * @author jjkrol
 * 
 */
public class Controller {
	private BlockingQueue<PROZEvent> blockingQueue =
			new LinkedBlockingQueue<PROZEvent>();
	private PaymentCalculator calculator;
	private final View view = new View();
	private final Model model = new Model();

	static Logger logger = Logger.getLogger(PROZJFrame.class);

	private final HashMap<Class, PROZStrategy> eventDictionary =
			new HashMap<Class, PROZStrategy>();

	private static volatile Controller instance = null;

	public static Controller getInstance() {
		if (instance == null) {
			synchronized (Controller.class) {
				if (instance == null)
					instance = new Controller();
			}
		}
		return instance;
	}

	protected Controller() {
		eventDictionary.put(OccupantsListNeededEvent.class,
				new DisplayOccupantsStrategy(view, model));
		eventDictionary.put(OccupantChosenForViewingEvent.class,
				new DisplayOccupantDataStrategy(view, model));
		eventDictionary.put(AddOccupantEvent.class, new AddOccupantDataStrategy(view, model));
		eventDictionary.put(SaveOccupantEvent.class, new SaveOccupantDataStrategy(view, model));
		eventDictionary
				.put(DeleteOccupantEvent.class, new DeleteOccupantDataStrategy(view, model));

		eventDictionary.put(LocumsListNeededEvent.class, new DisplayLocumsStrategy(view, model));
		eventDictionary.put(LocumChosenForViewingEvent.class,
				new DisplayLocumMeasurementsStrategy(view, model));
		eventDictionary.put(LocumMeasurementsAndQuotationsNeededEvent.class,
				new DisplayLocumMeasurementsAndQuotationsStrategy(view, model));
		eventDictionary.put(CalculatedResultsNeededEvent.class,
				new DisplayCalculatedResultsStrategy(view, model));
		eventDictionary.put(GenerateUsageTableEvent.class,
				new GenerateUsageTableStrategy(view, model));

		model.initializeData();
	}

	/**
	 * main function, creates and runs gui
	 * 
	 * @param calc
	 */
	public void run(PaymentCalculator calculator) {
		this.calculator = calculator;
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				logger.warn("Interrupted exception on take event "
						+ e.getMessage());
			}
		}
	}

	}
