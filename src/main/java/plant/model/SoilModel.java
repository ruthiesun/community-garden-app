package plant.model;

/**
 * Models soils
 */
public class SoilModel {
    private String type;
    private String alkalinity;
    private String aeration;
    public String getType() {
        return type;
    }

    public String getAlkalinity() {
        return alkalinity;
    }

    public String getAeration() {
        return aeration;
    }

    public SoilModel(String type, String alkalinity, String aeration) {
        this.type = type;
        this.alkalinity = alkalinity;
        this.aeration = aeration;
    }


}
