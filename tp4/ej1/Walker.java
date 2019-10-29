import jade.core.Agent;
import jade.core.ContainerID;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Walker extends Agent {
    Queue<ContainerID> containers;
    List<String> data;
    Long startTime;
    Long processingTime;

    @Override
    protected void setup() {
        try {
            System.out.println("\n\nEsperando... Tenés 10 segundos para levantar 4 containers más\n\n");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nBasta de esperar\n\n");
        containers = getContainers();
        data = new ArrayList<>(5);
        startTime = System.currentTimeMillis();
        processingTime = 0L;
        doMove(containers.remove());
    }

    @Override
    protected void afterMove() {
        long begin = System.currentTimeMillis();
        System.out.println("Successfully moved to " + here());
        data.add(getData());
        processingTime += System.currentTimeMillis() - begin;
        if (!containers.isEmpty()) {
            doMove(containers.remove());
        } else {
            System.out.println("Tiempo: " + (System.currentTimeMillis() - startTime - processingTime) + " milisegundos.");
            for (String d : data) {
                System.out.println(d);
            }
        }
    }

    private Queue<ContainerID> getContainers() {
        return new LinkedList<>(Arrays.asList(
                new ContainerID("Container-1", null),
                new ContainerID("Container-2", null),
                new ContainerID("Container-3", null),
                new ContainerID("Container-4", null),
                new ContainerID("Main-Container", null)
        ));
    }

    public String getData() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "Computer name: " + host + " | Free memory: " + runtime.freeMemory() + "B | Processing load: " + osBean.getSystemLoadAverage();
    }
}
