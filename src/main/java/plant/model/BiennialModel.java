package plant.model;

import java.util.LinkedHashMap;

/**
 * Models biennial plants
 */
public class BiennialModel extends PlantModel{
    private Integer vernalizationTemp;

    public int getVernalizationTemp() {
        return vernalizationTemp;
    }

    @Override
    public LinkedHashMap<String, String> getAdvancedCareReqs() {
        LinkedHashMap<String, String> reqs = new LinkedHashMap<>();
        String s = "N/A";
        if (vernalizationTemp != null) {
            s = Integer.toString(vernalizationTemp);
        }
        reqs.put("Vernalization temperature: ", s + " degrees Celsius");
        return reqs;
    }

    @Override
    public String getType() {
        return "Biennial";
    }

    public BiennialModel(String name, byte sowingStartDate, byte sowingEndDate, byte plantingStartDate, byte plantingEndDate,
                         byte harvestStartDate, byte harvestEndDate, byte dormancyStartDate, byte dormancyEndDate, String water, String fertilization, String shade,
                         int vernalizationTemp) {
        super(name, sowingStartDate, sowingEndDate, plantingStartDate, plantingEndDate, harvestStartDate, harvestEndDate, dormancyStartDate, dormancyEndDate, water, fertilization, shade);
        this.vernalizationTemp = vernalizationTemp;
    }
}
