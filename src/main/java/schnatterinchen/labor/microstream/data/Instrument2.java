package schnatterinchen.labor.microstream.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Instrument2 extends Instrument {

    private final static Logger logger = LoggerFactory.getLogger(Instrument2.class);

    public final String isin;
    public final String vvzid;

    public final List<PropertyKeyValue> propertyKeyValueList = new ArrayList<>();

    public Instrument2(String isin, String vvzid) {
        Objects.requireNonNull(isin);
        Objects.requireNonNull(vvzid);
        this.isin = isin;
        this.vvzid = vvzid;
    }
}
