package sqljdbc;

import javafx.beans.property.SimpleStringProperty;

/**
 * Stores information on a species as a result of the species query in Query.
 */
public class Species {
    public Species(String genus, String species, String tree_name) {
        this.genus = new SimpleStringProperty(genus);
        this.species = new SimpleStringProperty(species);
        this.tree_name = new SimpleStringProperty(tree_name);
    }
    
    public String getGenus() {
        return genus.get();
    }

    public String getSpecies() {
        return species.get();
    }
    
    public String getTree_name() {
        return tree_name.get();
    }
    
    private final SimpleStringProperty genus;
    private final SimpleStringProperty species;
    private final SimpleStringProperty tree_name;
}
