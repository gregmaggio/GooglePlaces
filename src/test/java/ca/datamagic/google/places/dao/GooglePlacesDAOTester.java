/**
 * 
 */
package ca.datamagic.google.places.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import ca.datamagic.google.places.dto.PlaceDTO;
import ca.datamagic.google.places.dto.PredictionDTO;
import ca.datamagic.google.places.testing.BaseTester;

/**
 * @author Greg
 *
 */
public class GooglePlacesDAOTester extends BaseTester {
	private static final Logger logger = LogManager.getLogger(GooglePlacesDAOTester.class);
	
	@Test
	public void test1() throws Exception {
		List<PredictionDTO> predictions = getDAO().loadAutoCompletePredictions("COLLEGE");
		Assert.assertNotNull(predictions);
		Gson gson = new Gson();
		logger.debug("predictions: " + gson.toJson(predictions));
		Assert.assertFalse(predictions.size() == 0);
		PredictionDTO prediction = predictions.get(0);
		PlaceDTO place = getDAO().loadPlace(prediction.getPlaceId());
		logger.debug("place: " + gson.toJson(place));
	}
}
