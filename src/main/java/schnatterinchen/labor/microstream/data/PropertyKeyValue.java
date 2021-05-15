package schnatterinchen.labor.microstream.data;

import java.util.Objects;

public class PropertyKeyValue {

    public final String assetclass;
    public final String classvalue;

    public PropertyKeyValue(String assetclass, String classvalue) {
        Objects.requireNonNull(assetclass);
        Objects.requireNonNull(classvalue);
        this.assetclass = assetclass;
        this.classvalue = classvalue;
    }
}
