package schnatterinchen.labor.microstream.persistence;

import schnatterinchen.labor.microstream.model.VvzInstrument;

import java.util.Objects;

public class VvzWarehouseInstrument {

    protected final String vvzid;
    protected final String isin;

    protected VvzWarehouseInstrument(VvzInstrument vvzInstrument) {
        Objects.requireNonNull(vvzInstrument);
        this.vvzid = vvzInstrument.vvzid;
        this.isin = vvzInstrument.isin;
    }
}
