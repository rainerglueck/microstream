package schnatterinchen.labor.microstream.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

public abstract class Instrument {

    public List<String> toJsonLines() {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            List<String> htmline = Arrays.asList(json.split("\\n"));
            return htmline;
        } catch (JsonProcessingException e) {
            return Arrays.asList(e.getMessage());
        }
    }
}
