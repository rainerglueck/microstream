package schnatterinchen.labor.microstream.api;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import schnatterinchen.labor.microstream.data.Instrument1;
import schnatterinchen.labor.microstream.data.Instrument1Root;

import java.nio.file.Paths;

@Controller
public class MigrationController {

    private final static Logger logger = LoggerFactory.getLogger(MigrationController.class);

    private final String storage_asset1;

    private EmbeddedStorageManager storage;
    private Instrument1Root instrument1Root;

    @Autowired
    private MigrationController(@Value("${storage.migration.asset1}") String storage_asset1) {
        this.storage_asset1 = storage_asset1;
        startAssetRoot();
    }

    @GetMapping(value = "/")
    String asset1(Model model) {
        logger.info("GET /");
        model.addAttribute("assetRoot1", instrument1Root);
        return "migration";
    }

    @PostMapping(value = "/asset1")
    String addAsset1(Model model) {
        logger.info("addAsset1 /");
        int size = instrument1Root.assetList.size() + 1;
        instrument1Root.assetList.add(new Instrument1("isin " + size, "vvzid " + size));
        model.addAttribute("assetRoot1", instrument1Root);
        return "migration";
    }

    private void startAssetRoot() {
        instrument1Root = new Instrument1Root();
        storage = EmbeddedStorage.start(instrument1Root, Paths.get(storage_asset1));
    }

    private void stopAssetRoot() {
        storage.shutdown();
        instrument1Root = null;
    }
}
