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
import schnatterinchen.labor.microstream.data.Instrument2Root;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MigrationController {

    private final static Logger logger = LoggerFactory.getLogger(MigrationController.class);

    private final String storage_asset1;
    private final Instrument1Root instrument1Root = new Instrument1Root();
    private final List<String> messagesList = new ArrayList<>();

    @Autowired
    private MigrationController(@Value("${storage.migration.asset1}") String storage_asset1) {
        this.storage_asset1 = storage_asset1;
    }

    @GetMapping(value = "/")
    String asset1(Model model) {
        logger.info("GET /");
        model.addAttribute("instrument1Root", instrument1Root);
        model.addAttribute("instrument2Root", readInstrument2Root());
        model.addAttribute("messagesList", messagesList);
        return "migration";
    }

    @PostMapping(value = "/clearall")
    String clearall(Model model) {
        logger.info("POST /clearall");
        messagesList.clear();
        return "redirect:/";
    }

    @PostMapping(value = "/reset")
    String addAsset1(Model model) {
        logger.info("POST /reset");
        messagesList.clear();
        {
            clearInstrument1_store();
        }

        addInstrument1();
        return "redirect:/";
    }

    private Instrument2Root readInstrument2Root() {
        Instrument2Root instrument2Root = new Instrument2Root();
        try {
            EmbeddedStorage.start(instrument2Root, Paths.get(storage_asset1));
        } catch (Exception ex) {
            messagesList.add(ex.getMessage());
            logger.error("readInstrument2Root", ex.getMessage());
        }
        return instrument2Root;
    }

    private Instrument1Root readInstrument1Root() {
        Instrument1Root instrument1Root = new Instrument1Root();
        try {
            EmbeddedStorage.start(instrument1Root, Paths.get(storage_asset1));
        } catch (Exception ex) {
            messagesList.add(ex.getMessage());
            logger.error("readInstrument1Root", ex.getMessage());
        }
        return instrument1Root;
    }

    private void clearInstrument1_store() {
        messagesList.add("clear instrument1List: instrument1Root.instrument1List.clear()");
        messagesList.add("store instrument1List: storageInstrument1.store(instrument1Root)");
        EmbeddedStorageManager storageInstrument1 = EmbeddedStorage.start(instrument1Root, Paths.get(storage_asset1));
        instrument1Root.instrument1List.clear();
        storageInstrument1.store(instrument1Root);
        storageInstrument1.close();
    }

    private void addInstrument1() {
        Instrument1 instrument1 = new Instrument1("isin 1", "vvzid 1");
        instrument1Root.instrument1List.add(instrument1);
    }
}
