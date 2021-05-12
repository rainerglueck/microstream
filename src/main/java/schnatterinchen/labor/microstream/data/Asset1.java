package schnatterinchen.labor.microstream.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Asset1 {

    public final String isin;
    public final String vvzid;

    public final List<AssetClass> assetClassList = new ArrayList<>();

    public Asset1(String isin, String vvzid) {
        Objects.requireNonNull(isin);
        Objects.requireNonNull(vvzid);
        this.isin = isin;
        this.vvzid = vvzid;
    }
}
