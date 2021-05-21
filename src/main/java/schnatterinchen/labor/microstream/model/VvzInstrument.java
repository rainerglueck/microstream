package schnatterinchen.labor.microstream.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VvzInstrument {

    public final String vvzid;
    public final String isin;

    public VvzInstrument(String vvzid, String isin) {
        Objects.requireNonNull(isin);
        Objects.requireNonNull(vvzid);
        this.isin = isin;
        this.vvzid = vvzid;
    }

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
