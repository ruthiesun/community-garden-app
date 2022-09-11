package plant.model;

/**
 * Models the relationship between plants and the hardiness zones that they can tolerate
 */
public class ThrivesInModel {
    private String name;
    private Integer zoneNumCount;
    private Integer minZoneNum;
    private Integer maxZoneNum;

    public String getName() {
        return name;
    }

    public Integer getZoneNumCount() {
        return zoneNumCount;
    }

    public Integer getMinZoneNum () {
        return minZoneNum;
    }
    public Integer getMaxZoneNum () {
        return maxZoneNum;
    }

    public ThrivesInModel(String name, Integer zoneNumCount, Integer minZoneNum, Integer maxZoneNum) {
        this.name = name;
        this.zoneNumCount = zoneNumCount;
        this.minZoneNum = minZoneNum;
        this.maxZoneNum = maxZoneNum;
    }
}
