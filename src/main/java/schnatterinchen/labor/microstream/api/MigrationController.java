package schnatterinchen.labor.microstream.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import schnatterinchen.labor.microstream.model.DataGenerator;
import schnatterinchen.labor.microstream.model.VvzInstrument;
import schnatterinchen.labor.microstream.usecases.VvzPersistence;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MigrationController {

    private final static Logger logger = LoggerFactory.getLogger(MigrationController.class);

    private final VvzPersistence vvzPersistence;
    private final DataGenerator dataGenerator;
    private final List<String> messagesList = new ArrayList<>();

    @Autowired
    private MigrationController(VvzPersistence vvzPersistence
            , DataGenerator dataGenerator) {
        this.vvzPersistence = vvzPersistence;
        this.dataGenerator = dataGenerator;
    }

    @GetMapping(value = "/")
    String asset1(Model model) {
        logger.info("GET /");
        model.addAttribute("vvzinstrumentlist", vvzPersistence.fetchvvzInstruments());
        model.addAttribute("instrument2Root", null);
        model.addAttribute("messagesList", null);
        return "migration";
    }

    @PostMapping(value = "/deleteall")
    String addAsset1(Model model) {
        logger.info("POST /deleteall");
        vvzPersistence.deleteAll();
        return "redirect:/";
    }

    @PostMapping(value = "/add")
    String add(Model model, @RequestParam(name = "nbr") int nbr) {
        logger.info("POST /add?nbr=" + nbr);
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
        return "redirect:/";
    }

    private void clearInstrument1_store() {

    }

    private void addInstrument1() {
        VvzInstrument instrument1 = dataGenerator.generateVvzInstruments(1).get(0);
        vvzPersistence.storeVvzInstrument(instrument1);

    }
}
