/**
 * 
 */
package ca.datamagic.google.places.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ca.datamagic.google.places.dao.GooglePlacesDAO;
import ca.datamagic.google.places.inject.DAOModule;
import ca.datamagic.google.places.util.IOUtils;

/**
 * @author Greg
 *
 */
public class GooglePlacesContextListener implements ServletContextListener {
	private static Logger logger = LogManager.getLogger(GooglePlacesContextListener.class);
	private static Injector injector = null;
	private static GooglePlacesDAO dao = null;
	
	public static GooglePlacesDAO getDAO() {
		return dao;
	}	
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		InputStream securePropertiesStream = null;
		try {
			String realPath = sce.getServletContext().getRealPath("/");
			String securePropertiesFile = MessageFormat.format("{0}/WEB-INF/classes/secure.properties", realPath);
			securePropertiesStream = new FileInputStream(securePropertiesFile);
			Properties secureProperties = new Properties();
			secureProperties.load(securePropertiesStream);
			GooglePlacesDAO.setApiKey(secureProperties.getProperty("google_maps_api_key"));
			injector = Guice.createInjector(new DAOModule());
			dao = injector.getInstance(GooglePlacesDAO.class);
			logger.debug("contextInitialized");
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

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.debug("contextDestroyed");
	}
}
