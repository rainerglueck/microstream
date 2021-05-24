# Microstream trials

## Setup
### Domain model
Class 'model.VvzInstrument'  
`{ "vvzid" : "05AIM<random 8 chars>", "isin" : "CH<random 10 numbers>" }`  

### Persistence
Class 'persistence.VvzDataRoot' as storage object for Microstream  
`protected final ConcurrentMap<String, VvzWarehouseInstrument> vvzWarehouseInstrumentConcurrentMap`

Class 'persistence.VvzWarehouseInstrument' as storage representation for VvzInstrument  
```
    protected VvzWarehouseInstrument(VvzInstrument vvzInstrument) {
        Objects.requireNonNull(vvzInstrument);
        this.vvzid = vvzInstrument.vvzid;
        this.isin = vvzInstrument.isin;
    }
```

Class 'persistence.VvzWarehous', stores to filesystem  
```
...
    private final EmbeddedStorageManager storageInstrument1;
    private final VvzDataRoot vvzDataRoot = new VvzDataRoot();
...
    private VvzWarehouse(@Value("${storage.migration.warehousevvz}") String vvzwarehousestorage
            , @Value("${storage.migration.warehousevvz.clone}") String vvzwarehousestorageclone) {
        ...
        this.storageInstrument1 = EmbeddedStorage.start(vvzDataRoot, Paths.get(vvzwarehousestorage));
    }    
...
    @Override
    public void storeVvzInstrument(Collection<VvzInstrument> vvzInstrumentCollection) {
        if (vvzInstrumentCollection != null) {
            vvzInstrumentCollection.forEach(x -> vvzDataRoot.vvzWarehouseInstrumentConcurrentMap
                    .put(x.vvzid, new VvzWarehouseInstrument(x)));
            store();
        }
    }
...
    @Override
    public void deleteAll() {
        vvzDataRoot.vvzWarehouseInstrumentConcurrentMap.clear();
        store();
    }
...
    private synchronized void store() {
        /* not thread safe */
        storageInstrument1.store(vvzDataRoot.vvzWarehouseInstrumentConcurrentMap);
    }
...
```
## Performance measures
for details see [performance.log](/logs/performance.log)
### store added objects
| initial | addes objects | time [ms] | new File Size [MB] |
|--------:|--------------:|----------:|-------------------:|
|        0|               |           |               0.01 |
|        0|             1 |       116 |               0.01 |
|        1|       100'000 |      2627 |                 17 |
|  100'001|        20'000 |        61 |                 22 |
|  120'001|       100'000 |      1806 |                 42 |
|  220'001|       100'000 |      1163 |                 61 |            
|  320'001|       100'000 |      1759 |                 76 |
|  420'001|       100'000 |      3616 |                 93 |
|  920'001|       100'000 |      1786 |                225 |
|1'020'001|    delete all |       132 |                182 |
|        0|               |           ||

### startup time ('clone and load'), search '
| size    | Clone and load [ms] | search '0' (all) [ms]  | search 'y' [ms] | search '-' [ms] |
|--------:|--------------------:|-----------------------:|----------------:|----------------:|
| no files|                1435 ||||
|        0|                  53 ||||
|        1|                1727 ||||
|  100'001|                2313 ||||
|  120'001|                     ||||
|  220'001|                2184 |                    153 |           100 ||
|  320'001|                2976 |                    166 |           550 |               399 |
|  420'001|                8529 |                    962 |           198 |               371 |
|  520'001|               17631 |                    353 |           270 |               753 |
|1'020'001|               17901 |                    694 |           804 |              1597 |
|        0|                2276 |                     15 | | |

## Problems / Questions
### File size does not shrink when removing objects
see above

### one.microstream.persistence.exceptions.PersistenceException, 
for details see [persistenceException.log](/logs/persistenceException.log):
```
2021-05-23 18:51:48.729  INFO 8164 --- [nio-8088-exec-6] s.l.m.api.VvzInstrumentController        : POST /add?nbr=[100000]
2021-05-23 18:51:49.775  INFO 8164 --- [nio-8088-exec-5] s.l.m.api.VvzInstrumentController        : POST /add?nbr=[20000]
2021-05-23 18:51:51.045 ERROR 8164 --- [nio-8088-exec-6] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is one.microstream.persistence.exceptions.PersistenceException: Inconsistent element count: specified 560000 vs. iterated 179275143558] with root cause

one.microstream.persistence.exceptions.PersistenceException: Inconsistent element count: specified 560000 vs. iterated 179275143558
	at one.microstream.persistence.binary.types.Binary.validatePostIterationState(Binary.java:2009) ~[persistence.binary-04.01.00-MS-GA.jar:na]
	at one.microstream.persistence.binary.types.Binary.storeMapEntrySet(Binary.java:525) ~[persistence.binary-04.01.00-MS-GA.jar:na]
	at one.microstream.java.util.AbstractBinaryHandlerMap.store(AbstractBinaryHandlerMap.java:66) ~[persistence.binary-04.01.00-MS-GA.jar:na]
	at one.microstream.java.util.AbstractBinaryHandlerMap.store(AbstractBinaryHandlerMap.java:1) ~[persistence.binary-04.01.00-MS-GA.jar:na]
	at one.microstream.persistence.binary.internal.AbstractBinaryHandlerCustom.store(AbstractBinaryHandlerCustom.java:1) ~[persistence.binary-04.01.00-MS-GA.jar:na]
	at one.microstream.persistence.binary.types.BinaryStorer$Default.storeItem(BinaryStorer.java:414) ~[persistence.binary-04.01.00-MS-GA.jar:na]
	at one.microstream.persistence.binary.types.BinaryStorer$Default.storeGraph(BinaryStorer.java:403) ~[persistence.binary-04.01.00-MS-GA.jar:na]
	at one.microstream.persistence.binary.types.BinaryStorer$Default.store(BinaryStorer.java:421) ~[persistence.binary-04.01.00-MS-GA.jar:na]
	at one.microstream.persistence.types.PersistenceManager$Default.store(PersistenceManager.java:274) ~[persistence-04.01.00-MS-GA.jar:na]
	at one.microstream.storage.types.StorageConnection.store(StorageConnection.java:344) ~[storage-04.01.00-MS-GA.jar:na]
	at schnatterinchen.labor.microstream.persistence.VvzWarehouse.store(VvzWarehouse.java:90) ~[classes/:na]
====> storageInstrument1.store(vvzDataRoot.vvzWarehouseInstrumentConcurrentMap);
	at schnatterinchen.labor.microstream.persistence.VvzWarehouse.storeVvzInstrument(VvzWarehouse.java:64) ~[classes/:na]
	at schnatterinchen.labor.microstream.api.VvzInstrumentController.add(VvzInstrumentController.java:85) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
```

### Migration of data
It is not possible to  
`store(VvzInstrument {"vvzid":"05AIM00000", "isin":"CH00000"})`  
and then load to another class with same structure, e.g.  
`EmbeddedStorage.start(XetraInstrument{"vvzid":?, "isin":?},...)`
