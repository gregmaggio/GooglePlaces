/**
 * 
 */
package ca.datamagic.google.places.dao;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ca.datamagic.google.places.dto.PlaceDTO;
import ca.datamagic.google.places.dto.PredictionDTO;
import ca.datamagic.google.places.inject.Performance;
import ca.datamagic.google.places.util.IOUtils;

/**
 * @author Greg
 *
 */
public class GooglePlacesDAO {
	private static Logger logger = LogManager.getLogger(GooglePlacesDAO.class);
	private static String apiKey = null;

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String newVal) {
        apiKey = newVal;
    }
    
    @Performance
    public List<PredictionDTO> loadAutoCompletePredictions(String searchText) throws IOException {
        URL url = new URL(MessageFormat.format("https://maps.googleapis.com/maps/api/place/autocomplete/json?input={0}&types=geocode&language=en&key={1}", searchText, apiKey));
        logger.debug("url: " + url.toString());
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            inputStream = connection.getInputStream();
            String responseText = IOUtils.readEntireString(inputStream);
            logger.debug("responseText: " + responseText);
            JSONObject responseObj = new JSONObject(responseText);
            JSONArray predictions = (JSONArray)responseObj.get("predictions");
            List<PredictionDTO> list = new ArrayList<PredictionDTO>();
            for (int jj = 0; jj < predictions.length(); jj++) {
                PredictionDTO prediction = new PredictionDTO(predictions.getJSONObject(jj));
                if ((prediction.getDescription() != null) && (prediction.getDescription().length() > 0) && (prediction.getDescription().toUpperCase().contains("USA"))) {
                    list.add(prediction);
                }
            }
            return list;
        } finally {
            IOUtils.closeQuietly(inputStream);
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                    logger.warn("Throwable", t);
                }
            }
        }
    }

    @Performance
    public PlaceDTO loadPlace(String placeId) throws IOException {
        URL url = new URL(MessageFormat.format("https://maps.googleapis.com/maps/api/place/details/json?placeid={0}&key={1}", placeId, apiKey));
        logger.debug("url: " + url.toString());
        HttpsURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            inputStream = connection.getInputStream();
            String responseText = IOUtils.readEntireString(inputStream);
            logger.debug("responseText: " + responseText);
            JSONObject responseObj = new JSONObject(responseText);
            return new PlaceDTO(responseObj.getJSONObject("result"));
        } finally {
            IOUtils.closeQuietly(inputStream);
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Throwable t) {
                	logger.warn("Throwable", t);
                }
            }
        }
    }
}
