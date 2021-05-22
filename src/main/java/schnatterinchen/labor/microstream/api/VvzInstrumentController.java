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
import schnatterinchen.labor.microstream.usecases.CloneAndMeasureLoadMicroserviceDb;
import schnatterinchen.labor.microstream.usecases.VvzPersistence;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class VvzInstrumentController {

    private final static Logger logger = LoggerFactory.getLogger(VvzInstrumentController.class);

    private final VvzPersistence vvzPersistence;
    private final DataGenerator dataGenerator;
    private final CloneAndMeasureLoadMicroserviceDb cloneAndMeasureLoadMicroserviceDb;
    private final List<String> messagesList = new ArrayList<>();
    private String lastAction = "";
    boolean ignoreLastAction = true;

    @Autowired
    private VvzInstrumentController(VvzPersistence vvzPersistence
            , DataGenerator dataGenerator
            , CloneAndMeasureLoadMicroserviceDb cloneAndMeasureLoadMicroserviceDb) {
        this.vvzPersistence = vvzPersistence;
        this.dataGenerator = dataGenerator;
        this.cloneAndMeasureLoadMicroserviceDb = cloneAndMeasureLoadMicroserviceDb;
    }

    @GetMapping(value = "/")
    String asset1(Model model) {
        logger.info("GET /");
        final List<VvzInstrument> first3elements = vvzPersistence.fetchvvzInstruments().stream()
                .limit(3)
                .collect(Collectors.toList());
        model.addAttribute("warehousedetailsVvz", vvzPersistence.fetchWarehouseDetails());
        model.addAttribute("vvzinstrumentlist", first3elements);
        model.addAttribute("lastAction", (ignoreLastAction) ? "" : lastAction);
        ignoreLastAction = true;
        return "migration";
    }

    @PostMapping(value = "/deleteall")
    String addAsset1(Model model) {
        logger.info("POST /deleteall");
        final Instant start = Instant.now();
        vvzPersistence.deleteAll();
        final Instant finish = Instant.now();
        lastAction = "deleteAll took [" + Duration.between(start, finish).toMillis() + "] ms";
        ignoreLastAction = false;
        return "redirect:/";
    }

    @PostMapping(value = "/add")
    String add(Model model, @RequestParam(name = "nbr") int nbr) {
        logger.info("POST /add?nbr=" + nbr);
        Instant start;
        Instant finish;

        start = Instant.now();
        final List<VvzInstrument> vvzInstrumentList = dataGenerator.generateVvzInstruments(nbr);
        finish = Instant.now();
        lastAction = "[" + nbr + "] VVzinstrument: generating objects in ["
                + Duration.between(start, finish).toMillis() + "] ms";

        start = finish;
        //vvzInstrumentList.forEach(x -> vvzPersistence.storeVvzInstrument(x)
        vvzPersistence.storeVvzInstrument(vvzInstrumentList);
        finish = Instant.now();

        lastAction = lastAction + ", storing in ["
                + Duration.between(start, finish).toMillis() + "] ms";
        ignoreLastAction = false;
        return "redirect:/";
    }

    @PostMapping(value = "/clone")
    String clone(Model model) {
        logger.info("POST /clone");
        lastAction = "loaded via Microstream: " + cloneAndMeasureLoadMicroserviceDb.cloneAndLoad();
        ignoreLastAction = false;
        return "redirect:/";
    }
}
