
```
   private synchronized void store() {
        /* not thread safe */
        storageInstrument1.store(vvzDataRoot.vvzWarehouseInstrumentConcurrentMap);
    }


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
	at schnatterinchen.labor.microstream.persistence.VvzWarehouse.storeVvzInstrument(VvzWarehouse.java:64) ~[classes/:na]
	at schnatterinchen.labor.microstream.api.VvzInstrumentController.add(VvzInstrumentController.java:85) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
```