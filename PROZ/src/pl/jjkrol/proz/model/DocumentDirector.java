package pl.jjkrol.proz.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;

/**
 * Class responsible for managing the building of documents
 * specified by the given DocumentBuilder.
 */
public class DocumentDirector {
	static Logger logger = Logger.getLogger(DocumentDirector.class);
	private DocumentBuilder builder;

	public DocumentDirector(DocumentBuilder builder) {
		this.builder = builder;
	}

	public void buildDocument(final Map<BillableService, Float> results,
			final Map<BillableService, Float> administrativeResults) {
		try {
			builder.initializeDocument();
			builder.initializeListeners("c:\\tabelka.pdf");
			builder.buildBody(results, administrativeResults);
			builder.finalizeDocument();
			
			try {
				Process p =
						Runtime.getRuntime()
								.exec("rundll32 url.dll,FileProtocolHandler c:\\tabelka.pdf");
				p.waitFor();
			} catch (Exception exc) {
				logger.debug(exc.getMessage());
			}
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
