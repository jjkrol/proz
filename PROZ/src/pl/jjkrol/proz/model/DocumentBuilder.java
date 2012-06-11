package pl.jjkrol.proz.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * The Class DocumentBuilder.
 */
abstract public class DocumentBuilder {

	/** The writer. */
	protected PdfWriter writer;

	/** The document. */
	protected Document document;

	/** The filename. */
	protected String filename;

	/**
	 * Initializes document.
	 */
	abstract public void initializeDocument();

	/**
	 * Initializes file listeners.
	 * 
	 * @param filename
	 *            path to the created pdf file
	 * @throws DocumentException
	 *             the document exception
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	abstract public void initializeListeners(final String filename)
			throws DocumentException, FileNotFoundException;

	/**
	 * Builds the document body.
	 * 
	 * @param values
	 *            the values
	 * @throws DocumentException
	 *             the document exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	abstract public void buildBody(final Map<String, String> values)
			throws DocumentException, IOException;

	/**
	 * Closes created document.
	 */
	abstract public void finalizeDocument();
}