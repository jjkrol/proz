package pl.jjkrol.proz.model;

import java.util.*;

public interface PaymentCalculator {
	public Result calculate(final Building building, final Locum loc,
			final Calendar from, final Calendar to, final String quotationName)
			throws NoSuchQuotationSet, NoSuchDate;
}
