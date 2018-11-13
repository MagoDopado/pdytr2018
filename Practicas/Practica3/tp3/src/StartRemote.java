/*
 * StartRemoteObject.java
 * Starts the remote object. More specifically:
 * 1) Creates the object which has the remote methods to be invoked
 * 2) Registers the object so that it becomes avaliable
 */

import java.rmi.registry.Registry; /* REGISTRY_PORT */
import java.rmi.Naming; /* rebind */
public class StartRemote
{
    public static void main (String args[])
    {
        try {
            /* Create ("start") the object which has the remote method */
            RemoteImpl remote = new RemoteImpl();
            /* Register the object using Naming.rebind(...) */
            String rname = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
            Naming.rebind(rname, remote);
        } catch (Exception e) {
            System.out.println("Hey, an error occurred at Naming.rebind. Stack Trace: ");
            e.printStackTrace();
            System.out.println("Message: ");
            System.out.println(e.getMessage());
        }
    }
}
