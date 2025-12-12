package com.kamalkavin96.common_stock_service.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.management.*;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MetricsService {


    @Autowired
    private MeterRegistry meterRegistry;

    private final Instant startTime = Instant.now();

    @Autowired(required = false)
    private BuildProperties buildProperties;

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();

        metrics.put("system", getSystemMetrics());
        metrics.put("memory", getMemoryMetrics());
        metrics.put("threads", getThreadMetrics());
        metrics.put("classes", getClassLoadingMetrics());
        metrics.put("gc", getGarbageCollectorMetrics());
        metrics.put("disks", getDiskMetrics());
        metrics.put("actuatorMetrics", meterRegistry.getMeters().size());

        return metrics;
    }

    private Map<String, Object> getSystemMetrics() {
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        return Map.of(
                "osName", os.getName(),
                "availableProcessors", os.getAvailableProcessors(),
                "systemLoadAverage", os.getSystemLoadAverage()
        );
    }

    private Map<String, Object> getMemoryMetrics() {
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = mem.getHeapMemoryUsage();
        MemoryUsage nonHeap = mem.getNonHeapMemoryUsage();

        return Map.of(
                "heap", Map.of(
                        "used", heap.getUsed(),
                        "max", heap.getMax(),
                        "committed", heap.getCommitted()
                ),
                "nonHeap", Map.of(
                        "used", nonHeap.getUsed(),
                        "max", nonHeap.getMax(),
                        "committed", nonHeap.getCommitted()
                )
        );
    }

    private Map<String, Object> getThreadMetrics() {
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        return Map.of(
                "threadCount", threads.getThreadCount(),
                "peakThreadCount", threads.getPeakThreadCount(),
                "daemonThreadCount", threads.getDaemonThreadCount()
        );
    }

    private Map<String, Object> getClassLoadingMetrics() {
        ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
        return Map.of(
                "loadedClassCount", cl.getLoadedClassCount(),
                "totalLoadedClassCount", cl.getTotalLoadedClassCount(),
                "unloadedClassCount", cl.getUnloadedClassCount()
        );
    }

    private List<Map<String, Object>> getGarbageCollectorMetrics() {
        List<Map<String, Object>> gcInfo = new ArrayList<>();
        for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcInfo.add(Map.of(
                    "name", gc.getName(),
                    "collectionCount", gc.getCollectionCount(),
                    "collectionTimeMs", gc.getCollectionTime()
            ));
        }
        return gcInfo;
    }

    private List<Map<String, Object>> getDiskMetrics() {
        List<Map<String, Object>> disks = new ArrayList<>();

        try {
            for (FileStore store : FileSystems.getDefault().getFileStores()) {
                disks.add(Map.of(
                        "name", store.name(),
                        "type", store.type(),
                        "totalSpace", store.getTotalSpace(),
                        "usableSpace", store.getUsableSpace(),
                        "unallocatedSpace", store.getUnallocatedSpace()
                ));
            }
        } catch (Exception ignored) {}

        return disks;
    }

}
