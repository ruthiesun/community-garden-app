package plant.model;

import plant.util.MonthConverter;
import java.util.LinkedHashMap;

/**
 * Models perennial plants
 */
public class PerennialModel extends PlantModel{
    private Byte pruningDate;
    private Integer lifespanYears;

    public Byte getPruningDate() {
        return pruningDate;
    }

    public Integer getLifespanYears() {
        return lifespanYears;
    }

    @Override
    public LinkedHashMap<String, String> getAdvancedCareReqs() {
        LinkedHashMap<String, String> reqs = new LinkedHashMap<>();
        String s = "N/A";
        if (pruningDate != null) {
            s = MonthConverter.numToStringLong(pruningDate);
        }
        reqs.put("Pruning month: ", s);

        s = "N/A";
        if (lifespanYears != null) {
            s = Integer.toString(lifespanYears);
        }
        reqs.put("Lifespan (years): ", s);
        return reqs;
    }

    @Override
    public String getType() {
        return "Perennial";
    }

    public PerennialModel(String name, byte sowingStartDate, byte sowingEndDate, byte plantingStartDate, byte plantingEndDate,
                          byte harvestStartDate, byte harvestEndDate, byte dormancyStartDate, byte dormancyEndDate, String water, String fertilization, String shade,
                          int lifespanYears, byte pruningDate) {
        super(name, sowingStartDate, sowingEndDate, plantingStartDate, plantingEndDate, harvestStartDate, harvestEndDate, dormancyStartDate, dormancyEndDate, water, fertilization, shade);
        this.lifespanYears = lifespanYears;
        this.pruningDate = pruningDate;
    }
}
