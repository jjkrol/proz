package pl.jjkrol.proz.mockups;

import java.util.Map;
import pl.jjkrol.proz.model.BillableService;

/**
 * @TODO  start using this
 * @author   jjkrol
 */
public class QuotationMockup {
	/**
	 * @uml.property  name="service"
	 * @uml.associationEnd  
	 */
	private final BillableService service;
	private final Float value;
	public QuotationMockup(final BillableService service, final Float value){
		this.service = service;
		this.value = value;
	}
}
