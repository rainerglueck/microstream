package schnatterinchen.labor.microstream.api;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.apache.commons.io.FileUtils;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
public class MigrationController {

    private final static Logger logger = LoggerFactory.getLogger(MigrationController.class);

    private final EmbeddedStorageManager storageInstrument1;
    private final String storage_instrument1;
    private final String storage_instrument2;
    private final Instrument1Root instrument1Root = new Instrument1Root();
    private final List<String> messagesList = new ArrayList<>();
    private boolean deletemessages = false;

    @Autowired
    private MigrationController(@Value("${storage.migration.instrument1}") String storage_instrument1,
                                @Value("${storage.migration.instrument2}") String storage_instrument2) {
        this.storage_instrument1 = storage_instrument1;
        this.storage_instrument2 = storage_instrument2;

        storageInstrument1 = EmbeddedStorage.start(instrument1Root, Paths.get(storage_instrument1));
    }

    @GetMapping(value = "/")
    String asset1(Model model) {
        logger.info("GET /");
        if (deletemessages) {
            messagesList.clear();
        }
        model.addAttribute("instrument1Root", instrument1Root);
        model.addAttribute("messagesList", messagesList);
        deletemessages = true;
        return "migration";
    }

    @PostMapping(value = "/reset")
    String addAsset1(Model model) {
        logger.info("POST /reset");
        messagesList.clear();
        {
            clearInstrument1_store();
            addInstrument1();
        }
        deletemessages = false;
        return "redirect:/";
    }

    @PostMapping(value = "/migrate1_to_2")
    String migrate1_to_2(Model model) {
        logger.info("POST /migrate1_to_2");
        {
            try {
                File src = Paths.get(storage_instrument1).toFile();
                File dst = Paths.get(storage_instrument2).toFile();
                if (dst.exists()) {
                    messagesList.add("delete [" + dst.toString() + "]");
                    FileUtils.deleteDirectory(dst);
                }
                messagesList.add("copy [" + src.toString() + "] --> [" + dst.toString() + "]");
                FileUtils.copyDirectory(src, dst);
            } catch (Exception e) {
                messagesList.add(e.getMessage());
            }
        }
        deletemessages = false;
        return "redirect:/";
    }

    private void clearInstrument1_store() {
        messagesList.add("clear instrument1List: instrument1Root.instrument1List.clear()");
        messagesList.add("store instrument1List: storageInstrument1.store(instrument1Root)");
        instrument1Root.instrument1List.clear();
        storageInstrument1.store(instrument1Root.instrument1List);
    }

    private void addInstrument1() {
        messagesList.add("add instrument1 to instrument1List");
        messagesList.add("store instrument1List: storageInstrument1.store(instrument1Root)");
        IntStream.range(0, 1).forEach(x -> {
            Instrument1 instrument1 = new Instrument1("isin " + (x + 1), "vvzid " + (x + 1));
            instrument1Root.instrument1List.add(instrument1);
        });
        storageInstrument1.store(instrument1Root.instrument1List);
    }
}
