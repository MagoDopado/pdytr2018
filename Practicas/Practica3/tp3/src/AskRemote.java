/*
 * AskRemote.java
 * a) Looks up for the remote object
 * b) "Makes" the RMI
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */


public class AskRemote
{
    public static void main(String[] args)
    {
        /* Look for hostname and msg length in the command line */
        if (args.length != 2)
        {
            System.out.println("1 argument needed: (remote) hostname fileName");
            System.exit(1);
        }
        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            String fileName = args[1];
            IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);

            readFileFromServer(remote, fileName);


            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readFileFromServer(IfaceRemoteClass server, String fileName) throws Exception
    {
      FileWriter file = new FileWriter(fileName);
      int remainingSize = server.getSize(fileName);
      int offset = 0;
      int chunckSize = 1024;
      do
      {
        char[] buffer = server.readBuffer(fileName, offset, chunckSize);

        int read = buffer.length;
        
        file.write(buffer, offset, read);
        remainingSize = 0;
      } while (remainingSize > 0);
      file.close();
    }

    /*public static File createNewFile(String fileName) throws Exception
    {
      Path path = Paths.get("./" + fileName);
      File file = path.toFile();
      if (file.createNewFile())
      {
        return file;
      }
      else
      {
        throw new Exception("Could not create file");
      }
    }*/
}

/*Path p = Paths.get("../../textTest.txt");

try (InputStream in = Files.newInputStream(p);
     BufferedReader reader =
             new BufferedReader(new InputStreamReader(in))) {
    String line = null;
    while ((line = reader.readLine()) != null) {
        remote.sendThisBack(line);
    }
} catch (IOException x) {
    System.err.println(x);
}*/
