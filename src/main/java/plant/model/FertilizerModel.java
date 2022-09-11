package plant.model;

/**
 * Models fertilizers
 */
public class FertilizerModel {
    private String name;
    private String npkRatio;
    private String type;
    private String description;

    public String getName() {
        return name;
    }

    public String getNpkRatio() {
        return npkRatio;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public FertilizerModel(String name, String npkRatio, String type, String description) {
        this.name = name;
        this.npkRatio = npkRatio;
        this.type = type;
        this.description = description;
    }


}
