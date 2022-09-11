package plant.util.querybuilder;

/**
 * Superclass for classes that put together query components
 */
public abstract class QueryBuilder {
    protected String finalQuery;
    public QueryBuilder() {
        finalQuery = "";
    }
    public String getFinalQuery() {
        return finalQuery;
    }
}
