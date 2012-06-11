package pl.jjkrol.proz.model;

import java.util.*;

/**
 * The Interface PaymentCalculator.
 */
public interface PaymentCalculator {

	/**
	 * Calculate.
	 * 
	 * @param building
	 *            the building
	 * @param loc
	 *            the loc
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param quotationName
	 *            the quotation name
	 * @return the result
	 * @throws NoSuchQuotationSet
	 *             the no such quotation set
	 * @throws NoSuchDate
	 *             the no such date
	 */
	public Result calculate(final Building building, final Locum loc,
			final Calendar from, final Calendar to, final String quotationName)
			throws NoSuchQuotationSet, NoSuchDate;

	/**
	 * Gets the last result.
	 * 
	 * @return the last result
	 */
	public Result getLastResult();
}
