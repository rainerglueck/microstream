package schnatterinchen.labor.microstream.model;

public class WarehouseDetails {

    public final long initialLoadTimeMilliSecs;
    public final long warehouseSize;

    public WarehouseDetails(long initialLoadTimeMilliSecs, long warehouseSize) {
        this.initialLoadTimeMilliSecs = initialLoadTimeMilliSecs;
        this.warehouseSize = warehouseSize;
    }
}
