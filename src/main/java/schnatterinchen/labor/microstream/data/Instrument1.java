package schnatterinchen.labor.microstream.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Instrument1 {

    public final String isin;
    public final String vvzid;

    public final List<PropertyKeyValue> propertyKeyValueList = new ArrayList<>();

    public Instrument1(String isin, String vvzid) {
        Objects.requireNonNull(isin);
        Objects.requireNonNull(vvzid);
        this.isin = isin;
        this.vvzid = vvzid;
    }

    public String toJsonString() {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
