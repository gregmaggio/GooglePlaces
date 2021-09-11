/**
 * 
 */
package ca.datamagic.google.places.dto;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Greg
 *
 */
public class PredictionDTO {
	private String placeId = null;
    private String description = null;

    public PredictionDTO() {

    }

    public PredictionDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.compareToIgnoreCase("place_id") == 0) {
                this.placeId = obj.getString(key);
            } else if (key.compareToIgnoreCase("description") == 0) {
            	this.description = obj.getString(key);
            }
        }
    }

    public String getPlaceId() {
        return this.placeId;
    }

    public void setPlaceId(String newVal) {
    	this.placeId = newVal;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String newVal) {
    	this.description = newVal;
    }
}
