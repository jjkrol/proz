package pl.jjkrol.proz.controller;
import java.util.HashMap;
import java.lang.reflect.*;
import pl.jjkrol.proz.view.*;

public class PaymentsController extends Controller {
	private final PaymentsView paymentsView = new PaymentsView();
	
	private final HashMap<Class, Method> eventDictionary = new HashMap<Class, Method>();

	private static volatile Controller instance = null;

	public static Controller getInstance() {
		if (instance == null) {
			synchronized (Controller.class) {
				if (instance == null)
					instance = new PaymentsController();
			}
		}
		return instance;
	}	
	
	private PaymentsController() {
		try{
		eventDictionary.put(MainButtonClickedEvent.class, Controller.class.getMethod("reactToEvent"));
		}
		catch(NoSuchMethodException e){
			System.out.println("wyjatek");
		}
	}	public void run(View view) {
	}
}
