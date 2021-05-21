package schnatterinchen.labor.microstream.persistence.vvz;

import java.util.Objects;

public class VvzWarehouseInstrument {

    private final String vvzid;

    protected VvzWarehouseInstrument(String vvzid) {
        Objects.requireNonNull(vvzid);

        this.vvzid = vvzid;
    }
}
