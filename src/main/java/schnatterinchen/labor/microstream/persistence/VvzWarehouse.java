package schnatterinchen.labor.microstream.persistence;

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
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class VvzWarehouse implements VvzPersistence {

    private final static Logger logger = LoggerFactory.getLogger(VvzWarehouse.class);

    private final long initialLoadTimeMilliSecs;
    private final EmbeddedStorageManager storageInstrument1;
    private final VvzDataRoot vvzDataRoot = new VvzDataRoot();

    @Autowired
    private VvzWarehouse(@Value("${storage.migration.warehousevvz}") String storage_instrument1) {
        Instant start = Instant.now();
        this.storageInstrument1 = EmbeddedStorage.start(vvzDataRoot, Paths.get(storage_instrument1));
        Instant finish = Instant.now();
        initialLoadTimeMilliSecs = Duration.between(start, finish).toMillis();
        logger.info("initialized warehouse with [{}] vvzinstruments in [{}] msecs", vvzDataRoot.vvzWarehouseInstrumentConcurrentMap.size(), initialLoadTimeMilliSecs);
    }

    @Override
    public void storeVvzInstrument(VvzInstrument vvzInstrument) {
        if (vvzInstrument != null) {
            vvzDataRoot.vvzWarehouseInstrumentConcurrentMap.put(vvzInstrument.vvzid, new VvzWarehouseInstrument(vvzInstrument));
            store();
        }
    }

    @Override
    public void storeVvzInstrument(Collection<VvzInstrument> vvzInstrumentCollection) {
        if (vvzInstrumentCollection != null) {
            vvzInstrumentCollection.forEach(x -> vvzDataRoot.vvzWarehouseInstrumentConcurrentMap
                    .put(x.vvzid, new VvzWarehouseInstrument(x)));
            store();
        }
    }

    @Override
    public Collection<VvzInstrument> fetchvvzInstruments() {
        Collection<VvzInstrument> vvzInstrumentCollection = vvzDataRoot.vvzWarehouseInstrumentConcurrentMap
                .values().stream().map(x -> convert(x)).collect(Collectors.toList());
        return Collections.unmodifiableCollection(vvzInstrumentCollection);
    }

    @Override
    public void deleteAll() {
        vvzDataRoot.vvzWarehouseInstrumentConcurrentMap.clear();
        store();
    }

    @Override
    public WarehouseDetails fetchWarehouseDetails() {
        return new WarehouseDetails(initialLoadTimeMilliSecs, vvzDataRoot.vvzWarehouseInstrumentConcurrentMap.size());
    }

    private synchronized void store() {
        /* not thread safe */
        storageInstrument1.store(vvzDataRoot.vvzWarehouseInstrumentConcurrentMap);
    }

    private VvzInstrument convert(VvzWarehouseInstrument vvzWarehouseInstrument) {
        return new VvzInstrument(vvzWarehouseInstrument.vvzid, vvzWarehouseInstrument.isin);
    }
}
