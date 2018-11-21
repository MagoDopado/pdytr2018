/*
 * AskRemote.java
 * a) Looks up for the remote object
 * b) "Makes" the RMI
 */
import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.System.exit;

public class AskRemote
{
    public static void main(String[] args)
    {
        if (args.length < 2) {
            System.out.println("usage: server_host iterations");
            exit(1);
        }

        String host = args[0];
        int iterations  = Integer.parseInt(args[1]);

        try {

            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);
            int bufferlength = 100;
            byte[] buffer = new byte[bufferlength];

            List<Double> elapses = new ArrayList<>();
            Double total = 0D, avg;

            for (int i = 0; i < iterations; i++) {

                Long startTime = Utils.timeElapsed("[ASK-REMOTE] Start");

                remote.sendThisBack(buffer);

                Long elapse = Utils.timeElapsed("[ASK-REMOTE] End -", startTime);

                total += (double)elapse / 1000000;
                elapses.add(Double.valueOf(elapse));
            }

            avg = total / iterations;

            System.out.println(Utils.nullSafeConcat("Average in ms: ", avg));
            System.out.println(Utils.nullSafeConcat("Standard Deviation: ", Utils.calculateStandardDeviation(elapses)/1000000));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}