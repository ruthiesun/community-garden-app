package plant.util.querybuilder;

/**
 * Puts together parts of queries that concern fertilizers
 */
public class FertilizersQueryBuilder extends QueryBuilder{
    private String queryFromPlant(String plantName) {
        String query = " from BENEFITSFROM, FERTILIZER" +
                " where BENEFITSFROM.NAME='" + plantName + "'" +
                " and BENEFITSFROM.FERTILIZERID=FERTILIZER.FERTILIZERID";
        return query;
    }

    public void makeQuery(String plantName) {
        finalQuery = queryFromPlant(plantName);
    }
}
