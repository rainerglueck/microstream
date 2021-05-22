package schnatterinchen.labor.microstream.usecases;

import schnatterinchen.labor.microstream.model.VvzInstrument;
import schnatterinchen.labor.microstream.model.WarehouseDetails;

import java.util.Collection;

public interface VvzPersistence {

    void storeVvzInstrument(VvzInstrument vvzInstrument);

    Collection<VvzInstrument> fetchvvzInstruments();

    void deleteAll();

    WarehouseDetails fetchWarehouseDetails();
}
