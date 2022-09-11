package plant.util.querybuilder;

/**
 * Puts together parts of queries that concern plant suppliers
 */
public class SuppliersQueryBuilder extends QueryBuilder{
    private String queryFromPlant(String plantName) {
        String query = "select SUPPLIERS_SELLSAT.BUSINESSNAME, SUPPLIERS_SELLSAT.HOUSENUM, SUPPLIERS_SELLSAT.STREET, SUPPLIERS_SELLSAT.POSTALCODE, LOCATION_POSTALCODECITY.CITY, LOCATION_POSTALCODEPROVINCE.PROVINCE" +
                " from PURCHASEFROM, SUPPLIERS_SELLSAT, LOCATION_PARTOF, LOCATION_POSTALCODEPROVINCE, LOCATION_POSTALCODECITY" +
                " where PURCHASEFROM.SUPPLIERID=SUPPLIERS_SELLSAT.SUPPLIERID" +
                " and PURCHASEFROM.name='" + plantName + "'" +
                " and SUPPLIERS_SELLSAT.POSTALCODE=LOCATION_PARTOF.POSTALCODE" +
                " and SUPPLIERS_SELLSAT.HOUSENUM=LOCATION_PARTOF.HOUSENUM" +
                " and SUPPLIERS_SELLSAT.STREET=LOCATION_PARTOF.STREET" +
                " and LOCATION_PARTOF.POSTALCODE=LOCATION_POSTALCODECITY.POSTALCODE" +
                " and LOCATION_POSTALCODECITY.POSTALCODE=LOCATION_POSTALCODEPROVINCE.POSTALCODE";
        return query;
    }

    public void makeQuery(String plantName) {
        finalQuery = queryFromPlant(plantName);
    }
}
