package be.fgov.caamihziv.services.quickmon.domain.assertions.spel;

import com.jayway.jsonpath.JsonPath;

/**
 * Created by gs on 24.04.17.
 */
public class SpelExtensions {
    
    /**
     * 
     * @param input
     * @param path
     * @return
     */
    public static Object extractFromJson(String input, String path) {
        JsonPath jsonPath = JsonPath.compile(path);
        return jsonPath.read(input);
    }

    public static Integer intOf(String input) {
        return new Integer(input);
    }
    
}
