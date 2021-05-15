package schnatterinchen.labor.microstream.data;

import java.util.Optional;

public class Instrument2 extends Instrument {

    public final Optional<String> isin;

    public Instrument2(String isin) {
        this.isin = Optional.of(isin);
    }
}
