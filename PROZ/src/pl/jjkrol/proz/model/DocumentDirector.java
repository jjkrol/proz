package pl.jjkrol.proz.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import pl.jjkrol.proz.mockups.ResultMockup;
import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;

/**
 * Class responsible for managing the building of documents specified by the given DocumentBuilder.
 */
public class DocumentDirector {
	static Logger logger = Logger.getLogger(DocumentDirector.class);
	/**
	 * Builder used to build documents
	 * @uml.property  name="builder"
	 * @uml.associationEnd  
	 */
	private DocumentBuilder builder;

	/**
	 * @param builder
	 * @uml.property  name="builder"
	 */
	public void setBuilder(DocumentBuilder builder) {
		this.builder = builder;
	}
	/**
	 * @return
	 * @uml.property  name="builder"
	 */
	public DocumentBuilder getBuilder() {
		return this.builder;
	}

	public void buildDocument(String filepath, String filename,
			Map<String, String> valuesMap) throws NoBuilderSet {
		if(builder == null)
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

	private void createDirectoryTree(String filepath) {
		try {
			new File(filepath).mkdirs();
		} catch (Exception e) {
			logger.warn("Error: " + e.getMessage());
		}
	}
}
