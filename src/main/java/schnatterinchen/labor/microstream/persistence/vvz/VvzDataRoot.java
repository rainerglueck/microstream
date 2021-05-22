package schnatterinchen.labor.microstream.persistence.vvz;

import schnatterinchen.labor.microstream.model.VvzInstrument;

import java.util.LinkedHashMap;
import java.util.Map;

public class VvzDataRoot {

    protected final Map<String, VvzInstrument> vvzInstrumentMap = new LinkedHashMap<>();
}
