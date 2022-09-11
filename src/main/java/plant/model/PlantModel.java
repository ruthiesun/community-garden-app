package plant.model;

import java.util.LinkedHashMap;

/**
 * Abstract class for modelling plants
 */
public abstract class PlantModel {
    private String name;
    private byte sowingStartDate;
    private byte sowingEndDate;
    private byte plantingStartDate;
    private byte plantingEndDate;
    private byte harvestStartDate;
    private byte harvestEndDate;
    private byte dormancyStartDate;
    private byte dormancyEndDate;
    private String water;
    private String fertilization;
    private String shade;

    public String getName() {
        return name;
    }

    public int getSowingStartDate() {
        return sowingStartDate;
    }

    public int getSowingEndDate() {
        return sowingEndDate;
    }

    public int getHarvestStartDate() {
        return harvestStartDate;
    }

    public int getHarvestEndDate() {
        return harvestEndDate;
    }

    public int getPlantingStartDate() {
        return plantingStartDate;
    }

    public int getPlantingEndDate() {
        return plantingEndDate;
    }

    public int getDormancyStartDate() {
        return dormancyStartDate;
    }

    public int getDormancyEndDate() {
        return dormancyEndDate;
    }

    public String getFertilization() {
        return fertilization;
    }

    public String getShade() {
        return shade;
    }

    public String getWater() {
        return water;
    }

    public abstract LinkedHashMap<String, String> getAdvancedCareReqs();
    public abstract String getType();

    public PlantModel(String name, byte sowingStartDate, byte sowingEndDate, byte plantingStartDate, byte plantingEndDate,
                      byte harvestStartDate, byte harvestEndDate, byte dormancyStartDate, byte dormancyEndDate, String water, String fertilization, String shade) {
        this.name = name;
        this.sowingStartDate = sowingStartDate;
        this.sowingEndDate = sowingEndDate;
        this.plantingStartDate = plantingStartDate;
        this.plantingEndDate = plantingEndDate;
        this.harvestStartDate = harvestStartDate;
        this.harvestEndDate = harvestEndDate;
        this.dormancyStartDate = dormancyStartDate;
        this.dormancyEndDate = dormancyEndDate;
        this.water = water;
        this.fertilization = fertilization;
        this.shade = shade;
    }
}