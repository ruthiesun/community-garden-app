package plant.util.querybuilder;

/**
 * Puts together parts of queries that concern hardiness zones
 */
public class HardinessQueryBuilder extends QueryBuilder{
    public void makeCountZonesQuery() {
        finalQuery = " from thrivesin" +
                " group by name";
    }

    public void makeWarmerWeatherPlantsQuery(String plantName) {
        finalQuery = " from thrivesin" +
                " group by name" +
                " having min(zonenum)>(select min(zonenum)" +
                " from thrivesin" +
                " where name='" + plantName + "')";
    }

    public void makeAdaptablePlantsQuery() {
        finalQuery = " from thrivesin" +
                " group by name" +
                " having count(zonenum)>=ALL" +
                " (select count(zonenum)" +
                " from thrivesin" +
                " group by name)";
    }

    public void makeSameSoilAndZonesQuery(String plantName) {
        finalQuery = "select unique t1.name" +
                " from thrivesin t1" +
                " where not exists" +
                " ((select g2.type, t2.zonenum" +
                " from growsin g2, thrivesin t2" +
                " where g2.name=t2.name" +
                " and g2.type in (select g3.type" +
                " from growsin g3" +
                " where g3.name='" + plantName + "')" +
                " and t2.zonenum in (select t3.zonenum" +
                " from thrivesin t3" +
                " where t3.name='" + plantName + "'))" +
                " minus" +
                " (select g2.type, t2.zonenum" +
                " from growsin g2, thrivesin t2" +
                " where g2.name=t1.name" +
                " and g2.name=t2.name))";
    }
}
