import java.rmi.registry.Registry;
import java.rmi.Naming;


public class Server
{
	public static void main (String args[])
	{
		try
		{
			RemoteClassInterface remote = new RemoteClass();
			String registry = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
			Naming.rebind(registry, remote);
		}
		catch (Exception e)
		{
			System.out.println("Hey, an error occurred at Naming.rebind");
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
