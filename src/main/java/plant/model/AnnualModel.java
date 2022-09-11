package plant.model;

import java.util.LinkedHashMap;

/**
 * Models annual plants
 */
public class AnnualModel extends PlantModel{

    @Override
    public LinkedHashMap<String, String> getAdvancedCareReqs() {
        return new LinkedHashMap<>();
    }

    @Override
    public String getType() {
        return "Annual";
    }

    public AnnualModel(String name, byte sowingStartDate, byte sowingEndDate, byte plantingStartDate, byte plantingEndDate,
                       byte harvestStartDate, byte harvestEndDate, byte dormancyStartDate, byte dormancyEndDate, String water, String fertilization, String shade) {
        super(name, sowingStartDate, sowingEndDate, plantingStartDate, plantingEndDate, harvestStartDate, harvestEndDate, dormancyStartDate, dormancyEndDate, water, fertilization, shade);
    }
}
