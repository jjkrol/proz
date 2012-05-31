package pl.jjkrol.proz.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import pl.jjkrol.proz.mockups.ResultMockup;
import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;

/**
 * Class responsible for managing the building of documents specified by the
 * given DocumentBuilder.
 */
public class DocumentDirector {
	static Logger logger = Logger.getLogger(DocumentDirector.class);
	private DocumentBuilder builder;

	public DocumentDirector(DocumentBuilder builder) {
		this.builder = builder;
	}

	public void buildDocument(String filepath, ResultMockup result) {
		final Map<BillableService, Float> results = result.results;
		final Map<BillableService, Float> administrativeResults =
				result.administrativeResults;
		final Calendar from = result.from;
		final Calendar to = result.to;
		//TODO transform into a key-value structure
		try {
			builder.initializeDocument();
			builder.initializeListeners(filepath);
			builder.buildBody(results, administrativeResults, from, to);
			builder.finalizeDocument();
		} catch (DocumentException exc) {
			logger.debug(exc.getMessage());
		} catch (FileNotFoundException exc) {
			logger.debug(exc.getMessage());
			exc.printStackTrace();
		} catch (IOException exc) {
			logger.debug(exc.getMessage());
		}
	}
}
