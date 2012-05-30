package pl.jjkrol.proz.controller;

import java.util.Map;

import pl.jjkrol.proz.model.BillableService;

public class GenerateUsageTableEvent extends PROZEvent {
	public final Map<BillableService, Float> results;
	public final Map<BillableService, Float> administrativeResults;

	public GenerateUsageTableEvent(final Map<BillableService, Float> results,
			final Map<BillableService, Float> administrativeResults) {
		this.results = results;
		this.administrativeResults = administrativeResults;
	}
}
