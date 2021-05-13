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
import schnatterinchen.labor.microstream.data.Asset1;
import schnatterinchen.labor.microstream.data.AssetRoot1;

import java.nio.file.Paths;

@Controller
public class MigrationController {

    private final static Logger logger = LoggerFactory.getLogger(MigrationController.class);

    private final String storage_asset1;

    private EmbeddedStorageManager storage;
    private AssetRoot1 assetRoot1;

    @Autowired
    private MigrationController(@Value("${storage.migration.asset1}") String storage_asset1) {
        this.storage_asset1 = storage_asset1;
        startAssetRoot();
    }

    @GetMapping(value = "/")
    String asset1(Model model) {
        logger.info("GET /");
        model.addAttribute("assetRoot1", assetRoot1);
        return "migration";
    }

    @PostMapping(value = "/asset1")
    String addAsset1(Model model) {
        logger.info("addAsset1 /");
        int size = assetRoot1.assetList.size() + 1;
        assetRoot1.assetList.add(new Asset1("isin " + size, "vvzid " + size));
        model.addAttribute("assetRoot1", assetRoot1);
        return "migration";
    }

    private void startAssetRoot() {
        assetRoot1 = new AssetRoot1();
        storage = EmbeddedStorage.start(assetRoot1, Paths.get(storage_asset1));
    }

    private void stopAssetRoot() {
        storage.shutdown();
        assetRoot1 = null;
    }
}
