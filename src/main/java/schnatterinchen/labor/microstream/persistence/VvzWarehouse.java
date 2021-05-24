package schnatterinchen.labor.microstream.persistence;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import schnatterinchen.labor.microstream.model.VvzInstrument;
import schnatterinchen.labor.microstream.model.WarehouseDetails;
import schnatterinchen.labor.microstream.usecases.CloneAndMeasureLoadMicroserviceDb;
import schnatterinchen.labor.microstream.usecases.VvzPersistence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class VvzWarehouse implements VvzPersistence, CloneAndMeasureLoadMicroserviceDb {

    private final static Logger logger = LoggerFactory.getLogger(VvzWarehouse.class);

    private final long initialLoadTimeMilliSecs;
    private final String vvzwarehousestorage;
    private final String vvzwarehousestorageclone;
    private final EmbeddedStorageManager storageInstrument1;
    private final VvzDataRoot vvzDataRoot = new VvzDataRoot();
    private final File warehousedirectory;

    @Autowired
    private VvzWarehouse(@Value("${storage.migration.warehousevvz}") String vvzwarehousestorage
            , @Value("${storage.migration.warehousevvz.clone}") String vvzwarehousestorageclone) {
        this.vvzwarehousestorage = vvzwarehousestorage;
        this.vvzwarehousestorageclone = vvzwarehousestorageclone;
        warehousedirectory = Paths.get(vvzwarehousestorage).toFile();

        Instant start = Instant.now();
        this.storageInstrument1 = EmbeddedStorage.start(vvzDataRoot, Paths.get(vvzwarehousestorage));
        initialLoadTimeMilliSecs = Duration.between(start, Instant.now()).toMillis();
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
        return new WarehouseDetails(initialLoadTimeMilliSecs
                , vvzDataRoot.vvzWarehouseInstrumentConcurrentMap.size()
                , getWarehouseSize(this.warehousedirectory));
    }

    private synchronized void store() {
        /* not thread safe */
        storageInstrument1.store(vvzDataRoot.vvzWarehouseInstrumentConcurrentMap);
    }

    private VvzInstrument convert(VvzWarehouseInstrument vvzWarehouseInstrument) {
        return new VvzInstrument(vvzWarehouseInstrument.vvzid, vvzWarehouseInstrument.isin);
    }

    @Override
    public String cloneAndLoad() {
        String message = "";
        try {
            String id = String.valueOf(Instant.now().getEpochSecond());
            File dst = Paths.get(vvzwarehousestorageclone, id).toFile();

            deleteDirIfExists(dst);
            logger.info("[{}].mkdirs(): [{}]", dst.toString(), dst.mkdirs());
            copyDir(warehousedirectory, dst);

            VvzDataRoot clone = new VvzDataRoot();
            long loading = executionStopWatch((x) -> EmbeddedStorage.start(clone, dst.toPath()));
            message = "Loaded Warehouse with [" + clone.vvzWarehouseInstrumentConcurrentMap.size()
                    + "] VvzInstruments in [" + loading + "] ms, size [" + getWarehouseSize(dst) + "]";
            deleteDirIfExists(dst);
        } catch (Exception e) {
            message = e.getMessage();
        }
        return message;
    }

    private long executionStopWatch(final Consumer<Object> consumer) {
        final Instant start = Instant.now();
        consumer.accept(start);
        return Duration.between(start, Instant.now()).toMillis();
    }

    private void deleteDirIfExists(File dir) throws IOException {
        if (dir.exists()) {
            logger.info("delete [" + dir.toString() + "]");
            FileUtils.deleteDirectory(dir);
        }
    }

    private void copyDir(File src, File dst) throws IOException {
        logger.info("copy [{}}] --> [{}], size [{}]", src.toString(), dst.toString(), getWarehouseSize(dst));
        FileUtils.copyDirectory(src, dst);
    }

    private String getWarehouseSize(File warehouse) {
        return FileUtils.byteCountToDisplaySize(FileUtils.sizeOfDirectory(warehouse));
    }

    @Override
    public Collection<VvzInstrument> filterBy(final String vvzid) {
        final String VVZID = vvzid.toUpperCase();
        return vvzDataRoot.vvzWarehouseInstrumentConcurrentMap.values()
                .stream()
                .filter(x -> x.vvzid.toUpperCase().contains(VVZID))
                .map(x -> convert(x))
                .collect(Collectors.toList());
    }
}
