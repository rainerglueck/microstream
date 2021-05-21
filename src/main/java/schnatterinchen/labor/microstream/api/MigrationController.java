package schnatterinchen.labor.microstream.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import schnatterinchen.labor.microstream.model.VvzInstrument;
import schnatterinchen.labor.microstream.usecases.VvzPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
public class MigrationController {

    private final static Logger logger = LoggerFactory.getLogger(MigrationController.class);

    private final VvzPersistence vvzPersistence;
    private final List<String> messagesList = new ArrayList<>();
    private boolean deletemessages = false;

    @Autowired
    private MigrationController(VvzPersistence vvzPersistence) {
        this.vvzPersistence = vvzPersistence;
    }

    @GetMapping(value = "/")
    String asset1(Model model) {
        logger.info("GET /");
        if (deletemessages) {
            messagesList.clear();
        }
        model.addAttribute("vvzinstrumentlist", vvzPersistence.fetchvvzInstruments());
        model.addAttribute("instrument2Root", null);
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
                /*
                File src = Paths.get(storage_instrument1).toFile();
                File dst = Paths.get(storage_instrument2).toFile();
                if (dst.exists()) {
                    messagesList.add("delete [" + dst.toString() + "]");
                    FileUtils.deleteDirectory(dst);
                }
                messagesList.add("copy [" + src.toString() + "] --> [" + dst.toString() + "]");
                FileUtils.copyDirectory(src, dst);
                EmbeddedStorage.start(instrument2Root, Paths.get(storage_instrument2)); */
            } catch (Exception e) {
                messagesList.add(e.getMessage());
            }
        }
        deletemessages = false;
        return "redirect:/";
    }

    private void clearInstrument1_store() {
        //messagesList.add("clear instrument1List: instrument1Root.instrument1List.clear()");
        //messagesList.add("store instrument1List: storageInstrument1.store(instrument1Root)");
        //instrument1Root.instrument1List.clear();
        //storageInstrument1.store(instrument1Root.instrument1List);
    }

    private void addInstrument1() {
        messagesList.add("add instrument1 to instrument1List");
        messagesList.add("store instrument1List: storageInstrument1.store(instrument1Root)");
        IntStream.range(0, 1).forEach(x -> {
            VvzInstrument instrument1 = new VvzInstrument("vvzid " + (x + 1), "isin " + (x + 1));
            vvzPersistence.storeVvzInstrument(instrument1);
        });
    }
}
