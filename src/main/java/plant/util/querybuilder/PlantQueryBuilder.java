package plant.util.querybuilder;

import java.util.HashSet;

/**
 * Puts together parts of queries that concerns plant lookup
 */
public class PlantQueryBuilder extends QueryBuilder {
    private String querySoil(HashSet<String> selected) {
        String query = "select unique plant.name" +
                " from plant, growsin" +
                " where plant.name=growsin.name";
        if (!selected.isEmpty()) {
            query += " and (";
            boolean first = true;
            for (String s : selected) {
                if (first) {
                    query += "growsin.type='" + s + "'";
                    first = false;
                } else {
                    query += " or growsin.type='" + s + "'";
                }
            }
            query += ")";
        } else {
            query += " minus " + query;
        }

        return query;
    }

    private String queryZones(int[] zones) {
        String query;
        if (zones[0]>zones[1]) {
            query = "select name from thrivesin minus select name from thrivesin";
        } else {
            query = "(select name" +
                    " from thrivesin" +
                    " group by name" +
                    " having max(zonenum)>=" + zones[1] +
                    " union" +
                    " select name" +
                    " from thrivesin" +
                    " group by name" +
                    " having min(zonenum)>=" + zones[0] + ")" ;
        }
        return query;
    }

    private String queryDates(int[] sowing, int[] planting, int[] harvesting) {
        String query = "select unique plant.name" +
                " from plant" +
                " where (sowingstartdate>=" + sowing[0] + " or sowingstartdate is null)" +
                " and (sowingenddate<=" + sowing[1] + " or sowingenddate is null)" +
                " and (plantstartdate>=" + planting[0] + " or plantstartdate is null)" +
                " and (plantenddate<=" + planting[1] + " or plantenddate is null)" +
                " and (harveststartdate>=" + harvesting[0] + " or harveststartdate is null)" +
                " and (harvestenddate<=" + harvesting[1] + " or harvestenddate is null)";
        return query;
    }

    private String queryGardenZones(HashSet<String> selected) {
        String query = "select unique plant.name" +
                " from plant, thrivesin" +
                " where plant.name=thrivesin.name" +
                " and thrivesin.zonenum in" +
                " (select l1.zonenum" +
                " from communitygarden_locatedin c, location_partof l1, location_postalcodecity l2, location_postalcodeprovince l3" +
                " where l2.postalcode=l3.postalcode" +
                " and l3.postalcode=l1.postalcode" +
                " and c.street=l1.street" +
                " and c.housenum=l1.housenum" +
                " and c.postalcode=l1.postalcode";
        boolean first = true;
        for (String s : selected) {
            if (first) {
                query += " and (";
                first = false;
            } else {
                query += " or ";
            }
            query += "c.name='" + selected + "'";
        }
        query += "))";

        return query;
    }

    private String queryEdible() {
        String query = "select unique plant.name" +
                " from plant" +
                " where harveststartdate is not null";
        return query;
    }

    private String queryOrnamental() {
        String query = "select unique plant.name" +
                " from plant" +
                " where harveststartdate is null";
        return query;
    }

    private String querySearchString(String s) {
        String query = "select unique name" +
                " from plant" +
                " where upper(name) like upper('%" + s + "%')";
        return query;
    }

    public void makeQuery(String searchString, HashSet<String> soil, int[] zones, int[] sowing, int[] planting, int[] harvesting, HashSet<String> gardens,
                     boolean edible, boolean ornamental) {
        finalQuery += querySearchString(searchString);
        finalQuery += " intersect ";
        finalQuery += querySoil(soil);
        finalQuery += " intersect ";
        finalQuery += queryZones(zones);
        finalQuery += " intersect ";
        finalQuery += queryDates(sowing, planting, harvesting);
        if (!gardens.isEmpty()) {
            finalQuery += " intersect ";
            finalQuery += queryGardenZones(gardens);
        }
        if (ornamental && edible) {
            finalQuery += " intersect ";
            finalQuery += " (";
            finalQuery += queryOrnamental();
            finalQuery += " union ";
            finalQuery += queryEdible();
            finalQuery += ")";
        } else if (edible) {
            finalQuery += " intersect ";
            finalQuery += queryEdible();
        } else if (ornamental) {
            finalQuery += " intersect ";
            finalQuery += queryOrnamental();
        } else {
            finalQuery += " minus " + finalQuery;
        }
    }
}
