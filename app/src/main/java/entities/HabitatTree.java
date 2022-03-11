package entities;

import javafx.beans.property.SimpleStringProperty;

/**
 * Store information on a tree and its habitat
 */
public class HabitatTree {
    public HabitatTree(String genus, String species, String soil_moisture,
            String soil_type, String habitat_type) {
        this.genus = new SimpleStringProperty(genus);
        this.species = new SimpleStringProperty(species);
        this.soil_moisture = new SimpleStringProperty(soil_moisture);
        this.soil_type = new SimpleStringProperty(soil_type);
        this.habitat_type = new SimpleStringProperty(habitat_type);
    }
    
    public String getGenus() {
        return genus.get();
    }
    
    public String getSpecies() {
        return species.get();
    }
    
    public String getSoil_moisture() {
        return soil_moisture.get();
    }
    
    public String getSoil_type() {
        return soil_type.get();
    }
    
    public String getHabitat_type() {
        return habitat_type.get();
    }
    
    private final SimpleStringProperty genus;
    private final SimpleStringProperty species;
    private final SimpleStringProperty soil_moisture;
    private final SimpleStringProperty soil_type;
    private final SimpleStringProperty habitat_type;
}
