package pl.jjkrol.proz.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;

/**
 * Class responsible for managing the building of documents specified by the
 * given DocumentBuilder.
 */
public class DocumentDirector {

	/** The logger. */
	static Logger logger = Logger.getLogger(DocumentDirector.class);

	/** Builder used to build documents. */
	private DocumentBuilder builder;

	/**
	 * Sets the builder.
	 * 
	 * @param builder
	 *            the new builder
	 */
	public void setBuilder(final DocumentBuilder builder) {
		this.builder = builder;
	}

	/**
	 * Gets the builder.
	 * 
	 * @return the builder
	 */
	public DocumentBuilder getBuilder() {
		return this.builder;
	}

	/**
	 * Builds the document.
	 * 
	 * @param filepath
	 *            the filepath
	 * @param filename
	 *            the filename
	 * @param valuesMap
	 *            the values map
	 * @throws NoBuilderSet
	 *             the no builder set
	 */
	public void buildDocument(final String filepath, final String filename,
			final Map<String, String> valuesMap) throws NoBuilderSet {
		if (builder == null)
			throw new NoBuilderSet();
		createDirectoryTree(filepath);

		try {
			builder.initializeDocument();
			builder.initializeListeners(filepath + filename);
			builder.buildBody(valuesMap);
			builder.finalizeDocument();
		} catch (DocumentException exc) {
			logger.warn(exc.getMessage());
		} catch (FileNotFoundException exc) {
			logger.warn(exc.getMessage());
			exc.printStackTrace();
		} catch (IOException exc) {
			logger.warn(exc.getMessage());
		}
	}

	/**
	 * Creates the directory tree.
	 * 
	 * @param filepath
	 *            the filepath
	 */
	private void createDirectoryTree(final String filepath) {
		try {
			new File(filepath).mkdirs();
		} catch (Exception e) {
			logger.warn("Error: " + e.getMessage());
		}
	}
}
