package schnatterinchen.labor.microstream.usecases;

import schnatterinchen.labor.microstream.model.VvzInstrument;
import schnatterinchen.labor.microstream.model.WarehouseDetails;

import java.util.Collection;

public interface VvzPersistence {

    void storeVvzInstrument(VvzInstrument vvzInstrument);

    void storeVvzInstrument(Collection<VvzInstrument> vvzInstrumentCollection);

    Collection<VvzInstrument> fetchvvzInstruments();

    Collection<VvzInstrument> filterBy(String vvzid);

    void deleteAll();

    WarehouseDetails fetchWarehouseDetails();
}
