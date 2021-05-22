package schnatterinchen.labor.microstream.persistence.vvz;

import schnatterinchen.labor.microstream.model.VvzInstrument;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VvzDataRoot {

    protected final ConcurrentMap<String, VvzInstrument> vvzInstrumentMap = new ConcurrentHashMap<>();
}
