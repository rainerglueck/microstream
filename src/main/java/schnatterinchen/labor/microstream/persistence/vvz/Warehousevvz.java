package schnatterinchen.labor.microstream.persistence.vvz;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import schnatterinchen.labor.microstream.model.VvzInstrument;
import schnatterinchen.labor.microstream.model.WarehouseDetails;
import schnatterinchen.labor.microstream.usecases.VvzPersistence;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;

@Component
public class Warehousevvz implements VvzPersistence {

    private final static Logger logger = LoggerFactory.getLogger(Warehousevvz.class);

    private final EmbeddedStorageManager storageInstrument1;
    private final VvzDataRoot vvzDataRoot = new VvzDataRoot();

    @Autowired
    private Warehousevvz(@Value("${storage.migration.warehousevvz}") String storage_instrument1) {
        this.storageInstrument1 = EmbeddedStorage.start(vvzDataRoot, Paths.get(storage_instrument1));
        logger.info("initialized warehouse with [{}] vvzinstruments", vvzDataRoot.vvzInstrumentMap.size());
    }

    @Override
    public void storeVvzInstrument(VvzInstrument vvzInstrument) {
        if (vvzInstrument != null) {
            vvzDataRoot.vvzInstrumentMap.put(vvzInstrument.vvzid, vvzInstrument);
            storageInstrument1.store(vvzDataRoot.vvzInstrumentMap);
        }
    }

    @Override
    public Collection<VvzInstrument> fetchvvzInstruments() {
        Collection<VvzInstrument> vvzInstrumentCollection = vvzDataRoot.vvzInstrumentMap.values();
        return Collections.unmodifiableCollection(vvzInstrumentCollection);
    }

    @Override
    public void deleteAll() {
        vvzDataRoot.vvzInstrumentMap.clear();
        storageInstrument1.store(vvzDataRoot);
    }

    @Override
    public WarehouseDetails fetchWarehouseDetails() {
        return new WarehouseDetails(vvzDataRoot.vvzInstrumentMap.size());
    }
}
