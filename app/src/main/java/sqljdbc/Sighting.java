package sqljdbc;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Stores information on a species as a result of the query in 
 */
public class Sighting {
    public Sighting(String sighting_date, float latitude, 
            float longitude, int altitude) {
        this.sighting_date = new SimpleStringProperty(sighting_date);
        this.latitude = new SimpleFloatProperty(latitude);
        this.longitude = new SimpleFloatProperty(longitude);
        this.altitude = new SimpleIntegerProperty(altitude);
    }
    
    public String getSighting_date() {
        return sighting_date.get();
    }

    public float getLatitude() {
        return latitude.get();
    }
    
    public float getLongitude() {
        return longitude.get();
    }
    
    
    public int getAltitude() {
        return altitude.get();
    }

    private final SimpleStringProperty sighting_date;
    private final SimpleFloatProperty latitude, longitude;
    private final SimpleIntegerProperty altitude;
}
