package schnatterinchen.labor.microstream.persistence.vvz;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import schnatterinchen.labor.microstream.model.VvzInstrument;
import schnatterinchen.labor.microstream.usecases.VvzPersistence;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Component
public class Warehousevvz implements VvzPersistence {

    private final String storage_instrument1;
    private final EmbeddedStorageManager storageInstrument1;
    private final VvzDataRoot vvzDataRoot = new VvzDataRoot();

    @Autowired
    private Warehousevvz(@Value("${storage.migration.warehousevvz}") String storage_instrument1) {
        this.storage_instrument1 = storage_instrument1;
        this.storageInstrument1 = EmbeddedStorage.start(vvzDataRoot, Paths.get(storage_instrument1));
    }

    @Override
    public void storeVvzInstrument(VvzInstrument vvzInstrument) {
        vvzDataRoot.instrumentList.add(vvzInstrument);
        storageInstrument1.store(vvzDataRoot);
    }

    @Override
    public List<VvzInstrument> fetchvvzInstruments() {
        return Collections.unmodifiableList(vvzDataRoot.instrumentList);
    }
}
