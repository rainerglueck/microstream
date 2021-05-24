package schnatterinchen.labor.microstream.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WarehouseDetails {

    private final ObjectMapper objectMapper = new ObjectMapper();
    public final long initialLoadTimeMilliSecs;
    public final long warehouseInventorySize;
    public final String warehouseSize;

    public WarehouseDetails(long initialLoadTimeMilliSecs, long warehouseInventorySize, String warehouseSize) {
        this.initialLoadTimeMilliSecs = initialLoadTimeMilliSecs;
        this.warehouseInventorySize = warehouseInventorySize;
        this.warehouseSize = warehouseSize;
    }

    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
