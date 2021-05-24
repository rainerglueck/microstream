package schnatterinchen.labor.microstream.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DataGenerator {

    public List<VvzInstrument> generateVvzInstruments(int nbrofinstruments) {
        return IntStream.range(0, nbrofinstruments).boxed().map(
                i -> {
                    final String vvzid = "05AIM" + RandomStringUtils.random(8,true,true);
                    final String isin = "CH" + RandomStringUtils.random(10,false, true);
                    return new VvzInstrument(vvzid, isin);
                }
        ).collect(Collectors.toList());
    }
}
