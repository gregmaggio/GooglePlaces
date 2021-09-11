/**
 * 
 */
package ca.datamagic.google.places.servlet;

import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import ca.datamagic.google.places.dto.PlaceDTO;
import ca.datamagic.google.places.dto.PredictionDTO;

/**
 * @author Greg
 *
 */
public class GooglePlacesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(GooglePlacesServlet.class);
	private static final Pattern autoCompletePredictionsPattern = Pattern.compile("/autoComplete/(?<searchText>\\w+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern placePattern = Pattern.compile("/place/(?<placeId>\\w+)", Pattern.CASE_INSENSITIVE);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String pathInfo = request.getPathInfo();
			logger.debug("pathInfo: " + pathInfo);			
			Matcher autoCompletePredictionsMatcher = autoCompletePredictionsPattern.matcher(pathInfo);
			if (autoCompletePredictionsMatcher.find()) {
				logger.debug("autoCompletePredictionsMatcher");
				String searchText = autoCompletePredictionsMatcher.group("searchText");
				logger.debug("searchText: " + searchText);
				List<PredictionDTO> predictions = GooglePlacesContextListener.getDAO().loadAutoCompletePredictions(searchText);
				//GooglePlacesContextListener.getDAO().loadPlace(placeId)
				String json = (new Gson()).toJson(predictions);
				response.setContentType("application/json");
				response.getWriter().println(json);
				return;
			}
			Matcher placeMatcher = placePattern.matcher(pathInfo);
			if (placeMatcher.find()) {
				logger.debug("placeMatcher");
				String placeId = placeMatcher.group("placeId");
				logger.debug("placeId: " + placeId);
				PlaceDTO place = GooglePlacesContextListener.getDAO().loadPlace(placeId);
				String json = (new Gson()).toJson(place);
				response.setContentType("application/json");
				response.getWriter().println(json);
				return;
			}
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (Throwable t) {
			logger.error("Exception", t);
			throw new IOError(t);
		}
	}
}
