import jade.core.Agent;
import jade.core.ContainerID;

import java.io.*;
import java.util.*;

public class Adder extends Agent {
    Queue<ContainerID> containers;
    int number;
    boolean done;
    String filename;

    @Override
    protected void setup() {
        try {
            System.out.println("\n\nEsperando... Tenés 10 segundos para levantar 1 container más\n\n");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nBasta de esperar\n\n");
        filename = "numeros";
        done = false;
        doMove(new ContainerID("Container-1", null));
    }

    @Override
    protected void afterMove() {
        System.out.println("Successfully moved to " + here());
        if (!done) {
            try {
                readAndSum();
                doMove(new ContainerID("Main-Container", null));
                done = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(number);
        }
    }

    public void readAndSum() throws Exception {
        BufferedReader file = new BufferedReader(new FileReader(filename));
        String line;
        line = file.readLine();
        while (line != null) {
            number += Integer.parseInt(line);
            line = file.readLine();
        }
        file.close();
    }
}

