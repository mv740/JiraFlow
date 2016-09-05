package ca.michalwozniak.jiraflow.utility;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by Michal Wozniak on 9/5/2016.
 */

public class LogManager {

    public static void displayJSON(String tag, Object object)
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        try {

            String json = mapper.writeValueAsString(object);
            Log.e(tag, json);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
