package be.fgov.caamihziv.services.quickmon.interfaces.api.healthChecks.lucene;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

/**
 * Created by gs on 12-12-16.
 */
public class QueryNodeEditor extends PropertyEditorSupport {

    private static final Logger logger = LoggerFactory.getLogger(QueryNodeEditor.class);

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        ObjectMapper mapper = new ObjectMapper();
        QueryNode queryNode = null;
        try {
            queryNode = mapper.readValue(text, QueryNode.class);
            setValue(queryNode);
        } catch (IOException e) {
            logger.debug("Unable to convert json to querynode", e);
            setValue(null);
        }
    }
}
