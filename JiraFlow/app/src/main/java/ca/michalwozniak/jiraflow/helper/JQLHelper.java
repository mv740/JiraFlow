package ca.michalwozniak.jiraflow.helper;

/**
 * Created by Michal Wozniak on 7/24/2016.
 * used to set first parameter in JQL query
 * eg: jql=assinee=user
 *
 *
 */



public class JQLHelper {

    public enum Query {
        ASSIGNEE("assignee"),
        PROJECT("project");

        String parameter;
        Query(String p) {
            parameter = p;
        }

    }

    private String query;
    private String parameter;
    private final String quote = "\"";

    public JQLHelper(Query query, String parameter) {
        this.query = query.toString();

        if(query == Query.ASSIGNEE)
        {
            this.parameter = quote+parameter+quote;
        }else
            this.parameter = parameter;
    }

    @Override
    public String toString() {
        return query + "=" + parameter;
    }
}
