package schnatterinchen.labor.microstream.persistence;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VvzDataRoot {

    protected final ConcurrentMap<String, VvzWarehouseInstrument> vvzWarehouseInstrumentConcurrentMap = new ConcurrentHashMap<>();
}
