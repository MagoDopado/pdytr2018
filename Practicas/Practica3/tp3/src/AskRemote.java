/*
 * AskRemote.java
 * a) Looks up for the remote object
 * b) "Makes" the RMI
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */

public class AskRemote
{
    public static void main(String[] args)
    {
        /* Look for hostname and msg length in the command line */
        if (args.length != 1)
        {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }
        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);

            Path p = Paths.get("../../textTest.txt");

            try (InputStream in = Files.newInputStream(p);
                 BufferedReader reader =
                         new BufferedReader(new InputStreamReader(in))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    remote.sendThisBack(line);
                }
            } catch (IOException x) {
                System.err.println(x);
            }

            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}