package schnatterinchen.labor.microstream.data;

import java.util.Objects;

public class AssetClass {

    public final String assetclass;
    public final String classvalue;

    public AssetClass(String assetclass, String classvalue) {
        Objects.requireNonNull(assetclass);
        Objects.requireNonNull(classvalue);
        this.assetclass = assetclass;
        this.classvalue = classvalue;
    }
}
