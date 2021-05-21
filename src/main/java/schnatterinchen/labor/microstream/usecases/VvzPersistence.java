package schnatterinchen.labor.microstream.usecases;

import schnatterinchen.labor.microstream.model.VvzInstrument;

import java.util.List;

public interface VvzPersistence {

    void storeVvzInstrument(VvzInstrument vvzInstrument);

    List<VvzInstrument> fetchvvzInstruments();
}
