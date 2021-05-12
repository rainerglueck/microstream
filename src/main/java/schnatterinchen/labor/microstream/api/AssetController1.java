package schnatterinchen.labor.microstream.api;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import schnatterinchen.labor.microstream.data.AssetRoot1;

import java.nio.file.Paths;

@RestController
public class AssetController1 {

    private EmbeddedStorageManager storage;
    private AssetRoot1 assetRoot1;

    private AssetController1() {
        startAssetRoot();
    }

    @GetMapping("/")
    String asset1(){
        return "assetRoot1.size() = " + assetRoot1.assetList.size();
    }


    private void startAssetRoot() {
        assetRoot1 = new AssetRoot1();
        storage = EmbeddedStorage.start(assetRoot1, Paths.get("storage/assetRoot1"));
    }

    private void stopAssetRoot() {
        storage.shutdown();
        assetRoot1 = null;
    }
}
