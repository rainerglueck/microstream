package schnatterinchen.labor.microstream.usecases;

import schnatterinchen.labor.microstream.model.VvzInstrument;

import java.util.Collection;

public interface VvzPersistence {

    void storeVvzInstrument(VvzInstrument vvzInstrument);

    Collection<VvzInstrument> fetchvvzInstruments();

    void deleteAll();
}
