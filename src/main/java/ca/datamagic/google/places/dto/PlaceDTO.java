/**
 * 
 */
package ca.datamagic.google.places.dto;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Greg
 *
 */
public class PlaceDTO {
	private String placeId = null;
    private String name = null;
    private Double latitude = null;
    private Double longtitude = null;
    private AddressComponentDTO[] addressComponents = null;

    public PlaceDTO() {

    }

    public PlaceDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.compareToIgnoreCase("place_id") == 0) {
                this.placeId = obj.getString(key);
            } else if (key.compareToIgnoreCase("name") == 0) {
            	this.name = obj.getString(key);
            } else if (key.compareToIgnoreCase("geometry") == 0) {
                JSONObject geometry = obj.getJSONObject(key);
                if (geometry.has("location")) {
                    JSONObject location = geometry.getJSONObject("location");
                    if (location.has("lat") && location.has("lng")) {
                    	this.latitude = location.getDouble("lat");
                    	this.longtitude = location.getDouble("lng");
                    }
                }
            } else if (key.compareToIgnoreCase("address_components") == 0) {
                JSONArray addressComponents = obj.getJSONArray(key);
                this.addressComponents = new AddressComponentDTO[addressComponents.length()];
                for (int ii = 0; ii < addressComponents.length(); ii++) {
                	this.addressComponents[ii] = new AddressComponentDTO(addressComponents.getJSONObject(ii));
                }
            }
        }
    }

    public String getPlaceId() {
        return this.placeId;
    }

    public void setPlaceId(String newVal) {
    	this.placeId = newVal;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newVal) {
    	this.name = newVal;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double newVal) {
    	this.latitude = newVal;
    }

    public Double getLongitude() {
        return this.longtitude;
    }

    public void setLongitude(Double newVal) {
    	this.longtitude = newVal;
    }

    public AddressComponentDTO[] getAddressComponents() {
        return this.addressComponents;
    }

    public void setAddressComponents(AddressComponentDTO[] newVal) {
    	this.addressComponents = newVal;
    }

    public String getCity() {
        if (this.addressComponents != null) {
            for (int ii = 0; ii < this.addressComponents.length; ii++) {
                if (this.addressComponents[ii].isCity()) {
                    return this.addressComponents[ii].getLongName();
                }
            }
        }
        return null;
    }

    public String getState() {
        if (this.addressComponents != null) {
            for (int ii = 0; ii < this.addressComponents.length; ii++) {
                if (this.addressComponents[ii].isState()) {
                    return this.addressComponents[ii].getShortName();
                }
            }
        }
        return null;
    }
}
