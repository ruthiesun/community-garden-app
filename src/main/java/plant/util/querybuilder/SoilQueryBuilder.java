package plant.util.querybuilder;

/**
 * Puts together parts of queries that concern soils
 */
public class SoilQueryBuilder extends QueryBuilder {
    private String queryFromPlant(String plantName) {
        String query = "select soil.type, soil.alkalinity, soil.aeration from soil, plant, growsin" +
                " where plant.name='" + plantName + "'" +
                " and plant.name=growsin.name" +
                " and growsin.type=soil.type";
        return query;
    }

    public void makeQuery(String plantName) {
        finalQuery = queryFromPlant(plantName);
    }
}
