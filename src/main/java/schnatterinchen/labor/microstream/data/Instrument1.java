package schnatterinchen.labor.microstream.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Instrument1 extends Instrument {

    private final static Logger logger = LoggerFactory.getLogger(Instrument1.class);

    public final String isin;
    public final String vvzid;

    public final List<PropertyKeyValue> propertyKeyValueList = new ArrayList<>();

    public Instrument1(String isin, String vvzid) {
        Objects.requireNonNull(isin);
        Objects.requireNonNull(vvzid);
        this.isin = isin;
        this.vvzid = vvzid;
    }
}
