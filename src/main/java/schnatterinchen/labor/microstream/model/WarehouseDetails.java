package schnatterinchen.labor.microstream.model;

public class WarehouseDetails {

    public final long initialLoadTimeMilliSecs;
    public final long warehouseInventorySize;
    public final String warehouseSize;

    public WarehouseDetails(long initialLoadTimeMilliSecs, long warehouseInventorySize, String warehouseSize) {
        this.initialLoadTimeMilliSecs = initialLoadTimeMilliSecs;
        this.warehouseInventorySize = warehouseInventorySize;
        this.warehouseSize = warehouseSize;
    }
}
