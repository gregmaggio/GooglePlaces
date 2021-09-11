/**
 * 
 */
package ca.datamagic.google.places.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ca.datamagic.google.places.dao.GooglePlacesDAO;
import ca.datamagic.google.places.inject.DAOModule;
import ca.datamagic.google.places.util.IOUtils;

/**
 * @author Greg
 *
 */
public abstract class BaseTester {
	private static final Logger logger = LogManager.getLogger(BaseTester.class);
	private static Injector injector = null;
	private static GooglePlacesDAO dao = null;
	
	public static GooglePlacesDAO getDAO() {
		return dao;
	}	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream securePropertiesStream = null;
		try {
			String realPath = (new File(".")).getAbsolutePath();
			String securePropertiesFile = MessageFormat.format("{0}/src/test/resources/secure.properties", realPath);
			securePropertiesStream = new FileInputStream(securePropertiesFile);
			Properties secureProperties = new Properties();
			secureProperties.load(securePropertiesStream);
			GooglePlacesDAO.setApiKey(secureProperties.getProperty("google_maps_api_key"));
			injector = Guice.createInjector(new DAOModule());
			dao = injector.getInstance(GooglePlacesDAO.class);
		} catch (FileNotFoundException ex) {
			logger.error("FileNotFoundException", ex);
		} catch (IOException ex) {
			logger.error("IOException", ex);
		} finally {
			if (securePropertiesStream != null) {
				IOUtils.closeQuietly(securePropertiesStream);
			}
		}
	}
}
