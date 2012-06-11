package pl.jjkrol.proz.controller;

import java.util.List;

import pl.jjkrol.proz.mockups.LocumMockup;

public interface LocumsDisplayer {
	
	/**
	 * Displays a list of locums
	 * @param locums
	 */
	public void displayLocumsList(final List<LocumMockup> locums);
}
