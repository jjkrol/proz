package pl.jjkrol.proz.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

abstract public class DocumentBuilder {
	protected PdfWriter writer;
	protected Document document;
	protected String filename;

	/**
	 * Initializes document
	 */
	abstract public void initializeDocument();

	/**
	 * Initializes file listeners 
	 * @param filename path to the created pdf file
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	abstract public void initializeListeners(String filename)
			throws DocumentException, FileNotFoundException;

	/**
	 * Builds the document body
	 * @param results
	 * @param administrativeResults
	 * @throws DocumentException
	 * @throws IOException
	 */
	abstract public void buildBody(final Map<BillableService, Float> results,
			final Map<BillableService, Float> administrativeResults)
			throws DocumentException, IOException;
	
	/**
	 * Closes created document
	 */
	abstract public void finalizeDocument(); 
}