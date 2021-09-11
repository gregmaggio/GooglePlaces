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
public class AddressComponentDTO {
	private String longName = null;
    private String shortName = null;
    private String[] types = null;

    public AddressComponentDTO() {

    }

    public AddressComponentDTO(JSONObject obj) throws JSONException {
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.compareToIgnoreCase("long_name") == 0) {
                this.longName = obj.getString(key);
            } else if (key.compareToIgnoreCase("short_name") == 0) {
            	this.shortName = obj.getString(key);
            } else if (key.compareToIgnoreCase("types") == 0) {
                JSONArray types = obj.getJSONArray(key);
                this.types = new String[types.length()];
                for (int ii = 0; ii < types.length(); ii++) {
                	this.types[ii] = types.get(ii).toString();
                }
            }
        }
    }

    public String getLongName() {
        return this.longName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String[] getTypes() {
        return this.types;
    }

    public void setLongName(String newVal) {
    	this.longName = newVal;
    }

    public void setShortName(String newVal) {
    	this.shortName = newVal;
    }

    public void setTypes(String[] newVal) {
    	this.types = newVal;
    }

    public boolean isCity() {
        if (this.types != null) {
            for (int ii = 0; ii < this.types.length; ii++) {
                if (types[ii].compareToIgnoreCase("locality") == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isState() {
        if (this.types != null) {
            for (int ii = 0; ii < this.types.length; ii++) {
                if (this.types[ii].compareToIgnoreCase("administrative_area_level_1") == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
