package pl.jjkrol.proz.mockups;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * The Class UsageTableData.
 */
public class UsageTableData {
	/** The locum name. */
	private String locumName;

	/** The occupant name. */
	private String occupantName;

	/** The advancement. */
	private BigDecimal advancement;

	/** date of first measurement. */
	private Calendar from;

	/** date of second measurement. */
	private Calendar to;

	/**
	 * Gets the locum name.
	 * 
	 * @return the locumName
	 */
	public String getLocumName() {
		return locumName;
	}

	/**
	 * Sets the locum name.
	 * 
	 * @param locumName
	 *            the locumName to set
	 */
	public void setLocumName(final String locumName) {
		this.locumName = locumName;
	}

	/**
	 * Gets the occupant name.
	 * 
	 * @return the occupantName
	 */
	public String getOccupantName() {
		return occupantName;
	}

	/**
	 * Sets the occupant name.
	 * 
	 * @param occupantName
	 *            the occupantName to set
	 */
	public void setOccupantName(final String occupantName) {
		this.occupantName = occupantName;
	}

	/**
	 * Gets the advancement.
	 * 
	 * @return the advancement
	 */
	public BigDecimal getAdvancement() {
		return advancement;
	}

	/**
	 * Sets the advancement.
	 * 
	 * @param advancement
	 *            the advancement to set
	 */
	public void setAdvancement(final BigDecimal advancement) {
		this.advancement = advancement;
	}

	/**
	 * Gets the from.
	 * 
	 * @return the from
	 */
	public Calendar getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 * 
	 * @param from
	 *            the from to set
	 */
	public void setFrom(final Calendar from) {
		this.from = from;
	}

	/**
	 * Gets the to.
	 * 
	 * @return the to
	 */
	public Calendar getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 * 
	 * @param to
	 *            the to to set
	 */
	public void setTo(final Calendar to) {
		this.to = to;
	}

	/**
	 * Instantiates a new usage table data.
	 * 
	 * @param locumName
	 *            the locum name
	 * @param occupantName
	 *            the occupant name
	 * @param advancement
	 *            the advancement
	 */
	public UsageTableData(final String locumName, final String occupantName,
			final BigDecimal advancement) {
		this.locumName = locumName;
		this.occupantName = occupantName;
		this.advancement = advancement;
	}

}
